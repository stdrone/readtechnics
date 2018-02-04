package ru.stdrone.home.readtechnics;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookListAdapter;

public class ListActivity extends AppCompatActivity {

    BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lvBooks = findViewById(R.id.list_of_books);

        adapter = new BookListAdapter(this, R.layout.book_list_item);
        lvBooks.setAdapter(adapter);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: implement
                BookListAdapter adapter = (BookListAdapter) parent.getAdapter();
                Book book = (Book) adapter.getItem(position);
                Snackbar.make(view, "Clicked " + book.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implement
                adapter.SaveList();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
