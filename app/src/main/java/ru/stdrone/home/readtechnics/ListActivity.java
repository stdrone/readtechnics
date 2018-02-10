package ru.stdrone.home.readtechnics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.views.BookView;

public class ListActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_BOOK = 1;

    BookView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        mList = findViewById(R.id.list_of_books);
        registerForContextMenu(mList);

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
}
