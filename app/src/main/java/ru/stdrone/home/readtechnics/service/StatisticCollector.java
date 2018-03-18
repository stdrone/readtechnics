package ru.stdrone.home.readtechnics.service;

import ru.stdrone.home.readtechnics.model.StatisticStorage;

public class StatisticCollector {


    private static final String STATISTIC_TOTAL = "STATISTIC_TOTAL";
    static StatisticCollector _instance;
    private StatisticStorage mTotal, mSession, mSentence;

    public StatisticCollector(StatisticStorage totalStat) {
        mTotal = totalStat;
        mSentence = new StatisticStorage();
        mSession = new StatisticStorage();
    }

    public void endWord() {
        mTotal.incWords();
        mSession.incWords();
        mSentence.incWords();
    }

    public StatisticStorage getTotal() {
        return mTotal;
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

