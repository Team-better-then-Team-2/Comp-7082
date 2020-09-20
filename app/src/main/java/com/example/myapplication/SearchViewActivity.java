package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SearchViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
    }

    public void onSearchClick(View view) {
        //String filename = grabFileName; editText1.getText()toString();
        Intent intent = new Intent();
//        intent.putExtra("MESSAGE", filename);
        setResult(201, intent);
        finish();
    }

}