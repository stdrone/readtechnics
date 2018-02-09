package ru.stdrone.home.readtechnics.books;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookList {

    private static final String BOOK_LIST = "BOOK_LIST";
    private static final String BOOKS_LIST_ASSETS_PATH = "books";

    private ArrayList<Book> mBookList;
    private SharedPreferences mPreferences;
    private Resources mResources;
    private Bundle mBundle;

    public BookList(SharedPreferences preferences, Resources resources) {
        mResources = resources;
        mPreferences = preferences;
    }

    private static ArrayList<Book> RestoreList(SharedPreferences preferences, Resources resources) {
        if (preferences.contains(BOOK_LIST)) {
            return Deserialize(preferences.getString(BOOK_LIST, ""));
        } else {
            return InitList(resources);
        }
    }

    private static ArrayList<Book> InitList(Resources resources) {
        String[] list = new String[0];
        AssetManager assetManager = resources.getAssets();
        String locale = resources.getConfiguration().locale.getCountry();
        String path = "";
        ArrayList<Book> arrayList = new ArrayList<>();
        try {
            path = locale + File.separator + BOOKS_LIST_ASSETS_PATH;
            list = assetManager.list(path);
        } catch (IOException e) {
            try {
                path = BOOKS_LIST_ASSETS_PATH;
                list = assetManager.list(path);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        for (String item : list) {
            try {
                arrayList.add(new Book(assetManager, path + File.separator + item));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private static String Serialize(ArrayList arrayList) {
        return new Gson().toJson(arrayList);
    }

    private static ArrayList<Book> Deserialize(String data) {
        Type type = new TypeToken<List<Book>>() {
        }.getType();

        return new Gson().fromJson(data, type);
    }

    public void SaveList() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(BOOK_LIST, Serialize(mBookList));
        editor.apply();
    }

    public void SaveList(Intent intent, ArrayList arrayList) {
        intent.putExtra(BOOK_LIST, Serialize(arrayList));
    }

    public ArrayList<Book> getList() {
        if (mBookList == null) {
            mBookList = RestoreList(mPreferences, mResources);
        }
        return mBookList;
    }
}
