package com.example.smano.app;

import android.content.Context;
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
import java.util.Date;
import java.util.Locale;
import java.util.Random;


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
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";
    private ValueEventListener valueEventListener;
    private ValueEventListener valueEventListenerForBlocks;
    private ValueEventListener valueEventListenerTeam;
    private DatabaseReference myDB;
    private MetrhshArrayAdapter metrhshArrayAdapter;
    private int hasNightMeasurements = 0;
    private TextView measurementsLegend;
    private ArrayList<Metrhsh> wholeValuesMetrhseis;
    private int team;
    private int countGourounakia =0;
    private TextView gourounakiaText;
    private TextView ExoikonomhshText;
    private ImageView gourounakiaImage;
    private double sumOfSavings;
    private TextView DaysLeft;

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
        //Adding footer on the ListView
        metrhshes = transformArraylistsDatesToWords(metrhshes);
        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        Entries.clear();
        float t1;
        for (int i = 0; i < metrhshes.size(); i++) {
            t1 = (float) metrhshes.get(i).getSumKilovatora();
            Entries.add(new BarEntry(t1, i));
        }

        barDataSet = new BarDataSet(Entries, "KWh");
        barDataSet.notifyDataSetChanged();
        // Εδω βάζω τις τιμές απο την λίστα metrhshes στο γράφημα
        // εδω χρείάζεται να γίνει με for loop για να μπαίνουν αυτόματα όλες οι τιμές της λίστας metrhshes
        theDays.clear();
        for (int i = 0; i < metrhshes.size(); i++) {
            theDays.add(metrhshes.get(i).getDay());
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

        double averageOf3 = getAverageConsumptionOf3(metrhshes);

        //Εισαγωγή πληροφοριών σχετικά με την ομάδα για την Συνθήκη νούμερα ένα


        Teams = (TextView) footer.findViewById(R.id.TheTeamYouBelong);


        String teamNote = "?";

        if (team == 1) {
            teamNote = "A";
        }
        if (team == 2) {
            teamNote = "B";
        }


        Teams.setText("Ανοίκεται στην ομάδα " + teamNote);


        if (countGourounakia > 0) {


            //Είκόνα για γουρουνάκια

            gourounakiaImage = (ImageView) findViewById(R.id.pigsImage);
            gourounakiaImage.setImageResource(R.drawable.piggy);


            //Στοιχεία για γουρουνάκια

            gourounakiaText = (TextView) findViewById(R.id.pigsText);

            gourounakiaText.setText(countGourounakia + "X ");

            //Στοιχεία για την συνολική εξοικονόμηση

            ExoikonomhshText = (TextView) findViewById(R.id.exoikonomhsh);


            ExoikonomhshText.setText("'Εχετε εξοικονομήσει μέχρι στιγμής "+CostEstimate.round(sumOfSavings,2)+"\u20ac"+ " !" );

            //Days Left Text View


            DaysLeft = (TextView) findViewById(R.id.daysLeft);


            DaysLeft.setText( "Έχετε συμπληρώσει : " +"\n" + (metrhshes.size())+" /14 μέρες" );
        }
    }




    private void createBlockBarchart(float block1, float block2) {
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("Ομάδα Α");
        theDate.add("Ομάδα Β");
        ArrayList<BarEntry> Entries2 = new ArrayList<>();
        Entries2.add(new BarEntry(block1, 0));
        Entries2.add(new BarEntry(block2, 1));
        BarDataSet barDataSet2 = new BarDataSet(Entries2, "KWh");
        BarData theData2 = new BarData(theDate, barDataSet2);
        barChart.setData(theData2);
        barChart.setNoDataText("Description that you want");
        barChart.getAxisLeft().setStartAtZero(true);
    }

    private ArrayList<Metrhsh> transformArraylistsDatesToWords(ArrayList<Metrhsh> metrhshes) {
        String newDate;
        for (int i = 0; i < metrhshes.size(); i++) {
            newDate = transformDateToWords(metrhshes.get(i).getDay());
            if (newDate != null) {
                metrhshes.get(i).setDay(newDate);
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
        if (username != null && !ANONYMOUS.equals(username)) {
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
                                averageOf3 = getAverageConsumptionOf3(metrhshes);

                                if (previousNight != -1 && nightKilovatora != -1) {
                                    subtractedNightKilovatora = nightKilovatora - previousNight;
                                    met = new Metrhsh(postSnapshot.getKey(), subtractedDayKilovatora, subtractedNightKilovatora, average, averageOf3);
                                    day = postSnapshot.getKey();
                                    metrhshes.add(met);
                                } else {
                                    met = new Metrhsh(postSnapshot.getKey(), subtractedDayKilovatora, 0, average, averageOf3);
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
                        checkLayout(counter);//over 1.5   //under 2.5
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                myDB.child("Users").child(username).child("Measurements").addValueEventListener(valueEventListener);
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

        if (valueEventListenerTeam == null) {
            valueEventListenerTeam = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object t;
                    if ((t = dataSnapshot.getValue()) != null){
                        team = Integer.parseInt(t.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            myDB.child("Users").child(username).child("Team").addValueEventListener(valueEventListenerTeam);
        }
    }

    private void dayOnlyMeasurementButtonListener() {
        final Button button = (Button) findViewById(R.id.input_button);
        final EditText editText = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

    private void dayNightMeasurementButtonListener() {
        final Button button = (Button) findViewById(R.id.input_buttonNight);
        final EditText editTextDay = (EditText) findViewById(R.id.editTextDay);
        final EditText editTextNight = (EditText) findViewById(R.id.editTextNight);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

    private void showGraphs() {
        footer.setVisibility(View.VISIBLE);
    }

    private void removeGraphLinearLayout() {
        //lv.removeFooterView(footer);
        footer.setVisibility(View.GONE);
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

    private void removeDayNightMeasurementBoxLinearLayout() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.mainLinearLayout);
        LinearLayout child = (LinearLayout) findViewById(R.id.measurementBoxNight);
        parent.removeView(child);
    }

    private void checkForMeasurementBoxRemoval(String day) {
        if (!day.isEmpty()) {
            //Metrhsh lastItem = metrhshes.get(metrhshes.size()-1);
            //String lastDatabaseDate = lastItem.getDay();
            String today = new SimpleDateFormat("dd MMMM", new Locale("el", "GR")).format(new Date());
            String formattedDay = "";
            try {
                formattedDay = transformDateToWords(day);
            } catch (Exception e) {
                return;
            }
            if (formattedDay != null && formattedDay.equals(today)) {
                removeInputLegend();
                removeDayOnlyMeasurementBoxLinearLayout();
                removeDayNightMeasurementBoxLinearLayout();
            } else if (hasNightMeasurements == 1) {
                removeDayNightMeasurementBoxLinearLayout();      // remove Night MeasurementBox if the first entry is for Day-Only Measurement
            } else if (hasNightMeasurements == 2) {
                removeDayOnlyMeasurementBoxLinearLayout();       // remove Day-Only MeasurementBox if the first entry is for DayNight Measurement
            }
        }
    }

    private void checkLayout(int c) {
        if (c >= 2){
            removeInstructionsLinearLayout();
        }
        if (c <= 2){
            removeGraphLinearLayout();
        }
        if (c >= 1){
            removeDayNightInvoiceLegends();
        }
        if (c <= 1){
            removeMeasurementsLegend();
        } else {
            addMeasurementsLegend();
        }
        if (c >= 3){
            showGraphs();
        }
    }



    private boolean checkInvalidMeasurement(String measurementDay, String measurementNight) {
        final String largerThan40Message = "Η μέτρηση είναι πολυ μεγάλη αναλογικά με τις προηγούμενες τιμές που έχετε εισάγει.";
        final String smallerThanPreviousMessage = "Η μέτρηση που εισάγατε είναι μικρότερη απο την τελευταία μέτρηση που εισάγατε.";
        final String correctMessage = "Η μέτρηση που εισάγατε καταχωρήθηκε επιτυχώς.";
        final double MAX_ALLOWED_KILOVAT_PER_DAY = 40;

        double lastDatabaseValue = wholeValuesMetrhseis.get(wholeValuesMetrhseis.size()-1).getDayKilovatora();
        double measure = Double.parseDouble(measurementDay);
        double diff = measure - lastDatabaseValue;

        double diffNight = 0;
        double measureNight = Double.parseDouble(measurementNight);
        if (measureNight != 0) {
            double lastDatabaseValueNight = wholeValuesMetrhseis.get(wholeValuesMetrhseis.size()-1).getNightKilovatora();
            diffNight = measure - lastDatabaseValueNight;
        }

        if (diff > MAX_ALLOWED_KILOVAT_PER_DAY || diffNight > MAX_ALLOWED_KILOVAT_PER_DAY) {
            popupMessage(largerThan40Message);
            return false;
        } else if (diff < 0 || diffNight < 0 ) {
            popupMessage(smallerThanPreviousMessage);
            return false;
        } else {
            popupMessage(correctMessage);
            return true;
        }
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
        editTextDay.addTextChangedListener(tw);
        editTextNight.addTextChangedListener(tw);
    }

    private void updateButtonState() {
        Button b = (Button) findViewById(R.id.input_buttonNight);
        final EditText editTextDay = (EditText) findViewById(R.id.editTextDay);
        final EditText editTextNight = (EditText) findViewById(R.id.editTextNight);
        String s1 = editTextDay.getText().toString();
        String s2 = editTextNight.getText().toString();
        b.setEnabled(!s1.trim().isEmpty() && !s2.trim().isEmpty());
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

        /*new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();*/
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

    private double coins (double kilovatwra, double mean)
    {
        Random randomGenerator = new Random();

        int randomInt = randomGenerator.nextInt(100);

        double coinsAmount;
        coinsAmount = kilovatwra*mean * randomInt;
        return coinsAmount;
    }


    public double getAverageConsumptionOf3(ArrayList<Metrhsh> metrhshes) {
        double sumMetrhseis = 0;
        double averageMerthsh = 0;
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
            return sumMetrhseis/3;
        }
    }



}