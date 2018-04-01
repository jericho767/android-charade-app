package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import database.DBWord;
import sprobe.training.miniproject.R;

public class PlayListWordsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<DBWord> mWords;

    public PlayListWordsAdapter(Context context, ArrayList<DBWord> words) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWords = words;
    }

    public void addItem(DBWord word) {
        mWords.add(word);
        notifyDataSetChanged();
    }

    public ArrayList<DBWord> getWords() {
        return mWords;
    }

    @Override
    public int getCount() {
        return mWords.size();
    }

    @Override
    public DBWord getItem(int i) {
        return mWords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mWords.get(i).getId();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewWordHolder viewWordHolder = new ViewWordHolder();

        if (view == null) {
            view = mInflater.inflate(R.layout.item_playlist_word, null, false);
            viewWordHolder.mViewText = view.findViewById(R.id.item_word_text);
            view.setTag(viewWordHolder);
        } else {
            viewWordHolder = (ViewWordHolder) view.getTag();
        }

        DBWord word = mWords.get(i);
        viewWordHolder.mViewText.setText(word.getText());

        return view;
    }

    private class ViewWordHolder {
        private TextView mViewText;
    }
}
