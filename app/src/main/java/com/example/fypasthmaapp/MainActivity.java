package com.example.fypasthmaapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.fypasthmaapp.maps.MapHandler;
import com.example.fypasthmaapp.reminders.Reminder;
import com.example.fypasthmaapp.symptoms.Symptoms;
import com.example.fypasthmaapp.symptoms.UserInputSymptom;
import com.example.fypasthmaapp.triggers.Triggers;
import com.example.fypasthmaapp.triggers.UserInputTrigger;
import com.example.fypasthmaapp.user.DatabaseHelper;
import com.example.fypasthmaapp.user.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The main menu screen for the user and Firebase user authentication handling
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "Login";
    private ArrayList<FirebaseUser> userList = new ArrayList<>();
    private SQLiteDatabase db;

    /**
     * Initialising the firebase UI components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> {
            Log.e(TAG, "Authentication state changed.");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            updateUI(user);
        };
        mAuth.addAuthStateListener(mAuthListener);
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
     * Builds the sign in methods and sends the user to sign in.
     */
    public void onClickSignIn(View view) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * Catches the result of sign in.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String id = user.getUid();
                String name = user.getDisplayName();
                String email = user.getEmail();
                User aUser = new User(id, name, email);
                writeNewUser(aUser);
            } else {
                if (response == null) {
                    showPopupMessage("Sign in cancelled!");
                    return;
                }
                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showPopupMessage("No internet connection!");
                    return;
                }
                showPopupMessage("Sign-in error: " + response.getError());
            }
        }
    }

    /**
     * Checks for the status of the current user and updates UI.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Changes the displayed strings containing user details.
     */
    private void updateUI(FirebaseUser currentUser) {
        FirebaseUser user = mAuth.getCurrentUser();
        Button signIn = findViewById(R.id.sign_in);
        Button signOut = findViewById(R.id.sign_out);
        signIn.setVisibility(View.VISIBLE);
        signOut.setVisibility(View.VISIBLE);
        TextView display_name = findViewById(R.id.display_name);
        if (user != null) {
            display_name.setVisibility(View.VISIBLE);
            display_name.setText(user.getDisplayName());
            showPopupMessage("User is logged in");
            signIn.setVisibility(View.INVISIBLE);
        } else {
            display_name.setText(R.string.user);
            display_name.setVisibility(View.INVISIBLE);
            signOut.setVisibility(View.INVISIBLE);
        }
    }
    /**
     Writes a user to the sql database
     */

    public void writeNewUser(User aUser) {
        FirebaseUser user = mAuth.getCurrentUser();
        if(!userList.contains(user)) {
            // Gets the data repository in write mode
            db= (new DatabaseHelper(this)).getWritableDatabase();
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put("name", aUser.getName());
            values.put("email", aUser.getEmail());
            //Array of users to prevent duplicate entries
            userList.add(user);
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert("users", null, values);
            if (newRowId == -1) {
                Log.d(TAG, "writeNewUser: Insert into database failed.");
            } else {
                Log.d(TAG, "writeNewUser: Insert into database succeeded.");
            }
        }else{
            Log.d(TAG, "writeNewUser: User already written to database.");
        }
    }
    /**
     Signs out the current user and the strings will update back to default.
     */
    public void onClickSignOut(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> showPopupMessage("Sign-out completed."));
    }
    /**
     Sends the user to the status activity
     */
    public void onClickSymptomUpdate(View view) {
        Intent statusIntent = new Intent(this, UserInputSymptom.class);
        startActivity(statusIntent);
    }

    /**
     Sends the user to the status activity
     */
    public void onClickSymptoms(View view) {
        Intent statusIntent = new Intent(this, Symptoms.class);
        startActivity(statusIntent);
    }
    /**
     Sends the user to the trigger activity
     */
    public void onClickTrigger(View view) {
        Intent triggerViewIntent = new Intent(this, UserInputTrigger.class);
        startActivity(triggerViewIntent);
    }
    /**
     Sends the user to the trigger input activity
     */
    public void onClickTriggerView(View view) {
        Intent triggerIntent = new Intent(this, Triggers.class);
        startActivity(triggerIntent);
    }

    /**
     Sends the user to the reminder activity
     */
    public void onClickSetReminder(View view) {
        Intent reminderIntent = new Intent(this, Reminder.class);
        startActivity(reminderIntent);
    }

    /**
     Sends the user to the mapHandler activity
     */
    public void onClickMapHandler(View view) {
        Intent mapIntent = new Intent(this, MapHandler.class);
        startActivity(mapIntent);
    }

}
