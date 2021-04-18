package com.example.fypasthmaapp.symptoms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fypasthmaapp.MainActivity;
import com.example.fypasthmaapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
/**
 * Activity for user to input their level of symptoms
 */
public class UserInputSymptom extends AppCompatActivity {
    static String EXTRA_MESSAGE = "";
    private static final String TAG = "userInput";
    String message = "";
    SQLiteDatabase db;
    private ArrayList<String> dateEntry = new ArrayList<>();

    TextView minorSymptom;
    TextView moderateSymptom;
    TextView majorSymptom;

    Button setMinor;
    Button setModerate;
    Button setMajor;
    Button submit;
    final String[] dataToPass = {""};
    String currentDate;


    /**
     * Handle keyboard event and button click event for passing user input to Triggers activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_symptom);

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        submit = findViewById(R.id.submit);

        minorSymptom = findViewById(R.id.minor);
        moderateSymptom = findViewById(R.id.moderate);
        majorSymptom = findViewById(R.id.major);

        setMinor = findViewById(R.id.setMinor);
        setModerate = findViewById(R.id.setModerate);
        setMajor = findViewById(R.id.setMajor);

        setMinor.setOnClickListener(v -> {
            dataToPass[0] = minorSymptom.getText().toString();
            showPopupMessage("Minor symptoms set");
            Log.d(TAG, "saveSymptom: I am clicked for minor");
        });

        setModerate.setOnClickListener(v -> {
            dataToPass[0] = moderateSymptom.getText().toString();
            showPopupMessage("Moderate symptoms set");
            Log.d(TAG, "saveSymptom: I am clicked for moderate");
        });

        setMajor.setOnClickListener(v -> {
            dataToPass[0] = majorSymptom.getText().toString();
            showPopupMessage("Major symptoms set");
            Log.d(TAG, "saveSymptom: I am clicked for major");
        });

        submit.setOnClickListener(v -> {
            saveSymptom();
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
            Log.d(TAG, "saveSymptom: I am clicked for submit");
        });
    }

    /**
     * Takes a string and creates a snackbar pop up message.
     */
    private void showPopupMessage(String message) {
        Log.e(TAG, message);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        Snackbar.make(coordinatorLayout, message,
                Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    /**
     * Write user input to the database
     */
    public void saveSymptom() {
        db = (new SymptomDbHelper(this)).getWritableDatabase();
        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        //Only one entry per date
        if (!(dateEntry.contains(currentDate))) {
            db = (new SymptomDbHelper(this)).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user", username);
            values.put("symptom", dataToPass[0]);
            //Array of users to prevent duplicate entries
            dateEntry.add(currentDate);
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert("symptoms", null, values);
            if (newRowId == -1) {
                Log.d(TAG, "saveSymptom: Insert into database failed.");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Log.d(TAG, "saveSymptom: Insert into database succeeded.");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            Log.d(TAG, "saveSymptom: Trigger already in db ");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}