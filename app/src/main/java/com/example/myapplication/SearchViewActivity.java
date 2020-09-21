package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.DataStorage.Photo;
import com.example.myapplication.DataStorage.PhotoDao;
import com.example.myapplication.DataStorage.PhotoDatabase;

import java.util.List;

public class SearchViewActivity extends AppCompatActivity {

    PhotoDatabase photoDatabase;
    PhotoDao photoDao;
    private String photo_uri;
    EditText searchKeyWord;
    EditText dateFrom;
    EditText dateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        photoDatabase = Room.databaseBuilder(this, PhotoDatabase.class, "photo database")
                .allowMainThreadQueries().build();

        //for the entity, we are going to use photoDao to access those Query functions
        photoDao = photoDatabase.getPhotoDao();

        searchKeyWord = findViewById(R.id.keywordsEditText);

        updateView();

    }

    public void onSearchClick(View view) {
        String keyword = searchKeyWord.getText().toString();

        if(keyword!=null){
            Log.d("when keyword empty", keyword);
            photo_uri = searchKey(keyword.toLowerCase());

            if(photo_uri !=null){
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", photo_uri);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "Can not find the photo", Toast.LENGTH_SHORT);
                toast.show();
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "You have to input a value", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("when keyword empty2", keyword);
        }

    }
    public String searchKey(String key){
        List<Photo> list = photoDao.getAllPhotos();
        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            String name = photo.getName().toLowerCase();
            String info = photo.getDescription().toLowerCase();
            if(name.contains(key)||info.contains(key)){
                return photo.getPhoto();
            }
        }
        return null;
    }


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

}