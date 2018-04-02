package inatel.com.br.restaurante.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mateus on 19/03/18.
 */

public class SharedPreferencesController {

    public static void putString(final Context context, final String key, final String value) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putString(key, value).commit();
        }
    }

    public static String getString(final Context context, final String key) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getString(key, "-1");
        }
        return "-1";
    }

    public static void putInt(final Context context, final String key, final int value) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putInt(key, value).commit();
        }
    }

    public static boolean getBool(final Context context, final String key) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getBoolean(key, false);
        }
        return false;
    }

    public static int getInt(final Context context, final String key) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getInt(key, -1);
        }
        return -1;
    }

    public static void putFloat(final Context context, final String key, final float value) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putFloat(key, value).commit();
        }
    }

    public static void putBool(final Context context, final String key, final boolean value) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putBoolean(key, value).commit();
        }
    }

    public static float getFloat(final Context context, final String key) {
        if(context != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            float test = sp.getFloat(key, -1);
            return sp.getFloat(key, -1);
        }
        return -1;
    }
}
