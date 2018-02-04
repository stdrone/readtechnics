package ru.stdrone.home.readtechnics.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private BookList mBookList;
    private int mResourceTextViewID;
    private Context mContext;

    public BookListAdapter(Context context, int resourceTextViewID) {
        mBookList = new BookList(context.getAssets(), context.getSharedPreferences(BookList.BOOKS_PREFERENES, Context.MODE_PRIVATE));
        mResourceTextViewID = resourceTextViewID;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(mResourceTextViewID, parent, false);
        }

        TextView text = (TextView) view;
        text.setText(mBookList.get(position).getName());

        return view;
    }

    public void SaveList() {
        mBookList.SavePreferences(mContext.getSharedPreferences(BookList.BOOKS_PREFERENES, Context.MODE_PRIVATE));
    }
}
