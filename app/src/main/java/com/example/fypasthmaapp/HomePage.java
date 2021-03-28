package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "Login";
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    /**
     * This method takes a string and creates a snackbar pop up message.
     */
    private void showPopupMessage(String message) {
        Log.e(TAG, message);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        Snackbar.make(coordinatorLayout, message,
                Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    public void onClickDisplay(View view) {
       // cursor = db.rawQuery("SELECT id,name,email FROM Users",null);
        //TextView textView = findViewById(R.id.welcome);
        //textView.setText(id, name, email);
    }


}