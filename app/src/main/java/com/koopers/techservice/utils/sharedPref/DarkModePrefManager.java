package com.koopers.techservice.utils.sharedPref;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


public class DarkModePrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "hardware-dark-mode";
    private static final String IS_NIGHT_MODE = "IsNightMode";
    private static final String IP_ADDRESS = "192.168.0.107";


    @SuppressLint("CommitPrefEdits")
    public DarkModePrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIpAddress(String ipAddress){
        editor.putString(IP_ADDRESS, ipAddress);
        editor.commit();
    }

    public void setDarkMode(boolean isFirstTime) {
        editor.putBoolean(IS_NIGHT_MODE, isFirstTime);
        editor.commit();

    }

    public boolean isNightMode() {
        return pref.getBoolean(IS_NIGHT_MODE, true);
    }


    public String getIpAddress(){
        return pref.getString(IP_ADDRESS,"192.168.0.107");
    }

}