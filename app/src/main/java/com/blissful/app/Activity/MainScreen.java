package com.blissful.app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.blissful.app.Adapter.Albums_Adapter;
import com.blissful.app.BuildConfig;
import com.blissful.app.DB.SQLDatabase;
import com.blissful.app.Model.Albums_Model;
import com.blissful.app.R;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    public InterstitialAd mInterstitialAd;
    ActionBarDrawerToggle t;
    Context context = this;
    GridView coursesGV;
    Dialog dialog;
    String name;
    String image;
    int id;
    SQLDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mydb = new SQLDatabase(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        dialog = ProgressDialog.show(MainScreen.this, "", "Loading...", true, false);
        
        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        SetAdapter();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                SetAdapter();
            }
        }, 1000);
        
        ImageView openView = findViewById(R.id.imageButton);
        openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout navDrawer = findViewById(R.id.main_activity);
                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!navDrawer.isDrawerOpen(GravityCompat.START))
                    navDrawer.openDrawer(GravityCompat.START);
                else navDrawer.closeDrawer(GravityCompat.END);
            }
        });
        NavigationView nv = (NavigationView) findViewById(R.id.nv);
        SwitchCompat switch_id = (SwitchCompat) nv.findViewById(R.id.darkModeMenu);
        SharedPreferences sharedPreferences = getSharedPreferences("switch", MODE_PRIVATE);
        if (switch_id != null) {
            switch_id.setChecked(sharedPreferences.getBoolean("value", false));
            // on below line we are adding check change listener for our switch.
            switch_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // on below line we are checking
                    // if switch is checked or not.
                    if (switch_id.isChecked()) {
                        // on below line we are setting text
                        // if switch is checked.
                        Toast.makeText(MainScreen.this, "Turn On Your Phone DarkMode", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                        setTheme(R.style.Theme_RelaxingMusic_Night);
                        editor.putBoolean("value", true);
                        editor.apply();
                        switch_id.setChecked(true);
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                        setTheme(R.style.Theme_RelaxingMusic);
                        editor.putBoolean("value", false);
                        editor.apply();
                        switch_id.setChecked(false);

                    }
                }
            });
        }
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.share:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, String.valueOf(R.string.app_name));
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {}
                        break;
                    case R.id.darkModeMenu:
                        Toast.makeText(MainScreen.this, "Turn On Your Phone DarkMode", Toast.LENGTH_LONG).show();

                        break;
                    case R.id.rate:
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(myAppLinkToMarket);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainScreen.this, " unable to find market app", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.About:
                        Uri uri2 = Uri.parse("https://Google.com");
                        Intent Aboutpage = new Intent(Intent.ACTION_VIEW, uri2);
                        try {
                            startActivity(Aboutpage);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(MainScreen.this, " unable to Open", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });


    }

    public void SetAdapter() {
        try {
            ArrayList<Albums_Model> Categories = new ArrayList<Albums_Model>();
            Categories.clear();
            Cursor rs = mydb.getReadableDatabase().rawQuery("select * from Categories", null);
            rs.moveToFirst();
            if (Categories.isEmpty()) {
                coursesGV = findViewById(R.id.idGVcourses);
                while (rs.isAfterLast() == false) {
                    name = rs.getString(1);
                    image = rs.getString(2);
                    id = rs.getInt(0);
                    Log.e("SQL", id + name + image);
                    Categories.add(new Albums_Model(id, name, image));
                    rs.moveToNext();
                }
                Albums_Adapter adapter = new Albums_Adapter(getApplicationContext(), Categories);
                coursesGV.setAdapter(adapter);

            }


            coursesGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent,
                                        View v, int position, long id) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainScreen.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                    Cursor db = mydb.getData(Categories.get(position).getId());
                    db.moveToFirst();
                    Intent i = new Intent(context, List_activity.class);
                    i.putExtra("VideoNames", db.getString(3));
                    i.putExtra("VideoUrl", db.getString(4));
                    i.putExtra("name", Categories.get(position).getCourse_name());
                    startActivity(i);
                }
            });
            dialog.cancel();

        } catch (Exception ex) {
            Log.e("Ex", ex.getMessage());
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainScreen.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


