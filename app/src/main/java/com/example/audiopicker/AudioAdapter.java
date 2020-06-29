package com.example.audiopicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    ArrayList<Audio> arrayList;
    AudioClickListener audioClickListener;

    public AudioAdapter(ArrayList<Audio> arrayList, AudioClickListener audioClickListener) {
        this.arrayList = arrayList;
        this.audioClickListener = audioClickListener;
    }

//    public AudioAdapter(ArrayList<Audio> arrayList) {
//        this.arrayList = arrayList;
//    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tname, talbum, tartist;
        AudioClickListener audioClickListener;

        public AudioViewHolder(@NonNull View itemView, AudioClickListener audioClickListener) {
            super(itemView);

            tname = itemView.findViewById(R.id.audio_name);
            talbum = itemView.findViewById(R.id.audio_album);
            tartist = itemView.findViewById(R.id.audio_artist);
            this.audioClickListener = audioClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            audioClickListener.OnAudioClick(getAdapterPosition());

        }
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audiocard, parent, false);
        return new AudioViewHolder(view, audioClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        Audio now = arrayList.get(position);

        holder.tname.setText(now.getaName());
        holder.tartist.setText(now.getaAtrist());
        holder.talbum.setText(now.getaAlbum());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface AudioClickListener {
        void OnAudioClick(int position);
    }


}
