package com.example.audiopicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;

public class AudioPicker extends AppCompatActivity implements AudioAdapter.AudioClickListener , Audio_fragment.Fragment_listen {

    public static final String TAG = "audio";
    private ArrayList<Audio> arrayList;

     public static Context appContext;
    public static Context getContextOfapp()
    {
        return appContext;
    }

    private RecyclerView recyclerView;
    AudioAdapter audioAdapter;
    Button refresh_button ,close_button;
    private UnifiedNativeAd nativeAd;

    FrameLayout frameLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_picker);

        appContext= getApplicationContext();
        arrayList = getAudio();
        recyclerView = findViewById(R.id.audio_picker_RecylerView);
        audioAdapter = new AudioAdapter(arrayList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AudioPicker.this
                ,RecyclerView.VERTICAL,false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AudioPicker.this,3,
                GridLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(audioAdapter);

        refresh_button = findViewById(R.id.refresh);

        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_ad();
            }
        });

        frameLayout = findViewById(R.id.audio_fragment);
        frameLayout.setVisibility(View.GONE);

        refresh_ad();
        close_button= findViewById(R.id.close);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout= findViewById(R.id.ad_holder);
                if(nativeAd!=null)  nativeAd.destroy();
                frameLayout.setVisibility(View.GONE);
                refresh_button.setVisibility(View.GONE);
                close_button.setVisibility(View.GONE);
            }
        });


    }
    private void refresh_ad(){

        refresh_button.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110" );

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if(nativeAd!=null)
                {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout = findViewById(R.id.ad_holder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.nativeadd,null);

                populateUnifiedNativeAdVIew(unifiedNativeAd,adView);

                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        AdLoader adLoader = builder.withAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(AudioPicker.this, "failed wiht "+i, Toast.LENGTH_SHORT).show();
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
        refresh_button.setEnabled(true);
    }

    private void populateUnifiedNativeAdVIew(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView){
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        //adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((MediaView)adView.getMediaView()).setMediaContent(nativeAd.getMediaContent());

        if(nativeAd.getBody() == null){
          adView.getBodyView().setVisibility(View.INVISIBLE);
        }else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView)adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

       /* if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }*/

        adView.setNativeAd(nativeAd);

    }



    private ArrayList<Audio> getAudio() {
        ArrayList<Audio> temp = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {"DISTINCT "+MediaStore.Audio.Media.BUCKET_DISPLAY_NAME};

        Cursor audioCursor = getContentResolver().query(uri,projection,null,null, null);

       if(audioCursor!=null){
           while (audioCursor.moveToNext())
           {
               String name       = audioCursor.getString(0);
               String album      = "3434";


               temp.add(new Audio("",name,"Music Folder",""));

               Log.d(TAG, "getAudio: "+name+" " );


           }
           audioCursor.close();
       }
       else
       {
           Toast.makeText(this, "No audio Found", Toast.LENGTH_SHORT).show();
       }
        return temp;
    }

    @Override
    public void OnAudioClick(int position) {


       // Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();

        frameLayout.setVisibility(View.VISIBLE);
        //recyclerView.setVisibility(View.INVISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Audio_fragment audio_fragment = new Audio_fragment(arrayList.get(position).getaName());
        transaction.add(R.id.audio_fragment,audio_fragment);
        transaction.addToBackStack("now");
        recyclerView.setVisibility(View.GONE);
        transaction.commit();




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(nativeAd!=null)  nativeAd.destroy();
    }

    @Override
    public void onDistroy() {
        frameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void Detail(String Name, String Album, String Artist) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.AUDIO_NAME_TAG,Name);
        intent.putExtra(MainActivity.AUDIO_ALBUM_TAG,Album);
        intent.putExtra(MainActivity.AUDIO_ATRIST_TAG,Artist);
        setResult(RESULT_OK,intent);
        finish();
    }
}
