package ru.stdrone.home.readtechnics.books;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Book {
    private StoreType storeType;
    private String name;

    private String filePath;
    private AssetManager assetManager;

    Book(AssetManager assetManager, String filePath) throws IOException {
        InputStream stream = assetManager.open(filePath);
        InputStreamReader isReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isReader);
        this.name = reader.readLine();

        this.storeType = StoreType.RESOURCE;
        this.filePath = filePath;
        this.assetManager = assetManager;
    }

    Book(String name, String filePath) {
        this.storeType = StoreType.TEXT_FILE;
        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public InputStream getStream() throws IOException {
        switch (storeType) {
            case RESOURCE:
                return assetManager.open(filePath);
            case TEXT_FILE:
                return new FileInputStream(new File(filePath));
        }
        return null;
    }

    public enum StoreType {
        RESOURCE, TEXT_FILE
    }
}
