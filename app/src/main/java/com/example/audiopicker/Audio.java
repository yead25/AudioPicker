package com.example.audiopicker;

import android.os.Parcelable;

import androidx.recyclerview.widget.RecyclerView;

public class Audio {

    private String aPath;
    private String aName;
    private String aAlbum;
    private String aAtrist;


    Audio(String aPath, String aName, String aAlbum, String aAtrist) {
        this.aPath = aPath;
        this.aName = aName;
        this.aAlbum = aAlbum;
        this.aAtrist = aAtrist;
    }

    public String getaPath() {
        return aPath;
    }

    public void setaPath(String aPath) {
        this.aPath = aPath;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAlbum() {
        return aAlbum;
    }

    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }

    public String getaAtrist() {
        return aAtrist;
    }

    public void setaAtrist(String aAtrist) {
        this.aAtrist = aAtrist;
    }
}
