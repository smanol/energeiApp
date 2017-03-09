package com.example.smano.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
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


        // Date Configuration
        TextView hmeromhnia = (TextView) view.findViewById(R.id.Date);
        String hmeromhnias = metrhsh.getDay();
        hmeromhnia.setText(hmeromhnias);

        //ImageView Configuration
        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setImageResource(metrhsh.getImage());

        //set price and rental attributes
        //τώρα μπορεί να κάνει και πρόσθεση
        double totalBillCost = CostEstimate.calculateCostDay(metrhsh.getDayKilovatora()) + CostEstimate.calculateCostNight(metrhsh.getNightKilovatora());
        double costOfDay = CostEstimate.round(totalBillCost/60D, 2);





        TextView kwh = (TextView) view.findViewById(R.id.KwhN);
        kwh.setText(metrhsh.getSumKilovatora()+ "kWh");


        TextView cost = (TextView) view.findViewById(R.id.CostN);
        cost.setText(costOfDay +" " +"\u20ac");




        if (metrhsh.getSumKilovatora()>1.5*metrhsh.getAverage()) {
            cost.setTextColor(Color.parseColor("#F9A825"));
        }
        else if (metrhsh.getSumKilovatora()>1.2*metrhsh.getAverage()) {
            cost.setTextColor(Color.parseColor("#FFD54F"));
        }
        else if (metrhsh.getSumKilovatora()>1*metrhsh.getAverage()) {
            cost.setTextColor(Color.parseColor("#1E88E5"));
        }
        else if (metrhsh.getSumKilovatora()>0.8*metrhsh.getAverage()) {
            cost.setTextColor(Color.parseColor("#00BCD4"));
        }
        else
            {
            cost.setTextColor(Color.parseColor("#00C853"));
                cost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);;
                cost.setTypeface(null, Typeface.BOLD);
                cost.setText(costOfDay +" " +"\u20ac"+ " !");
        }




//        TextView lowerBill = (TextView) view.findViewById(R.id.Bill);
//
//        if ( metrhsh.getSavings()>0)
//        {
//            lowerBill.setText( " ** Μείωση Λογαριασμού: " + metrhsh.getSavings()*60D + "\u20ac" +" ! **"  );
//
//        }
//
//        //get the image associated with this property






        return view;




    }




}
