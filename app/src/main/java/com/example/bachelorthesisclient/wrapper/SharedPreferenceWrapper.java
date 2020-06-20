package com.example.bachelorthesisclient.wrapper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceWrapper {
    private SharedPreferences sharedPreferences;
    private static SharedPreferenceWrapper instance;

    private static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";

    public static SharedPreferenceWrapper getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("SharedPreferenceWrapper instance not created");
        }

        return instance;
    }

    public static void createInstance(Context context) {
        instance = new SharedPreferenceWrapper(context);
    }

    private SharedPreferenceWrapper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void put(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            editor.putLong(key, Double.doubleToRawLongBits((double) value));
        } else if (value instanceof String) {
            editor.putString(key, value.toString());
        }

        editor.apply();
    }

    public Object get(String key, Object defaultValue) {
        try {
            if (defaultValue instanceof Integer) {
                return sharedPreferences.getInt(key, (Integer) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
            } else if (defaultValue instanceof Long) {
                return sharedPreferences.getLong(key, (Long) defaultValue);
            } else if (defaultValue instanceof Float) {
                return sharedPreferences.getFloat(key, (Float) defaultValue);
            } else if (defaultValue instanceof Double) {
                return Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToLongBits((double) defaultValue)));
            } else if (defaultValue instanceof String) {
                return sharedPreferences.getString(key, defaultValue.toString());
            }
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e("Exception", ex.getMessage());
            } else {
                Log.e("Exception", "Exception in SharedPreferencesWrapper [get]");
            }

        }

        return  defaultValue;
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
