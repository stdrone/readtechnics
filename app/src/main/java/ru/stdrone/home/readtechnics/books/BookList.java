package ru.stdrone.home.readtechnics.books;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookList {

    private static final String BOOK_LIST = "BOOK_LIST";
    private static final String BOOKS_LIST_ASSETS_PATH = "books";

    private ArrayList<Book> mBookList;
    private SharedPreferences mPreferences;
    private AssetManager mAssetManager;
    private Bundle mBundle;

    public BookList(AssetManager assets, Bundle intent, SharedPreferences preferences) {
        mAssetManager = assets;
        mPreferences = preferences;
        mBundle = intent;
    }

    public void SaveList() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(BOOK_LIST, Serialize(mBookList));
        editor.apply();
    }

    private static ArrayList<Book> RestoreList(AssetManager assetManager, Bundle intent, SharedPreferences preferences) {
        if (intent != null && intent.containsKey(BOOK_LIST)) {
            return Deserialize(intent.getString(BOOK_LIST));
        } else if (preferences.contains(BOOK_LIST)) {
            return Deserialize(preferences.getString(BOOK_LIST, ""));
        } else {
            return InitList(assetManager);
        }
    }

    private static ArrayList<Book> InitList(AssetManager assetManager) {
        String[] list = new String[0];
        ArrayList<Book> arrayList = new ArrayList<>();
        try {
            list = assetManager.list(BOOKS_LIST_ASSETS_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String item : list) {
            try {
                arrayList.add(new Book(assetManager, BOOKS_LIST_ASSETS_PATH + "/" + item));
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

    public void SaveList(Intent intent, ArrayList arrayList) {
        intent.putExtra(BOOK_LIST, Serialize(arrayList));
    }

    public ArrayList<Book> getList() {
        if (mBookList == null) {
            mBookList = RestoreList(mAssetManager, mBundle, mPreferences);
        }
        return mBookList;
    }
}
