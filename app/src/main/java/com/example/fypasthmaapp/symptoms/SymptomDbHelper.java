package com.example.fypasthmaapp.symptoms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Database helper class for Symptoms user data
 */
class SymptomDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "symptom.db";

    public SymptomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String sqlStmt= "CREATE TABLE IF NOT EXISTS symptoms(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user TEXT, " +
                "symptom TEXT," +
                "date TEXT)";
        db.execSQL(sqlStmt);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        String sqlStmt = "DROP TABLE IF EXISTS symptoms";
        db.execSQL(sqlStmt);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}