package com.example.frost.testapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EmployeeDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "employee.db";
    private static final int DB_VERSION = 11;

    public EmployeeDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + EmployeeDbContract.TABLE_NAME + "("
                + EmployeeDbContract._ID + " INTEGER PRIMARY KEY, "
                + EmployeeDbContract.COLUMN_SURNAME + " TEXT NOT NULL, "
                + EmployeeDbContract.COLUMN_NAME + " TEXT NOT NULL, "
                + EmployeeDbContract.COLUMN_PATRONYMIC + " TEXT NOT NULL, "
                + EmployeeDbContract.COLUMN_BIRTHDAY + " DATETIME,"
                + EmployeeDbContract.COLUMN_PHOTO + " TEXT"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeDbContract.TABLE_NAME);
        onCreate(db);
    }
}
