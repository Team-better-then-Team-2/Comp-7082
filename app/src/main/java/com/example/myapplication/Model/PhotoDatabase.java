package com.example.myapplication.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Photo.class}, version=1, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {
    public abstract PhotoDao getPhotoDao();
}
