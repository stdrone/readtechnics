package ru.stdrone.home.readtechnics.service;

import android.content.Context;

import ru.stdrone.home.readtechnics.model.Settings;
import ru.stdrone.home.readtechnics.model.StatisticStorage;

public class ReadingRules {

    private static final int READ_TIME_RATE = 60; // reading rate per minute
    static private ReadingRules _instance;

    private Settings mSettings;

    public ReadingRules(Context context) {
        mSettings = new Settings(context);
    }

    public boolean CheckSentence(StatisticStorage statistics) {
        return (statistics.getSeconds() > 0) && mSettings.getRuleCorrectWordsPerSentenceRate() <= (statistics.getWords() / statistics.getSeconds() * READ_TIME_RATE);
    }
}
