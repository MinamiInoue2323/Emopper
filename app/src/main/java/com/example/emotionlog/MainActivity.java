package com.example.emotionlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.ListenBuilder;
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Bookmark;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.Listen;
import com.aldebaran.qi.sdk.object.conversation.ListenResult;
import com.aldebaran.qi.sdk.object.conversation.Phrase;
import com.aldebaran.qi.sdk.object.conversation.PhraseSet;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Say;
import com.aldebaran.qi.sdk.object.conversation.Topic;
import com.aldebaran.qi.sdk.object.human.Human;
import com.aldebaran.qi.sdk.object.humanawareness.HumanAwareness;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private QiChatbot qiChatbot;
    private ConstraintLayout mainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrame = findViewById((R.id.mainFrame));
        QiSDK.register(this, this);
        //findViewById(R.id.emotionface).setVisibility(View.GONE);
    }

    public void button(View v){

    }

    @Override
    protected void onDestroy() {
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        emotionManager e = new emotionManager();
        Calendar today = Calendar.getInstance();
        diary emo_diary = new diary();

        Future<Void> listenFuture = SayBuilder.with(qiContext).withText("お帰りなさい！今日はどんな一日でした？").build().async().run();
        listenFuture.thenConsume(voidFuture -> {
            if(listenFuture.isSuccess()){
                //Pepperに聞き取ってほしいワードの一覧
                PhraseSet answerPhraseSet1 = PhraseSetBuilder.with(qiContext).withTexts("楽しかった","嬉しかった","悲しいことがあった","疲れた","穏やかな","普通").build();
                Listen listen1 = ListenBuilder.with(qiContext).withPhraseSet(answerPhraseSet1).build();
                Future<ListenResult> futureListen1 =  listen1.async().run();
                futureListen1.andThenConsume (aVoid_ -> {
                    //聞き取った内容を抽出
                    String value = futureListen1.getValue().getHeardPhrase().getText();
                    day_log alog = new day_log(getEmotionFromListening(value));
                    emo_diary.addlog(alog);
                    //これを入れないと真下のSayが続きません
                    futureListen1.requestCancellation();

                    final int androidTabletColor = e.getEmotionColor(alog.getEmotion());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //change background color and set image of emotion
                            mainFrame.setBackgroundColor(androidTabletColor);
                            ImageView myemotion = (ImageView) findViewById(R.id.emotionface);
                            int address = e.getImageOfEmotion(alog.getEmotion());
                            if(address == 0){
                                return;
                            }
                            myemotion.setImageResource(address);
                            AlphaAnimation alpha = new AlphaAnimation(0,0.5f);
                            alpha.setDuration(1000);
                            myemotion.startAnimation(alpha);
                            
                            //myemotion.setVisibility(View.VISIBLE);
                        }

                    });

                    Future<Void> listenFuture2 = SayBuilder.with(qiContext).withText(value+
                            "のですね。   何があったのですか？").build().async().run();
                    listenFuture2.thenConsume(voidFuture2 -> {
                        if(listenFuture2.isSuccess()){
                            //Pepperに聞き取ってほしいワードの一覧
                            PhraseSet answerPhraseSet2 = PhraseSetBuilder.with(qiContext).withTexts("だ","た","あ","い","う","え","お",
                                    "は","を","で").build();
                            Listen listen2 = ListenBuilder.with(qiContext).withPhraseSet(answerPhraseSet2).build();
                            Future<ListenResult> futureListen2 =  listen2.async().run();
                            futureListen1.andThenConsume (aVoid2_ -> {
                                //聞き取った内容を抽出
                                String value2 = futureListen2.getValue().getHeardPhrase().getText();
                                //これを入れないと真下のSayが続きません
                                futureListen1.requestCancellation();
                                SayBuilder.with(qiContext).withText("そうなのですね").build().run();

                                //Animation animation = AnimationBuilder.with(qiContext)
                                //        .withResources(R.raw.nicereaction_a001).build();
                                //Animate animate = AnimateBuilder.with(qiContext)
                                //        .withAnimation(animation).build();
                                //animate.async().run();

                                Say sayhappycomment333 = SayBuilder.with(qiContext)
                                        .withPhrase(e.getCommenttoEmotion(alog.getEmotion())).build();
                                sayhappycomment333.run();

                                if(alog.getEmotion().equals("悲しい")){
                                    Animation animation = AnimationBuilder.with(qiContext)
                                            .withResources(R.raw.spread_both_hands_b003).build();
                                    Animate animate = AnimateBuilder.with(qiContext)
                                            .withAnimation(animation).build();
                                    animate.async().run();

                                    Say shinkokyu = SayBuilder.with(qiContext)
                                            .withPhrase(new Phrase("\\rspd=60\\\\vct=115\\吸ってーーーーーーーーーーーーーーーーーーーーーーーーー，　　　　　　　　　　　　はいてーーーーーーーーーーーーーーーーーーーーーーーーー　　　　　　　　　　　　　")).build();
                                    shinkokyu.run();
                                    Animation animation2 = AnimationBuilder.with(qiContext)
                                            .withResources(R.raw.spread_both_hands_b003).build();
                                    Animate animate2 = AnimateBuilder.with(qiContext)
                                            .withAnimation(animation).build();
                                    animate.async().run();

                                    Say shinkokyu2 = SayBuilder.with(qiContext)
                                            .withPhrase(new Phrase("\\rspd=60\\\\vct=115\\吸ってーーーーーーーーーーーーーーーーーーーーーーーーー，　　　　　　　　　　　　はいてーーーーーーーーーーーーーーーーーーーーーーーーー　　　　　　　　　　　　　")).build();
                                    shinkokyu2.run();

                                    Animation animation3 = AnimationBuilder.with(qiContext)
                                            .withResources(R.raw.spread_both_hands_b003).build();
                                    Animate animate3 = AnimateBuilder.with(qiContext)
                                            .withAnimation(animation).build();
                                    animate.async().run();

                                    Say shinkokyu3 = SayBuilder.with(qiContext)
                                            .withPhrase(new Phrase("\\rspd=60\\\\vct=115\\吸ってーーーーーーーーーーーーーーーーーーーーーーーーー，　　　　　　　　　　　　はいてーーーーーーーーーーーーーーーーーーーーーーーーー　　　　　　　　　　　　　")).build();
                                    shinkokyu3.run();
                                }

                                /*runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //update diary view
                                        setContentView(R.layout.diary);
                                        int showdata = today.get(Calendar.DAY_OF_WEEK) - 1;
                                        Log.i("showdata",Integer.toString(showdata));

                                        if(showdata == 0){
                                            Log.i("showdata_0dayo","0dayo");
                                            showdata = 7;
                                        }
                                        for(int i = 0; i < showdata; i++){
                                            String emotion = emo_diary.getEmotionDaysAgo(today,i);
                                            if(!emotion.equals("none")){
                                                Log.i("view","pass");
                                                Log.i("emotion",emotion);
                                                int dayofweek =e.getDayOfWeekAgo(today.get(Calendar.DAY_OF_WEEK),i);
                                                Log.i("dayofweek",Integer.toString(dayofweek));

                                                ImageView log_face=
                                                        (ImageView) findViewById(e.getDayImageAddress(dayofweek));

                                                int address = e.getImageOfEmotion(emotion);
                                                if(address == 0){
                                                    continue;
                                                }
                                                Log.i("address",Integer.toString(address));
                                                log_face.setImageResource(R.drawable.kanashii);

                                                log_face.setVisibility(View.VISIBLE);
                                                        

                                            }

                                        }

                                    }

                                });

                                 */

                                if(today.get(Calendar.DAY_OF_WEEK) == 6){
                                    Future<Void> listenFuture3 = SayBuilder.with(qiContext).withText("。。。。。そういえば，今日は金曜日ですね、、、　今週一週間を振り返ってみませんか？").build().async().run();
                                    listenFuture3.thenConsume(voidFuture3 -> {
                                        if(listenFuture3.isSuccess()){
                                            //Pepperに聞き取ってほしいワードの一覧
                                            PhraseSet answerPhraseSet3 = PhraseSetBuilder.with(qiContext).withTexts("うん","いいえ").build();
                                            Listen listen3 = ListenBuilder.with(qiContext).withPhraseSet(answerPhraseSet3).build();
                                            Future<ListenResult> futureListen3 =  listen3.async().run();
                                            futureListen3.andThenConsume (aVoid3_ -> {
                                                //聞き取った内容を抽出
                                                String value3 = futureListen3.getValue().getHeardPhrase().getText();
                                                String phrase = "";
                                                if(value3.equals("うん")){
                                                    //これを入れないと真下のSayが続きません
                                                    futureListen3.requestCancellation();
                                                    SayBuilder.with(qiContext).withText("それでは今週一週間の記録を表示します").build().run();
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            setContentView(R.layout.diary);
                                                        }

                                                    });
                                                    int week_score = emo_diary.getWeekScore();


                                                    if(week_score < 0){
                                                        phrase = "。。。。今週は大変なことが多かったですね．平日頑張った分，休日は好きなことして，ゆっくり休んでください。。。いちにちの終わりに，ハイタッチをしましょう。";

                                                    }else if(week_score > 0){
                                                        phrase = "今週は楽しかったですね．　その調子で，休日も楽しんでいきましょう！！！いちにちの終わりに，ハイタッチをしましょう。";
                                                    }else{
                                                        phrase = "今週は楽しいことも大変なこともありましたね．  休日は楽しく遊びましょう。。。いちにちの終わりに，ハイタッチをしましょう。";
                                                    }



                                                    Say weekcomment = SayBuilder.with(qiContext)
                                                            .withPhrase(new Phrase(phrase)).build();
                                                    weekcomment.run();

                                                    Animation animation4 = AnimationBuilder.with(qiContext)
                                                            .withResources(R.raw.right_hand_high_b001).build();
                                                    Animate animate4 = AnimateBuilder.with(qiContext)
                                                            .withAnimation(animation4).build();
                                                    animate4.async().run();

                                                    Say shinkokyu3 = SayBuilder.with(qiContext)
                                                            .withPhrase(new Phrase("\\rspd=60\\\\vct=115\\いきますよ。。。。。。。。。。。。。いえーーーーーい")).build();
                                                    shinkokyu3.run();






                                                }else{
                                                    //これを入れないと真下のSayが続きません
                                                    futureListen3.requestCancellation();
                                                    SayBuilder.with(qiContext).withText("分かりました").build().async().run();
                                                }
                                                Say shinkokyu3 = SayBuilder.with(qiContext)
                                                        .withPhrase(new Phrase("それでは良い休日を")).build();
                                                shinkokyu3.run();

                                            });
                                        }
                                    });
                                }





                                


                            });
                        }
                    });


                });
            }
        });







    }

    private void updateTabletEmotion() {
        if(qiChatbot == null){
            return;
        }

        String todayMind = qiChatbot.variable("today_mind").getValue();
        final int androidTabletColor = getAndroidTabletColor(todayMind);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainFrame.setBackgroundColor(androidTabletColor);
            }
        });
    }

    private int getAndroidTabletColor(String emotion) {
        switch (emotion){
            case "楽しい":
                return Color.YELLOW;
            case "嬉しい":
                return Color.YELLOW;
            case "悲しい":
                return Color.BLUE;
            case "イライラ":
                return Color.RED;
            default:
                return 0;



        }
    }

    private String getEmotionFromListening(String text){
        switch (text){
            case "楽しかった":
                return "楽しい";
            case "嬉しかった":
                return "嬉しい";
            case "悲しいことがあった":
                return "悲しい";
            case "疲れた":
                return "疲れた";
            case "穏やかな":
                return "穏やか";
            case "普通":
                return "普通";
            default:
                return "";
        }
    }

    @Override
    public void onRobotFocusLost() {

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }
}