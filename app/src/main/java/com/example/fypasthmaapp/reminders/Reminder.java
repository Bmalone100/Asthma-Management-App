package com.example.fypasthmaapp.reminders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fypasthmaapp.MainActivity;
import com.example.fypasthmaapp.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.fypasthmaapp.R.id.cancel;
import static com.example.fypasthmaapp.R.id.confirm;
import static com.example.fypasthmaapp.R.id.set;

/**
 * Initialise a pending intent to be received as an alarm
 */
public class Reminder extends AppCompatActivity implements View.OnClickListener {
    String message = "";

    /**
     * Creating buttons and listeners for the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        // Set onClick Listener
        Button setBtn = findViewById(R.id.set);
        Button cancelBtn = findViewById(R.id.cancel);
        Button confirmBtn = findViewById(R.id.confirm);
        confirmBtn.setVisibility(View.INVISIBLE);
        setBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
    }

    /**
     * Initialising the alarm and the events for the listeners
     */
    @Override
    public void onClick(View v) {
        Button setBtn = findViewById(R.id.set);
        Button cancelBtn = findViewById(R.id.cancel);
        Button confirmBtn = findViewById(R.id.confirm);
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker = findViewById(R.id.timePicker);

        // Intent
        Intent intent = new Intent(Reminder.this, AlertReceiver.class);
        int notificationId = 1;
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", editText.getText().toString());

        // PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                Reminder.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        );

        // AlarmManager
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        switch (v.getId()) {
            case set:
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Create time.
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();
                // Set Alarm
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

                //Set Confirm Visible
                confirmBtn.setVisibility(View.VISIBLE);
                break;

            case cancel:
                confirmBtn.setVisibility(View.INVISIBLE);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();

            case confirm:
                Intent confirmIntent = new Intent(this, MainActivity.class);
                message = "Confirmed, alarm set";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(confirmIntent);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}