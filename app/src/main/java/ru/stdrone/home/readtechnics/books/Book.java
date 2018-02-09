package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Book implements Serializable {
    public static final String EXTRA_BOOK = "book";

    private StoreType storeType;
    private String name;
    private String mPath;

    Book(AssetManager assetManager, String assetPath) throws IOException {
        InputStream stream = assetManager.open(assetPath);
        InputStreamReader isReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isReader);
        this.name = reader.readLine();
        reader.close();
        stream.close();

        this.storeType = StoreType.RESOURCE;
        this.mPath = assetPath;
    }

    public Book(String name, String filePath) {
        this.storeType = StoreType.TEXT_FILE;
        this.name = name;
        this.mPath = filePath;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    InputStream getStream(Context context) throws IOException {
        switch (storeType) {
            case RESOURCE:
                return context.getAssets().open(mPath);
            case TEXT_FILE:
                return new FileInputStream(new File(mPath));
        }
        return null;
    }

    public enum StoreType {
        RESOURCE, TEXT_FILE
    }
}
