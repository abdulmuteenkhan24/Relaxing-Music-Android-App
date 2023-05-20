package com.blissful.app.DB;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDB  extends Service {

    String TAG = "KHAN";
    String Name;
    String Imageurl;
    String VideosName;
    String VideosLink;
    int id = 1;

    public void GetData(Context context) {
        try {
            SQLDatabase mydb = new SQLDatabase(context);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Tables");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (!mydb.isTableExists("Categories")) {
                            mydb.CreateTable();
                            for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                Log.e("DBCHECK", String.valueOf(productSnapshot));
                                Name = productSnapshot.child("NAME").getValue().toString();
                                Imageurl = productSnapshot.child("IMAGE").getValue().toString();
                                VideosName = productSnapshot.child("VIDEOS_NAME").getValue().toString();
                                VideosLink = productSnapshot.child("VIDEOS_LINK").getValue().toString();
                                mydb.insertCategories(Name, Imageurl, VideosName, VideosLink);
                            }
                        } else {
                            mydb.deleteAll("Categories");
                            for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                Log.e("DBCHECK", String.valueOf(productSnapshot));
                                Name = productSnapshot.child("NAME").getValue().toString();
                                Imageurl = productSnapshot.child("IMAGE").getValue().toString();
                                VideosName = productSnapshot.child("VIDEOS_NAME").getValue().toString();
                                VideosLink = productSnapshot.child("VIDEOS_LINK").getValue().toString();
                                mydb.insertCategories(Name, Imageurl, VideosName, VideosLink);
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }

            });
        }catch (Exception exception){
            Log.e("Catch",exception.getMessage());
        }
     }

    public void UpdateData(Context context) {
        try {
            SQLDatabase mydb = new SQLDatabase(context);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Tables");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                            Log.e(TAG, String.valueOf(productSnapshot));
                            Name = productSnapshot.child("NAME").getValue().toString();
                            Imageurl = productSnapshot.child("IMAGE").getValue().toString();
                            VideosName = productSnapshot.child("VIDEOS_NAME").getValue().toString();
                            VideosLink = productSnapshot.child("VIDEOS_LINK").getValue().toString();
                            mydb.updateCategories(id ,Name, Imageurl, VideosName, VideosLink);
                            id++;
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }catch (Exception exception){
            Log.e("Catch",exception.getMessage());
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
