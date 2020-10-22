package com.example.myapplication.Presenter;

import com.example.myapplication.Model.Photo;

public interface GalleryPresenter {
    int addPhoto(String name, String path, String uri, String timeStamp, String info, String city);
    int leftPhoto(int id);
    int rightPhoto(int id);
    void editCaption(String time, String caption);
    void updateViewFromSearchResult(int id);
    Photo uploadBnt(int id);
}
