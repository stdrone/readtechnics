package ru.stdrone.home.readtechnics.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class StatisticStorage implements Cloneable {
    private int mWords = 0, mSentences = 0, mSeconds = 0;
    private Date mStart, mEnd;

    static public StatisticStorage Deserialize(String storage) {
        if (storage == null) {
            return new StatisticStorage();
        } else {
            Type type = new TypeToken<StatisticStorage>() {
            }.getType();

            return new Gson().fromJson(storage, type);
        }
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

    public String Serialize() {
        return new Gson().toJson(this);
    }

    public int getWords() {
        return mWords;
    }

    public int getSeconds() {
        return mSeconds;
    }
}
