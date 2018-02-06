package ru.stdrone.home.readtechnics.books;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Book implements Serializable {
    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_URI = "uri";

    private StoreType storeType;
    private String name;
    private Uri mUri;

    private AssetManager mAssetManager;
    private String mAssetPath;

    public Book(Intent intent) {
        storeType = StoreType.TEXT_FILE;
        name = intent.getStringExtra(EXTRA_NAME);
        mUri = Uri.parse(intent.getStringExtra(EXTRA_URI));
    }

    Book(AssetManager assetManager, String assetPath) throws IOException {
        InputStream stream = assetManager.open(assetPath);
        InputStreamReader isReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isReader);
        this.name = reader.readLine();

        this.storeType = StoreType.RESOURCE;
        this.mAssetPath = assetPath;
        this.mAssetManager = assetManager;
    }

    Book(String name, Uri filePath) {
        this.storeType = StoreType.TEXT_FILE;
        this.name = name;
        this.mUri = filePath;
    }

    public String getName() {
        return name;
    }

    public InputStream getStream() throws IOException {
        switch (storeType) {
            case RESOURCE:
                return mAssetManager.open(mAssetPath);
            case TEXT_FILE:
                return new FileInputStream(new File(mUri.getPath()));
        }
        return null;
    }

    public enum StoreType {
        RESOURCE, TEXT_FILE
    }
}
