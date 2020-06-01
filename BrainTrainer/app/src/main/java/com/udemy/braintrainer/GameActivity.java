package com.udemy.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView timerTextView, question, countOfCorrectAns, result;
    private Button button1, button2, button3, button4, playAgainButton;
    private Random random;
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private int locationOfCorrectAnswer, score, noOfQues;
    private CountDownTimer cdt;
    private View view;


    public void chooseOption(View view){

        MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.click);
        mediaPlayer.start();
        noOfQues++;
        if(String.valueOf(locationOfCorrectAnswer).equals(view.getTag().toString())){
            result.setVisibility(View.VISIBLE);
            result.setText("Correct :)");
            score++;
        }
        else{
            result.setVisibility(View.VISIBLE);
            result.setText("Wrong :(");
        }

        countOfCorrectAns.setText(String.valueOf(score)+"/"+String.valueOf(noOfQues));

        generateQuestion();
    }

    public void generateQuestion(){

        result.setVisibility(View.INVISIBLE);

        random = new Random();

        int a = random.nextInt(1000);
        int b = random.nextInt(1000);

        question.setText(Integer.toString(a)+" + "+Integer.toString(b)); //Generates a random question

        arrayList.clear();

        locationOfCorrectAnswer = random.nextInt(4);
        int wrongAnswer;

        for(int i=0; i<4; i++){

            if(i == locationOfCorrectAnswer){
                arrayList.add(a+b);
            }
            else{
                wrongAnswer = random.nextInt(1000);

                if(wrongAnswer == (a+b)){
                    wrongAnswer = random.nextInt(1000);
                }
                arrayList.add(wrongAnswer);
            }

        }

        button1.setText(arrayList.get(0).toString());
        button2.setText(arrayList.get(1).toString());
        button3.setText(arrayList.get(2).toString());
        button4.setText(arrayList.get(3).toString());
    }

    public void playAgainButton(View view){

        playAgainButton = (Button) view;
        playAgainButton.setVisibility(View.INVISIBLE);
        score = 0;
        noOfQues = 0;
        timerTextView.setText("30s");
        countOfCorrectAns.setText(String.valueOf(score)+"/"+String.valueOf(noOfQues));
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);

        generateQuestion();
        startTimer();

    }

    public void startTimer(){

        cdt = new CountDownTimer(30000+100,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timerTextView.setText(String.valueOf(millisUntilFinished/1000+"s"));

            }

            @Override
            public void onFinish() {

                button1.setEnabled(false);
                button2.setEnabled(false);
                button3.setEnabled(false);
                button4.setEnabled(false);

                playAgainButton.setVisibility(View.VISIBLE);

                MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.air_horn);
                mediaPlayer.start();
            }
        }.start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timerTextView = (TextView)findViewById(R.id.timerTextView);
        question = (TextView)findViewById(R.id.questionTextView);
        countOfCorrectAns = (TextView)findViewById(R.id.totalTextView);
        result = (TextView)findViewById(R.id.resultTextView);

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        playAgainButton = (Button)findViewById(R.id.playAgainButton);

        generateQuestion();

        startTimer();
    }
}
