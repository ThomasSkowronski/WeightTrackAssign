package com.example.weighttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User {
    private String name;
    private String units;
    private float height;
    private float weight;
    private boolean isNew;

    private static final String PREF_NAME = "name";
    private static final String PREF_UNIT = "unit";
    private static final String PREF_HEIGHT = "height";
    private static final String PREF_WEIGHT = "weight";
    private static final String PREF_NEW = "new";

    public User (Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        setName( pref.getString(PREF_NAME, "username"));
        setUnits( pref.getString(PREF_UNIT, "Kg"));
        setHeight(pref.getFloat(PREF_HEIGHT, 0));
        setWeight(pref.getFloat(PREF_WEIGHT, 0));
        setNew(pref.getBoolean(PREF_NEW, true));
    }

    public void setPrefs (Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_NAME, name);
        editor.putString(PREF_UNIT, units);
        editor.putFloat(PREF_HEIGHT, height);
        editor.putFloat(PREF_WEIGHT, weight);
        editor.putBoolean(PREF_NEW, isNew);
        editor.commit();
    }

    public boolean isNew() { return isNew; }

    public void setNew(boolean b) {
        isNew = b;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setWeight(float d){
        weight = d;
    }

    public float getWeight() {
        return weight;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public void setUnits(String s) {
        this.units = s;
    }

    public String getUnit() {
        return units;
    }
}