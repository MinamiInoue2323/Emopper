package com.example.emotionlog;

import android.graphics.Color;

import com.aldebaran.qi.sdk.object.conversation.Phrase;


public class emotionManager {
    public int getEmotionColor(String text){
        switch (text){
            case "楽しい":
                return Color.rgb(255,165,0);
            case "嬉しい":
                return Color.rgb(255,182,193);
            case "悲しい":
                return Color.rgb(135,206,250);
            case "疲れた":
                return Color.rgb(95,158,160);
            case "穏やか":
                return Color.rgb(144,238,144);
            case "普通":
                return Color.rgb(238,232,170);
            default:
                return Color.rgb(220,220,220);
        }
    }

    public int getImageOfEmotion(String text){
        switch (text){
            case "楽しい":
                return R.drawable.tanoshii;
            case "嬉しい":
                return R.drawable.ureshii;
            case "悲しい":
                return R.drawable.kanashii;
            case "疲れた":
                return R.drawable.tsukareta;
            case "穏やか":
                return R.drawable.odayaka;
            case "普通":
                return R.drawable.hutsuu;
            default:
                return 0;
        }
    }

    public Phrase getCommenttoEmotion(String text){
        switch (text){
            case "楽しい":
                return new Phrase("貴方が良い一日を過ごせて，私も嬉しいです！");
            case "嬉しい":
                return new Phrase("貴方が良い一日を過ごせて，私も嬉しいです！");
            case "悲しい":
                return new Phrase("悲しい時は，深呼吸をすると良いですよ．　僕と一緒にやってみましょう");
            case "疲れた":
                return new Phrase("今日も一日，頑張ったのですね．　お疲れ様です．　ゆっくり休んでください");
            case "穏やか":
                return new Phrase("貴方が良い一日を過ごせて，私も嬉しいです！");
            case "普通":
                return new Phrase("何もない普通の日こそ，　　実はかけがえのないものなのかもしれませんね．");
            default:
                return new Phrase("今日も一日お疲れ様でした！");
        }
    }

    public int getEmotionScore(String text){
        switch (text){
            case "楽しい":
                return 1;
            case "嬉しい":
                return 1;
            case "悲しい":
                return -1;
            case "疲れた":
                return -1;
            case "穏やか":
                return 1;
            case "普通":
                return 0;
            default:
                return 0;
        }
    }

    public int getDayImageAddress(int date){
        switch (date){
            case 1:
                return R.id.e_monday;
            case 2:
                return R.id.e_tuesday;
            case 3:
                return R.id.e_wednesday;
            case 4:
                return R.id.e_thursday;
            case 5:
                return R.id.e_friday;
            case 6:
                return R.id.e_saturday;
            case 7:
                return R.id.e_sunday;
            default:
                return 0;
        }
    }

    public int getDayOfWeekAgo(int today_date, int ago){
        int date = today_date - ago;
        if(ago <= 0){
            date += 7;

        }
        return date;
    }



}
