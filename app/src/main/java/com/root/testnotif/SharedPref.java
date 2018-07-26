package com.root.testnotif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String PREF_NAME = "shared_data";
    private static SharedPreferences mPref;
    private static SharedPreferences.Editor mEditor;

    /**
     * Store all key used in the shared preference.
     */
    public enum Key{
        STRING_FIREBASE_TOKEN,
        STRING_URL,
        STRING_USER_TOKEN
    }

    private SharedPref(Context context){
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context){
        return new SharedPref(context);
    }

    public void put(Key key, String val) {
        doEdit();
        mEditor.putString(key.name(), val);
        doCommit();
    }

    public void put(Key key, int val) {
        doEdit();
        mEditor.putInt(key.name(), val);
        doCommit();
    }

    public void put(Key key, boolean val) {
        doEdit();
        mEditor.putBoolean(key.name(), val);
        doCommit();
    }

    public void put(Key key, float val) {
        doEdit();
        mEditor.putFloat(key.name(), val);
        doCommit();
    }

    public String getString(Key key, String defaultValue) {
        return mPref.getString(key.name(), defaultValue);
    }

    public String getString(Key key) {
        return mPref.getString(key.name(), null);
    }

    public int getInt(Key key) {
        return mPref.getInt(key.name(), 0);
    }

    public int getInt(Key key, int defaultValue) {
        return mPref.getInt(key.name(), defaultValue);
    }

    public long getLong(Key key) {
        return mPref.getLong(key.name(), 0);
    }

    public long getLong(Key key, long defaultValue) {
        return mPref.getLong(key.name(), defaultValue);
    }

    public float getFloat(Key key) {
        return mPref.getFloat(key.name(), 0);
    }

    public float getFloat(Key key, float defaultValue) {
        return mPref.getFloat(key.name(), defaultValue);
    }

    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    @SuppressLint("CommitPrefEdits")
    private void doEdit() {
        mEditor = mPref.edit();
    }

    private void doCommit() {
        mEditor.commit();
        mEditor = null;
    }
}
