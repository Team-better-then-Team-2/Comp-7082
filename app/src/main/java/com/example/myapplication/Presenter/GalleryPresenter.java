package com.example.myapplication.Presenter;

public interface GalleryPresenter {
    void addPhoto(String name, String path, String uri, String timeStamp, String info, String city);
    int leftPhoto(int id);
    int rightPhoto(int id);
    void editCaption(String time, String caption);

}
