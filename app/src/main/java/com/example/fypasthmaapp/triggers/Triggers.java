package com.example.fypasthmaapp.triggers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fypasthmaapp.MainActivity;
import com.example.fypasthmaapp.R;
import com.example.fypasthmaapp.symptoms.Symptoms;

import java.util.ArrayList;


/**
 * Activity for viewing the contents of the recyclerView
 */
public class Triggers extends AppCompatActivity implements MyAdapter.MyAdapterListener {
    private static final String TAG = "Trigger";
    RecyclerView recyclerView;
    private static ArrayList<String> triggers = new ArrayList<>();
    SQLiteDatabase db;


    /**
     * Read from the database and populate the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triggers);
        recyclerView = findViewById(R.id.recycler);
        //Clear list before reading to avoid multiple reads
        triggers.clear();
        triggerDatabaseRead();
        MyAdapter myAdapter = new MyAdapter(triggers, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Database read
     */
    private void triggerDatabaseRead() {
        String query = "SELECT trg From triggers";
        db = (new TriggerDbHelper(this)).getReadableDatabase();
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

        /**
        * Button listener
        */
        @Override
        public void editBtnOnClick(View v, int position) {
            Log.d(TAG, "onClick: I am clicked");
        }

        /**
        * Button listener
        */
        @Override
        public void deleteBtnOnClick(View v, int position) {
            Log.d(TAG, "onClick: I am clicked too");
        }

    public void onClickHome(View view) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }
}