package com.example.smano.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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
    TextView Teams;
    TextView MOKatanalwsh;
    TextView XrewshEwsTwra;
    TextView LogariasmosE;
    TextView MeiwshE;
    TextView OmadaKatanalwshInput;
    TextView OmadaKatanalwsh;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";
    private ValueEventListener valueEventListener;
    private ValueEventListener valueEventListenerForBlocks;
    private ValueEventListener valueEventListenerTeam;
    private ValueEventListener valueEventListenerT;
    private DatabaseReference myDB;
    private MetrhshArrayAdapter metrhshArrayAdapter;
    private int hasNightMeasurements = 0;
    private TextView measurementsLegend;
    private ArrayList<Metrhsh> wholeValuesMetrhseis;
    private int team = 0;
    private int countGourounakia = 0;
    private TextView gourounakiaText;
    private TextView ExoikonomhshText;
    private ImageView gourounakiaImage;
    private double sumOfSavings;
    private TextView DaysLeft;
    private long countOfUsers = 0;
    private TextView moBase;
    private int ranking = 0;

    float team1 = 0.0f;
    float team2 = 0.0f;


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
                    username = getUsernameFromFireBaseUser(user);

                    onSignedInInitialize();
                } else {
                    //user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()/*,
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()*/))
                                    .build(),
                            RC_SIGN_IN);

                    registerUserOnDB(firebaseAuth.getCurrentUser());
                }
            }
        };
    }

    private void init() {
        footer = getLayoutInflater().inflate(R.layout.footer, null);
        barChart = (BarChart) footer.findViewById(R.id.bargraph2);
        barChar = (BarChart) footer.findViewById(R.id.bargraph);
        lv = (ListView)findViewById(R.id.lv);

        lv.addFooterView(footer, null, false);
        View header = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
        lv.addHeaderView(header, null, false);
    }

    public void createDisplay(ArrayList<Metrhsh> metrhshes) {

        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        Entries.clear();
        float t1;
        for (int i =0; i < metrhshes.size(); i++) {
            t1 = (float) metrhshes.get(i).getSumKilovatora();
            Entries.add(new BarEntry(t1, i));
        }

        barDataSet = new BarDataSet(Entries, "KWh");
        barDataSet.notifyDataSetChanged();
        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        theDays.clear();
        for (int i = 0; i < metrhshes.size(); i++) {
            theDays.add(transformDateToDayMonth(metrhshes.get(i).getDay()));
        }
        // Τοποθέτηση δεδομένων στο barchar
        theData = new BarData(theDays, barDataSet);
        theData.notifyDataChanged();

        //barChar.clear();
        barChar.setData(theData);
        barChar.notifyDataSetChanged();
        barChar.invalidate();
        barChar.setDescription("");

        //διάφορες άλλες επιλογές
        barChar.setDragEnabled(true);
        barChar.setScaleEnabled(true);
        barChar.setTouchEnabled(true);
        barChar.getAxisLeft().setStartAtZero(true);
        barChar.setNoDataText("Description that you want");

        metrhshArrayAdapter = new MetrhshArrayAdapter(this, 0, metrhshes);
        lv.setAdapter(metrhshArrayAdapter);


        //Είκόνα για γουρουνάκια

//            gourounakiaImage = (ImageView) findViewById(R.id.pigsImage);
//            gourounakiaImage.setImageResource(R.drawable.piggy);
//




        // Στοιχεία Δοκιμαστικής Περιόδου
        if (metrhshes.size()>=3){

//            TextView averageOf3kwh = (TextView) findViewById(R.id.MOKatanalwshsBase);
            double averO3 = getAverageConsumptionOf3DayInit(metrhshes)+getAverageConsumptionOf3NightInit(metrhshes);
            double averageOf3 = CostEstimate.round((averO3),2);
//            averageOf3kwh.setText(averageOf3+" KWh");

//            TextView consumptionOf3 =(TextView) findViewById(R.id.MeanCostBase);
            double consumptionOf3Cost = (CostEstimate.calculateCostDay(getAverageConsumptionOf3DayInit(metrhshes),1)+CostEstimate.calculateCostNight(getAverageConsumptionOf3NightInit(metrhshes),1))/120;
//            consumptionOf3.setText(CostEstimate.round(consumptionOf3Cost,2)+" \u20ac");

//            TextView BillOf3 =(TextView) findViewById(R.id.CostProjectionBase);
            double consumptionOf3Bill = (CostEstimate.calculateCostDay(getAverageConsumptionOf3DayInit(metrhshes),1)+CostEstimate.calculateCostNight(getAverageConsumptionOf3NightInit(metrhshes),1));
//            BillOf3.setText(CostEstimate.round(consumptionOf3Bill,2)+" \u20ac");



            //Γενικά στοιχεία


                gourounakiaText = (TextView) findViewById(R.id.SynoloPontwn);
                gourounakiaText.setText(countGourounakia + "");

                //Στοιχεία για την συνολική εξοικονόμηση


                //Days Left Text View


                DaysLeft = (TextView) findViewById(R.id.MeresPouApomenoyn);
                int meres = 14 - metrhshes.size();
                DaysLeft.setText("" + meres);

                MOKatanalwsh = (TextView) findViewById(R.id.MOKatanalwshs);

                MOKatanalwsh.setText(CostEstimate.round(getAverageConsumption(metrhshes), 2) + " KWh");

                LogariasmosE = (TextView) findViewById(R.id.LogariasmosEktimhsh);

                Double kwhEwsTwraDay = wholeValuesMetrhseis.get(metrhshes.size()).getDayKilovatora() - wholeValuesMetrhseis.get(0).getDayKilovatora();

                Double kwhEwsTwraNight = wholeValuesMetrhseis.get(metrhshes.size()).getNightKilovatora() - wholeValuesMetrhseis.get(0).getNightKilovatora();

                Double costEwsTwraDay = CostEstimate.calculateCostDay(kwhEwsTwraDay, metrhshes.size()) / 120 * metrhshes.size();

                Double costEwsTwraNight = CostEstimate.calculateCostNight(kwhEwsTwraNight, metrhshes.size()) / 120 * metrhshes.size();

                Double xrewshEwsTwra = costEwsTwraDay + costEwsTwraNight;

                XrewshEwsTwra = (TextView) findViewById(R.id.XrewshEwsTwra);

                XrewshEwsTwra.setText(CostEstimate.round(xrewshEwsTwra, 2) + " \u20ac");


                Double logariasmosE = CostEstimate.calculateCostDay(kwhEwsTwraDay, metrhshes.size())+CostEstimate.calculateCostNight(kwhEwsTwraNight, metrhshes.size());

                LogariasmosE.setText(CostEstimate.round(logariasmosE,2)+" \u20ac");

                TextView MeiwshEktimhsh =(TextView) findViewById(R.id.MeiwshEktimhsh);

                double meiwshEkt = CostEstimate.round(consumptionOf3Bill-logariasmosE,2);

                if (meiwshEkt>0 ) {
                    MeiwshEktimhsh.setText(meiwshEkt + " \u20ac");
                }
                else{
                    MeiwshEktimhsh.setText(0 + " \u20ac");
            }
        }





    }


    private void displayTeamsText() {

        Teams =  (TextView) findViewById(R.id.TheTeamYouBelong);


        String teamNote = "?";

        if (team == 1) {
            teamNote = "A";

            OmadaKatanalwsh = (TextView) findViewById(R.id.Omada_Katataksh);
            if (OmadaKatanalwsh != null) {
                OmadaKatanalwsh.setText("Ομάδα");


            }
            if (Teams !=  null)
            {
                Teams.setText("Ανήκετε στην ομάδα " + teamNote);
            }




            OmadaKatanalwshInput = (TextView) findViewById(R.id.Omada_KatatakshInput);
            if (OmadaKatanalwshInput != null) {
                OmadaKatanalwshInput.setText(teamNote);

            }

        }
        if (team == 2 ) {
            teamNote = "B";
            OmadaKatanalwsh = (TextView) findViewById(R.id.Omada_Katataksh);
            if (OmadaKatanalwsh != null) {
                OmadaKatanalwsh.setText("Κατάταξη");
            }
            OmadaKatanalwshInput = (TextView) findViewById(R.id.Omada_KatatakshInput);
            if (ranking != 0 && OmadaKatanalwshInput != null) {
                OmadaKatanalwshInput.setText(ranking + "η θέση");
            }
            //Katataksh
            if (Teams !=  null)
            {
                Teams.setText("Είστε στη "+ ranking + "η θέση.");
            }

        }
    }



    private void createBlockBarchart(float block1, float block2) {
        ArrayList<String> theDate = new ArrayList<>();
        if (team==1) {
            theDate.add("Ομάδα Α");
            theDate.add("Ομάδα Β");
        }
        else
        {
            theDate.add("Εσείς");
            theDate.add("Μ.Ο. Συμπαικτών");
        }

        ArrayList<BarEntry> Entries2 = new ArrayList<>();
        Entries2.add(new BarEntry(block1, 0));
        Entries2.add(new BarEntry(block2, 1));
        BarDataSet barDataSet2 = new BarDataSet(Entries2, "Πόντοι");
        BarData theData2 = new BarData(theDate, barDataSet2);
        barChart.setData(theData2);
        barChart.setNoDataText("Description that you want");
        barChart.getAxisLeft().setStartAtZero(true);
        barChart.setDescription("");

    }

    private String transformDateToDayMonth(String inputDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        DateFormat targetFormat = new SimpleDateFormat("dd/MM", new Locale("el", "GR"));
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
        attachDatabaseReadListener();
        dayOnlyMeasurementButtonListener();
        dayNightMeasurementButtonListener();
        buttonValidator();
    }

    private void onSignedOutCleanup() {
        username = ANONYMOUS;
        if (metrhshArrayAdapter != null) {
            metrhshArrayAdapter.clear();
        }
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Παρακαλώ Περιμένετε...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        if (username != null && !ANONYMOUS.equals(username)) {
            if (valueEventListenerTeam == null) {
                valueEventListenerTeam = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object t;
                        if ((t = dataSnapshot.getValue()) != null){
                            team = Integer.parseInt(t.toString());


                            if (valueEventListenerForBlocks == null) {
                                valueEventListenerForBlocks = new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Object t1 = dataSnapshot.child("Team1").getValue();
                                        if (t1 != null) {
                                            team1 = Float.parseFloat(t1.toString());
                                        }
                                        Object t2 = dataSnapshot.child("Team2").getValue();
                                        if (t2 != null) {
                                            team2 = Float.parseFloat(t2.toString());
                                        }
                                        if (team == 1) {
                                            createBlockBarchart(team1, team2);
                                        } else if (team == 2) {
                                            listenForCountOfUsers();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                };
                                myDB.child("Blocks").addValueEventListener(valueEventListenerForBlocks);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                myDB.child("Users").child(username).child("Team").addListenerForSingleValueEvent(valueEventListenerTeam);
            }

            if (valueEventListener == null) {
                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        wholeValuesMetrhseis = new ArrayList<>();
                        final ArrayList<Metrhsh> metrhshes = new ArrayList<>();
                        double dayKilovatora = -1;
                        double nightKilovatora = -1;
                        double subtractedDayKilovatora = -1;
                        double subtractedNightKilovatora = -1;
                        double previousNight = -1;
                        double previousDay = -1;
                        double average = -1;
                        double averageOf3= -1;
                        double averageOf3Day= -1;
                        double averageOf3Night= -1;

                        String day = "";
                        int counter = 0;
                        Metrhsh wholeMetrhsh;
                        countGourounakia = 0;
                        sumOfSavings=0;
                        for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            day = postSnapshot.getKey();
                            try {
                                dayKilovatora = -1;
                                nightKilovatora = -1;
                                dayKilovatora = Double.parseDouble(postSnapshot.child("Day").getValue().toString());
                                nightKilovatora = Double.parseDouble(postSnapshot.child("Night").getValue().toString());
                            } catch (NullPointerException npe) {
                                ;
                            }
                            if (previousDay != -1 && dayKilovatora != -1) {
                                Metrhsh met;
                                subtractedDayKilovatora = dayKilovatora - previousDay;

                                average = getAverageConsumption(metrhshes);
                                averageOf3Day = getAverageConsumptionOf3Day(metrhshes);
                                averageOf3Night = getAverageConsumptionOf3Night(metrhshes);
                                averageOf3= (averageOf3Day + averageOf3Night);

                                if (previousNight != -1 && nightKilovatora != -1) {
                                    subtractedNightKilovatora = nightKilovatora - previousNight;
                                    met = new Metrhsh(postSnapshot.getKey(), subtractedDayKilovatora, subtractedNightKilovatora, average, averageOf3,averageOf3Day,averageOf3Night);
                                    day = postSnapshot.getKey();
                                    metrhshes.add(met);
                                } else {
                                    met = new Metrhsh(postSnapshot.getKey(), subtractedDayKilovatora, 0, average, averageOf3,averageOf3Day,averageOf3Night);
                                    metrhshes.add(met);
                                }


                                countGourounakia += met.getGourounakia();
                                sumOfSavings += met.getSavings();
                            }
                            if (dayKilovatora != -1) {
                                previousDay = dayKilovatora;
                                hasNightMeasurements = 1;
                            }
                            if (nightKilovatora != -1) {
                                previousNight = nightKilovatora;
                                hasNightMeasurements = 2;
                            }


                            if (dayKilovatora != -1 && nightKilovatora != -1) {
                                wholeMetrhsh = new Metrhsh(postSnapshot.getKey(), dayKilovatora, nightKilovatora);
                                if (wholeMetrhsh != null) {
                                    wholeValuesMetrhseis.add(wholeMetrhsh);
                                }
                            } else if (dayKilovatora != -1) {
                                wholeMetrhsh = new Metrhsh(postSnapshot.getKey(), dayKilovatora, 0);
                                wholeValuesMetrhseis.add(wholeMetrhsh);
                            }


                            counter++;
                        }
                        createDisplay(metrhshes);
                        checkForMeasurementBoxRemoval(day);
                        displayTeamsText();
                        checkLayout(counter);
                        getRanking();
                        dialog.hide();
                        uploadComparableVariable(countGourounakia);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                myDB.child("Users").child(username).child("Measurements").addValueEventListener(valueEventListener);
            }
        }

    }

    private void listenForCountOfUsers() {
        myDB.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               countOfUsers = dataSnapshot.getChildrenCount();
               float averagePigsPerUser = (team1 + team2) / countOfUsers ;
               createBlockBarchart(countGourounakia, averagePigsPerUser);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       }

        );
    }

    private void uploadComparableVariable(int countGourounakia) {
        myDB.child("Users").child(username).child("ComparableVariable").setValue(countGourounakia);

        valueEventListenerT = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object t = dataSnapshot.getValue();
                if (t != null){
                    team = Integer.parseInt(t.toString());
                    summationOfPigs();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myDB.child("Users").child(username).child("Team").addValueEventListener(valueEventListenerT);
    }

    private void summationOfPigs() {
        ValueEventListener summation = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double counter = 0;
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    try {
                        //if (user.child("Team").equals(myteam)) {
                        DataSnapshot cv = user.child("ComparableVariable");
                        if (cv != null) {
                            Object cvv;
                            if ((cvv = cv.getValue()) != null) {
                                counter += Double.parseDouble(cvv.toString());
                            }
                        }
                        //  }
                    } catch (Exception e) {
                        ;
                    }
                }
                //if (counter != 0 && myteam != 0) {
                myDB.child("Blocks").child("Team" + team).setValue(counter);
                //}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        //Every user counts his team's piggies
        if (team != 0) {
            myDB.child("Users").orderByChild("Team").equalTo(Integer.toString(team)).addListenerForSingleValueEvent(summation);
        }
    }

    private void getRanking() {
        ValueEventListener rankingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ranking = 1;
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                   try {
                       Object val = user.child("ComparableVariable").getValue();
                       String str = val.toString();
                       int l = Integer.valueOf(str);
                       if (l > countGourounakia) {
                           ranking++;
                       }
                   } catch (Exception e) {
                       ;
                   }
                }
                displayTeamsText();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        // Count the ranking by retrieving the users that have a greater ComparableVariable value
        myDB.child("Users").orderByChild("ComparableVariable").startAt(countGourounakia).addListenerForSingleValueEvent(rankingListener);
    }


    private void dayOnlyMeasurementButtonListener() {
        final Button button = (Button) findViewById(R.id.input_button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    makeNotification();
                    String EMPTY_TEXT_BOX = "Δεν είναι δυνατή η αποστολή της μέτρησης. Παρακαλώ εισάγετε έγκυρη τιμή στο κουτί μέτρησεων.";
                    String measurement = editText.getText().toString();
                    if (StringUtils.isBlank(measurement)) {
                        Toast.makeText(MainActivity.this, EMPTY_TEXT_BOX, Toast.LENGTH_LONG).show();
                    } else {
                        boolean bool = checkInvalidMeasurement(measurement, "0");
                        if (bool) {
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            myDB.child("Users").child(username).child("Measurements").child(date).child("Day").setValue(measurement);
                            removeDayOnlyMeasurementBoxLinearLayout();
                        }
                    }
                }
            });
        }
    }

    private void dayNightMeasurementButtonListener() {
        final Button button = (Button) findViewById(R.id.input_buttonNight);
        final EditText editTextDay = (EditText) findViewById(R.id.editTextDay);
        final EditText editTextNight = (EditText) findViewById(R.id.editTextNight);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    makeNotification();
                    String EMPTY_TEXT_BOX = "Δεν είναι δυνατή η αποστολή της μέτρησης. Παρακαλώ εισάγετε έγκυρες τιμές στο κουτί μέτρησεων.";
                    String measurementDay = editTextDay.getText().toString();
                    String measurementNight = editTextNight.getText().toString();
                    if (StringUtils.isBlank(measurementDay) || StringUtils.isBlank(measurementNight)) {
                        Toast.makeText(MainActivity.this, EMPTY_TEXT_BOX, Toast.LENGTH_LONG).show();
                    } else {
                        boolean bool = checkInvalidMeasurement(measurementDay, measurementNight);
                        if (bool) {
                            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            myDB.child("Users").child(username).child("Measurements").child(date).child("Day").setValue(measurementDay);
                            myDB.child("Users").child(username).child("Measurements").child(date).child("Night").setValue(measurementNight);
                            removeDayNightMeasurementBoxLinearLayout();
                        }
                    }
                }
            });
        }
    }

    private void removeGraphKwh() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.Graphs);
        LinearLayout child = (LinearLayout) findViewById(R.id.KwhGraph);
        parent.removeView(child);

    }

    private void removeGraphCompare() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.Graphs);
        LinearLayout child = (LinearLayout) findViewById(R.id.CopmareGraph);
        parent.removeView(child);
    }

    private void removeMeasurementsLegend() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.measurementLegendLL);
        TextView child = (TextView) findViewById(R.id.measurementsLegend);
        if (child != null) {
            measurementsLegend = child;
        }
        parent.removeView(child);
    }

    private void addMeasurementsLegend() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.measurementLegendLL);
        if (measurementsLegend == null) {
            measurementsLegend = (TextView) findViewById(R.id.measurementsLegend);
        }
        if (measurementsLegend != null && measurementsLegend.getParent() == null && parent != null) {
            parent.addView(measurementsLegend);
        }
    }

    private void removeInstructionsLinearLayout() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        TextView child = (TextView) findViewById(R.id.eisagwgiko);
        parent.removeView(child);
    }

    private void removeDayNightInvoiceLegends() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        TextView child = (TextView) findViewById(R.id.exeteGeniko);
        TextView childNight = (TextView) findViewById(R.id.exeteNyxterino);
        parent.removeView(child);
        parent.removeView(childNight);
    }

    private void removeInputLegend() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        TextView child = (TextView) findViewById(R.id.eisageteThShmerinh);
        parent.removeView(child);
    }

    private void removeDayOnlyMeasurementBoxLinearLayout() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        LinearLayout child = (LinearLayout) findViewById(R.id.measurementBox);
        parent.removeView(child);
    }


    private void removeGeneralInfoBoxLinearLayout() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        LinearLayout child = (LinearLayout) findViewById(R.id.GenikaInfoBox);
        parent.removeView(child);}

//    private void removeDokimastikhPeriodosBox() {
//        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
//        LinearLayout child = (LinearLayout) findViewById(R.id.DokimastikhPeriodosBox);
//        parent.removeView(child);}


    private void removeDayNightMeasurementBoxLinearLayout() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        LinearLayout child = (LinearLayout) findViewById(R.id.measurementBoxNight);
        parent.removeView(child);
    }

    private void checkForMeasurementBoxRemoval(String day) {
        if (DateUtils.isToday (day)) {
            removeInputLegend();
            removeDayOnlyMeasurementBoxLinearLayout();
            removeDayNightMeasurementBoxLinearLayout();
        } else if (hasNightMeasurements == 1) {
            removeDayNightMeasurementBoxLinearLayout();      // remove Night MeasurementBox if the first entry is for Day-Only Measurement
        } else if (hasNightMeasurements == 2) {
            removeDayOnlyMeasurementBoxLinearLayout();       // remove Day-Only MeasurementBox if the first entry is for DayNight Measurement
        }
    }

    private void checkLayout(int c) {
        if (c >= 2){
            removeInstructionsLinearLayout();
        }
//        if (c < 14){
//            removeDokimastikhPeriodosBox();
//        }

        if (c < 4){

            removeGeneralInfoBoxLinearLayout();
        }
        if (c >= 1){
            removeDayNightInvoiceLegends();
        }
        if (c < 2){
            removeMeasurementsLegend();
        } else {
            addMeasurementsLegend();
        }
        if (c < 3){

            removeGraphCompare();
        }
        if (c<4){
            removeGraphKwh();
        }
    }



    private boolean checkInvalidMeasurement(String measurementDay, String measurementNight) {
        final String largerThan40Message = "Η μέτρηση είναι πολυ μεγάλη αναλογικά με τις προηγούμενες τιμές που έχετε εισάγει.";
        final String smallerThanPreviousMessage = "Η μέτρηση που εισάγατε είναι μικρότερη απο την τελευταία μέτρηση που εισάγατε.";
        final String correctMessage = "Η μέτρηση που εισάγατε καταχωρήθηκε επιτυχώς.";
        final double MAX_ALLOWED_KILOVAT_PER_DAY = 40;

        if (wholeValuesMetrhseis.size() > 0) {
            double lastDatabaseValue = wholeValuesMetrhseis.get(wholeValuesMetrhseis.size() - 1).getDayKilovatora();
            double measure = Double.parseDouble(measurementDay);
            double diff = measure - lastDatabaseValue;

            double diffNight = 0;
            double measureNight = Double.parseDouble(measurementNight);
            if (measureNight != 0) {
                double lastDatabaseValueNight = wholeValuesMetrhseis.get(wholeValuesMetrhseis.size() - 1).getNightKilovatora();
                diffNight = measure - lastDatabaseValueNight;
            }

            if (diff > MAX_ALLOWED_KILOVAT_PER_DAY || diffNight > MAX_ALLOWED_KILOVAT_PER_DAY) {
                popupMessage(largerThan40Message);
                return false;
            } else if (diff < 0 || diffNight < 0) {
                popupMessage(smallerThanPreviousMessage);
                return false;
            }
        }
        popupMessage(correctMessage);
        return true;
    }

    private void detachDatabaseReadListener() {
        if (valueEventListener != null) {
            myDB.child("Users").child(username).child("Measurements").removeEventListener(valueEventListener);
            valueEventListener = null;
        }
        if (valueEventListenerForBlocks != null) {
            myDB.child("Blocks").removeEventListener(valueEventListenerForBlocks);
            valueEventListenerForBlocks = null;
        }
    }

    private void buttonValidator() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                updateButtonState();
            }
        };

        final EditText editTextDay = (EditText) findViewById(R.id.editTextDay);
        final EditText editTextNight = (EditText) findViewById(R.id.editTextNight);
        if (editTextDay != null) {
            editTextDay.addTextChangedListener(tw);
        }
        if (editTextNight != null) {
            editTextNight.addTextChangedListener(tw);
        }
    }

    private void updateButtonState() {
        Button b = (Button) findViewById(R.id.input_buttonNight);
        final EditText editTextDay = (EditText) findViewById(R.id.editTextDay);
        final EditText editTextNight = (EditText) findViewById(R.id.editTextNight);
        if (editTextDay != null && editTextNight != null) {
            String s1 = editTextDay.getText().toString();
            String s2 = editTextNight.getText().toString();
            b.setEnabled(!s1.trim().isEmpty() && !s2.trim().isEmpty());
        }
    }

    private void registerUserOnDB(FirebaseUser user) {
        if (user != null) {
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

    private void popupMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }


    public static double getAverageConsumption(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        double averageMerthsh = 0;
        if (metrhshes.size() == 0) {
            return -1;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            sumMetrhseis += metrhshes.get(i).getSumKilovatora();
        }
        averageMerthsh = sumMetrhseis/metrhshes.size();
        return averageMerthsh;
    }
    public static double getAverageConsumptionDay(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        double averageMerthsh = 0;
        if (metrhshes.size() == 0) {
            return -1;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            sumMetrhseis += metrhshes.get(i).getDayKilovatora();
        }
        averageMerthsh = sumMetrhseis/metrhshes.size();
        return averageMerthsh;
    }
    public static double getAverageConsumptionNight(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        double averageMerthsh = 0;
        if (metrhshes.size() == 0) {
            return -1;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            sumMetrhseis += metrhshes.get(i).getNightKilovatora();
        }
        averageMerthsh = sumMetrhseis/metrhshes.size();
        return averageMerthsh;
    }
    public static double getSumNightKilo(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        if (metrhshes.size() == 0) {
            return 0;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            sumMetrhseis += metrhshes.get(i).getNightKilovatora();
        }
        return sumMetrhseis;

    }
    public static double getSumDayKilo(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        if (metrhshes.size() == 0) {
            return 0;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            sumMetrhseis += metrhshes.get(i).getDayKilovatora();
        }
        return sumMetrhseis;

    }
    public static double getCostSumNightKilo(ArrayList<Metrhsh> metrhshes) {
        double costSumMetrhseis = 0;
        if (metrhshes.size() == 0) {
            return 0;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            costSumMetrhseis += CostEstimate.calculateCostNight(metrhshes.get(i).getNightKilovatora(),1)/120;
        }
        return costSumMetrhseis;

    }
    public static double getCostSumDayKilo(ArrayList<Metrhsh> metrhshes) {
        double costSumMetrhseis = 0;
        if (metrhshes.size() == 0) {
            return 0;
        }
        for (int i = 0; i < metrhshes.size() ; i++) {
            costSumMetrhseis += CostEstimate.calculateCostDay(metrhshes.get(i).getDayKilovatora(),1)/120;
        }
        return costSumMetrhseis;

    }



    public double getAverageConsumptionOf3Day(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        if (metrhshes.size() <= 1) {
            return -1;
        } else if (metrhshes.size() == 2){
            for (int i = 0; i < 2 ; i++) {
                sumMetrhseis += metrhshes.get(i).getDayKilovatora();
            }
            return sumMetrhseis/2;
        } else {
            for (int i = 0; i < 3 ; i++) {
                sumMetrhseis += metrhshes.get(i).getDayKilovatora();
            }
            double averageOf3 = sumMetrhseis/3;
            return averageOf3;
        }
    }

    public double getAverageConsumptionOf3DayInit(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        if (metrhshes.size() <= 1) {
            return -1;
        } else if (metrhshes.size() == 2){
            for (int i = 0; i < 2 ; i++) {
                sumMetrhseis += metrhshes.get(i).getDayKilovatora();
            }
            return sumMetrhseis/2;
        } else {
            for (int i = 0; i < 3; i++) {
                sumMetrhseis += metrhshes.get(metrhshes.size() - i - 1).getDayKilovatora();
            }
            double averageOf3 = sumMetrhseis / 3;
            return averageOf3;
        }
    }

    public double getAverageConsumptionOf3NightInit(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        if (metrhshes.size() <= 1) {
            return -1;
        } else if (metrhshes.size() == 2){
            for (int i = 0; i < 2 ; i++) {
                sumMetrhseis += metrhshes.get(i).getDayKilovatora();
            }
            return sumMetrhseis/2;
        } else {
            for (int i = 0; i < 3; i++) {
                sumMetrhseis += metrhshes.get(metrhshes.size() - i - 1).getNightKilovatora();
            }
            double averageOf3 = sumMetrhseis / 3;
            return averageOf3;
        }
    }
    public double getAverageConsumptionOf3Night(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        if (metrhshes.size() <= 1) {
            return -1;
        } else if (metrhshes.size() == 2){
            for (int i = 0; i < 2 ; i++) {
                sumMetrhseis += metrhshes.get(i).getNightKilovatora();
            }
            return sumMetrhseis/2;
        } else {
            for (int i = 0; i < 3 ; i++) {
                sumMetrhseis += metrhshes.get(i).getNightKilovatora();
            }
            return sumMetrhseis/3;
        }
    }



    public void makeNotification(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);

        Intent intent = new Intent(getApplicationContext(), NotifyService.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_FIFTEEN_MINUTES , pendingIntent);
    }
}