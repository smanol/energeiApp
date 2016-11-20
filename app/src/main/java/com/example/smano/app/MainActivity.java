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

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        System.out.println("point1");

        // Connect to the Firebase database
        Firebase myDB = new Firebase("https://energieapp-6e34c.firebaseio.com/Users/");

        System.out.println("point1.5");
        // Writing data to the database
        //myDB.child("AgnostosAgnostou").setValue("Do you have data? You'll love Firebase.");
        myDB.child("George Manoliadis").addValueEventListener(new com.firebase.client.ValueEventListener() {

            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                final ArrayList<Metrhsh> metrhshes = new ArrayList<>();
                System.out.println("point2");
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " posts");
                for (com.firebase.client.DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    System.out.println("key " + postSnapshot.getKey());
                    System.out.println("value " + Double.parseDouble(postSnapshot.getValue().toString()));
                    Metrhsh metrhsh = new Metrhsh(postSnapshot.getKey(), Double.parseDouble(postSnapshot.getValue().toString()));
                    System.out.println("kilovatora " + metrhsh.getKilovatora());
                    System.out.println("hmera " + metrhsh.getHmera());
                    metrhshes.add(metrhsh);
                }

                System.out.println("POINT1"+metrhshes.size());
                createDisplay(metrhshes);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    
    public void createDisplay(ArrayList<Metrhsh> metrhshes) {
        //Adding footer on the ListView

        footer = getLayoutInflater().inflate(R.layout.footer, null);

        System.out.println("display footer"+footer.toString());
        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        ArrayList<BarEntry> Entries = new ArrayList<>();
        float t1;
        for (int i = 0; i < metrhshes.size(); i++) {
            t1 = (float) metrhshes.get(i).getKilovatora();
            Entries.add(new BarEntry(t1,i));
        }
        System.out.println("Size of Entries: "+ Entries.size());

        BarDataSet barDataSet = new BarDataSet(Entries, "Dates");

        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        ArrayList<String> theDays = new ArrayList<>();
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        for(int i = 0; i < metrhshes.size(); i++) {
            theDays.add(metrhshes.get(i).getHmera());
        }

        // Τοποθέτηση δεδομένων στο barchar
        BarData theData = new BarData(theDays, barDataSet);
        BarChart barChar = (BarChart) footer.findViewById(R.id.bargraph);
        barChar.notifyDataSetChanged();
        barChar.invalidate();
        barChar.setData(theData);
        System.out.println( "barChar: " + barChar.toString());
        System.out.println( "theData: " + theData.toString());

        System.out.println("place");
        //διάφορες άλλες επιλογές
        barChar.setDragEnabled(true);
        barChar.setScaleEnabled(true);
        barChar.setTouchEnabled(true);
        barChar.setNoDataText("Description that you want");

        ListView lv;
        lv = (ListView)findViewById(R.id.lv);
        lv.addFooterView(footer, null, false);
        lv.setAdapter(new MetrhshArrayAdapter(this, 0, metrhshes));
        //ένα δοκιμαστικό textView που είναι στο footer layout μαζί με το barchar
        //TextView t =(TextView) findViewById(R.id.hello);
        //t.setText("hi");
    }



     //TODO: implement a function that will translate the kilovatores to KilovatoresDifferences
}