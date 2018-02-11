package ru.stdrone.home.readtechnics;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.views.BookListView;

public class ListActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_BOOK = 1;

    BookListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.list_of_books);
        registerForContextMenu(mList);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Book> adapter = mList.getAdapter();
                Book book = adapter.getItem(position);
                if (book != null) {
                    Intent intent = new Intent(ListActivity.this, BookTextActivity.class);
                    intent.putExtra(Book.EXTRA_BOOK, book);
                    startActivity(intent);
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

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_BOOK:
                if (resultCode == RESULT_OK) {
                    mList.getAdapter().add((Book) data.getSerializableExtra(Book.EXTRA_BOOK));
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
                    mList.onCreateContextMenu(menu, menuInfo);
            }
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(mList);
            return true;
        }
        return false;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mList.getAdapter().getFilter().filter(query);
        }
    }
}
