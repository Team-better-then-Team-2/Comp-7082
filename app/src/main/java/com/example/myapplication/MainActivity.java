package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.myapplication.DataStorage.Photo;
import com.example.myapplication.DataStorage.PhotoDao;
import com.example.myapplication.DataStorage.PhotoDatabase;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InfoInputDialog.InfoInputDialogListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    PhotoDatabase photoDatabase;
    PhotoDao photoDao;
    Button buttonCaption, buttonLeft, buttonRight, buttonSnap, buttonSearch;
    ImageView img_photo;
    String currentPhotoPath;
    TextView timeStampView, captionTextView;
    String timeStamp;
    static final int REQUEST_TAKE_PHOTO = 100;
    Uri photoURI;
    int photoNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoDatabase = Room.databaseBuilder(this, PhotoDatabase.class, "photo database")
                .allowMainThreadQueries().build();

        //for the entity, we are going to use photoDao to access those Query functions
        photoDao = photoDatabase.getPhotoDao();

        buttonLeft = findViewById(R.id.buttonLeft);
        buttonRight = findViewById(R.id.buttonRight);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSnap = findViewById(R.id.buttonSnap);
        img_photo = findViewById(R.id.imageView);
        timeStampView = findViewById(R.id.textViewTimeStamp);
        buttonCaption = findViewById(R.id.editCaptionBtn);
        captionTextView = findViewById(R.id.edit_Add_Captions);
        photoNumber = getpictureindex();

        buttonSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images");
        try {
            //make sure the directory was created
            storageDir.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File image = new File(storageDir, imageFileName + ".jpg");
        Log.d("uri", image.getAbsolutePath());

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File photoFile = null;
        try {
            // give value for image file");
            photoFile = createImageFile();

        } catch (IOException ex) {
            //   Log.d("fail", "no photo file");

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(this,
                    "com.example.myapplication.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

        openDialog();
        //  Log.d("log4", "takepictureintent is null");
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            timeStampView.setText(timeStamp);
            img_photo.setImageURI(photoURI);
            photoNumber = getpictureindex();
        }
    }

    public void searchButton(View view) {
        Intent intent = new Intent(this, SearchViewActivity.class);
        startActivityForResult(intent, 201);
    }

    public void leftButton(View view) {
        if (photoNumber > 1) {
            grabPhoto(--photoNumber);
        }
    }

    public void rightButton(View view) {
        List<Photo> photoList = photoDao.getAllPhotos();

        if (photoNumber < photoList.size() -1) {
            grabPhoto(++photoNumber);
        }
    }

    public void grabPhoto(int y) {
        List<Photo> photoList = photoDao.getAllPhotos();

        Photo photo = photoList.get(y);
        Uri uri = Uri.parse(photo.getPhotoUri());
        img_photo.setImageURI(uri);
    }

    public int getpictureindex(){
        List<Photo> list = photoDao.getAllPhotos();
        for(int i= 0; i< list.size(); ++i){
            Photo photo = list.get(i);
            if(photo.getTimeStamp().equals(timeStampView.getText().toString())){
                return photo.getId();
            }
        }
        return 0;
    }

    public void openDialog(){
        InfoInputDialog myDialog = new InfoInputDialog();
        myDialog.show(getSupportFragmentManager(),"information dilog");

    }

    public void editCaption(View view){
        //buttonCaption, captionTextView
        List<Photo> list = photoDao.getAllPhotos();
        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            if(photo.getTimeStamp().equals(timeStampView.getText().toString())){
                photo.setDescription(captionTextView.getText().toString());
                photoDao.updateWords(photo);
                String text = photo.getId() + ": " + photo.getName()+ "\n" + photo.getTimeStamp() + "\n"
                        +photo.getPhoto()+ "\n" + photo.getDescription() + "\n\n\n";
                Log.d("my photos", text);//delete this and above after
                captionTextView.setText("");
                break;
            }
        }
    }

    @Override
    public void applyText(String name, String info) {
        Photo photo  = new Photo(name, currentPhotoPath,photoURI.toString(),timeStamp,info,"Vancouver");
        photoDao.addPhoto(photo);
        updateView();
    }

    void updateView(){
        List<Photo> list = photoDao.getAllPhotos();
        String text="";

        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            text += photo.getId() + ": " + photo.getName()+ "\n" + photo.getTimeStamp() + "\n"
                    +photo.getPhoto()+ "\n" + photo.getDescription() + "\n\n\n";

        }
        Log.d("my photos", text);
    }
}
