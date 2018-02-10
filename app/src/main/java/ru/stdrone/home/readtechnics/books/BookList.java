package ru.stdrone.home.readtechnics.books;

import android.content.SharedPreferences;
import android.database.DataSetObserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookList extends DataSetObserver {

    private static final String BOOK_LIST = "BOOK_LIST";
    private static final String BOOKS_LIST_ASSETS_PATH = "books";

    private ArrayList<Book> mBookList;
    private SharedPreferences mPreferences;

    public BookList(SharedPreferences preferences) {
        mPreferences = preferences;
        if (preferences.contains(BOOK_LIST)) {
            mBookList = Deserialize(preferences.getString(BOOK_LIST, ""));
        } else {
            mBookList = new ArrayList<>();
        }
    }

    private static String Serialize(ArrayList arrayList) {
        return new Gson().toJson(arrayList);
    }

    private static ArrayList<Book> Deserialize(String data) {
        Type type = new TypeToken<List<Book>>() {
        }.getType();

        return new Gson().fromJson(data, type);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        sort();
        saveList();
    }

    private void sort() {
        Collections.sort(mBookList, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getmName().compareTo(o2.getmName());
            }
        });
    }

    private void saveList() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(BOOK_LIST, Serialize(mBookList));
        editor.apply();
    }

    public ArrayList<Book> getList() {
        return mBookList;
    }
}
