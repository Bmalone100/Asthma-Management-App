package com.example.fypasthmaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Triggers extends AppCompatActivity {
    private static final String TAG = "Trigger";
    //private SQLiteDatabase db;
    RecyclerView recyclerView;
    //private ArrayList<Trigger> triggers;
    private String[] triggers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triggers);
        recyclerView = findViewById(R.id.recycler);
        //triggers = new ArrayList<>();
        setTriggers();
        MyAdapter myAdapter = new MyAdapter(triggers);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void setTriggers() {
        /*for(int i = 0; i< triggers.length;i++) {
            triggers[i] = //userinput
        }*/
        triggers = new String[10];
        triggers[0] = "Animal Fur";
        triggers[1] = "Smoking";
        triggers[2] = "Emotional Distress";
        triggers[3] = "Dust";
        triggers[4] = "Air Pollution";
        triggers[5]= "Mold";
        triggers[6] = "Cleaning Products";
        triggers[7] = "Pollen";
        triggers[8] = "Exercise";
    }
    /*private void setTriggers() {
        Log.d(TAG, "setTriggers: Do I get here?");
        triggers.add(new Trigger("Animal Fur"));
        Log.d(TAG, "setTriggers: How about here??");
        triggers.add(new Trigger("Smoking"));
        triggers.add(new Trigger("Emotional distress"));
        triggers.add(new Trigger("Dust"));
        triggers.add(new Trigger("Air Pollution"));
        triggers.add(new Trigger("Mold"));
        triggers.add(new Trigger("Cleaning Products"));
        triggers.add(new Trigger("Pollen"));
        triggers.add(new Trigger("Exercise"));
    }*/

}