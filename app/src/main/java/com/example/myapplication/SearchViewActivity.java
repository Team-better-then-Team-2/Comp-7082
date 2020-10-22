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

import com.example.myapplication.Model.Photo;
import com.example.myapplication.Model.PhotoDao;
import com.example.myapplication.Model.PhotoDatabase;
import com.example.myapplication.Presenter.CameraPresenter;
import com.example.myapplication.Presenter.SearchBtnPresenter;

import java.util.List;

public class SearchViewActivity extends AppCompatActivity {

    private int photo_id;
    private String location;
    EditText searchKeyWord;
    EditText editTextdateFrom;
    EditText editTextdateTo;
    EditText searchlocation;
    int dateFrom;
    int dateTo;
    SearchBtnPresenter myPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        myPresenter = new SearchBtnPresenter(this);

        editTextdateFrom = findViewById(R.id.editTextFromDate);
        editTextdateTo = findViewById(R.id.editTextToDate);


        searchKeyWord = findViewById(R.id.keywordsEditText);
        searchlocation = findViewById(R.id.editTextLocaltionName);

    }

    public void onSearchClick(View view) {
        String keyword = searchKeyWord.getText().toString();

        if(keyword!=null){
            Log.d("when keyword not empty", keyword);
            photo_id = searchKey(keyword.toLowerCase());
            if(photo_id !=0){
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", String.valueOf(photo_id));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "Can not find the photo", Toast.LENGTH_SHORT);
                toast.show();
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "You have to input a value", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public int searchKey(String key){
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

        return myPresenter.searchKeyword(key, dateFrom,dateTo,location1);
    }

    public String[] getLocationdata(String photolocation){
        return photolocation.split(", ", 2);
    }


}