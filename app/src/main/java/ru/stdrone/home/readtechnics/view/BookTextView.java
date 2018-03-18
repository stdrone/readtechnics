package ru.stdrone.home.readtechnics.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.IOException;

import ru.stdrone.home.readtechnics.model.BookReader;
import ru.stdrone.home.readtechnics.service.SpeechListener;

public class BookTextView extends AppCompatTextView implements SpeechListener.ListenObserver {

    private static final String FORMAT_TEXT = "<font color=\"%s\">%s</font>";
    private String mCorrectSentenceColor = "#15e4be";
    private String mCurrentSentenceColor = "#00FF00";
    private String mCurrentWordColor = "#afde2d";
    private String mLastTextColor = "#000000";

    private BookReader mBookReader;
    private ScrollRunnable mScroller;

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

        mScroller = new ScrollRunnable(getLayout(), (View) this.getParent());
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

    public BookReader getBookReader() {
        return mBookReader;
    }

    public void setBookReader(BookReader bookReader) throws IOException {
        mBookReader = bookReader;
        setCurrentText();
    }

    public void ScrollToCurrent() {
        mScroller.setNewPosition(mBookReader.getPositionInView());
        post(mScroller);
    }

    @Override
    public void OnListenWord(String word) {
        if (mBookReader != null) {
            try {
                mBookReader.checkWord(word);
                setCurrentText();
            } catch (IOException e) {
                e.printStackTrace();
                setText(e.getMessage());
            }
        }
    }

    class ScrollRunnable implements Runnable {
        int mNewPosition, mCurrentPosition;

        Layout mLayout;
        View mParent;

        ScrollRunnable(Layout layout, View parent) {
            mNewPosition = 0;
            mCurrentPosition = 0;
            mParent = parent;
            mLayout = layout;
        }

        void setNewPosition(int newPosition) {
            mNewPosition = newPosition;
        }

        @Override
        public void run() {
            Layout layout = getLayout();
            if (layout != null) {
                int lineHeight = layout.getLineTop(2) - layout.getLineTop(1);
                int viewHeight = mParent.getHeight();
                int lineCount = viewHeight / lineHeight;
                int newPosition = layout.getLineForOffset(mNewPosition) - lineCount / 2;
                newPosition = Math.min(getLayout().getLineCount(), Math.max(0, newPosition));

                int y = getLayout().getLineTop(newPosition);

                if (mCurrentPosition != newPosition) {
                    ((ScrollView) getParent()).smoothScrollTo(0, y);
                    mCurrentPosition = newPosition;
                }
            }
        }
    }
}
