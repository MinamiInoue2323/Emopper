package com.example.emotionlog;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class day_log {
    public Calendar date;
    private String emotion;

    public day_log(String emotion){
        date = Calendar.getInstance();
        this.emotion = emotion;
        Log.i("date_first2",Integer.toString(date.get(Calendar.DATE)));

    }
    public day_log(String emotion, Calendar date_p){
        date = date_p;
        this.emotion = emotion;
        Log.i("date_first2",Integer.toString(date.get(Calendar.DATE)));

    }
    public String getEmotion(){
        return emotion;
    }
    public Calendar getDate(){
        return date;
    }


}
