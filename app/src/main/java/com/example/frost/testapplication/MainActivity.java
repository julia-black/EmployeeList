package com.example.frost.testapplication;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.frost.testapplication.db.DbBitmapUtility;
import com.example.frost.testapplication.db.EmployeeDbContract;
import com.example.frost.testapplication.db.EmployeeDbHelper;
import com.example.frost.testapplication.fragments.EmployeeFragment;
import com.example.frost.testapplication.fragments.ListEmployeeFragment;
import com.example.frost.testapplication.model.Employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ListEmployeeFragment.Listener, EmployeeFragment.Listener {

    private static final String LOG_TAG = "TestApp " + MainActivity.class.getSimpleName();
    private List<Employee> mEmployeeList;
    private Employee mCurrentEmployee = new Employee();
    private boolean mCurrentFlagNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "OnCreate");
        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt("employeeId");
            Log.i(LOG_TAG, id + "");
            mCurrentEmployee = getEmployeeById(id);
            Log.i(LOG_TAG, mCurrentEmployee.toString());
            mCurrentFlagNew = savedInstanceState.getBoolean("flagNew");
            Log.i(LOG_TAG, "" + mCurrentFlagNew);
        }
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Список сотрудников");

        // getEmployeeInDB();

        //initEmployeeList();
        displayDatabaseInfo();

        ListEmployeeFragment listEmployeeFragment = new ListEmployeeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, listEmployeeFragment)
                .show(listEmployeeFragment)
                .commit();

    }

    private Employee getEmployeeById(int id) {
        Employee employee = new Employee();
        SQLiteDatabase db = new EmployeeDbHelper(getApplicationContext()).getReadableDatabase();
        String selection = "_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(EmployeeDbContract.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            employee.setId(cursor.getInt(0));
            employee.setmSurName(cursor.getString(1));
            employee.setmName(cursor.getString(2));
            employee.setmPatronymic(cursor.getString(3));
            // employee.setmPhoto(DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(EmployeeDbContract.COLUMN_PHOTO))));
            employee.setmPhoto(DbBitmapUtility.convertToBitmap(cursor.getString(cursor.getColumnIndex(EmployeeDbContract.COLUMN_PHOTO))));
            int idxBirthday = cursor.getColumnIndex(EmployeeDbContract.COLUMN_BIRTHDAY);

            SimpleDateFormat parseFormat =
                    new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            Date newDate = null;
            try {
                newDate = parseFormat.parse(cursor.getString(idxBirthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            employee.setmBirthday(newDate);
        }
        return employee;
    }

    private void initEmployeeList() {
        mEmployeeList = new ArrayList<>();

        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();
        try {
            date1 = new SimpleDateFormat("dd.MM.yyyy").parse("28.12.1988");
            date2 = new SimpleDateFormat("dd.MM.yyyy").parse("20.10.1983");
            date3 = new SimpleDateFormat("dd.MM.yyyy").parse("10.5.1996");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mEmployeeList.add(new Employee(0, "Петров", "Петр", "Петрович", date1, null));
        mEmployeeList.add(new Employee(1, "Иванов", "Иван", "Иванович", date2, null));
        mEmployeeList.add(new Employee(2, "Семенов", "Семен", "Семенович", date3, null));
    }

    @Override
    public void onEmployeeClicked(Employee employee) {
        mCurrentEmployee = employee;
        mCurrentFlagNew = false;
        EmployeeFragment fragment = new EmployeeFragment(employee, false);
        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public List<Employee> getEmployeeList() {
        return mEmployeeList;
    }

    @Override
    public void deleteEmployee(Employee employee) {
        SQLiteDatabase db = new EmployeeDbHelper(getApplicationContext()).getWritableDatabase();
        int delCount = db.delete(EmployeeDbContract.TABLE_NAME, EmployeeDbContract._ID + " = " + employee.getId(), null);
        displayDatabaseInfo();
        this.recreate();
    }

    @Override
    public Employee getCurrentEmployee() {
        return mCurrentEmployee;
    }

    @Override
    public boolean getCurrentFlagNew() {
        return mCurrentFlagNew;
    }

    @Override
    public void setCurrentEmployee(Employee employee) {
        mCurrentEmployee = employee;
    }

    @Override
    public void setCurrentFlagNew(boolean flag) {
        mCurrentFlagNew = flag;
    }

    @Override
    public void onUpdateEmployee(Employee newEmployee) {
        //mCurrentEmployee = newEmployee;
        Log.d(LOG_TAG, "Update:");
        SQLiteDatabase db = new EmployeeDbHelper(getApplicationContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.i(LOG_TAG, newEmployee.toString());
        values.put(EmployeeDbContract.COLUMN_SURNAME, newEmployee.getmSurName());
        values.put(EmployeeDbContract.COLUMN_NAME, newEmployee.getmName());
        values.put(EmployeeDbContract.COLUMN_PATRONYMIC, newEmployee.getmPatronymic());
        values.put(EmployeeDbContract.COLUMN_BIRTHDAY, newEmployee.getmBirthday().toString());
        //values.put(EmployeeDbContract.COLUMN_PHOTO, DbBitmapUtility.getBytes(newEmployee.getmPhoto()));
        values.put(EmployeeDbContract.COLUMN_PHOTO, DbBitmapUtility.convertToBase64(newEmployee.getmPhoto()));

        Log.i(LOG_TAG, values.toString());
        // обновляем по id
        int updCount = db.update(EmployeeDbContract.TABLE_NAME, values, EmployeeDbContract._ID + " = ?",
                new String[]{newEmployee.getId() + ""});
        Log.d(LOG_TAG, "updated rows count = " + updCount);
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = new EmployeeDbHelper(getApplicationContext()).getReadableDatabase();
        List<Employee> res = null;
        Cursor cursor = db.query(EmployeeDbContract.TABLE_NAME, new String[]{
                EmployeeDbContract._ID,
                EmployeeDbContract.COLUMN_SURNAME,
                EmployeeDbContract.COLUMN_NAME,
                EmployeeDbContract.COLUMN_PATRONYMIC,
                EmployeeDbContract.COLUMN_BIRTHDAY,
                EmployeeDbContract.COLUMN_PHOTO
        }, null, null, null, null, null + " DESC");
        try {
            res = new ArrayList<>();
            while (cursor.moveToNext()) {
                Employee employee = new Employee();
                employee.setId(cursor.getInt(0));
                employee.setmSurName(cursor.getString(1));
                employee.setmName(cursor.getString(2));
                employee.setmPatronymic(cursor.getString(3));
                // employee.setmPhoto(DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(EmployeeDbContract.COLUMN_PHOTO))));
                employee.setmPhoto(DbBitmapUtility.convertToBitmap(cursor.getString(cursor.getColumnIndex(EmployeeDbContract.COLUMN_PHOTO))));
                int idxBirthday = cursor.getColumnIndex(EmployeeDbContract.COLUMN_BIRTHDAY);

                SimpleDateFormat parseFormat =
                        new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

                Date newDate = parseFormat.parse(cursor.getString(idxBirthday));
                employee.setmBirthday(newDate);
                res.add(employee);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
        mEmployeeList = res;
        Log.i(LOG_TAG, res.toString());
        Log.i(LOG_TAG, res.toString());
    }

    @Override
    public void onAddEmployee(Employee newEmployee) {
        SQLiteDatabase db = new EmployeeDbHelper(getApplicationContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeDbContract.COLUMN_SURNAME, newEmployee.getmSurName());
        values.put(EmployeeDbContract.COLUMN_NAME, newEmployee.getmName());
        values.put(EmployeeDbContract.COLUMN_PATRONYMIC, newEmployee.getmPatronymic());
        values.put(EmployeeDbContract.COLUMN_BIRTHDAY, newEmployee.getmBirthday().toString());
        values.put(EmployeeDbContract.COLUMN_PHOTO, DbBitmapUtility.convertToBase64(newEmployee.getmPhoto()));
        Log.i(LOG_TAG, "adding " + newEmployee);
        Log.i(LOG_TAG, values.toString());
        long newRowId = db.insert(EmployeeDbContract.TABLE_NAME, null, values);
        Log.i(LOG_TAG, newRowId + "");
        displayDatabaseInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "mCurrentEmployee: " + mCurrentEmployee);
        outState.putInt("employeeId", mCurrentEmployee.getId());
        outState.putBoolean("flagNew", mCurrentFlagNew);
    }
}
