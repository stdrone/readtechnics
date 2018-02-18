package ru.stdrone.home.readtechnics.model;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class StatisticStorage implements Cloneable {
    private static final String STATISTIC = "STATISTIC";
    private int mWords = 0, mSentences = 0, mSeconds = 0;
    private Date mStart, mEnd;

    static public StatisticStorage fromPreferences(SharedPreferences preferences) {
        StatisticStorage statistic;
        if (preferences.contains(STATISTIC)) {
            String data = preferences.getString(STATISTIC, "");
            Type type = new TypeToken<StatisticStorage>() {
            }.getType();

            statistic = new Gson().fromJson(data, type);
        } else {
            statistic = new StatisticStorage();
        }
        return statistic;
    }

    public StatisticStorage clone() throws CloneNotSupportedException {
        StatisticStorage clone = (StatisticStorage) super.clone();
        clone.mWords = mWords;
        clone.mSentences = mSentences;
        clone.mSeconds = mSeconds;
        clone.mStart = mStart;
        clone.mEnd = mEnd;
        return clone;
    }

    public void resetWords() {
        mWords = 0;
    }

    public void reset() {
        mWords = 0;
        mSeconds = 0;
        mSentences = 0;
        mStart = null;
        mEnd = null;
    }

    public void incWords() {
        mWords++;
    }

    public void incSentences() {
        mSentences++;
    }

    public void start() {
        mStart = new Date();
    }

    public void finish() {
        if (mStart != null) {
            mEnd = new Date();
            mSeconds = (int) ((mEnd.getTime() - mStart.getTime()) / 1000);
        }
    }

    public void store(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(STATISTIC, new Gson().toJson(this));
        editor.apply();
    }

    public int getWords() {
        return mWords;
    }

    public int getSeconds() {
        return mSeconds;
    }
}
