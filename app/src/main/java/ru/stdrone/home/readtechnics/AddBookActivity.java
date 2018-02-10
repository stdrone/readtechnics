package ru.stdrone.home.readtechnics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.stdrone.home.readtechnics.books.Book;

public class AddBookActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 1;
    Uri mUri;
    EditText mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mNameView = findViewById(R.id.editName);

        Button button = findViewById(R.id.editButton);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = mNameView.getText().toString();
                if (!name.equals("") && mUri != null) {
                    Book book = new Book(name, mUri.toString());
                    Intent intent = new Intent();
                    intent.putExtra(Book.EXTRA_BOOK, book);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        });

        EditText editUri = findViewById(R.id.editUri);
        editUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case READ_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    mUri = data.getData();
                    EditText editUri = findViewById(R.id.editUri);
                    editUri.setText(mUri.getLastPathSegment());
                    if (mNameView.getText().toString().equals("")) {
                        try {
                            InputStream stream = getContentResolver().openInputStream(mUri);
                            if (stream != null) {
                                InputStreamReader isReader = new InputStreamReader(stream);
                                BufferedReader reader = new BufferedReader(isReader);
                                mNameView.setText(reader.readLine());
                                reader.close();
                                stream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
