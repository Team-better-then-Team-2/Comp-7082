package com.example.myapplication.Presenter;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.myapplication.Model.Photo;
import com.example.myapplication.Model.PhotoDao;
import com.example.myapplication.Model.PhotoDatabase;

import java.util.List;

public class SearchBtnPresenter implements SearchPresenter {
    private PhotoDatabase photoDatabase;
    private PhotoDao photoDao;
    private Context mContext;

    public SearchBtnPresenter(Context mContext) {
        this.mContext = mContext;
        photoDatabase = Room.databaseBuilder(mContext,PhotoDatabase.class, "photo database")
                .allowMainThreadQueries().build();
        //for the entity, we are going to use photoDao to access those Query functions
        photoDao = photoDatabase.getPhotoDao();
    }

    @Override
    public int searchKeyword(String key,int dateFrom,int dateTo, String location1) {

        List<Photo> list = photoDao.getAllPhotos();
        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            String name = photo.getName().toLowerCase();
            String info = photo.getDescription().toLowerCase();
            String locationinfo = photo.getLocation();

            if(name.contains(key)||info.contains(key)){
                if(locationinfo.contains(location1)) {
                    if (dateFrom != 0) {
                        String temp = photo.getTimeStamp();
                        int temp2 = Integer.parseInt(temp.substring(0, 8));
                        if (temp2 >= dateFrom && temp2 <= dateTo) {

                            return photo.getId();
                        }
                    } else {
                        return photo.getId();
                    }
                }else{
                    return photo.getId();
                }
            }
        }
        return 0;
    }
/*
    void updateView(){
        List<Photo> list = photoDao.getAllPhotos();
        String text="";

        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            text += photo.getId() + ": " + photo.getName()+ "\n" + photo.getTimeStamp() + "\n"
                    +photo.getPhoto()+ "\n" + photo.getDescription() + "\n\n\n";

        }
        Log.d("my photos from search", text);
    }

 */

}
