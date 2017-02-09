package com.example.smano.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.AuthUI;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

//import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;


public class MainActivity extends AppCompatActivity {
    String username;
    View footer;
    ArrayList<BarEntry> Entries = new ArrayList<>();
    BarDataSet barDataSet;
    ArrayList<String> theDays = new ArrayList<>();
    BarData theData;
    BarChart barChar;
    BarChart barChart;
    ListView lv;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";
    private ValueEventListener valueEventListener;
    private ValueEventListener valueEventListenerForBlocks;
    private DatabaseReference myDB;
    private MetrhshArrayAdapter metrhshArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //username = ANONYMOUS;

        init();

        //Firebase.setAndroidContext(this);
        //FirebaseApp.initializeApp(this);

        // Connect to the Firebase database
        myDB = FirebaseDatabase.getInstance().getReferenceFromUrl("https://energieapp-6e34c.firebaseio.com/");
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Writing data to the database
        //myDB.child("AgnostosAgnostou").setValue("Do you have data? You'll love Firebase.");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    Toast.makeText(MainActivity.this, "You're signed in. Welcome to EnergieApp", Toast.LENGTH_SHORT).show();
                    username = getUsernameFromFireBaseUser(user);

                    onSignedInInitialize();
                } else {
                    //user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);

                    registerUserOnDB(firebaseAuth.getCurrentUser());
                }
            }
        };
    }



    private void init() {
        footer = getLayoutInflater().inflate(R.layout.footer, null);
        barChart= (BarChart) footer.findViewById(R.id.bargraph2);
        barChar = (BarChart) footer.findViewById(R.id.bargraph);
        lv = (ListView)findViewById(R.id.lv);
        lv.addFooterView(footer, null, false);
        View header = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
        lv.addHeaderView(header, null, false);
    }

    public void createDisplay(ArrayList<Metrhsh> metrhshes) {
        //Adding footer on the ListView
        metrhshes = transformArraylistsDatesToWords(metrhshes);
        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        Entries.clear();
        float t1;
        for (int i = 0; i < metrhshes.size(); i++) {
            t1 = (float) metrhshes.get(i).getKilovatora();
            Entries.add(new BarEntry(t1,i));
        }

        barDataSet = new BarDataSet(Entries, "Dates");
        barDataSet.notifyDataSetChanged();
        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        theDays.clear();
        for(int i = 0; i < metrhshes.size(); i++) {
            theDays.add(metrhshes.get(i).getHmera());
        }
        // Τοποθέτηση δεδομένων στο barchar
        theData = new BarData(theDays, barDataSet);
        theData.notifyDataChanged();

        //barChar.clear();
        barChar.setData(theData);
        barChar.notifyDataSetChanged();
        barChar.invalidate();

        //διάφορες άλλες επιλογές
        barChar.setDragEnabled(true);
        barChar.setScaleEnabled(true);
        barChar.setTouchEnabled(true);
        barChar.getAxisLeft().setStartAtZero(true);
        barChar.setNoDataText("Description that you want");
        metrhshArrayAdapter = new MetrhshArrayAdapter(this, 0, metrhshes);
        lv.setAdapter(metrhshArrayAdapter);
        //ένα δοκιμαστικό textView που είναι στο footer layout μαζί με το barchar
        //TextView t =(TextView) findViewById(R.id.hello);
        //t.setText("hi");
    }

    private void createBlockBarchart(float block1, float block2) {
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("Πολυκατοικία Α");
        theDate.add("Πολυκατοικία Β");
        ArrayList<BarEntry> Entries2 = new ArrayList<>();
        Entries2.add(new BarEntry(block1, 0));
        Entries2.add(new BarEntry(block2, 1));
        BarDataSet barDataSet2 = new BarDataSet(Entries2, "Dates");
        BarData theData2 = new BarData(theDate, barDataSet2);
        barChart.setData(theData2);
        barChart.setNoDataText("Description that you want");
        barChart.getAxisLeft().setStartAtZero(true);
    }

    private ArrayList<Metrhsh> transformArraylistsDatesToWords(ArrayList<Metrhsh> metrhshes) {
        String newDate;
        double kilovatora;
        for (int i = 0; i < metrhshes.size(); i++) {
            newDate = transformDateToWords(metrhshes.get(i).getHmera());
            kilovatora = metrhshes.get(i).getKilovatora();
            if (newDate != null) {
                metrhshes.set(i, new Metrhsh(newDate,kilovatora));
            }
        }
        return metrhshes;
    }

    private String transformDateToWords(String inputDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        DateFormat targetFormat = new SimpleDateFormat("dd MMMM", new Locale("el", "GR"));
        return targetFormat.format(date);
    }

    //Activity on the foreground
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    //Activity no longer on the foreground
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        if (metrhshArrayAdapter != null) {
            metrhshArrayAdapter.clear();
        }
        detachDatabaseReadListener();
    }

    private void onSignedInInitialize() {
        ArrayList<Metrhsh> m = attachDatabaseReadListener();
        measurementButtonListener(m);
    }

    private void onSignedOutCleanup() {
        username = ANONYMOUS;
        if (metrhshArrayAdapter != null) {
            metrhshArrayAdapter.clear();
    }
        detachDatabaseReadListener();
    }

    private ArrayList<Metrhsh> attachDatabaseReadListener() {
        final ArrayList<Metrhsh> metrhshes = new ArrayList<>();
        if (username != null && !ANONYMOUS.equals(username)) {
            if (valueEventListener == null) {
                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double previous = -1;
                        double subtractedKilovatora = -1;
                        for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            double current = Double.parseDouble(postSnapshot.getValue().toString());
                            if (previous != -1) {
                                subtractedKilovatora = current - previous;
                                Metrhsh metrhsh = new Metrhsh(postSnapshot.getKey(), subtractedKilovatora);
                                metrhshes.add(metrhsh);
                            }
                            previous = current;
                        }
                        createDisplay(metrhshes);

                        checkForMeasurementBoxRemoval(metrhshes);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                myDB.child("Users").child(username).addValueEventListener(valueEventListener);
            }
        }
        if (valueEventListenerForBlocks == null) {
            valueEventListenerForBlocks = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    float block1 = Float.parseFloat(dataSnapshot.child("Block1").getValue().toString());
                    float block2 = Float.parseFloat(dataSnapshot.child("Block2").getValue().toString());
                    createBlockBarchart(block1, block2);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            myDB.child("Blocks").addValueEventListener(valueEventListenerForBlocks);
        }
        return metrhshes;
    }

    private void measurementButtonListener(final ArrayList<Metrhsh> metrhshes) {
        final Button button = (Button) findViewById(R.id.input_button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String measurement = editText.getText().toString();
                //checkInvalidMeasurement(measurement, metrhshes);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                myDB.child("Users").child(username).child(date).setValue(measurement);
                removeMeasurementBoxLinearLayout();
            }
        });
    }

    private void removeMeasurementBoxLinearLayout() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        LinearLayout child = (LinearLayout) findViewById(R.id.measurementBox);
        parent.removeView(child);
    }

    private void checkForMeasurementBoxRemoval(ArrayList<Metrhsh> metrhshes) {
        if (metrhshes != null && !metrhshes.isEmpty()) {
            Metrhsh lastItem = metrhshes.get(metrhshes.size()-1);
            String lastDatabaseDate = lastItem.getHmera();
            String today = new SimpleDateFormat("dd MMMM", new Locale("el", "GR")).format(new Date());
            if (lastDatabaseDate.equals(today)) {
                removeMeasurementBoxLinearLayout();
            }
        }
    }

    private void checkInvalidMeasurement(String measurement, ArrayList<Metrhsh> metrhshes) {
        double GREATEST_ALLOWED_KILOVAT_PER_DAY = 40;
        double lastDatabaseValue = metrhshes.get(metrhshes.size()-1).getKilovatora();
        double measure = Double.parseDouble(measurement);
        double diff = measure - lastDatabaseValue;
        if (diff > GREATEST_ALLOWED_KILOVAT_PER_DAY) {

        } else if (diff <= 0) {

        }
    }

    private void detachDatabaseReadListener() {
        if (valueEventListener != null) {
            myDB.child("Users").child(username).removeEventListener(valueEventListener);
            valueEventListener = null;
        }
        if (valueEventListenerForBlocks != null) {
            myDB.child("Blocks").removeEventListener(valueEventListenerForBlocks);
            valueEventListenerForBlocks = null;
        }
    }

    private void registerUserOnDB(FirebaseUser user) {
        if (user != null){
            String username = usernameFromEmail(user.getEmail());

            // Write new user
            writeNewUser(username);
        }
    }

    private String getUsernameFromFireBaseUser(FirebaseUser user) {
        return usernameFromEmail(user.getEmail());
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String name) {
        User user = new User("2000-01-01", "");

        myDB.child("Users").child(name).setValue(user);
    }



    //TODO: implement a function that will translate the kilovatores to KilovatoresDifferences
}