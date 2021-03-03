package com.example.emotionlog;

import android.graphics.Color;


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

}
