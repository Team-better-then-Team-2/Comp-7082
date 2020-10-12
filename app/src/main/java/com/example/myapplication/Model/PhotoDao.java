package com.example.myapplication.Model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotoDao {
    @Insert
    void addPhoto(Photo photo);

    @Update
    void updatePhotos(Photo photo);

    @Delete
    void DeletePhotos(Photo photo);

    @Query("DELETE FROM PHOTO")
    void DeleteAll();

    @Query("SELECT * FROM PHOTO ORDER BY ID DESC")
    List<Photo> getAllPhotos();

}
