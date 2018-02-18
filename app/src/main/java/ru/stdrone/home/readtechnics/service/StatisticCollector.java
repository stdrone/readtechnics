package ru.stdrone.home.readtechnics.service;

import android.content.Context;

import ru.stdrone.home.readtechnics.model.StatisticStorage;

public class StatisticCollector {


    private static final String STATISTIC_TOTAL = "STATISTIC_TOTAL";
    static StatisticCollector _instance;
    private StatisticStorage mTotal, mSession, mSentence;

    public StatisticCollector(Context context) {
        mTotal = StatisticStorage.fromPreferences(context.getSharedPreferences(STATISTIC_TOTAL, Context.MODE_PRIVATE));
        mSentence = new StatisticStorage();
        mSession = new StatisticStorage();
    }

    public void endWord() {
        mTotal.incWords();
        mSession.incWords();
        mSentence.incWords();
    }

    public void store(Context context) {
        context.getSharedPreferences(STATISTIC_TOTAL, Context.MODE_PRIVATE);
    }


    public StatisticStorage endSentence() {
        mTotal.incSentences();
        mSession.incSentences();
        mSentence.incSentences();

        mSentence.finish();

        StatisticStorage stats;
        try {
            stats = mSentence.clone();
        } catch (CloneNotSupportedException e) {
            stats = new StatisticStorage();
        }

        mSentence.resetWords();
        mSentence.start();
        return stats;
    }


}

