package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.Spanned;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public class BookText implements Closeable {
    private static final int BUFFER = 2000;
    private static final String BOOK_PREFERENCES = "BOOK_PREFERENCES";
    private static final String CLOSE_FONT = "</font>";
    private static final String OPEN_FONT = "<font color=\"%s\">";
    private StringBuilder mTextCorrent, mTextCurrent, mTextFuture;
    private String mPath;
    private BufferedReader mReader;
    private int mPosition;

    public BookText(Context context, Book book) throws IOException {
        mPath = book.getPath();
        mReader = book.getReader(context);

        SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        mPosition = preferences.getInt(mPath, 48);

        mTextCorrent = new StringBuilder();
        mTextCurrent = new StringBuilder();
        mTextFuture = new StringBuilder();

        if (mReader.skip(Math.max(mPosition - BUFFER, 0)) >= 0) {
            char[] buffer = new char[Math.min(mPosition, BUFFER)];
            if (mReader.read(buffer, 0, Math.min(mPosition, BUFFER)) > 0) {
                mTextCorrent.append(ch2str(buffer));
            }
            buffer = new char[BUFFER];
            if (mReader.read(buffer, 0, BUFFER) > 0) {
                mTextFuture.append(ch2str(buffer));
            }
        }
        nextWord();
    }

    public void Store(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(BOOK_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(mPath, mPosition);
        edit.apply();
    }

    public Spanned readText() throws IOException {
        char[] buffer = new char[BUFFER * 2];

        String builder = String.format(OPEN_FONT, "#00FF00") +
                mTextCorrent +
                CLOSE_FONT +
                String.format(OPEN_FONT, "#afde2d") +
                mTextCurrent +
                CLOSE_FONT +
                mTextFuture;

        return Html.fromHtml(builder);
    }

    private String ch2str(char[] buffer) {
        return String.valueOf(buffer).replace("\n", "<br>").replace("\0", "");
    }

    private void nextWord() {

    }

    @Override
    public void close() throws IOException {
        mReader.close();
    }
}
