package ru.stdrone.home.readtechnics.books;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class BookText {
    // TODO
    private Book mBook;
    private InputStream mStream;

    BookText(Book book, Context context) throws IOException {
        mBook = book;
        mStream = book.getStream(context);
    }
}
