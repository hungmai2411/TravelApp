package com.travelappproject.SharedPreferences;

import android.content.Context;

public class LocalDataManager {
    private static LocalDataManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new LocalDataManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static LocalDataManager getInstance(){
        if(instance == null)
            instance = new LocalDataManager();

        return instance;
    }

    public static void setLanguage(String language){
        LocalDataManager.getInstance().mySharedPreferences.putStringValue("language", language);
    }

    public static String getLanguage(){
        return LocalDataManager.getInstance().mySharedPreferences.getStringValue("language");
    }
}
