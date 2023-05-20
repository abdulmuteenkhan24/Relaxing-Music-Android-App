package com.blissful.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.blissful.app.DB.FirebaseDB;
import com.blissful.app.DB.SQLDatabase;
import com.blissful.app.R;

public class Splashscreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 1000;
    SQLDatabase mydb;
    Context context = this;
    String name;
    String image;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_RelaxingMusic);
        setContentView(R.layout.splash_screen);
        super.onCreate(savedInstanceState);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        mydb = new SQLDatabase(context);
        if (netInfo != null && internetIsConnected()) {
                new FirebaseDB().GetData(context);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen.this, MainScreen.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }


    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

}
