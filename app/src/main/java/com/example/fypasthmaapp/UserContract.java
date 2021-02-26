package com.example.fypasthmaapp;

import android.provider.BaseColumns;

public final class UserContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserContract() {}
    /* Inner class that defines the table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String _ID = "id";
        public static final String _NAME = "name";
        public static final String _EMAIL = "email";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " TEXT, " +
                    UserEntry._NAME + " TEXT, " +
                    UserEntry._EMAIL + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
}

