package com.udemy.snapchatclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> friendEmails = new ArrayList<String>();
    private ArrayList<DataSnapshot> snapshot = new ArrayList<DataSnapshot>();


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.listview_format,R.id.myTextView, friendEmails);
        listView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getChildrenCount() > 0){
                    friendEmails.add(dataSnapshot.child("from").getValue().toString());
                    snapshot.add(dataSnapshot);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                int index = 0;

                for(DataSnapshot snaps : snapshot){
                    if(snaps.getKey().equals(dataSnapshot.getKey())){
                        snapshot.remove(index);
                        friendEmails.remove(index);
                    }
                    index++;
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataSnapshot dataSnapshot = snapshot.get(position);

                Intent intent = new Intent(MainActivity.this, showSnaps.class);
                intent.putExtra("image",dataSnapshot.child("image").getValue().toString());
                intent.putExtra("imageURL",dataSnapshot.child("imageURL").getValue().toString());
                intent.putExtra("message",dataSnapshot.child("message").getValue().toString());
                intent.putExtra("snapshotKey",dataSnapshot.getKey().toString());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.snaps,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.e("menuItem", String.valueOf(item.getItemId()));
        if(item.getItemId() == R.id.createSnap){
            startActivity(new Intent(MainActivity.this,createSnapActivity.class));
        }
        else if(item.getItemId() == R.id.logout){
            finish();
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        mAuth.signOut();
    }


}
