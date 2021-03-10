package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class userInputTrigger extends AppCompatActivity {
    static String EXTRA_MESSAGE = "";
    private static final String TAG = "userInput";
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_trigger);
    }

    /**
     * Sends information to the specified activity.
     */
    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.simpleEditText);
        message = editText.getText().toString();
        Intent intent = new Intent(this, Triggers.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}




