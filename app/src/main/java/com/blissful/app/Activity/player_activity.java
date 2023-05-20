package com.blissful.app.Activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blissful.app.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;

public class player_activity extends AppCompatActivity {

    Context context = this;
    private RewardedAd mRewardedAd;

    //  List<String> mList = new ArrayList<>();
    VideoView videoView;
    public static final String REWARD_AD_UNIT_ID= String.valueOf(R.string.Rewarded_Ad_id);
    boolean isPlay = false;
    boolean loop = true;
    String TAG = "myADS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_screen);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        loadRewardedAds();


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
          if(!internetIsConnected()) {
              new AlertDialog.Builder(player_activity.this)
                      .setTitle(getResources().getString(R.string.app_name))
                      .setMessage(getResources().getString(R.string.internet_error))
                      .setPositiveButton("OK", null).show();
              //   dialog.show();
          }
        }
        Intent i = getIntent();
        String name = i.getExtras().getString("name");
        String url = i.getExtras().getString("url");
        TextView tv1 = (TextView) findViewById(R.id.tvAudioName);
        tv1.setText(name);
        //   List<String> mList = new ArrayList<String>(Arrays.asList(url.split(",")));

        try {

            videoView = (VideoView) findViewById(R.id.videoView);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoView.setVideoURI(Uri.parse(url));
            videoView.requestFocus();
            videoView.start();

            ImageButton btnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
            btnPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPlay) {
                        videoView.start();
                        btnPlayPause.setImageResource(R.drawable.arg_pause);
                        isPlay = false;
                    } else {
                        btnPlayPause.setImageResource(R.drawable.arg_play);
                        videoView.pause();
                        isPlay = true;

                        showRewardAds();

                    }
                }
            });

            ImageButton btnFullscreen = (ImageButton) findViewById(R.id.btnFullscreen);
            btnFullscreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "Full screen is Not available in this phone", Toast.LENGTH_SHORT).show();

                }
            });


            ImageButton btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
            btnRepeat.setImageResource(R.drawable.arg_repeat_not);
            btnRepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loop) {
                        btnRepeat.setImageResource(R.drawable.arg_repeat);
                        Toast.makeText(getApplicationContext(), "Repeat mode OFF", Toast.LENGTH_SHORT).show();
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(false);
                            }
                        });
                        //   videoView.start();
                        loop = false;
                    } else {
                        btnRepeat.setImageResource(R.drawable.arg_repeat_not);
                        Toast.makeText(getApplicationContext(), "Repeat mode ON", Toast.LENGTH_SHORT).show();
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        //   videoView.start();
                        loop = true;
                    }
                }
            });
            ImageButton timer = (ImageButton) findViewById(R.id.btnTimer);
            timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(player_activity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                long hour = hourOfDay * 3600000;
                                long min = minutes * 60 * 1000;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Time Completed", Toast.LENGTH_SHORT).show();
                                        onDestroy();
                                        System.exit(0);
                                        finish();
                                    }
                                }, hour + min);
                                Toast.makeText(getApplicationContext(), hourOfDay + " Hour " + minutes + " Mintes " + " Timer is Set", Toast.LENGTH_SHORT).show();
                            }
                        }, 0, 0, false);
                        timePickerDialog.show();
                    } catch (Exception ex) {

                    }

                }
            });


        } catch (Exception ex) {
            new AlertDialog.Builder(player_activity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Failed to " + ex.getMessage())
                    .setPositiveButton("OK", null).show();
        }

    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void showRewardAds() {


        if (mRewardedAd!=null)
        {
            mRewardedAd.show(player_activity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int amount = rewardItem.getAmount();
                    String type = rewardItem.getType();
                    //       mTextView.setText("The content is unlocked : Answer is 40");
                }
        });
    }else {
        Toast.makeText(player_activity.this, "Reward ads is not ready yet", Toast.LENGTH_SHORT).show();
    }
}


    private void loadRewardedAds() {


        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, REWARD_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {


                super.onAdFailedToLoad(loadAdError);
                Log.e(TAG,loadAdError.toString());
                mRewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
                showRewardAds();
                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });

            }
        });
    }
        @Override
    public void onDestroy() {
        super.onDestroy();

    }
}