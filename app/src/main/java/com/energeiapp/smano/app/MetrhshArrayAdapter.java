package com.energeiapp.smano.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import static com.energeiapp.smano.app.R.id.ArrayAdapterGeneral;

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

        //Τελευταία και φρέσκια

        if(position == 0 && DateUtils.isToday(metrhsh.getDay()) ){
            LinearLayout General = (LinearLayout) view.findViewById(R.id.ArrayAdapterGeneral);
            General.setBackgroundColor(Color.parseColor("#FFF59D"));
        }
        // Date Configuration
        TextView hmeromhnia = (TextView) view.findViewById(R.id.ArrayAdapterDate);
        String hmeromhnias = metrhsh.getFullDay();

        hmeromhnia.setText(hmeromhnias);


        //set price and rental attributes
        //τώρα μπορεί να κάνει και πρόσθεση

        double totalBillCost = CostEstimate.round(CostEstimate.calculateCostDay(metrhsh.getDayKilovatora(),1) + CostEstimate.calculateCostNight(metrhsh.getNightKilovatora(),1),2);
        double costOfDay = CostEstimate.round(totalBillCost/120D, 2);
        double percentage = (metrhsh.getAverageof3()- metrhsh.getSumKilovatora())/metrhsh.getAverageof3()*100;
        double percentageValue = (CostEstimate.round(percentage,1));

        double xrewshTetramhnoyAverageOf3 =CostEstimate.calculateCostDay(metrhsh.getAverageof3Day(),1) + CostEstimate.calculateCostNight(metrhsh.getAverageof3Night(),1);
            metrhsh.getAverageof3();
        double difference =  CostEstimate.round(xrewshTetramhnoyAverageOf3 -  totalBillCost,2);


            if (metrhsh.getAverageof3()> metrhsh.getSumKilovatora() && position<metrhshes.size()-3){

                    TextView percentageText = (TextView) view.findViewById(R.id.ExtraBoxText);
                    percentageText.setText("Συγχαρητήρια μειώσατε την κατανάλωσή σας σε ποσοστό " + percentageValue + "%." + "\nΑν καταναλώνατε κάθε μέρα όσο σήμερα ο λογαριασμός τετραμήνου θα ήταν " + totalBillCost + "€ και θα εξοικονομούσατε " + difference + "€.");
                    percentageText.setBackgroundColor((Color.parseColor("#FFC107")));

            }
        else {
                LinearLayout parent1 = (LinearLayout) view.findViewById(ArrayAdapterGeneral);
                LinearLayout child1 = (LinearLayout) view.findViewById(R.id.ExtraBox);

                parent1.removeView(child1);


            }


        TextView kwh = (TextView) view.findViewById(R.id.UsualBoxKwhN);
        kwh.setText(CostEstimate.round(metrhsh.getSumKilovatora(),2)+ "kWh");


        TextView cost = (TextView) view.findViewById(R.id.UsualBoxCostN);
        cost.setText(costOfDay +" " +"\u20ac");


        //ImageView Configuration
        ImageView image = (ImageView) view.findViewById(R.id.image);


        if (metrhsh.getSumKilovatora()< metrhsh.getAverageof3()& metrhsh.getSumKilovatora()> 0.9 * metrhsh.getAverageof3()&& position<metrhshes.size()-3) {
            image.setImageResource(R.drawable.piggy1);
            cost.setTextColor(Color.parseColor("#1E88E5"));

        } else if (metrhsh.getSumKilovatora()< 0.9 * metrhsh.getAverageof3()& metrhsh.getSumKilovatora()> 0.8 * metrhsh.getAverageof3()&& position<metrhshes.size()-3) {
            image.setImageResource(R.drawable.piggy2);
            cost.setTextColor(Color.parseColor("#00BCD4"));
        } else if (metrhsh.getSumKilovatora()< 0.8 * metrhsh.getAverageof3() && position<metrhshes.size()-3) {
            image.setImageResource(R.drawable.piggy3);
            cost.setTextColor(Color.parseColor("#00C853"));
        }
//        TextView lowerBill = (TextView) view.findViewById(R.id.Bill);
//
//        if ( metrhsh.getSavings()>0)
//        {
//            lowerBill.setText( " ** Μείωση Λογαριασμού: " + metrhsh.getSavings()*60D + "\u20ac" +" ! **"
//
//        }
//
//        //get the image associated with this property
        return view;
    }
}
