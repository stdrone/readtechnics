package ru.stdrone.home.readtechnics.books;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;

public class BooksList extends ArrayList<Book> {

    private final String BOOKS_LIST_ASSETS_PATH = "books";

    public BooksList(AssetManager assetManager) {
        super();
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

}
