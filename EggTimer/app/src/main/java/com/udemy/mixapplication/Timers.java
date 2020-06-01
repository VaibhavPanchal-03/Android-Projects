package com.udemy.mixapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Timers extends AppCompatActivity {

    private Button go;
    private TextView timer;
    private SeekBar seekBar;
    private int minutes, seconds;
    private CountDownTimer cdt;
    private Boolean counterIsActive = false;

    public void updateTimer(int progress){

        minutes = progress/60;
        seconds = progress - (minutes*60);

        if(seconds<10){
            timer.setText(String.valueOf(minutes)+":"+"0"+String.valueOf(seconds));
        }
        else {
            timer.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
        }
    }



    public void GoButton(View view){
        go = (Button) view;

        if(counterIsActive){

            counterIsActive = false;
            go.setText("start");
            seekBar.setEnabled(true);
            cdt.cancel();
            timer.setText("0:30");

        }
        else {

            counterIsActive = true;
            go.setText("stop");
            seekBar.setEnabled(false);

            cdt = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer((int) millisUntilFinished / 1000);


                }

                @Override
                public void onFinish() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(Timers.this, R.raw.airhorn);
                    mediaPlayer.start();
                }
            }.start();

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (TextView)findViewById(R.id.timerTextView);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        seekBar.setMax(600);
        seekBar.setProgress(30);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updateTimer(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
}
