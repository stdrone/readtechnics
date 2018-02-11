package ru.stdrone.home.readtechnics.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import ru.stdrone.home.readtechnics.R;
import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookList;

import static android.content.Context.MODE_PRIVATE;

public class BookListView extends ListView implements SearchView.OnQueryTextListener {
    private static final String BOOK_LIST = "BOOK_LIST";

    public BookListView(Context context) {
        super(context);
        onCreate(context);
    }

    public BookListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate(context);
    }

    public BookListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate(context);
    }

    private void onCreate(Context context) {
        BookList bookList = new BookList(context.getSharedPreferences(BOOK_LIST, MODE_PRIVATE));

        ArrayAdapter<Book> adapter;
        adapter = new ArrayAdapter<>(context, R.layout.list_item, bookList.getList());
        adapter.registerDataSetObserver(bookList);

        setAdapter(adapter);
    }

    public void onCreateContextMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Book book = getAdapter().getItem(info.position);
        if (book != null) {
            menu.setHeaderTitle(book.getmName());

            MenuItem menuItem = menu.add(R.string.list_context_menu_delete);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    getAdapter().remove(book);
                    invalidateViews();
                    return true;
                }
            });
        }
        super.onCreateContextMenu(menu);
    }

    @SuppressWarnings("unchecked")
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
