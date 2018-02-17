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

import java.io.IOException;

import ru.stdrone.home.readtechnics.book.Book;
import ru.stdrone.home.readtechnics.booktext.BookText;
import ru.stdrone.home.readtechnics.booktext.BookTextView;
import ru.stdrone.home.readtechnics.statistics.StatisticCollector;

public class BookReadingActivity extends AppCompatActivity implements View.OnTouchListener {

    BookText bookText;

    BookTextView mBookTextView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text);

        StatisticCollector.getInstance().init(this);

        Book book = (Book) getIntent().getSerializableExtra(Book.EXTRA_BOOK);
        mBookTextView = findViewById(R.id.book_text);
        mBookTextView.setBook(book);

        ScrollView scrollView = findViewById(R.id.scroll_text);
        scrollView.setOnTouchListener(this);

        ToggleButton toggleButton = findViewById(R.id.toggleSpeach);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: change listen status
                try {
                    mBookTextView.checkWord("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buttonView.setChecked(true);
                Snackbar.make(buttonView, "Replace with your own action " + isChecked, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
