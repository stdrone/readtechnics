package ru.stdrone.home.readtechnics;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.stdrone.home.readtechnics.model.Book;
import ru.stdrone.home.readtechnics.view.BookListView;

public class ListActivity extends AppCompatActivity {

    private static final String BOOK_LIST = "BOOK_LIST";
    private static final int REQUEST_ADD_BOOK = 1;
    ArrayList<Book> mList;
    BookListView mBookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        mBookListView = findViewById(R.id.list_of_books);

        mList = ListSerialization.Deserialize(getPreferences(MODE_PRIVATE).getString(BOOK_LIST, null));
        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, R.layout.list_item, mList);
        adapter.registerDataSetObserver(new ListDatasetObserver(mList));
        mBookListView.setAdapter(adapter);

        mBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = ((BookListView) view).getAdapter().getItem(position);
                if (book != null) {
                    Intent intent = new Intent(ListActivity.this, BookReadingActivity.class);
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
                    mBookListView.getAdapter().add((Book) data.getSerializableExtra(Book.EXTRA_BOOK));
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
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
            searchView.setOnQueryTextListener(mBookListView);
            return true;
        }
        return false;
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mBookListView.getAdapter().getFilter().filter(query);
        }
    }

    @Override
    protected void onPause() {
        final SharedPreferences.Editor edit = getPreferences(MODE_PRIVATE).edit();
        edit.putString(BOOK_LIST, ListSerialization.Serialize(mList));
        edit.apply();
        super.onPause();
    }

    private static class ListSerialization {
        static String Serialize(ArrayList arrayList) {
            return new Gson().toJson(arrayList);
        }

        static ArrayList<Book> Deserialize(String data) {
            if (data == null) return new ArrayList<>();
            else {
                Type type = new TypeToken<List<Book>>() {
                }.getType();

                return (ArrayList<Book>) new Gson().fromJson(data, type);
            }
        }

    }

    class ListDatasetObserver extends DataSetObserver {
        ArrayList<Book> mList;

        ListDatasetObserver(ArrayList<Book> list) {
            mList = list;
        }

        private void sort() {
            Collections.sort(mList, new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
    }
}
