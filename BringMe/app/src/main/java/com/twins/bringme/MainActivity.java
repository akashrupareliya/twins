package com.twins.bringme;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Akash Rupareliya on 11/16/2016.
 */

//Starting Screen of the app.
//Class to for main view (Display 2 options 1)Add Product 2)Search Product)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To display main screen
        setContentView(R.layout.main_view);

        //Initialize preference to store and get variables
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //Get values from the preferences.
        int launch = prefs.getInt("launch", 0);

        //Alert Box only first time user use the application to guide user about Help Section.
        //Check if app is initialize first time or not.

        if(launch != 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //On click event to get help.
            builder.setPositiveButton("Get Help", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putInt("launch", 1);
                    editor.commit();

                    //Navigate to Help Section.
                    Intent intnt = new Intent(MainActivity.this, help.class);
                    startActivity(intnt);
                    finish();

                }
            }).setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putInt("launch", 1);
                    editor.commit();

                }
            });
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = getLayoutInflater();

            //To display layout of alert box
            View dialogLayout = inflater.inflate(R.layout.splash, null);
            dialog.setView(dialogLayout);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();

            //To set the height and width of the app.
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);

                    Bitmap icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.faq);
                    float imageWidthInPX = (float) image.getWidth();

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                            Math.round(imageWidthInPX * (float) icon.getHeight() / (float) icon.getWidth()));
                    image.setLayoutParams(layoutParams);
                }
            });

            editor.putInt("launch", 1);
            editor.commit();
        }
    }

    /* add click event to the Add Product  button */
    public void scanBarcode(View v){
        Intent intent = new Intent(this,ScanBarcodeActivity.class);
        startActivityForResult(intent,0);
    }

    /* add click event to the Search Product button */
    public void searchproduct(View v){
        Intent intent = new Intent(this,Search.class);
        startActivityForResult(intent,0);
    }

    //Menu Item for help
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Click event to nevigate help section
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent = new Intent(this,help.class);
        startActivityForResult(intent,0);
        return super.onOptionsItemSelected(item);
    }
}
