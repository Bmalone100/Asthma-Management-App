package com.example.fypasthmaapp.symptoms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fypasthmaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Activity for managing user's generic symptoms
 */
public class Symptoms extends AppCompatActivity {

    private static final String TAG = "dbTag";
    RecyclerView recyclerView;
    private static ArrayList<String> symptoms = new ArrayList<>();
    SQLiteDatabase db;
    String currentDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom);

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        TextView textViewDate = findViewById(R.id.date);
        textViewDate.setText(currentDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView textViewName = findViewById(R.id.display_name);
        assert user != null;
        textViewName.setText(user.getDisplayName());

        recyclerView = findViewById(R.id.symptomRecycler);
        //Clear list before reading to avoid multiple reads
        symptoms.clear();
        symptomDatabaseRead();
        MySymptomAdapter anAdapter = new MySymptomAdapter(symptoms);
        recyclerView.setAdapter(anAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Database read
     */
    private void symptomDatabaseRead() {
        String query = "SELECT symptom From symptoms";
        db = (new SymptomDbHelper(this)).getReadableDatabase();
        Cursor csr = db.rawQuery(query, new String[]{});
        csr.moveToFirst();
        while (!csr.isAfterLast()) {
            String symptom = csr.getString(csr.getColumnIndex("symptom"));
            Log.d(TAG, "triggerDatabaseRead: Trigger: " + symptom);
            symptoms.add(symptom);
            csr.moveToNext();
        }
        csr.close();
    }

}