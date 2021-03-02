package com.example.emotionlog;

import java.util.Date;

public class day_log {
    private Date date;
    private String emotion;

    public day_log(String emotion){
        date = new Date();
        this.emotion = emotion;

    }
    public String getEmotion(){
        return emotion;
    }


}
