package com.example.shake2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class splash extends AppCompatActivity {

    ImageView Splashimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Splashimage = (ImageView) findViewById(R.id.imageSplash);


        Thread myThread =new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sleep(3000);
                    Intent i= new Intent(splash.this,login_form.class);
                    startActivity(i);
                    splash.this.finish();



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        myThread.start();

    }
}



