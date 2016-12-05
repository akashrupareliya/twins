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
        String helptext ="<u><b>What is Location Authenticty?</b></u><br>Description of Location Authenticity</br>";

        //To convert HTML text to normal text.
        help.setText(Html.fromHtml(helptext));
    }
}