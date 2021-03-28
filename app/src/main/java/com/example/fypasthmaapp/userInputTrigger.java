package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class userInputTrigger extends AppCompatActivity {
    static String EXTRA_MESSAGE = "";
    private static final String TAG = "userInput";
    String message = "";

    SQLiteDatabase db;
    private ArrayList<String> triggerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_trigger);

        EditText editText = findViewById(R.id.simpleEditText);
        editText.setOnKeyListener(((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                message = editText.getText().toString();
                saveTrigger();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
            return false;
        }));
    }

    /**
     * Sends information to the specified activity.
     */
    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.simpleEditText);
        message = editText.getText().toString();
        saveTrigger();
        //Intent intent = new Intent(this, Triggers.class);
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }

    public void saveTrigger() {
        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        EditText editText = findViewById(R.id.simpleEditText);

        if (!(triggerList.contains(message))) {
            db = (new triggerDbHelper(this)).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user", username);
            values.put("trg", editText.getText().toString());
            //Array of users to prevent duplicate entries
            triggerList.add(message);
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert("triggers", null, values);
            if (newRowId == -1) {
                Log.d(TAG, "saveTrigger: Insert into database failed.");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Log.d(TAG, "saveTrigger: Insert into database succeeded.");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Log.d(TAG, "saveTrigger: Trigger already in db ");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}




