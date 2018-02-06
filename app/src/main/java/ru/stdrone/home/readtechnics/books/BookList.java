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

public class BookList {

    private static final String BOOK_LIST = "BOOK_LIST";
    private static final String BOOKS_LIST_ASSETS_PATH = "books";

    static public void SaveList(Intent intent, ArrayList arrayList) {
        intent.putExtra(BOOK_LIST, Serialize(arrayList));
    }

    static public void SaveList(SharedPreferences preferences, ArrayList arrayList) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BOOK_LIST, Serialize(arrayList));
        editor.commit();
    }

    static public ArrayList<Book> RestoreList(AssetManager assetManager, Bundle intent, SharedPreferences preferences) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {
        }.getType();
        if (intent.containsKey(BOOK_LIST)) {
            return gson.fromJson(intent.getString(BOOK_LIST), type);
        } else if (preferences.contains(BOOK_LIST)) {
            return gson.fromJson(preferences.getString(BOOK_LIST, ""), type);
        } else {
            return InitList(assetManager);
        }
    }

    static ArrayList<Book> InitList(AssetManager assetManager) {
        String[] list = new String[0];
        ArrayList<Book> arrayList = new ArrayList<Book>();
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
        return null;
    }

    static String Serialize(ArrayList arrayList) {
        Gson gson = new Gson();
        return gson.toJson(arrayList);
    }
}
