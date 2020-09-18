package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.DataStorage.PhotoDao;
import com.example.myapplication.DataStorage.PhotoDatabase;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    PhotoDatabase photoDatabase;
    PhotoDao photoDao;
    Button buttonSend, buttonLeft, buttonRight, buttonSnap, buttonSearch;
    ImageView img_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoDatabase = Room.databaseBuilder(this,PhotoDatabase.class,"photo database")
                .allowMainThreadQueries().build();

        //for the entity, we are going to use photoDao to access those Query functions
        photoDao = photoDatabase.getPhotoDao();
        buttonSend = findViewById(R.id.buttonSend);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSnap = findViewById(R.id.buttonSnap);
        img_photo = findViewById(R.id.imageView);

    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
