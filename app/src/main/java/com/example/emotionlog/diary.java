package com.example.emotionlog;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class diary {
    private ArrayList<day_log> diary = new ArrayList<day_log>();
    private emotionManager e = new emotionManager();

    public diary(){
        setdefault();
        Iterator<day_log> iterator = diary.iterator();
        while( iterator.hasNext() ){
            day_log alog = iterator.next();
            Log.i("date",Integer.toString(alog.date.get(Calendar.DATE)));
            Log.i("emotion",alog.getEmotion());
        }

    }

    public void addlog(day_log alog){
        diary.add(alog);
    }

    public void setdefault(){

        //add 4 days log
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DATE, -1);
        diary.add(new day_log("楽しい" ,date));

        Calendar date1 = Calendar.getInstance();
        date1.add(Calendar.DATE, -2);
        diary.add(new day_log("嬉しい",date1));
        Calendar date2 = Calendar.getInstance();
        date2.add(Calendar.DATE, -3);
        diary.add(new day_log("悲しい",date2));
        Calendar date3 = Calendar.getInstance();
        date3.add(Calendar.DATE, -4);
        diary.add(new day_log("疲れた",date3));


    }

    public Calendar changedate(Calendar date, int adddate){
        date.add(Calendar.DAY_OF_MONTH, adddate);
        return date;
    }

    public int getWeekScore(){
        Iterator<day_log> iterator = diary.iterator();
        int score;
        score = 0;
        while( iterator.hasNext() ){
            day_log alog = iterator.next();
            score += e.getEmotionScore(alog.getEmotion());

        }

        return score;

    }

    public String getEmotionOfDate(Calendar cl){
        Iterator<day_log> iterator = diary.iterator();

        while( iterator.hasNext() ){
            day_log alog = iterator.next();
            if(alog.getDate().get(Calendar.YEAR) == cl.get(Calendar.YEAR) &&
                    alog.getDate().get(Calendar.DAY_OF_YEAR) == cl.get(Calendar.DAY_OF_YEAR)){
                return alog.getEmotion();
            }
        }
        return "none";

    }

    public String getEmotionDaysAgo(Calendar cl,int day ){
        Calendar clc = cl;
        changedate(clc, -day);
        return getEmotionOfDate(clc);
    }





}


