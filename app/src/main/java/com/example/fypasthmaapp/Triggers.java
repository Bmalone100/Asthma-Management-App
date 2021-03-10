package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

public class Triggers extends AppCompatActivity implements MyAdapter.OnTriggerListener {
    private static final String TAG = "Trigger";
    RecyclerView recyclerView;
    //private String[] triggers;
    private static ArrayList<String> triggers = new ArrayList<>();
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triggers);
        recyclerView = findViewById(R.id.recycler);
        setTriggers();
        MyAdapter myAdapter = new MyAdapter(triggers, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void setTriggers() {
        Intent intent = getIntent();
        message = intent.getStringExtra(userInputTrigger.EXTRA_MESSAGE);
        Log.d(TAG, "This is the string: " + message);
        triggers.add(message);
        //Let's remove duplicates
        for(int i =0;i<triggers.size()-1;i++){
            if(triggers.get(i).equals(triggers.get(i+1))){
                triggers.remove(triggers.get(i));
            }else{
                Log.d(TAG, "setTriggers: No duplicates");
            }

        }
    }

    @Override
    public void onTriggerClick(int position) {
        Log.d(TAG, "onTriggerClick: Removing: " +triggers.get(position));
        triggers.remove(triggers.get(position));
    }
}