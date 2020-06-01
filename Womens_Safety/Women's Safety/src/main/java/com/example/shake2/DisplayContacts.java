package com.example.shake2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayContacts extends AppCompatActivity {

    TextView display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        display = (TextView)findViewById(R.id.display);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("contact");
        display.setText(data);



    }
}
