package com.example.smano.app;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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



        ListView listView = (ListView)findViewById(R.id.lv);


        // καποιες δικιμές για το ΑrrayAdapter το απλό

                //create a string array for listview


                //        ArrayList<String> list = new ArrayList<String>();
                //
                //        for(int i =1;i<15;i++)
                //        {
                //            list.add("Μέτρηση υπ' αριθμόν: "+ i);
                //        }
                //
                //        //create adapter to show this array like a list
                //
                //        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                // (this,android.R.layout.simple_list_item_1,list);

                //put this adapter to listview
                //
                //        listView.setAdapter(adapter);

        ArrayList<Metrhsh> metrhshes = new ArrayList<>();

        metrhshes.add(new Metrhsh("13 Noemvriou", 14));
        metrhshes.add(new Metrhsh("14 Noemvriou", 4));
        metrhshes.add(new Metrhsh("15 Noemvriou", 3));
        metrhshes.add(new Metrhsh("16 Noemvriou", 0.1));
        metrhshes.add(new Metrhsh("17 Noemvriou", 14));
        metrhshes.add(new Metrhsh("18 Noemvriou", 4));




        //Α ρε adapter...
        ArrayAdapter<Metrhsh> adapter = new MetrhshArrayAdapter(this, 0, metrhshes);



        ListView lv;
        lv = (ListView)findViewById(R.id.lv);
        View footer = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);


        lv.addFooterView(footer, null, false);


        lv.setAdapter(adapter);

//
//        final LayoutInflater factory = getLayoutInflater();
//
//        final View textEntryView = factory.inflate(R.layout.footer, null);
//        View view;
//        LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.mylayout, null);
//
//        RelativeLayout item = (RelativeLayout) view.findViewById(R.id.item);




        barChar= (BarChart) footer.findViewById(R.id.bargraph);

        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα

        ArrayList<BarEntry> Entries = new ArrayList<>();


        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes

        float ttt;
        for(int i =0;i<metrhshes.size();i++)
        {
        ttt= (float) metrhshes.get(i).getKilovatora();
        }


        for(int i =0;i<metrhshes.size();i++)
        {
            Entries.add(new BarEntry(7 ,i ));
        }

        // Τοποθέτηση δεδομένων στο barchar


        //οι δοκιμαστικές τιμές για το γράφημα
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f, 0));
        barEntries.add(new BarEntry(88f, 1));
        barEntries.add(new BarEntry(66f, 2));
        barEntries.add(new BarEntry(12f, 3));
        barEntries.add(new BarEntry(19f, 4));
        barEntries.add(new BarEntry(91f, 5));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        //η δοκιμαστική λίστα με τις μέρες
        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("19 December");
        theDates.add("20 December");
        theDates.add("21 December");
        theDates.add("22 December");
        theDates.add("23 December");
        theDates.add("24 December");

        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα

        ArrayList<String> theDays = new ArrayList<>();


        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        for(int i =0;i<metrhshes.size();i++)
        {
            theDays.add(metrhshes.get(i).getHmera());
        }

        // Τοποθέτηση δεδομένων στο barchar

        BarData theData = new BarData(theDays, barDataSet);
        barChar.setData(theData);

        //διάφορες άλλες επιλογές
        barChar.setDragEnabled(true);
        barChar.setScaleEnabled(true);
        barChar.setTouchEnabled(true);
        barChar.setNoDataText("Description that you want");


        //ένα δοκιμαστικό textView που είναι στο footer layout μαζί με το barchar
        TextView t =(TextView) findViewById(R.id.hello);
        t.setText("hi");




    }


}

