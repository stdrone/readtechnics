package ru.stdrone.home.readtechnics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.io.IOException;

import ru.stdrone.home.readtechnics.model.Book;
import ru.stdrone.home.readtechnics.model.BookReader;
import ru.stdrone.home.readtechnics.model.BookText;
import ru.stdrone.home.readtechnics.model.Settings;
import ru.stdrone.home.readtechnics.model.StatisticStorage;
import ru.stdrone.home.readtechnics.service.ReadingRules;
import ru.stdrone.home.readtechnics.service.SpeechListener;
import ru.stdrone.home.readtechnics.service.StatisticCollector;
import ru.stdrone.home.readtechnics.view.BookTextView;

public class BookReadingActivity extends AppCompatActivity implements View.OnTouchListener, SpeechListener.EventObserver {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static final String STATISTIC = "STATISTIC";


    BookText bookText;

    BookTextView mBookTextView;
    SpeechListener mListner;
    ToggleButton mToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text);

        Book book = (Book) getIntent().getSerializableExtra(Book.EXTRA_BOOK);
        mBookTextView = findViewById(R.id.book_text);

        try {
            int position = getPreferences(MODE_PRIVATE).getInt(book.getPath(), 0);
            Settings settings = new Settings(this);
            StatisticCollector collector = new StatisticCollector(StatisticStorage.Deserialize(getPreferences(Context.MODE_PRIVATE).getString(STATISTIC, null)));
            mBookTextView.setBookReader(new BookReader(position, collector, new ReadingRules(settings)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollView scrollView = findViewById(R.id.scroll_text);
        scrollView.setOnTouchListener(this);

        mListner = new SpeechListener(this);
        mListner.setActionObserver(this);
        mListner.setListenObserver(mBookTextView);

        mToggleButton = findViewById(R.id.toggleSpeach);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListner.reset(isChecked);
            }
        });

        checkPermissions();
    }

    private void checkPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RECORD_AUDIO:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    mToggleButton.setEnabled(false);
                }
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void OnListenEvent() {
        mToggleButton.setChecked(mListner.isListening());
        mToggleButton.invalidate();
    }

    @Override
    protected void onPause() {
        mListner.reset(false);

        final SharedPreferences.Editor edit = getPreferences(MODE_PRIVATE).edit();

        edit.putString(STATISTIC, mBookTextView.getBookReader().getStatistic().getTotal().Serialize());

        Book book = (Book) getIntent().getSerializableExtra(Book.EXTRA_BOOK);
        edit.putInt(book.getPath(), mBookTextView.getBookReader().getPosition());

        edit.apply();

        super.onPause();
    }

}
