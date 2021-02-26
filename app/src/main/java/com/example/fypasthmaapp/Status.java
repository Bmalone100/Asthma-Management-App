package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class Status extends AppCompatActivity {

    private static final String TAG = "dbTag";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.date);
        textViewDate.setText(currentDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView textViewName = findViewById(R.id.display_name);
        textViewName.setText(user.getDisplayName());


    }

    public void OnClickHandleStatus(View view) {

    }

    public void writeUserStatus(String userId, String status) {
        User user = new User(userId, status);
        mDatabase.child("User Status").child(userId).setValue(user);
    }
}