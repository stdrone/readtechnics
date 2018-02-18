package ru.stdrone.home.readtechnics.model;

import java.io.Serializable;

public class Book implements Serializable {
    public static final String EXTRA_BOOK = "book";
    private int mPosition;
    private String mName;
    private String mPath;

    public Book(String name, String filePath) {
        this.mName = name;
        this.mPath = filePath;
    }

    public String getmName() {
        return mName;
    }

    String getPath() {
        return mPath;
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public String toString() {
        return getmName();
    }
}
