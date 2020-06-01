package com.example.rideefy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CustomerRequestForm extends AppCompatActivity implements ActivityConstants{

    private EditText mName, mPhone, mSeat;
    private TextView mSource;
    private Button mSave;
    String driverId;
    int PLACE_PICKER_REQUEST = 103;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_form);

        mName = (EditText)findViewById(R.id.crfName);
        mPhone = (EditText)findViewById(R.id.crfContact);
        mSeat = (EditText)findViewById(R.id.crfSeat);
        mSource = (TextView)findViewById(R.id.crfSource);
        mSave = (Button)findViewById(R.id.crfSave);

        mSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(CustomerRequestForm.this, DriverRouteMap.class);
                intent.putExtra("driverId", driverId);
                startActivity(intent);*/


            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                driverId = intent.getStringExtra("driverId");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("driverSchedule").child(driverId).child("CustomerBooking");

                String name = mName.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String seat = mSeat.getText().toString().trim();
                String source = mSource.getText().toString().trim();

                HashMap map = new HashMap();
                map.put("Name", name);
                map.put("Phone",phone);
                map.put("Pickup",source);
                map.put("Seat",seat);

                ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(CustomerRequestForm.this, ""+o.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerRequestForm.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                String result = (String) place.getAddress();
                mSource.setText(result);
            }
        }
    }*/
}
