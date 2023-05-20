package com.blissful.app.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blissful.app.Adapter.List_Adapter;
import com.blissful.app.Model.List_Model;
import com.blissful.app.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class List_activity extends AppCompatActivity {
    ArrayList<List_Model> ModelArrayList = new ArrayList<List_Model>();
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    String HederName;
    String VideoNames;
    String VideoUrl;
    String name;
    String url;
    int next = 0;
    String TAG = "myADS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        TextView text = findViewById(R.id.textView);
        Intent i = getIntent();
        HederName = i.getExtras().getString("name");
        VideoNames = i.getExtras().getString("VideoNames");
        VideoUrl = i.getExtras().getString("VideoUrl");
        text.setText("  " + HederName);
        SetAdapter_List();
        LoadAD();
    }

    public void SetAdapter_List() {
        List<String> VideoNamesList = new ArrayList<String>(Arrays.asList(VideoNames.split(",")));
        List<String> VideosUrlList = new ArrayList<String>(Arrays.asList(VideoUrl.split(",")));

        RecyclerView courseRV = findViewById(R.id.idRVCourse);
        try {
            for (String ds : VideoNamesList) {
                name = ds;
                url = VideosUrlList.get(next);
                ModelArrayList.add(new List_Model(name, url));
                next++;
            }
            System.out.println(ModelArrayList);
            List_Adapter adapter = new List_Adapter(getApplicationContext(), ModelArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            courseRV.setLayoutManager(linearLayoutManager);
            courseRV.setAdapter(adapter);
            Showads();
        } catch (Exception ex) {
            new AlertDialog.Builder(List_activity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Failed to " + ex.getMessage())
                    .setPositiveButton("OK", null).show();
        }

    }

    ;

    public void LoadAD() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(String.valueOf(R.string.banner_AD_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        InterstitialAd.load(this, String.valueOf(R.string.InterstitialAd_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        Showads();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    };

    public void Showads() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(List_activity.this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    mInterstitialAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    mInterstitialAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });

        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }
}

