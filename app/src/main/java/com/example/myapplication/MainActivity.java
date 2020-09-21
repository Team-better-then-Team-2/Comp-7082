package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DataStorage.PhotoDao;
import com.example.myapplication.DataStorage.PhotoDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    PhotoDatabase photoDatabase;
    PhotoDao photoDao;
    Button buttonSend, buttonLeft, buttonRight, buttonSnap, buttonSearch;
    ImageView img_photo;
    String currentPhotoPath;
    TextView timeStampView;
    String timeStamp;
    static final int REQUEST_TAKE_PHOTO = 100;
    Uri photoURI;
    File storageDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoDatabase = Room.databaseBuilder(this, PhotoDatabase.class, "photo database")
                .allowMainThreadQueries().build();

        //for the entity, we are going to use photoDao to access those Query functions
        photoDao = photoDatabase.getPhotoDao();
        buttonSend = findViewById(R.id.buttonSend);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSnap = findViewById(R.id.buttonSnap);
        img_photo = findViewById(R.id.imageView);
        timeStampView = findViewById(R.id.textViewTimeStamp);

        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(!storageDir.isDirectory()){
            storageDir.mkdir();
            Log.d("uri", "onCreate: folder made");
        } else {
            Log.d("uri", "onCreate: folder already made");
        }

    }

    public void snapClicked(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        //start the camera app to take a photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File photoFile = null;
        try {
            // give value for image file");
            photoFile = createImageFile();

        } catch (IOException ex) {
            //   Log.d("fail", "no photo file");
            Log.e("MYAPP:", "exception", ex);

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(this,
                    "com.example.myapplication.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
        //  Log.d("log4", "takepictureintent is null");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = new File(storageDir, imageFileName + ".jpg");
        Log.d("uri", image.getAbsolutePath());

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            timeStampView.setText(timeStamp);
            img_photo.setImageURI(photoURI);
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }



    public void searchButton(View view) {
        Intent intent = new Intent(this, SearchViewActivity.class);
        startActivityForResult(intent, 201);
    }
}
