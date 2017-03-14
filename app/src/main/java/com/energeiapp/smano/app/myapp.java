package com.energeiapp.smano.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
/**
 * Created by Georgios.Manoliadis on 18/12/2016.
 */

public class myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FirebaseApp.initializeApp(this);
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
