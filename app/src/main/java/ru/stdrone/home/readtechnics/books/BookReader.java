package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.primitives.Chars;

import java.io.IOException;

import static ru.stdrone.home.readtechnics.books.BookText.TERMINATOR;

public class BookReader {

    private static final String BOOK_PREFERENCES = "BOOK_PREFERENCES";

    private final char[] separator = new char[]{'.', '!', '?', TERMINATOR};

    private int mPositionWord, mPositionSentence, mNextWord;
    private SharedPreferences mPreferences;
    private String mPrefPath;

    private BookText mBookText;

    public BookReader(Context context, Book book) {
        mPrefPath = book.getPath();
        mBookText = new BookText(book);

        mPreferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        mPositionWord = mPreferences.getInt(mPrefPath, 0);
    }

    public void init(Context context) throws IOException {
        mBookText.init(context, mPositionWord);
        mNextWord = nextWord(mPositionWord);
        mPositionSentence = prevSentence();
    }

    public void checkWord(String word) throws IOException {
        mPositionWord = mNextWord;
        mNextWord = nextWord(mPositionWord);
    }

    public int getPosition() {
        return mBookText.getBufferPosition(mPositionWord);
    }

    public Object getCorrectSentences() throws IOException {
        return mBookText.getBuffer(0, mPositionSentence);
    }

    public Object getCurrentSentence() throws IOException {
        return mBookText.getBuffer(mPositionSentence, mPositionWord);
    }

    public Object getCurrentWord() throws IOException {
        return mBookText.getBuffer(mPositionWord, mNextWord);
    }

    public Object getLastText() throws IOException {
        return mBookText.getBuffer(mNextWord);
    }

    private void store() {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(mPrefPath, mPositionWord);
        edit.apply();
    }

    public void reset() {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.remove(mPrefPath);
        edit.apply();
    }

    private int prevSentence() throws IOException {
        int position = mPositionWord;
        do {
            position--;
        } while (!Chars.contains(separator, mBookText.charAt(position)));
        return position;
    }

    private boolean checkSentence() {
        return true;
    }

    private int nextWord(int start) throws IOException {
        int position = start;
        boolean foundLetter = false, finish = false;
        while (!finish) {
            if (!foundLetter) {
                if (Chars.contains(separator, mBookText.charAt(position))) {
                    mPositionWord = mPositionSentence = position + 1;
                    if (checkSentence()) {
                        store();
                    }
                }
                foundLetter = Character.isLetter(mBookText.charAt(position));
            } else {
                finish = !Character.isLetter(mBookText.charAt(position));
            }
            if (!finish) {
                position += 1;
            }
        }
        return position;
    }
}
