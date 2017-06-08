package com.example.doma.travel_diary;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Doma on 2017-01-24.
 */

public class Data_Log implements Serializable {

    private String mContent;
    private String title;
    private String time;
    private String date;
    private String image_Uri;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setImage_Uri(String image_Uri) {
        this.image_Uri = image_Uri;
    }

    public String getImage_Uri() {
        return image_Uri;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
            this.time = time;
    }

    public String getmContent() {
        return mContent;
    }


    public void setmContent(String mContent) {
        this.mContent = mContent;
    }


}


