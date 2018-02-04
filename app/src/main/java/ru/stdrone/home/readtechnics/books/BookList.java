package ru.stdrone.home.readtechnics.books;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.DataSetObserver;

import java.io.IOException;
import java.util.ArrayList;

class BookList extends ArrayList<Book> {

    static final String BOOKS_PREFERENES = "bookList";
    private final String BOOKS_LIST_ASSETS_PATH = "books";

    BookList(AssetManager assetManager, SharedPreferences preferences) {
        super();
        InitFromAssets(assetManager);
        InitFromPreferences(preferences);
    }

    void SavePreferences(SharedPreferences preferences) {
        // TODO: implement
    }

    private void InitFromAssets(AssetManager assetManager) {
        String[] list = new String[0];
        try {
            list = assetManager.list(BOOKS_LIST_ASSETS_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String item : list) {
            try {
                this.add(new Book(assetManager, BOOKS_LIST_ASSETS_PATH + "/" + item));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void InitFromPreferences(SharedPreferences preferences) {
        // TODO:
    }
}
