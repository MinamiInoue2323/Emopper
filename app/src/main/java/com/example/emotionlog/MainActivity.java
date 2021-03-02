package com.example.emotionlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    private QiChatbot qiChatbot;
    private ConstraintLayout mainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrame = findViewById((R.id.mainFrame));
        QiSDK.register(this, this);
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


        Future<Void> listenFuture = SayBuilder.with(qiContext).withText("お帰りなさい！今日はどんな一日でした？").build().async().run();
        listenFuture.thenConsume(voidFuture -> {
            if(listenFuture.isSuccess()){
                //Pepperに聞き取ってほしいワードの一覧
                PhraseSet answerPhraseSet1 = PhraseSetBuilder.with(qiContext).withTexts("楽しかった","眠かった","嬉しかった","悲しかった","イライラ").build();
                Listen listen1 = ListenBuilder.with(qiContext).withPhraseSet(answerPhraseSet1).build();
                Future<ListenResult> futureListen1 =  listen1.async().run();
                futureListen1.andThenConsume (aVoid_ -> {
                    //聞き取った内容を抽出
                    String value = futureListen1.getValue().getHeardPhrase().getText();
                    day_log alog = new day_log(getEmotionFromListening(value));
                    //これを入れないと真下のSayが続きません
                    futureListen1.requestCancellation();
                    SayBuilder.with(qiContext).withText(value).build().async().run();

                    final int androidTabletColor = getAndroidTabletColor(alog.getEmotion());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainFrame.setBackgroundColor(androidTabletColor);
                        }
                    });

                    Animation animation = AnimationBuilder.with(qiContext)
                            .withResources(R.raw.nicereaction_a001).build();
                    Animate animate = AnimateBuilder.with(qiContext)
                            .withAnimation(animation).build();
                    animate.async().run();

                    Say saycomment = SayBuilder.with(qiContext)
                            .withPhrase(new Phrase("貴方が良い一日を過ごせて，私も嬉しいです！")).build();
                    saycomment.async().run();





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
            case "悲しかった":
                return "悲しい";
            case "イライラ":
                return "イライラ";
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