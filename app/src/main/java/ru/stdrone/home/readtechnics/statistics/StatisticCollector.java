package ru.stdrone.home.readtechnics.statistics;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class StatisticCollector {


    private static final String STATISTIC_PREFERENCES = "STATISTIC_PREFERENCES";
    static StatisticCollector _instance;
    private StatisticStorage mTotal, mSession, mSentence;

    public static StatisticCollector getInstance() {
        if (_instance == null)
            _instance = new StatisticCollector();
        return _instance;
    }

    public void endWord() {
        mTotal.mWords++;
        mSession.mWords++;
        mSentence.mWords++;
    }


    public StatisticStorage endSentence() throws CloneNotSupportedException {
        mTotal.mSentences++;
        mSession.mSentences++;
        mSentence.mSentences++;

        mSentence.mEnd = new Date();
        mSentence.mSeconds = (int) ((mSentence.mEnd.getTime() - mSentence.mStart.getTime()) / 1000);

        StatisticStorage stats = mSentence.clone();

        mSentence.mWords = 0;
        mSentence.mStart = new Date();
        return stats;
    }

    public void init(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(STATISTIC_PREFERENCES, Context.MODE_PRIVATE);
        if (preferences.contains(STATISTIC_PREFERENCES)) {
            String data = preferences.getString(STATISTIC_PREFERENCES, "");
            Type type = new TypeToken<StatisticStorage>() {
            }.getType();

            mTotal = new Gson().fromJson(data, type);
        }
        ReadingRules.getInstancle().init(context);
    }

    public void store(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(STATISTIC_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(STATISTIC_PREFERENCES, new Gson().toJson(mTotal));
        editor.apply();
    }

    class StatisticStorage implements Cloneable {
        int mWords = 0, mSentences = 0, mSeconds = 0;
        Date mStart, mEnd;

        public StatisticStorage clone() throws CloneNotSupportedException {
            StatisticStorage clone = (StatisticStorage) super.clone();
            clone.mWords = mWords;
            clone.mSentences = mSentences;
            clone.mSeconds = mSeconds;
            clone.mStart = mStart;
            clone.mEnd = mEnd;
            return clone;
        }
    }
}

