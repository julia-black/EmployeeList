package com.example.frost.testapplication.model;

import android.graphics.Bitmap;

import com.example.frost.testapplication.db.DbBitmapUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee {

    private int id;
    private String mSurName;
    private String mName;
    private String mPatronymic;
    private Date mBirthday;
    private Bitmap mPhoto;


    public Employee(int id, String mSurName, String mName, String mPatronymic, Date mBirthday, Bitmap mPhoto) {
        this.id = id;
        this.mSurName = mSurName;
        this.mName = mName;
        this.mPatronymic = mPatronymic;
        this.mBirthday = mBirthday;
        this.mPhoto = mPhoto;
    }

    public Employee(int id, String mSurName, String mName, String mPatronymic, String strBirthday, Bitmap mPhoto) {
        this.id = id;
        this.mSurName = mSurName;
        this.mName = mName;
        this.mPatronymic = mPatronymic;
        Date date = new Date();
        try {
            date = new SimpleDateFormat("Дата рождения: dd.MM.yyyy").parse(strBirthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.mBirthday = date;
        this.mPhoto = mPhoto;
    }

    public void setmPhoto(Bitmap mPhoto) {
        this.mPhoto = mPhoto;
    }

    public Bitmap getmPhoto() {
        return mPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee() {
    }

    public String getmSurName() {
        return mSurName;
    }

    public String getmName() {
        return mName;
    }

    public String getmPatronymic() {
        return mPatronymic;
    }

    public Date getmBirthday() {
        return mBirthday;
    }

    public void setmSurName(String mSurName) {
        this.mSurName = mSurName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmPatronymic(String mPatronymic) {
        this.mPatronymic = mPatronymic;
    }

    public void setmBirthday(Date mBirthday) {
        this.mBirthday = mBirthday;
    }

    public String getFullName(){
        return mSurName + " " + mName + " " + mPatronymic;
    }

    public String  getStringBirthday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("dd.MM.yyyy");
        return "Дата рождения: " + simpleDateFormat.format(mBirthday);
    }

   // @Override
   // public String toString() {
   //     return "Employee{" +
   //             "id=" + id +
   //             ", mSurName='" + mSurName + '\'' +
   //             ", mName='" + mName + '\'' +
   //             ", mPatronymic='" + mPatronymic + '\'' +
   //             ", mBirthday=" + mBirthday +
   //             '}';
   // }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", mSurName='" + mSurName + '\'' +
                ", mName='" + mName + '\'' +
                ", mPatronymic='" + mPatronymic + '\'' +
                ", mBirthday=" + mBirthday +
               // ", mPhoto=" + DbBitmapUtility.convertToBase64(mPhoto) +
                '}';
    }
}
