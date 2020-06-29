package com.example.audiopicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int READ_REQUESTCODE = 1;
    public static final int AUDIO_FILE_REQUEST_CODE = 4;
    public static final String AUDIO_NAME_TAG = "audio_name";
    public static final String AUDIO_ALBUM_TAG = "audio_album";
    public static final String AUDIO_ATRIST_TAG = "audio_artist";
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        load_interstitialAd();

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                pick_audio();
                load_interstitialAd();
            }
        });

    }

    Boolean flip = false;

    public void load_interstitialAd() {
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void start(View view) {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (interstitialAd.isLoaded() && flip == true) {
                interstitialAd.show();
                flip = false;

            } else {

                if (flip == false) flip = true;
                pick_audio();

            }
        } else {

            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUESTCODE);

        }

    }

    void pick_audio() {
        Intent intent = new Intent(MainActivity.this, AudioPicker.class);
        startActivityForResult(intent, AUDIO_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUDIO_FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                RelativeLayout relativeLayout = findViewById(R.id.audio_holder);
                relativeLayout.setVisibility(View.VISIBLE);
                TextView textname, textartist, textalbum;

                textname = findViewById(R.id.audio_name);
                textartist = findViewById(R.id.audio_artist);
                textalbum = findViewById(R.id.audio_album);

                String name = data.getStringExtra(AUDIO_NAME_TAG);
                name = name_proces(name);
                textname.setText(name);
                textartist.setText(data.getStringExtra(AUDIO_ATRIST_TAG));
                textalbum.setText(data.getStringExtra(AUDIO_ALBUM_TAG));


            }
        }
    }

    public String name_proces(String name) {
        name = name.substring(0, name.length() - 4);

        return name;

    }
}

