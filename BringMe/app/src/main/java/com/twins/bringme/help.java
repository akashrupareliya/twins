package com.twins.bringme;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Akash Rupareliya on 11/16/2016.
 */

//Class to get help
public class help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display Help Screen.
        setContentView(R.layout.help);
        TextView help = (TextView)findViewById(R.id.help_text);
        String helptext ="<u><b>1.Location Authenticity</b></u><br>This rating is the measure of how correct the store location is, this is calculated based on the user provided ratings</br><br><br><u><b>2.Seller Rating</b></u><br>This rating is the measure of seller popularity.</br><br><br><u><b>3.On-Sale</b></u><br>This is an optional field which indicates whether the item is on-sale or not.</br><br><br><u><b>4.Approx. Quantity </b></u><br>This is an optional field in which user can enter the approximate number of the item available in particular store.</br>";

        //To convert HTML text to normal text.
        help.setText(Html.fromHtml(helptext));
    }
}
