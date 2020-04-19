package com.panaceasoft.firoozboard;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.panaceasoft.firoozboard.edit.model.Detail;


public class SharedPrefManager {

    private SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        String SHARED_PREF_NAME = "SHARED_PREF";
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void save(Detail detail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(detail);
        editor.putString("Detail", json);
        editor.apply();
    }


    public void empty() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(new Detail());
        editor.putString("Detail", json);
        editor.apply();
    }

    public Detail get() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Detail", "");
        return gson.fromJson(json, Detail.class);
    }


}
