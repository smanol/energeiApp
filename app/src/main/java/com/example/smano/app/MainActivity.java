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

    BarChart barChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        // Connect to the Firebase database
        Firebase myDB = new Firebase("https://energieapp-6e34c.firebaseio.com/");
        // Writing data to the database
        //myDB.child("AgnostosAgnostou").setValue("Do you have data? You'll love Firebase.");
        myDB.child("AgnostosAgnostou").addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                System.out.println("Athena's data: "+dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


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


        barChar= (BarChart) footer.findViewById(R.id.bargraph);

        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα

        ArrayList<BarEntry> Entries = new ArrayList<>();


        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes

        float t1;

        for(int i=0;i<metrhshes.size();i++)
        {

            t1= (float) metrhshes.get(i).getKilovatora();
            Entries.add(new BarEntry(t1,i));
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


        BarDataSet barDataSet = new BarDataSet(Entries, "Dates");

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

