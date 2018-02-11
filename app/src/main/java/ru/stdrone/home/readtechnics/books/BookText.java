package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.common.primitives.Chars;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BookText implements Closeable {
    private static final int BUFFER = 2000;
    private static final String BOOK_PREFERENCES = "BOOK_PREFERENCES";
    private final char[] separator = new char[]{'.', '!', '?'};
    private StringBuffer mTextBuffer;
    private String mPath;
    private BufferedReader mReader;
    private int mPositionWord, mPositionSentence, mNextWord, mBufferStart;


    public BookText(Book book) {
        mPath = book.getPath();
    }

    public void init(Context context) throws IOException {
        if (mTextBuffer == null) {
            mReader = getReader(context);

            SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
            mPositionWord = preferences.getInt(mPath, 0);

            mTextBuffer = new StringBuffer();

            mBufferStart = Math.max(mPositionWord - BUFFER, 0);

            if (mReader.skip(mBufferStart) >= 0) {
                readText(BUFFER * 2);
                mPositionWord -= mBufferStart;
                mNextWord = nextWord(mPositionWord);
                mPositionSentence = prevSentence();
            }
        }
    }

    public void Store(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(mPath, mBufferStart + mPositionWord);
        edit.apply();
    }

    public void reset(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(mPath);
        edit.apply();
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

    public void checkWord(String word) throws IOException {
        mPositionWord = mNextWord;
        mNextWord = nextWord(mPositionWord);
        checkBuffer();
    }

    private void checkBuffer() throws IOException {
        int halfBuffer = BUFFER / 2;
        if (mNextWord > BUFFER + halfBuffer) {
            mTextBuffer.delete(0, halfBuffer);
            readText(halfBuffer);
            mBufferStart += halfBuffer;
            mPositionWord -= halfBuffer;
            mNextWord -= halfBuffer;
            mPositionSentence -= halfBuffer;
        }
    }

    @Override
    public void close() throws IOException {
        mReader.close();
    }

    private void readText(int len) throws IOException {
        char[] buffer = new char[len];
        if (mReader.read(buffer, 0, len) > 0) {
            mTextBuffer.append(String.valueOf(buffer).replace("\0", ""));
        }
    }

    private BufferedReader getReader(Context context) throws FileNotFoundException {
        InputStream stream = context.getContentResolver().openInputStream(Uri.parse(mPath));
        if (stream != null) {
            InputStreamReader isReader = new InputStreamReader(stream);
            return new BufferedReader(isReader);
        }
        return null;
    }

}
