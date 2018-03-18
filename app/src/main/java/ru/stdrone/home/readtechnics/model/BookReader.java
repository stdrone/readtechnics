package ru.stdrone.home.readtechnics.model;

import com.google.common.primitives.Chars;

import java.io.IOException;
import java.io.InputStream;

import ru.stdrone.home.readtechnics.service.ReadingRules;
import ru.stdrone.home.readtechnics.service.StatisticCollector;

import static ru.stdrone.home.readtechnics.model.BookText.TERMINATOR;

public class BookReader {

    private final char[] separator = new char[]{'.', '!', '?', TERMINATOR};

    private int mPositionWord, mPositionSentence, mNextWord;

    private StatisticCollector mStatistic;
    private ReadingRules mRules;

    private BookText mBookText;

    public BookReader(int position, StatisticCollector collector, ReadingRules rules) {
        mStatistic = collector;
        mRules = rules;
        mPositionWord = position;
    }

    public StatisticCollector getStatistic() {
        return mStatistic;
    }

    public void init(InputStream stream) throws IOException {
        mBookText = new BookText(stream, mPositionWord);
        mNextWord = nextWord(mPositionWord);
        mPositionSentence = prevSentence();
    }

    public void checkWord(String word) throws IOException {
        if (word.toLowerCase().equals(getCurrentWord().toLowerCase())) {
            mPositionWord = mNextWord;
            mNextWord = nextWord(mPositionWord);
            mStatistic.endWord();
        }
    }

    public int getPositionInView() {
        return mBookText.getBufferPosition(mPositionWord);
    }

    public String getCorrectSentences() throws IOException {
        return mBookText.getBuffer(0, mPositionSentence);
    }

    public String getCurrentSentence() throws IOException {
        return mBookText.getBuffer(mPositionSentence, mPositionWord);
    }

    public String getCurrentWord() throws IOException {
        return mBookText.getBuffer(mPositionWord, mNextWord);
    }

    public String getLastText() throws IOException {
        return mBookText.getBuffer(mNextWord);
    }

    public int getPosition() {
        return mPositionWord;
    }

    private int prevSentence() throws IOException {
        int position = mPositionWord;
        do {
            position--;
        } while (!Chars.contains(separator, mBookText.charAt(position)));
        return position;
    }

    private boolean checkSentence() {
        StatisticStorage stat = mStatistic.endSentence();
        return mRules.CheckSentence(stat);
    }

    private int nextWord(int start) throws IOException {
        int position = start;
        boolean foundLetter = false, finish = false;
        while (!finish) {
            char chCur = mBookText.charAt(position);
            if (!foundLetter) {
                if (Chars.contains(separator, chCur)) {
                    if (checkSentence()) {
                        mPositionSentence = position + 1;
                    }
                }
                if (foundLetter = Character.isLetter(chCur) || chCur == BookText.TERMINATOR) {
                    mPositionWord = position;
                }
            } else {
                finish = !Character.isLetter(chCur);
            }
            if (!finish) {
                position += 1;
            }
        }
        return position;
    }
}
