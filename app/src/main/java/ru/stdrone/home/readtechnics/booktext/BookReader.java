package ru.stdrone.home.readtechnics.booktext;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.primitives.Chars;

import java.io.IOException;

import ru.stdrone.home.readtechnics.book.Book;
import ru.stdrone.home.readtechnics.statistics.StatisticCollector;

import static ru.stdrone.home.readtechnics.booktext.BookText.TERMINATOR;

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

    void init(Context context) throws IOException {
        mBookText.init(context, mPositionWord);
        mNextWord = nextWord(mPositionWord);
        mPositionSentence = prevSentence();
    }

    void checkWord(String word) throws IOException {
        if (word.equals("")) {
            mPositionWord = mNextWord;
            mNextWord = nextWord(mPositionWord);
            StatisticCollector.getInstance().endWord();
        }
    }

    int getPosition() {
        return mBookText.getBufferPosition(mPositionWord);
    }

    Object getCorrectSentences() throws IOException {
        return mBookText.getBuffer(0, mPositionSentence);
    }

    Object getCurrentSentence() throws IOException {
        return mBookText.getBuffer(mPositionSentence, mPositionWord);
    }

    Object getCurrentWord() throws IOException {
        return mBookText.getBuffer(mPositionWord, mNextWord);
    }

    Object getLastText() throws IOException {
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
        StatisticCollector.StatisticStorage stat = StatisticCollector.getInstance().endSentence();


        return true;
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
                        store();
                    }
                }
                if (foundLetter = Character.isLetter(chCur) || chCur == '\0') {
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
