package com.twins.bringme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Akash Rupareliya on 11/21/2016.
 */

//Class to display detailed information of selected product.

public class Description extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextView title1,quantity1,onsale1,location1,map,location_authenticity;
    String title,location,quantity,onsale,distance,barcode,viewinmap,latitude,longitude,location_rating;
    float rating,getRating;
    int id,total_rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To set display of product_description.xml
        setContentView(R.layout.product_description);

        //Initialize preference to store & get values
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFS", MODE_PRIVATE);

        //To edit values
        SharedPreferences.Editor editor = prefs.edit();

        //get values from the variable of preferences.
        title = prefs.getString("title", null);
        location = prefs.getString("location", null);
        quantity = prefs.getString("quantity", null);
        onsale = prefs.getString("sale", null);
        distance = prefs.getString("distance",null);
        barcode = prefs.getString("barcode",null);
        latitude= prefs.getString("latitude",null);
        longitude= prefs.getString("longitude",null);
        location_rating= prefs.getString("location_rating",null);
        total_rating = prefs.getInt("total_rating",0);
        getRating = prefs.getFloat("rating",0);

        id = prefs.getInt("id",0);

        //Set url to show location of selected product in map
        viewinmap = "https://www.google.co.in/maps/place/"+latitude+","+longitude;

        title1=(TextView) findViewById(R.id.title);
        quantity1=(TextView) findViewById(R.id.quantity);
        onsale1 = (TextView)findViewById(R.id.onsale);
        location1 = (TextView)findViewById(R.id.location);
        location_authenticity = (TextView)findViewById(R.id.rating);
        ((RatingBar)findViewById(R.id.ratingBar)).setRating(getRating);


        //To display location in map
        map = (TextView)findViewById(R.id.map);
        map.setPaintFlags(map.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(viewinmap));
                startActivity(intent);
            }
        });

        //Set values and details of all textviews.
        title1.setText(Html.fromHtml("<b>Product Name: </b>"+title+"<br>"+"<b>Barcode: </b>"+barcode) );
        location1.setText(Html.fromHtml("<b>Location: </b>"+location +"<br><br>"+"<b>Distance: </b>" +distance ));
        quantity1.setText(Html.fromHtml("<b>Quantity: </b>" +quantity));
        onsale1.setText(Html.fromHtml("<b>Onsale: </b>"+onsale));
        location_authenticity.setText(Html.fromHtml("<b>Authenticity of Location: </b>" + location_rating+  "/5" +" (" +total_rating +")" ));

        //Button to submit rating
        Button submit_rating = (Button) findViewById(R.id.submit_rating);

        submit_rating.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Get the value of Ratingbar
                rating = ((RatingBar)findViewById(R.id.ratingBar)).getRating();
                String rating1= Float.toString(rating).trim().replaceAll(" ","%20");
                String id1 = Integer.toString(id).trim().replaceAll(" ","%20");

                //To submit rating to the database
                new Description.RatingSubmit(Description.this,"Submitting...").execute(AppFieldNames.SIGNUP,rating1,id1);

            }
        });
    }


    //Class to get the value of rating bar and  submit rating to database
    class RatingSubmit extends AsyncTask<String,String,String>
    {

        boolean isWeakInternet;
        String  response,status,messageForProDialog,url;
        JSONObject mainJson;
        int AddStatus;
        Context context;

        public RatingSubmit(Context con,String messageForDialog) {
            this.messageForProDialog=messageForDialog;
            this.context=con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Display Process of submitting rating using process dialog.
            progressDialog=new ProgressDialog(Description.this);
            progressDialog.setMessage(messageForProDialog);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try{

                if(params[0].equals(AppFieldNames.SIGNUP))
                {
                    //Parameter to pass the values
                    url=AppFieldNames.RATING_URL+"rating="+params[1]+"&id="+params[2];
                    response=webservice.getResponseText(url,15000);
                    AddStatus=2;
                }

                //Check the response.
                if(response.equals("false"))
                {
                    isWeakInternet=true;
                }
                else
                {
                    mainJson=new JSONObject(response);
                    status=mainJson.getString("status");
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            //Check if internet connection is weak or not.
            if(isWeakInternet)
            {

                if(AddStatus==2)

                    //Display if internet connection is weak
                    Snackbar.make(findViewById(R.id.snackbar),"Check your Connection", Snackbar.LENGTH_SHORT).show();
                     }
            else
            {
                //Check it rating is submitted successfully or not
                if(result.equals("OK"))
                {
                    if(AddStatus==2)
                    {
                        //Display if rating is submitted successfully.

                        Snackbar.make(findViewById(R.id.snackbar),"Rating Submitted Successfully", Snackbar.LENGTH_SHORT).show();

                    }

                    //Initialize preference to store and retrive values.
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("MYPREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    //To store variable values into prefrences.
                    editor.putFloat("rating", rating);
                    editor.commit();



                }

            }
        }
    }


    //Menu item for help
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    //Click event to get the help
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        Intent intent = new Intent(this,help.class);
        startActivityForResult(intent,0);
        return super.onOptionsItemSelected(item);
    }


}