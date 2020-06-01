package com.udemy.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        seekBar = (SeekBar)findViewById(R.id.seekBar2);
        seekBar.setMax(50);
        seekBar.setProgress(10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Toast.makeText(MainActivity.this, "Table of Number : "+Integer.toString(progress), Toast.LENGTH_LONG).show();

                ArrayList<String> arrayList = new ArrayList<String>();
                int value;
                int min=1;
                if(progress<min){
                    value= min;
                }
                else{
                    value = progress;
                }
                for(int i=1; i<100;i++){
                    arrayList.add(Integer.toString(value)+" x "+i+" = "+Integer.toString(i*value));

                }

                arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(arrayAdapter);

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
