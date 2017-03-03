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
import java.util.Collections;

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
        Collections.reverse(objects);
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
        String hmeromhnias = metrhsh.getDay();
        hmeromhnia.setText(hmeromhnias);


        //set price and rental attributes
        //τώρα μπορεί να κάνει και πρόσθεση
        double totalBillCost = CostEstimate.calculateCostDay(metrhsh.getDayKilovatora()) + CostEstimate.calculateCostNight(metrhsh.getNightKilovatora());
        double costOfDay = CostEstimate.round(totalBillCost/60D, 2);
        kwh.setText( "Κατανάλωση: " + metrhsh.getSumKilovatora()+ "kWh"+"\n" + "Κόστος: " + costOfDay +" " +"\u20ac" + "\n" + "Αν ξοδεύατε κάθε μέρα όσο σήμερα ο λογαριασμός θα ήταν " + totalBillCost + "\u20ac" );


        //get the image associated with this property

        image.setImageResource(metrhsh.getImage());



        return view;




    }




}
