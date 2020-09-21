package com.example.myapplication.DataStorage;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;


@Entity
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String photo;

    private String photoUri;

    private String timeStamp;

    private String description;

    private String localtion;

    public Photo(String name, String photo, String photoUri, String timeStamp, String description, String localtion) {
        this.name = name;
        this.photo = photo;
        this.photoUri = photoUri;
        this.timeStamp = timeStamp;
        this.description = description;
        this.localtion = localtion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoUri() { return photoUri; }

    public void setPhotoURI(String photoUri) { this.photoUri = photoUri; }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocaltion() {
        return localtion;
    }

    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }

}
