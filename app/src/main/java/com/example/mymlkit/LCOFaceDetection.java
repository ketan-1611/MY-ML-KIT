package com.example.mymlkit;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class LCOFaceDetection extends Application {

    public static String resultTxt = "Result Text";
    public static String resDialog = "Result Dialog";

    public void onCreate() {

        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
