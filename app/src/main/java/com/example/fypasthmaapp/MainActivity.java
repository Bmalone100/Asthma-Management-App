package com.example.fypasthmaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "Login";

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
     * This method takes a string and creates a snackbar pop up message.
     */
    private void showPopupMessage(String message) {
        Log.e(TAG, message);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout);
        Snackbar.make(coordinatorLayout, message,
                Snackbar.LENGTH_INDEFINITE)
                .show();
    }

    /**
     * This method builds the sign in methods and sends the user to sign in.
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
     * This method catches the result of sign in.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                showPopupMessage("Sign in successful: " + "displayName=" + user.getDisplayName() );
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
     * This method checks for the status of the current user and updates UI.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * This method changes the displayed strings containing user details.
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
     This method signs out the current user and the strings will update back to default.
     */
    public void onClickSignOut(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> showPopupMessage("Sign-out completed."));
    }
}