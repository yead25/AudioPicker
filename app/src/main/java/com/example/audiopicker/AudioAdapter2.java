package com.example.audiopicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AudioAdapter2 extends RecyclerView.Adapter<AudioAdapter2.AViewHolder> {

    ArrayList<Audio> arrayList;
    AudioSelect audioSelect;

    public AudioAdapter2(ArrayList<Audio> arrayList, AudioSelect audioSelect) {
        this.arrayList = arrayList;
        this.audioSelect = audioSelect;
    }


    public class AViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tname, talbum, tartist;
        ImageView imageView;

        public AViewHolder(@NonNull View itemView, AudioSelect audioSelect) {
            super(itemView);
            tname = itemView.findViewById(R.id.audio_name);
            talbum = itemView.findViewById(R.id.audio_album);
            tartist = itemView.findViewById(R.id.audio_artist);
            imageView = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            audioSelect.Select(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audiocard, parent, false);
        return new AViewHolder(v, audioSelect);
    }

    @Override
    public void onBindViewHolder(@NonNull AViewHolder holder, int position) {
        Audio now = arrayList.get(position);

        holder.tname.setText(now.getaName());
        holder.tartist.setText(now.getaAtrist());
        holder.talbum.setText(now.getaAlbum());
        holder.imageView.setImageResource(R.drawable.audio_ic);

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface AudioSelect {
        void Select(int position);
    }
}
