package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.DataStorage.Photo;
import com.example.myapplication.DataStorage.PhotoDao;
import com.example.myapplication.DataStorage.PhotoDatabase;

import java.util.List;

public class SearchViewActivity extends AppCompatActivity {

    PhotoDatabase photoDatabase;
    PhotoDao photoDao;
    private String photo_uri;
    private String location;
    EditText searchKeyWord;
    EditText editTextdateFrom;
    EditText editTextdateTo;
    EditText searchlocation;
    int dateFrom;
    int dateTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        photoDatabase = Room.databaseBuilder(this, PhotoDatabase.class, "photo database")
                .allowMainThreadQueries().build();

        //for the entity, we are going to use photoDao to access those Query functions
        photoDao = photoDatabase.getPhotoDao();

        editTextdateFrom = findViewById(R.id.editTextFromDate);
        editTextdateTo = findViewById(R.id.editTextToDate);


        searchKeyWord = findViewById(R.id.keywordsEditText);
        searchlocation = findViewById(R.id.editTextLocaltionName);
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
        String date1;
        String date2;
        String location1;
        date1 = editTextdateFrom.getText().toString();
        date2 = editTextdateTo.getText().toString();
        location1 = searchlocation.getText().toString();

        if(!date1.isEmpty() && !date2.isEmpty()){
            dateFrom = Integer.parseInt(date1);
            dateTo = Integer.parseInt(date2);
        }else{
            dateFrom = 0;
            dateTo = 0;
        }

        Log.d("time limit", "From " + dateFrom +", To "+dateTo);

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
                        Log.d("time", "From " + dateFrom + ", To " + dateTo + " , Now: " + temp2);

                        if (temp2 >= dateFrom && temp2 <= dateTo) {

                            return photo.getPhoto();
                        }
                    } else {
                        return photo.getPhoto();
                    }
                }
            }
        }
        return null;
    }

    public String[] getLocationdata(String photolocation){
        return photolocation.split(", ", 2);
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