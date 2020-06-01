package com.example.shake2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;

import static com.example.shake2.gps_location.lat;
import static com.example.shake2.gps_location.lon;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {


    ListView listView ;
    ArrayList<String> contactsArray ;
    ArrayAdapter<String> arrayAdapter ;
    Button contactsButton;
    Button callButton;
    Button logout;
    Cursor cursor ;
    String name, contactNumber ;

    String showContact;
    boolean[] chechItems;
    Integer chechItemsCount;

    String PERMISSIONS []={
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };



    private static final int PERMISSION_REQUEST_CODE = 1;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location mlocation;

    public static String lat;
    public static String lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        logout = (Button)findViewById(R.id.btnlogout);

        listView = (ListView)findViewById(R.id.listview);
        contactsArray = new ArrayList<String>();
        contactsButton = (Button)findViewById(R.id.contacts);



        // Permissions check
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermission()) {

                Log.e("permission", "Permission already granted.");

            } else {

                requestPermission();

            }
        }




        // Instance of Sensor (ACCELEROMETER)
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE) ;
        ShakeDetector shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sm);



        // Current Location determining Function
        getLastLocation();




        //clicking LOAD CONTACTS button
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContactstoArray();


                arrayAdapter = new ArrayAdapter<String>(
                         MainActivity.this,
                                R.layout.contact_listview, R.id.textView,
                                contactsArray
                );

                listView.setAdapter(arrayAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            }

        });


        //clicking contacts
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();


                 showContact = contactsArray.get(i);

                Toast.makeText(MainActivity.this, showContact, Toast.LENGTH_SHORT).show();


                 Intent intent = new Intent(MainActivity.this, DisplayContacts.class);
                 intent.putExtra("contact",showContact);
                 startActivity(intent);


            }
        });





        //LOGOUT button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {

                        Toast.makeText(MainActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,login_form.class));
                        MainActivity.this.finish();

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(MainActivity.this, "Error :"+ fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });





    }


    public void AddContactstoArray(){

        String sortOrder= ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;  //alphabetical order

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, sortOrder);
        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contactsArray.add(name + " " + ":" + " " + contactNumber);

            chechItemsCount = contactsArray.size();
        }

        cursor.close();

    }


   public boolean checkPermission() {

      if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
          requestPermission();
        }

        int SmsPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);

        return SmsPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);

    }



    // shake listener
    @Override
    public void hearShake() {
        Toast.makeText(this, lat+lon, Toast.LENGTH_SHORT).show();
         sendSMS(showContact,"Help!!!!!!! \n"+"http://www.google.com/maps/place/"+lat+","+lon);
    }



    //Sending SMS function
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }





    //getting CURRENT LOCATION function
    private void getLastLocation()
    {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    mlocation = location;
                    //Toast.makeText(gps_location.this, mlocation.getLatitude() + "" + mlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                   // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                         //   .findFragmentById(R.id.map);
                    // mapFragment.getMapAsync(gps_location.this);
                    lat = String.valueOf(mlocation.getLatitude());
                    lon = String.valueOf(mlocation.getLongitude());
                    //maplink.setText("http://www.google.com/maps/place/"+lat+","+lon);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "location is null", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}



