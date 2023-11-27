package com.example.blurdatingapplication.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FunctionUtil {
    public static void setFaceImage(Context context, Uri uri, ImageView imageView){
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public static void setProfileImage(Context context, Uri uri, ImageView imageView){
        Glide.with(context).load(uri).into(imageView);
    }
    public static  void removeProfileImage(Context context, ImageView imageView){
        Glide.with(context).clear(imageView);
    }


    public static int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date dateBirthday = sdf.parse(birthday);
            Date dateToday = new Date();

            // Create Calendar instances and set time
            Calendar calBirthday = Calendar.getInstance();
            Calendar calCurrentDay = Calendar.getInstance();
            calBirthday.setTime(dateBirthday);
            calCurrentDay.setTime(dateToday);

            int age = calCurrentDay.get(Calendar.YEAR) - calBirthday.get(Calendar.YEAR);

            // Check if birthday has occurred this year
            if (calCurrentDay.get(Calendar.DAY_OF_YEAR) < calBirthday.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }

            return age;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if there is an error parsing the date
    }

}
