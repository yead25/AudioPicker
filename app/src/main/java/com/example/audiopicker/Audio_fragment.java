package com.example.audiopicker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Audio_fragment extends Fragment implements AudioAdapter2.AudioSelect {
    ArrayList<Audio> arrayList;
    Fragment_listen fragment_listen;

    @Override
    public void Select(int position) {
        fragment_listen.Detail(arrayList.get(position).getaName(), arrayList.get(position).getaAlbum(),
                arrayList.get(position).getaAtrist());
        onDestroy();
    }

    public interface Fragment_listen {
        void onDistroy();

        void Detail(String Name, String Album, String Artist);

    }

    String bucket;

    public Audio_fragment(String bucket) {
        this.bucket = bucket;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.audio_fragment, container, false);
        arrayList = new ArrayList<>();
        Context context = AudioPicker.getContextOfapp();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.BUCKET_DISPLAY_NAME};
        String s = MediaStore.Audio.Media.BUCKET_DISPLAY_NAME;

        Cursor audioCursor = context.getContentResolver().query(uri, projection,
                s + "=?", new String[]{bucket}, null);

        if (audioCursor != null) {
            while (audioCursor.moveToNext()) {

                String name = audioCursor.getString(0);

                String album = audioCursor.getString(2);
                String artist = audioCursor.getString(1);

                arrayList.add(new Audio("", name, album, artist));


            }
            audioCursor.close();
        }

        RecyclerView recyclerView = v.findViewById(R.id.fragment_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        AudioAdapter2 audioAdapter2 = new AudioAdapter2(arrayList, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(audioAdapter2);

        return v;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Fragment_listen)
            fragment_listen = (Fragment_listen) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment_listen.onDistroy();
    }
}
