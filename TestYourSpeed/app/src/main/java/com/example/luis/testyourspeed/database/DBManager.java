package com.example.luis.testyourspeed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.luis.testyourspeed.gameModel.Result;

import java.util.ArrayList;

public class DBManager {

    private DBHelper dbHelper;

    public DBManager(Context c) {
        dbHelper = new DBHelper(c);
    }

    public int insert(Result result){
        SQLiteDatabase db = null;
        try {
            //Open connection to write data
            db = dbHelper.getWritableDatabase();
        }catch(Exception e){
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put(Result.KEY_TEST_LANGUAGE, result.getTestLanguage());
        values.put(Result.KEY_USERNAME, result.getName());
        values.put(Result.KEY_WPM,result.getWpm());
        values.put(Result.KEY_RIGTH, result.getRigth());
        values.put(Result.KEY_WRONG, result.getWrong());
        values.put(Result.KEY_ACCURACY, result.getAccuracy());

        // Inserting Row
        long result_Id = db.insert(Result.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) result_Id;
    }

    public ArrayList<Result> getStudentList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Result.KEY_ID + "," +
                Result.KEY_TEST_LANGUAGE + "," +
                Result.KEY_USERNAME + "," +
                Result.KEY_WPM + "," +
                Result.KEY_RIGTH + "," +
                Result.KEY_WRONG + "," +
                Result.KEY_ACCURACY +
                " FROM " + Result.TABLE;

        ArrayList<Result> resultList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(cursor.getInt(cursor.getColumnIndex(Result.KEY_ID)));
                result.setTestLanguage(cursor.getInt(cursor.getColumnIndex(Result.KEY_TEST_LANGUAGE)));
                result.setName(cursor.getString(cursor.getColumnIndex(Result.KEY_USERNAME)));
                result.setWpm(cursor.getString(cursor.getColumnIndex(Result.KEY_WPM)));
                result.setRigth(cursor.getString(cursor.getColumnIndex(Result.KEY_RIGTH)));
                result.setWrong(cursor.getString(cursor.getColumnIndex(Result.KEY_WRONG)));
                result.setAccuracy(cursor.getString(cursor.getColumnIndex(Result.KEY_ACCURACY)));
                resultList.add(result);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return resultList;

    }

    public void delete(int result_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(Result.TABLE, Result.KEY_ID + "= ?", new String[] { String.valueOf(result_Id) });
        db.close(); // Closing database connection
    }
}
