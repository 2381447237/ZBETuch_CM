package com.youli.zbetuch.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {
    public static void putString(String key,String content, Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,content);
        editor.commit();
    }
    public static String getString(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo.txt", Context.MODE_PRIVATE);
       return sp.getString(key,"");
    }
}
