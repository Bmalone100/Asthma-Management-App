package com.example.fypasthmaapp.triggers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Database helper class for Triggers user data
 */
class TriggerDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "trigger.db";

    public TriggerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sqlStmt= "CREATE TABLE IF NOT EXISTS triggers(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT, " +
                "trg     TEXT)";
        db.execSQL(sqlStmt);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        String sqlStmt = "DROP TABLE IF EXISTS triggers";
        db.execSQL(sqlStmt);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
