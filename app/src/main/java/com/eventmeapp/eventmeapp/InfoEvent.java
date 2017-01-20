package com.eventmeapp.eventmeapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Erik on 12/24/2016.
 */

public class InfoEvent {
    private LatLng mLatLng;
    private String mTitle, mLocation, mDescription;
    private int mYear, mMonth, mDay, mHour, mMin;

    public InfoEvent(LatLng latLng, String title, String location, String description, int year,
                     int month, int day, int hour, int min) {
        mLatLng = latLng;
        mTitle = title;
        mLocation = location;
        mDescription = description;
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMin = min;
    }

    /********** Setters *************************/
    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }
    public void setTitle (String title) {
        mTitle = title;
    }
    public  void setLocation(String location) {
        mLocation = location;
    }
    public void setDescription(String description) {
        mDescription = description;
    }
    public void setYear(int year) {
        mYear = year;
    }
    public  void setMonth(int month) {
        mMonth = month;
    }
    public void setDay(int day) {
        mDay = day;
    }
    public void setHour(int hour) {
        mHour = hour;
    }
    public void setMin(int min) {
        mMin = min;
    }

    /********** Getters *************************/
    public LatLng getLatLng() {
        return mLatLng;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getLocation() {
        return mLocation;
    }
    public String getDescription() {
        return mDescription;
    }
    public int getYear() {
        return mYear;
    }
    public int getMonth() {
        return mMonth;
    }
    public int getDay() {
        return mDay;
    }
    public int getHour() {
        return mHour;
    }
    public int getMin() {
        return mMin;
    }
}
