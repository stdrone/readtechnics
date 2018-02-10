package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Book implements Serializable {
    public static final String EXTRA_BOOK = "book";
    private String mName;
    private String mPath;
    int mPosition;

    public Book(String name, String filePath) {
        this.mName = name;
        this.mPath = filePath;
    }

    public String getmName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    @Override
    public String toString() {
        return getmName();
    }

    BufferedReader getReader(Context context) throws FileNotFoundException {
        InputStream stream = context.getContentResolver().openInputStream(Uri.parse(mPath));
        if (stream != null) {
            InputStreamReader isReader = new InputStreamReader(stream);
            return new BufferedReader(isReader);
        }
        return null;
    }
}
