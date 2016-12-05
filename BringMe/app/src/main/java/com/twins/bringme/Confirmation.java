package com.twins.bringme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Akash Rupareliya on 11/21/2016.
 */

//Class to give confirmation after adding product detais
public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To display confirmation view.
        setContentView(R.layout.confirmation);
    }

    /* add click event to mainmenu button */
    public void mainmenu(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivityForResult(intent,0);
        finish();
    }

    /* add click event to Scan more Product button */
    public void scanmoreproduct(View v){
        Intent intent = new Intent(this,ScanBarcodeActivity.class);
        startActivityForResult(intent,0);
        finish();
    }

    //Menu item to show help button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //add click event on help menu to get help
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent = new Intent(this,help.class);
        startActivityForResult(intent,0);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivityForResult(intent,0);
        finish();
    }
}
