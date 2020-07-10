package com.example.bariki_othmane;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=4200;

    Animation rightAnime , bottomAnime,leftAnime;
    TextView logo;
    LottieAnimationView logoAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_page2);

        //animations

        rightAnime = AnimationUtils.loadAnimation(this,R.anim.a1);
        bottomAnime = AnimationUtils.loadAnimation(this,R.anim.bottom);
        leftAnime = AnimationUtils.loadAnimation(this,R.anim.a2);
        //hooks
        logoAnime = findViewById(R.id.truck);
        logo = findViewById(R.id.matus);
        logoAnime.setAnimation(rightAnime);
        logo.setAnimation(leftAnime);


        //splash section

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent0;
                intent0 = new Intent(MainActivity.this,Home.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(logo,"text2");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                }
                intent0.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent0.putExtra("caller", "MainActivity");
                startActivity(intent0,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);



    }

}

