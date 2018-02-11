package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.primitives.Chars;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public class BookText implements Closeable {
    private static final int BUFFER = 2000;
    private static final String BOOK_PREFERENCES = "BOOK_PREFERENCES";
    private final char[] separator = new char[]{'.', '!', '?'};
    private StringBuffer mTextBuffer;
    private String mPath;
    private BufferedReader mReader;
    private int mPositionWord, mPositionSentence, mNextWord;


    public BookText(Context context, Book book) throws IOException {
        mPath = book.getPath();
        mReader = book.getReader(context);

        SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        mPositionWord = preferences.getInt(mPath, 0);

        mTextBuffer = new StringBuffer();

        if (mReader.skip(Math.max(mPositionWord - BUFFER, 0)) >= 0) {
            char[] buffer = new char[BUFFER * 2];
            if (mReader.read(buffer, 0, BUFFER * 2) > 0) {
                mTextBuffer.append(String.valueOf(buffer).replace("\0", ""));
            }
        }
        mNextWord = nextWord(mPositionWord);

        mPositionSentence = prevSentence();
    }

    public void Store(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(mPath, mPositionWord);
        edit.apply();
    }

    public void reset() {
        mPositionSentence = 0;
        mPositionWord = 0;
        mNextWord = nextWord(mPositionWord);
    }

    public int getPosition() {
        return mPositionWord;
    }

    public String getCorrectSentences() {
        return mTextBuffer.substring(0, mPositionSentence);
    }

    public String getCurrentSentence() {
        return mTextBuffer.substring(mPositionSentence, Math.max(mPositionWord, mPositionSentence));
    }

    public String getCurrentWord() {
        return mTextBuffer.substring(mPositionWord, mNextWord);
    }

    public String getLastText() {
        return mTextBuffer.substring(mNextWord);
    }

    private int prevSentence() {
        int position = mPositionWord;
        do {
            position--;
        } while (position >= 0 && !Chars.contains(separator, mTextBuffer.charAt(position)));
        return Math.max(position, 0);
    }

    private int nextWord(int start) {
        int position = start;
        boolean foundLetter = false, finish = false;
        while (!finish) {
            if (!foundLetter) {
                if (Chars.contains(separator, mTextBuffer.charAt(position))) {
                    mPositionWord = mPositionSentence = position;
                }
                foundLetter = Character.isLetter(mTextBuffer.charAt(position));
            } else {
                finish = !Character.isLetter(mTextBuffer.charAt(position));
            }
            if (!finish) {
                position += 1;
                finish = position == 0 || position >= mTextBuffer.length();
            }
        }
        return Math.max(0, Math.min(position, mTextBuffer.length() - 1));
    }

    public void checkWord(String word) {
        mPositionWord = mNextWord;
        mNextWord = nextWord(mPositionWord);
    }

    @Override
    public void close() throws IOException {
        mReader.close();
    }
}
