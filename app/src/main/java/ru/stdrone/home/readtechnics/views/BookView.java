package ru.stdrone.home.readtechnics.views;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.stdrone.home.readtechnics.R;
import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookList;

import static android.content.Context.MODE_PRIVATE;

public class BookView extends ListView implements AdapterView.OnItemClickListener {
    private static final String BOOK_LIST = "BOOK_LIST";

    public BookView(Context context) {
        super(context);
        onCreate(context);
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate(context);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate(context);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreate(context);
    }

    private void onCreate(Context context) {
        BookList bookList = BookList.Restore(context.getSharedPreferences(BOOK_LIST, MODE_PRIVATE));

        ArrayAdapter<Book> adapter;
        adapter = new ArrayAdapter<>(context, R.layout.book_list_item, bookList.getList());
        adapter.registerDataSetObserver(bookList);

        setAdapter(adapter);

        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter adapter = getAdapter();
        Book book = (Book) adapter.getItem(position);
        if (book != null) {
            // TODO: implement
            Snackbar.make(view, "Clicked " + book.getName(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void onCreateContextMenu(ContextMenu menu, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Book book = getAdapter().getItem(info.position);
        if (book != null) {
            menu.setHeaderTitle(book.getName());

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
}
