package com.example.emotionlog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class diary {
    private ArrayList<day_log> diary;
    private emotionManager e = new emotionManager();

    public diary(){
        setdefault();

    }

    public void addlog(day_log alog){
        diary.add(alog);
    }

    public void setdefault(){
        //add 4 days log
        Calendar date = Calendar.getInstance();
        changedate(date, -1);
        diary.add(new day_log("楽しい",date));
        changedate(date, -1);
        diary.add(new day_log("嬉しい",date));
        changedate(date, -1);
        diary.add(new day_log("悲しい",date));
        changedate(date, -1);
        diary.add(new day_log("疲れた",date));


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


