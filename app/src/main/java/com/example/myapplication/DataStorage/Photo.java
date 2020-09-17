package com.example.myapplication.DataStorage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;

@Entity
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private File photo;

    private String timeStamp;

    private String description;

    private String localtion;

    public Photo(String name, File photo, String timeStamp, String description, String localtion) {
        this.name = name;
        this.photo = photo;
        this.timeStamp = timeStamp;
        this.description = description;
        this.localtion = localtion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

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
