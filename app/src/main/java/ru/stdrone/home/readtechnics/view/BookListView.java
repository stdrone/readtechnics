package ru.stdrone.home.readtechnics.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import ru.stdrone.home.readtechnics.model.Book;

public class BookListView extends ListView implements SearchView.OnQueryTextListener {
    public BookListView(Context context) {
        super(context);
    }

    public BookListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArrayAdapter<Book> getAdapter() {
        return ((ArrayAdapter<Book>) super.getAdapter());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getAdapter().getFilter().filter(newText);
        return true;
    }
}
