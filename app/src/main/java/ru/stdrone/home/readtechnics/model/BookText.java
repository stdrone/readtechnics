package ru.stdrone.home.readtechnics.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BookText implements Closeable {
    static final char TERMINATOR = '\0';
    private static final int BUFFER = 2000;
    private InputStream mStream;
    private StringBuffer mTextBuffer;
    private BufferedReader mReader;
    private int mBufferStart;
    private int mBufferLength;

    BookText(InputStream stream, int position) throws IOException {
        if (mTextBuffer == null) {
            mReader = getReader(mStream = stream);

            mTextBuffer = new StringBuffer();

            mBufferStart = Math.max(position - BUFFER, 0);

            if (mReader.skip(mBufferStart) >= 0) {
                readText(BUFFER * 2);
            }
        }
    }

    int getBufferPosition(int position) {
        return Math.max(0, Math.min(mBufferLength, position - mBufferStart));
    }

    String getBuffer(int start, int end) throws IOException {
        checkBuffer(start);
        end = Math.max(start, end);
        return mTextBuffer.substring(getBufferPosition(start), getBufferPosition(end));
    }

    String getBuffer(int start) throws IOException {
        checkBuffer(start);
        return mTextBuffer.substring(getBufferPosition(start));
    }

    char charAt(int position) throws IOException {
        checkBuffer(position);
        position -= mBufferStart;
        if (position < 0 || position >= mBufferLength)
            return TERMINATOR;
        return mTextBuffer.charAt(position);
    }

    private void checkBuffer(int position) throws IOException {
        int halfBuffer = BUFFER / 2;
        if (getBufferPosition(position) > BUFFER + halfBuffer) {
            mTextBuffer.delete(0, halfBuffer);
            readText(halfBuffer);
            mBufferStart += halfBuffer;
        }
    }

    @Override
    public void close() throws IOException {
        mReader.close();
    }

    private void readText(int len) throws IOException {
        char[] buffer = new char[len];
        if ((mBufferLength = mReader.read(buffer, 0, len)) > 0) {
            mTextBuffer.append(String.valueOf(buffer).replace("\0", ""));
            mBufferLength = mTextBuffer.length();
        }
    }

    private BufferedReader getReader(InputStream stream) {
        InputStreamReader isReader = new InputStreamReader(stream);
        return new BufferedReader(isReader);
    }

    public void reset(Context context) {

    }
}
