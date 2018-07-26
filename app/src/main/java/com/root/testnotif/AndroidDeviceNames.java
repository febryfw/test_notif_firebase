package com.root.testnotif;

import android.content.Context;
import android.os.Build;
import android.util.Log;


public class AndroidDeviceNames {
     Context mContext;

    public AndroidDeviceNames(Context context){
        this.mContext = context;
    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }

    }

    public  float getAPIVerison() {

        float f=1f;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(Build.VERSION.RELEASE.substring(0, 2));
            f= Float.valueOf(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("myApp", "error retriving api version" + e.getMessage());
        }

        return f;
    }


}
