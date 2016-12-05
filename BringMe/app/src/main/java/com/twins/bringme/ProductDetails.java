package com.twins.bringme;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


/**
 * Created by Akash Rupareliya on 11/17/2016.
 */

//Class to add product details
public class ProductDetails extends AppCompatActivity{

    //Initialize variables and objects
    EditText title1,quantity1;
    TextView barcode_result,location1;
    Switch sale1;
    ProgressDialog progressDialog;
    Button submit;
    String saledetail;
    double latitude1,longitude1;
    String barcode,address_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail); //Set layout

            //Check if barcode is found or not.
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                barcode  ="No Barcode/QR code Found";
            } else {

                //Get the barcode value from previous activity
                barcode = extras.getString("barcode");
            }

        //Display barcode result
        barcode_result =(TextView)findViewById(R.id.barcode_result);
        barcode_result.setText(Html.fromHtml("<b>Barcode/QR Code: </b>" +barcode));

        title1=(EditText)findViewById(R.id.add_title);
        quantity1=(EditText)findViewById(R.id.quantity);
        location1 =(TextView) findViewById(R.id.searched_address);
        sale1=(Switch) findViewById(R.id.sale);
        sale1.setChecked(false);
        saledetail = "No";

        //attach a listener to check for changes in state
        sale1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    saledetail="Yes";
                }else{
                    saledetail="No";
                }

            }
        });


        submit = (Button) findViewById(R.id.submit);

        //Submit Product detail to database  by clicking on submit button
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //To validate Edit Texts and values entered by user
                if(validateFeedback())
                {
                    //Function to Submit product values to database
                    feedback();
                }
            }
        });
    }

    //Submit Product details to database
    private void feedback() {

        String title= title1.getText().toString().trim().replaceAll(" ","%20");
        String quantity=quantity1.getText().toString().trim().replaceAll(" ","%20");
        String sale=saledetail.trim().replaceAll(" ","%20");
        String location=address_text.trim().replaceAll(" ","%20").replaceAll("\n","%0A");
        String latitude = Double.toString(latitude1);
        String longitude = Double.toString(longitude1);
        String Barcode = barcode.toString().trim().replaceAll(" ","%20").replaceAll("\n","%0A");

        //Execute URL
        new ProductDetailSubmit(ProductDetails.this,"Submitting...").execute(AppFieldNames.SIGNUP,title,quantity,sale,location,latitude,longitude,Barcode);
    }

    //Function for validation of entered data.
    //Check if any necessary field is null or not.
    private boolean validateFeedback() {

        String title= title1.getText().toString().trim();
        String quantity=quantity1.getText().toString().trim();
        String location=location1.getText().toString().trim();
        String latitude = Double.toString(latitude1);
        String longitude = Double.toString(longitude1);

        //Check if title is null or not
        if(!AppFieldNames.IsStringValid(title))
        {
            Snackbar.make(findViewById(R.id.snackbar1),"Enter the title of Product", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        //Check if Quantity is null or not
        else if(!AppFieldNames.IsStringValid(quantity))
        {
            Snackbar.make(findViewById(R.id.snackbar1),"Enter Quantity of the Product", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        //Check if location is selected or not.
        else if (location.contains("Select"))
        {
            Snackbar.make(findViewById(R.id.snackbar1),"Please Select the location", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        //Check if entered location is correct or not
        else if(!AppFieldNames.IsStringValid(latitude))
        {
            Snackbar.make(findViewById(R.id.snackbar1),"Please Select the location", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        //Check if entered location is correct or not
        else if(!AppFieldNames.IsStringValid(longitude))
        {
            Snackbar.make(findViewById(R.id.snackbar1),"Please Select the location", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    /*
    * In this method, Start PlaceAutocomplete activity
    * PlaceAutocomplete activity provides--
    * a search box to search Google places
    */
    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                //Set Text of location to TextView.Show it in plain text.Convert it from HTML to Plain
                 address_text = place.getName()+",<br>"+
                        place.getAddress() +"<br><br>" +"<b>Contact Detail: </b>" + place.getPhoneNumber() + "<br><br>" +"<b>Rating of Seller: </b>" + place.getRating();
                ((TextView) findViewById(R.id.searched_address))
                        .setText( Html.fromHtml("<b>Location: </b>"+place.getName()+",<br>"+
                                place.getAddress() +"<br><br>" +"<b>Contact Detail: </b>" + place.getPhoneNumber() + "<br><br>" +"<b>Rating of Seller: </b>" + place.getRating()));

                //Get Latitude and longitude
                latitude1 = place.getLatLng().latitude;
                longitude1 =place.getLatLng().longitude;

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    /* add click event to the submit button */
    public void submit(View v){
        Intent intent = new Intent(this,Confirmation.class);
        startActivityForResult(intent,0);
    }

    //Class to submit product details to database
    class ProductDetailSubmit extends AsyncTask<String,String,String>
    {
        boolean isWeakInternet;
        String  response,status,messageForProDialog,url;
        JSONObject mainJson;
        int AddStatus;
        Context context;

        public ProductDetailSubmit(Context con,String messageForDialog) {
            this.messageForProDialog=messageForDialog;
            this.context=con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(ProductDetails.this);
            progressDialog.setMessage(messageForProDialog);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try{

                if(params[0].equals(AppFieldNames.SIGNUP))
                {
                    //Pass parameter to send values to database
                    url=AppFieldNames.ADD_PRODUCT_URL+"title="+params[1]+"&quantity="+params[2]+"&sale="+params[3]+"&location="+params[4]+"&latitude="+params[5]+"&longitude="+params[6]+"&barcode="+params[7];
                    //Execute file
                    response=webservice.getResponseText(url,15000);
                    AddStatus=2;
                }
                //Check if response is false or true
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

            //Check if Internet Conectivity is weak or not
            if(isWeakInternet)
            {

                if(AddStatus==2) {

                    //Display message if internet connection is weak
                    Snackbar.make(findViewById(R.id.snackbar1),"Check your Connection", Snackbar.LENGTH_SHORT).show();
                  }
                }
            else
            {
                //Check if product details submitted successfully or not
                if(result.equals("OK"))
                {
                    if(AddStatus==2)
                    {
                        //Display message if product details is submitted successfully.
                        Snackbar.make(findViewById(R.id.snackbar1),"Submitted Successfully", Snackbar.LENGTH_SHORT).show();
                    }


                    //Navigation to confirmation page after submitting product details successfully
                    Intent intnt = new Intent(ProductDetails.this,Confirmation.class);
                    context.startActivity(intnt);
                    ((Activity)context).finish();

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

    //click to go to help section.
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent = new Intent(this,help.class);
        startActivityForResult(intent,0);
        return super.onOptionsItemSelected(item);
    }
}