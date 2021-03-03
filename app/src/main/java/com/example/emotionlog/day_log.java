package com.example.emotionlog;

import java.util.Calendar;
import java.util.Date;

public class day_log {
    private Calendar date;
    private String emotion;

    public day_log(String emotion){
        date = Calendar.getInstance();
        this.emotion = emotion;

    }
    public day_log(String emotion, Calendar date){
        this.date = date;
        this.emotion = emotion;

    }
    public String getEmotion(){
        return emotion;
    }
    public Calendar getDate(){
        return date;
    }


}
