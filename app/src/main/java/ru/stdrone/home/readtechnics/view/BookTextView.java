package ru.stdrone.home.readtechnics.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.IOException;

import ru.stdrone.home.readtechnics.model.Book;
import ru.stdrone.home.readtechnics.model.BookReader;

public class BookTextView extends android.support.v7.widget.AppCompatTextView {

    private static final String FORMAT_TEXT = "<font color=\"%s\">%s</font>";
    private String mCorrectSentenceColor = "#15e4be";
    private String mCurrentSentenceColor = "#00FF00";
    private String mCurrentWordColor = "#afde2d";
    private String mLastTextColor = "#000000";

    private BookReader mBookReader;
    private int mCurrentLine = 0;

    public BookTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        create(context, attrs);
    }

    private void create(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            // TODO: get color attrs
        }

        setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        // TODO: setScrollBar

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        setLayoutParams(params);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        ScrollToCurrent();
    }

    private void setCurrentText() throws IOException {
        if (mBookReader != null) {
            String text = String.format(FORMAT_TEXT, mCorrectSentenceColor, mBookReader.getCorrectSentences())
                    .concat(String.format(FORMAT_TEXT, mCurrentSentenceColor, mBookReader.getCurrentSentence()))
                    .concat(String.format(FORMAT_TEXT, mCurrentWordColor, mBookReader.getCurrentWord()))
                    .concat(String.format(FORMAT_TEXT, mLastTextColor, mBookReader.getLastText()))
                    .replace("\n", "<br>");

            setText(Html.fromHtml(text));
        } else {
            setText("");
        }
    }

    public void setBookText(BookReader bookReader) throws IOException {
        mBookReader = bookReader;
        setCurrentText();
    }

    public void setBook(Book book) {
        try {
            Context context = this.getContext();
            BookReader text = new BookReader(context, book);
            text.init(context);
            setBookText(text);
        } catch (IOException e) {
            setText(e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkWord(String word) throws IOException {
        if (mBookReader != null) {
            mBookReader.checkWord(word);
            setCurrentText();
        }
    }

    public void ScrollToCurrent() {

        post(new Runnable() {
            @Override
            public void run() {
                if (mBookReader != null) {
                    Layout layout = getLayout();
                    if (layout != null) {
                        int lineHeight = layout.getLineTop(2) - layout.getLineTop(1);
                        int viewHeight = ((View) getParent()).getHeight();
                        int lineCount = viewHeight / lineHeight;
                        int position = mBookReader.getPosition();
                        int line = layout.getLineForOffset(position) - lineCount / 2;
                        line = Math.min(getLayout().getLineCount(), Math.max(0, line));

                        int y = getLayout().getLineTop(line);

                        if (mCurrentLine != line) {
                            ((ScrollView) getParent()).smoothScrollTo(0, y);
                            mCurrentLine = line;
                        }
                    }
                }
            }
        });
    }

    public void setStatisticCollector() {
    }
}
