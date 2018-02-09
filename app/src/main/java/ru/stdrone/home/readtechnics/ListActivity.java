package ru.stdrone.home.readtechnics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookList;

public class ListActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_BOOK = 1;
    private static final String LIST_REMOVE_POSITION = "LIST_REMOVE_POSITION";

    BookList mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBookList = new BookList(getAssets(), savedInstanceState, getPreferences(MODE_PRIVATE));

        ListView lvBooks = findViewById(R.id.list_of_books);
        ArrayAdapter<Book> adapter;
        adapter = new ArrayAdapter<>(this, R.layout.book_list_item, mBookList.getList());
        lvBooks.setAdapter(adapter);
        registerForContextMenu(lvBooks);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
                Book book = (Book) adapter.getItem(position);
                if (book != null) {
                    // TODO: implement
                    Snackbar.make(view, "Clicked " + book.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(ListActivity.this, AddBookActivity.class);
                startActivityForResult(addIntent, REQUEST_ADD_BOOK);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_BOOK:
                if (resultCode == RESULT_OK) {
                    mBookList.getList().add((Book) data.getSerializableExtra(Book.EXTRA_BOOK));
                    mBookList.SaveList();
                    ListView lvBooks = findViewById(R.id.list_of_books);
                    lvBooks.invalidateViews();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.list_of_books:
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                    final Book book = mBookList.getList().get(info.position);
                    final ListView listView = (ListView) v;
                    menu.setHeaderTitle(book.getName());
                    for (String item : getResources().getStringArray(R.array.list_context_menu)) {
                        MenuItem menuItem = menu.add(item);
                        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                mBookList.getList().remove(book);
                                mBookList.SaveList();
                                listView.invalidateViews();
                                return true;
                            }
                        });
                    }
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
