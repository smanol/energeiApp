package com.example.smano.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by smano on 1/11/2016.
 */

//custom ArrayAdapter
class MetrhshArrayAdapter extends ArrayAdapter<Metrhsh> {

    private Context context;
    private ArrayList<Metrhsh> metrhshes;

    //constructor, call on creation
    public MetrhshArrayAdapter(Context context, int resource, ArrayList<Metrhsh> objects) {
        super(context, resource, objects);

        this.context = context;
        this.metrhshes = objects;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        Metrhsh metrhsh = metrhshes.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout, null);

        TextView hmeromhnia = (TextView) view.findViewById(R.id.textView1);
        TextView kwh = (TextView) view.findViewById(R.id.textView2);

        ImageView image = (ImageView) view.findViewById(R.id.imageView1);


        //set address and description
        String hmeromhnias = metrhsh.getHmera();
        hmeromhnia.setText(hmeromhnias);


        //set price and rental attributes
        kwh.setText("Kwh: " + metrhsh.getKilovatora());

        //get the image associated with this property

        image.setImageResource(metrhsh.getImage());

        return view;




    }




}