package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class Triggers extends AppCompatActivity implements MyAdapter.MyAdapterListener {
    private static final String TAG = "Trigger";
    RecyclerView recyclerView;
    private static ArrayList<String> triggers = new ArrayList<>();
    String message = "";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triggers);
        recyclerView = findViewById(R.id.recycler);
        //setTriggers();
        //Clear list before reading
        triggers.clear();
        triggerDatabaseRead();
        MyAdapter myAdapter = new MyAdapter(triggers, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void triggerDatabaseRead() {
        String query = "SELECT trg From triggers";
        db = (new triggerDbHelper(this)).getReadableDatabase();
        Cursor csr = db.rawQuery(query, new String[]{});
        csr.moveToFirst();
        while (!csr.isAfterLast()) {
            String trg = csr.getString(csr.getColumnIndex("trg"));
            Log.d(TAG, "triggerDatabaseRead: Trigger: " + trg);
            triggers.add(trg);
            csr.moveToNext();
        }
        csr.close();
    }

        @Override
        public void editBtnOnClick(View v, int position) {
            Log.d(TAG, "onClick: I am clicked");
        }

        @Override
        public void deleteBtnOnClick(View v, int position) {
            Log.d(TAG, "onClick: I am clicked too");
        }

    //Method to set Trigger value using intent retrived string - replaced with db read
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

    /**
     This method sends the user to the home activity
     */
    public void onClickHome(View view) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }
}