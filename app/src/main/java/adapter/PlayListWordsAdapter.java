package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import data.Word;
import sprobe.training.miniproject.R;

public class PlayListWordsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Word> mWords;

    public PlayListWordsAdapter(LayoutInflater mInflater, List<Word> mWords) {
        this.mInflater = mInflater;
        this.mWords = mWords;
    }

    @Override
    public int getCount() {
        return mWords.size();
    }

    @Override
    public Word getItem(int i) {
        return mWords.get(i);
    }

    @Override
    public long getItemId(int i) {
        // TODO: After the database thingy modify this
        return i;
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

        Word word = mWords.get(i);
        viewWordHolder.mViewText.setText(word.getText());

        return view;
    }

    private class ViewWordHolder {
        private TextView mViewText;
    }
}
