package ru.stdrone.home.readtechnics;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class BookTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_text);

        ToggleButton toggleButton = findViewById(R.id.toggleSpeach);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(true);
                Snackbar.make(buttonView, "Replace with your own action " + isChecked, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
