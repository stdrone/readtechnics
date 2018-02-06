package ru.stdrone.home.readtechnics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookList;

public class ListActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_BOOK = 1;

    ArrayList<Book> mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBookList = BookList.RestoreList(getAssets(),savedInstanceState,getPreferences(MODE_PRIVATE));

        ListView lvBooks = findViewById(R.id.list_of_books);
        ArrayAdapter<Book> adapter = new ArrayAdapter(this, R.layout.book_list_item, mBookList);
        lvBooks.setAdapter(adapter);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter) parent.getAdapter();
                Book book = (Book) adapter.getItem(position);
                // TODO: implement
                Snackbar.make(view, "Clicked " + book.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                if(resultCode == RESULT_OK) {
                    mBookList.add(new Book(data));
                    BookList.SaveList(getPreferences(MODE_PRIVATE), mBookList);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }


}
