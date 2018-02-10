package ru.stdrone.home.readtechnics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookText;

public class BookTextActivity extends AppCompatActivity {

    BookText bookText;
    TextView mTextView;
    ScrollView mScroll;
    int mLine = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text);

        mTextView = findViewById(R.id.book_text);
        mScroll = findViewById(R.id.scroll_text);

        try {
            bookText = new BookText(this, (Book) getIntent().getSerializableExtra(Book.EXTRA_BOOK));
            mTextView.setText(bookText.readText());
        } catch (java.io.IOException e) {
            mTextView.setText(e.getMessage());
            e.printStackTrace();
        }

        mScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        ScrollTo(mLine);

        ToggleButton toggleButton = findViewById(R.id.toggleSpeach);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(true);
                Snackbar.make(buttonView, "Replace with your own action " + isChecked, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mLine++;
                ScrollTo(mLine);
            }
        });
        toggleButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLine = 0;
                ScrollTo(mLine);
                return true;
            }
        });
    }

    void ScrollTo(final int LineNumber) {
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                int line = Math.max(0, LineNumber);
                line = Math.min(line, mTextView.getLayout().getLineCount());

                int y = mTextView.getLayout().getLineTop(line);
                int diff = y - mTextView.getScrollY();

                mTextView.scrollTo(0, y);
                TranslateAnimation slide = new TranslateAnimation(0, 0, diff, 0);
                slide.setDuration(500);
                slide.setInterpolator(new LinearInterpolator());
                mTextView.startAnimation(slide);
            }
        });
    }

}
