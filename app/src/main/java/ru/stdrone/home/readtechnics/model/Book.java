package ru.stdrone.home.readtechnics.model;

import java.io.Serializable;

public class Book implements Serializable {
    public static final String EXTRA_BOOK = "book";
    private String mName;
    private String mPath;

    public Book(String name, String filePath) {
        this.mName = name;
        this.mPath = filePath;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    @Override
    public String toString() {
        return getName();
    }
}
