package com.example.smano.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    BarChart barChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barChar = (BarChart) findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f, 0));
        barEntries.add(new BarEntry(88f, 1));
        barEntries.add(new BarEntry(66f, 2));
        barEntries.add(new BarEntry(12f, 3));
        barEntries.add(new BarEntry(19f, 4));
        barEntries.add(new BarEntry(91f, 5));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("19 December");
        theDates.add("20 December");
        theDates.add("21 December");
        theDates.add("22 December");
        theDates.add("23 December");
        theDates.add("24 December");


        BarData theData = new BarData(theDates, barDataSet);
        barChar.setData(theData);
        barChar.setDragEnabled(true);
        barChar.setScaleEnabled(true);
        barChar.setTouchEnabled(true);


//        ListView listView = (ListView)findViewById(R.id.lv);
//        //create a string array for listview
//
//        ArrayList<String> list = new ArrayList<String>();
//
//        for(int i =1;i<15;i++)
//        {
//            list.add("Μέτρηση υπ' αριθμόν: "+ i);
//        }
//
//        //create adapter to show this array like a list
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
//
//        //put this adapter to listview
//
//        listView.setAdapter(adapter);

        ArrayList<Metrhsh> metrhshes = new ArrayList<>();

        metrhshes.add(new Metrhsh("13 Noemvriou", 14));
        metrhshes.add(new Metrhsh("14 Noemvriou", 4));
        metrhshes.add(new Metrhsh("15 Noemvriou", 3));
        metrhshes.add(new Metrhsh("16 Noemvriou", 0.1));
        metrhshes.add(new Metrhsh("13 Noemvriou", 14));
        metrhshes.add(new Metrhsh("14 Noemvriou", 4));
        metrhshes.add(new Metrhsh("15 Noemvriou", 3));
        metrhshes.add(new Metrhsh("16 Noemvriou", 0.1));



        //custom ArrayAdapter
        class metrhshArrayAdapter extends ArrayAdapter<Metrhsh> {

            private Context context;
            private ArrayList<Metrhsh> metrhshes;

            //constructor, call on creation
            public metrhshArrayAdapter(Context context, int resource, ArrayList<Metrhsh> objects) {
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
//
//                View  = getLayoutInflater().inflate(R.layout.header, null);
//                View footer = getLayoutInflater().inflate(R.layout.footer, null);
//
//
//                view.addHeaderView(header);
//                metrhshes.addFooterView(footer);
//
//
//                View viewheader = View.inflate(context, R.layout.header, null);
//                 = (ListView) findViewById(R.id.lv);
//
//
//                lv1.addHeaderView(view);
            }
        }
        ArrayAdapter<Metrhsh> adapter = new metrhshArrayAdapter(this,0,metrhshes);

        ListView listView =(ListView) findViewById(R.id.lv);
        listView.setAdapter(adapter);

    }


}
