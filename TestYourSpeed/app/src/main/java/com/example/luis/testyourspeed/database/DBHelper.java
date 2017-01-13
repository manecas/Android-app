package com.example.luis.testyourspeed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.luis.testyourspeed.gameModel.Result;

    class DBHelper extends SQLiteOpenHelper {

    // Database Information
    static final String DB_NAME = "TestYourSpeed.db";

    // database version
    static final int DB_VERSION = 1;

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + Result.TABLE  + "("
                + Result.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Result.KEY_TEST_LANGUAGE + " INTEGER, "
                + Result.KEY_USERNAME + " TEXT, "
                + Result.KEY_WPM + " TEXT, "
                + Result.KEY_RIGTH + " TEXT, "
                + Result.KEY_WRONG + " TEXT, "
                + Result.KEY_ACCURACY + " TEXT );";

        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Result.TABLE);

        // Create tables again
        onCreate(db);

    }

}
