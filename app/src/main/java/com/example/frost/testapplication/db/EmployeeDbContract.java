package com.example.frost.testapplication.db;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

public class EmployeeDbContract implements BaseColumns {
    public static final String TABLE_NAME = "employees";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PATRONYMIC = "patronymic";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_PHOTO = "photo";

    private EmployeeDbContract() {

    }
}
