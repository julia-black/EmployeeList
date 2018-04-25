package com.example.frost.testapplication.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class DbBitmapUtility {
   //конвертируем Bitmap на Base64
   public static String convertToBase64(Bitmap bitmap) {
       if(bitmap != null) {
           ByteArrayOutputStream os = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
           byte[] byteArray = os.toByteArray();
           return Base64.encodeToString(byteArray, 0);
       }
       return "";
   }

    //и наоборот
    public static Bitmap convertToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmapResult = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmapResult;
    }
}
