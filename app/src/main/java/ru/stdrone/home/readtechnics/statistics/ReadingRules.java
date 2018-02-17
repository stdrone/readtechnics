package ru.stdrone.home.readtechnics.statistics;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ReadingRules {

    public static final String SETTINGS = "SETTINGS";
    private static final String SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES";
    static private ReadingRules _instance;

    private Settings mSettings;

    static public ReadingRules getInstancle() {
        if (_instance == null)
            _instance = new ReadingRules();
        return _instance;
    }

    boolean CheckSentence(StatisticCollector.StatisticStorage statistics) {
        if (mSettings.mRuleCorrectSentence <= (statistics.mWords / statistics.mSeconds * 60.0))
            return true;
        return false;
    }

    public void init(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        if (preferences.contains(SETTINGS_PREFERENCES)) {
            String data = preferences.getString(SETTINGS_PREFERENCES, "");
            Type type = new TypeToken<Settings>() {
            }.getType();

            mSettings = new Gson().fromJson(data, type);
        } else {
            mSettings.mRuleCorrectSentence = 25;
        }
    }

    public void store(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putString(SETTINGS_PREFERENCES, new Gson().toJson(mSettings));
        editor.apply();
    }

    private class Settings {
        int mRuleCorrectSentence;
    }
}
