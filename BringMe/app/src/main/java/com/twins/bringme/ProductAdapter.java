package com.twins.bringme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    Double rating,sendrating;
    List<ProductData> data = Collections.emptyList();
    ArrayList<ProductData> arraylist;
    ProductData current;
    int currentPos = 0;

    // constructor to innitilize context and data sent from ProductDetails.java
    public ProductAdapter(Context context, List<ProductData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        arraylist = new ArrayList<ProductData>();
        arraylist.addAll(data);



    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.search_product, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;
        ProductData current = data.get(position);
        myHolder.title.setText(current.title);

        //round rating to two decimal points
        rating = roundTwoDecimals(Double.parseDouble(current.rating));

        //Set Text and convert it to Plain Text from HTML Text
        myHolder.location.setText(Html.fromHtml("<b>Location: </b>" + current.location));
        myHolder.rating.setText(Html.fromHtml("<b>Authenticity of Location: </b>" + rating.toString().trim() +  "/5" +" (" +current.total_rating +")" ));
        myHolder.distance.setText("Distance: " + roundTwoDecimals(current.distance) + " Km");
        myHolder.distance.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));


        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                //Initialize the preference to store values
                SharedPreferences prefs = context.getSharedPreferences("MYPREFS", 0);
                SharedPreferences.Editor editor = prefs.edit();

                sendrating = roundTwoDecimals(Double.parseDouble(data.get(pos).rating));

                //Store the values in preferences.
                String SendDistance = Double.toString(roundTwoDecimals(data.get(pos).distance));
                editor.putString("title", data.get(pos).title);
                editor.putString("location", data.get(pos).location);
                editor.putString("sale", data.get(pos).sale);
                editor.putString("barcode", data.get(pos).barcode);
                editor.putString("distance", SendDistance);
                editor.putString("quantity",data.get(pos).quantity);
                editor.putString("latitude",data.get(pos).latitude);
                editor.putString("longitude",data.get(pos).longitude);
                editor.putFloat("rating", 0);
                editor.putString("location_rating",sendrating.toString());
                editor.putInt("total_rating",data.get(pos).total_rating);
                editor.putInt("id",data.get(pos).id);
                editor.commit();

                //Navigate to description page of selected product
                Intent intent = new Intent(context,Description.class);
                context.startActivity(intent);
            }
        });

    }

    // return total number of data from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView location;
        TextView distance;
        TextView rating;
        ItemClickListener itemClickListener;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            location = (TextView) itemView.findViewById(R.id.location);
            distance = (TextView) itemView.findViewById(R.id.distance);
            rating = (TextView) itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
    }

    //Function to filter data with entered text in search bar.
    public  void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);

        } else {
            for (ProductData postDetail : arraylist) {

                //Compare text With title
                if (charText.length() != 0  && postDetail.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(postDetail);
                }
                //Compare entered text with location
                else if (charText.length() != 0  && postDetail.location.toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }

    //Function to round decimal to two decimal number
    double roundTwoDecimals(double d) {

        //Formate to convert it
        DecimalFormat twoDForm = new DecimalFormat("#.##");

        //return the value
        return Double.valueOf(twoDForm.format(d));
    }

}