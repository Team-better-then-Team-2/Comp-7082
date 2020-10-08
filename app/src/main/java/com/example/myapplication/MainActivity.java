package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.DataStorage.Photo;
import com.example.myapplication.DataStorage.PhotoDao;
import com.example.myapplication.DataStorage.PhotoDatabase;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements InfoInputDialog.InfoInputDialogListener, LocationListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    PhotoDatabase photoDatabase;
    PhotoDao photoDao;
    Button buttonCaption, buttonLeft, buttonRight, buttonSnap, buttonSearch, buttonUpload;
    ImageView img_photo;
    String currentPhotoPath;
    TextView timeStampView, captionTextView;
    String timeStamp;
    double latitude;
    double longitude;
    static final int REQUEST_TAKE_PHOTO = 100;
    static final int REQUEST_SEARCH_PHOTO = 201;
    Uri photoURI;
    int photoNumber;
    LocationManager locationManager;
    String city;

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
        buttonUpload = findViewById(R.id.buttonUpload);

        buttonSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //camera
                //There are 5 steps to take photo, means five functions are called
                //please follow the 5 steps in orderS
                dispatchTakePictureIntent();
                openDialog();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {

        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    //Camera Snap function: 2 Create a file for incoming photo
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

    //Camera Snap function: 1 open a camera intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File photoFile = null;
        try {
            // Call step 2 function, open a file for incoming photo
            photoFile = createImageFile();

        } catch (IOException ex) {
            Log.d("fail", "no photo file");

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            //After file created, save the photo in it after click snap
            photoURI = FileProvider.getUriForFile(this,
                    "com.example.myapplication.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            // call step 3 function
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }

        // call step 4 function.
    }

    //Camera Snap function: 3 update all info views in the main layout
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            timeStampView.setText(timeStamp);
            img_photo.setImageURI(photoURI);
          photoNumber = getpictureindex();
        } else if(requestCode == REQUEST_SEARCH_PHOTO) {
            if(resultCode == Activity.RESULT_OK){
                String message = data.getStringExtra("MESSAGE");
                if(!message.isEmpty()){
                    updateActivityView(message);
                }
                Log.d("onclickSearchFunction: ", message);
            }
        }
    }

    public void updateActivityView(String imageUri){
        List<Photo> list = photoDao.getAllPhotos();
        for(int i= 0; i< list.size(); ++i){
            Photo photo = list.get(i);
            Log.d("uAv", "uAv: " + photo.getPhoto() + " : " + imageUri);
            if(photo.getPhoto().equalsIgnoreCase(imageUri)){
                img_photo.setImageURI(Uri.parse(imageUri));
                timeStampView.setText(photo.getTimeStamp());
                captionTextView.setText(photo.getDescription());
            }
        }
    }

    public void searchButton(View view) {
        Intent intent = new Intent(this, SearchViewActivity.class);
        startActivityForResult(intent, REQUEST_SEARCH_PHOTO);
    }

    public void leftButton(View view) {
        if (photoNumber > 0) {
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
        updateActivityView(photo.getPhoto());
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


    //Camera Snap function: 4 after we have the picture
    //                      open a new intent to let user to enter related information
    //                      include name and description
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
                captionTextView.setText(captionTextView.getText().toString());
                break;
            }
        }
    }


    void updateLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //updates lat and long variables when location changes
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationManager.removeUpdates(this);
        }
    }

    //converts coordinates to city and province/state
    public String coordinatesToCity(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String cityName = addresses.get(0).getLocality();
            String provinceState = addresses.get(0).getAdminArea();
            return cityName + ", " + provinceState;

        } catch (IOException e) {
            e.printStackTrace();
            return "City Not Found";
        }
    }


    public void sharePhoto(String imageUri) {
        //Bitmap icon = mBitmap;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void uploadButton(View view) {

        List<Photo> photoList = photoDao.getAllPhotos();

        Photo photo = photoList.get(photoNumber);
        sharePhoto(photo.getPhotoUri());
    }
  

    //Camera Snap function: 5, there is a listener inside dialog intent
    //                      once something is enter inside dialog has been update
    //                      tihs function will be automatically called.
    //After snap a picture, make a photo object and save it into database
    //request location update
  
    @Override
    public void applyText(String name, String info) {
        updateLocation();
        city = coordinatesToCity();

        Photo photo  = new Photo(name, currentPhotoPath,photoURI.toString(),timeStamp,info,city);
        photoDao.addPhoto(photo);
        updateView();
    }


    //log test function, to see whether the texts successfully created.
    void updateView(){
        List<Photo> list = photoDao.getAllPhotos();
        String text="";

        for(int i=0;i<list.size();i++){
            Photo photo = list.get(i);
            text += photo.getId() + ": " + photo.getName()+ "\n" + photo.getTimeStamp() + "\n"
                    +photo.getPhoto()+ "\n" + photo.getDescription() + "\n" + photo.getLocaltion()+ "\n\n\n";

        }
        Log.d("my photos", text);
    }

    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}
