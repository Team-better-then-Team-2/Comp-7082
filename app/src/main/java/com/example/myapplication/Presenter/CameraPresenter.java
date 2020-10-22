package com.example.myapplication.Presenter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.room.Room;

import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Photo;
import com.example.myapplication.Model.PhotoDao;
import com.example.myapplication.Model.PhotoDatabase;
import com.example.myapplication.View.CameraView;

import java.util.List;

public class CameraPresenter implements GalleryPresenter{
    private PhotoDatabase photoDatabase;
    private PhotoDao photoDao;
    private CameraView myView;
    private Context mContext;


   public CameraPresenter(CameraView myView, Context mContext) {
       this.myView = myView;
       this.mContext = mContext;

       photoDatabase = Room.databaseBuilder(mContext,PhotoDatabase.class, "photo database")
               .allowMainThreadQueries().build();
       //for the entity, we are going to use photoDao to access those Query functions
       photoDao = photoDatabase.getPhotoDao();
    }


    //function for snap onclick
    @Override
    public int addPhoto(String name, String path, String uri, String timeStamp, String info, String city) {
        Photo photo  = new Photo(name, path,uri,timeStamp,info,city);
        int size = 0;
        photoDao.addPhoto(photo);
        size = updateView();
        myView.updatePhoto(photo);
        return size;
    }

    //function for left button onclick
    @Override
    public int leftPhoto(int id) {
       int myid = id-1;
        if (myid >= 0) {
            return grabPhoto(myid);
        }else {
            Toast toast = Toast.makeText(mContext, "This is this the first photo!", Toast.LENGTH_SHORT);
            toast.show();
            return 0;
        }

    }

    //function for right button onclick
    @Override
    public int rightPhoto(int id) {
        int myid = id+1;

            return grabPhoto(myid);
    }

    @Override
    public void updateViewFromSearchResult(int id) {
        grabPhoto(id);
    }

    @Override
    public Photo uploadBnt(int id) {
        List<Photo> photoList = photoDao.getAllPhotos();
        for(int i=0;i<photoList.size();i++) {
            Photo photo = photoList.get(i);
            if(id == photo.getId()){
                return photo;
            }
        }
        return null;
    }

    //search a photo based on ID
    private int grabPhoto(int id) {
        List<Photo> photoList = photoDao.getAllPhotos();
        if(id > photoList.size()){
            Toast toast = Toast.makeText(mContext, "This is the last photo!", Toast.LENGTH_SHORT);
            toast.show();
            return id-1;
        }else{
            for(int i=0;i<photoList.size();i++) {
                Photo photo = photoList.get(i);
                if(id == photo.getId()){
                    myView.updatePhoto(photo);
                    return id;
                }
            }
            return 0;
        }
    }

    //function for editCaption button onclick
    @Override
    public void editCaption(String time, String caption){
        //buttonCaption, captionTextView
        List<Photo> list = photoDao.getAllPhotos();
        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            if(photo.getTimeStamp().equals(time)){

                photo.setDescription(caption);
                photoDao.updatePhotos(photo);
                String text = photo.getId() + ": " + photo.getName()+ "\n" + photo.getTimeStamp() + "\n"
                        +photo.getPhoto()+ "\n" + photo.getDescription() + "\n\n\n";
                Log.d("my photos", text);//delete this and above after

                myView.updatePhoto(photo);
                break;
            }
        }
    }


    //log test function, to see whether the texts successfully created.
    private int updateView(){
        List<Photo> list = photoDao.getAllPhotos();
        String text="";

        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            text += photo.getId() + ": " + photo.getName()+ "\n" + photo.getTimeStamp() + "\n"
                    +photo.getPhoto()+ "\n" + photo.getDescription() + "\n" + photo.getLocation()+ "\n\n\n";

        }
        Log.d("my new photos", text);
        return list.size();
    }
}
