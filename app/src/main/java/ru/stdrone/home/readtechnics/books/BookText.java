package ru.stdrone.home.readtechnics.books;

import java.io.IOException;
import java.io.InputStream;

public class BookText {
    // TODO
    private Book mBook;
    private InputStream mStream;

    BookText(Book book) throws IOException {
        mBook = book;
        mStream = book.getStream();
    }
}
