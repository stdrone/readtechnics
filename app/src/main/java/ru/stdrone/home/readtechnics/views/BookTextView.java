package ru.stdrone.home.readtechnics.views;

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

import ru.stdrone.home.readtechnics.books.Book;
import ru.stdrone.home.readtechnics.books.BookText;

public class BookTextView extends android.support.v7.widget.AppCompatTextView {

    private static final String FORMAT_TEXT = "<font color=\"%s\">%s</font>";
    private long ANIMATION_DURATION = 500;
    private String mCorrectSentenceColor = "#15e4be";
    private String mCurrentSentenceColor = "#00FF00";
    private String mCurrentWordColor = "#afde2d";
    private String mLastTextColor = "#000000";

    private BookText mBookText;
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

    public void reset() {
        if (mBookText != null) {
            mBookText.reset();
            setText();
            mBookText.Store(this.getContext());
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mBookText != null) {
            Layout layout = getLayout();
            if (layout != null) {
                int lineHeight = layout.getLineTop(2) - layout.getLineTop(1);
                int viewHeight = ((View) this.getParent()).getHeight();
                int lineCount = viewHeight / lineHeight;
                int position = mBookText.getPosition();
                int line = layout.getLineForOffset(position) - lineCount / 2;
                ScrollTo(line);
            }
        }
    }

    private void setText() {
        if (mBookText != null) {
            String text = String.format(FORMAT_TEXT, mCorrectSentenceColor, mBookText.getCorrectSentences())
                    .concat(String.format(FORMAT_TEXT, mCurrentSentenceColor, mBookText.getCurrentSentence()))
                    .concat(String.format(FORMAT_TEXT, mCurrentWordColor, mBookText.getCurrentWord()))
                    .concat(String.format(FORMAT_TEXT, mLastTextColor, mBookText.getLastText()))
                    .replace("\n", "<br>");

            setText(Html.fromHtml(text));
        } else {
            setText("");
        }
    }

    public void setBookText(BookText bookText) {
        mBookText = bookText;
        setText();
    }

    public void setBook(Book book) {
        try {
            setBookText(new BookText(this.getContext(), book));
        } catch (IOException e) {
            setText(e.getMessage());
            e.printStackTrace();
        }
    }

    public void checkWord(String word) {
        if (mBookText != null) {
            mBookText.checkWord(word);
            mBookText.Store(this.getContext());
            setText();
        }
    }

    public void ScrollTo(final int LineNumber) {

        post(new Runnable() {
            @Override
            public void run() {
                int line = Math.min(getLayout().getLineCount(), Math.max(0, LineNumber));

                int y = getLayout().getLineTop(line);
                int diff = y - getLayout().getLineTop(mCurrentLine);

                if (mCurrentLine != line) {
                    ((ScrollView) getParent()).smoothScrollTo(0, y);
                }

                mCurrentLine = line;
            }
        });
    }
}
