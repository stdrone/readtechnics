package ru.stdrone.home.readtechnics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookText;
import ru.stdrone.home.readtechnics.views.BookTextView;

public class BookTextActivity extends AppCompatActivity implements View.OnTouchListener {

    BookText bookText;

    BookTextView mBookTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text);

        Book book = (Book) getIntent().getSerializableExtra(Book.EXTRA_BOOK);
        mBookTextView = findViewById(R.id.book_text);
        mBookTextView.setBook(book);

        ScrollView scrollView = findViewById(R.id.scroll_text);

        ToggleButton toggleButton = findViewById(R.id.toggleSpeach);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: change listen status
                mBookTextView.checkWord("");
                buttonView.setChecked(true);
                Snackbar.make(buttonView, "Replace with your own action " + isChecked, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        toggleButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mBookTextView.reset();
                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
