package ru.stdrone.home.readtechnics.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static final String SETTINGS = "SETTINGS";

    private static final String RuleCorrectWordsPerSentenceRate = "RuleCorrectWordsPerSentenceRate";
    private static final int RuleCorrectWordsPerSentenceRateDefault = 25;
    private int mRuleCorrectWordsPerSentenceRate;

    public Settings(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        mRuleCorrectWordsPerSentenceRate = preferences.getInt(RuleCorrectWordsPerSentenceRate, RuleCorrectWordsPerSentenceRateDefault);
    }

    public void store(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putInt(RuleCorrectWordsPerSentenceRate, mRuleCorrectWordsPerSentenceRate);
        editor.apply();
    }

    public int getRuleCorrectWordsPerSentenceRate() {
        return mRuleCorrectWordsPerSentenceRate;
    }
}
