package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import common.Util;
import data.Word;
import sprobe.training.miniproject.R;

public class RoundWordsAdapter {
    private LayoutInflater mInflater;
    private CheckedWordsAdapter mCheckedWordsAdapter;
    private UncheckedWordsAdapter mUncheckedWordsAdapter;
    private Context mContext;

    public CheckedWordsAdapter getCheckedWordsAdapter() {
        return mCheckedWordsAdapter;
    }

    public UncheckedWordsAdapter getUncheckedWordsAdapter() {
        return mUncheckedWordsAdapter;
    }

    private class UncheckedWordsAdapter extends BaseAdapter {
        private ArrayList<Word> mWords;
        private int mIndexOfLastWord;

        private UncheckedWordsAdapter(ArrayList<Word> words, int indexOfLastWord) {
            mWords = words;
            mIndexOfLastWord = indexOfLastWord;
        }

        @Override
        public int getCount() {
            return mWords.size();
        }

        @Override
        public Word getItem(int position) {
            return mWords.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewWordHolder viewWordHolder = new ViewWordHolder();

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_word, null, false);

                viewWordHolder.mViewText = convertView.findViewById(R.id.item_word_text);
                viewWordHolder.mViewSubtext = convertView.findViewById(R.id.item_word_subtext);

                convertView.setTag(viewWordHolder);
            } else {
                viewWordHolder = (ViewWordHolder) convertView.getTag();
            }

            Word word = mWords.get(position);
            viewWordHolder.mViewText.setText(word.getText());

            if (position == getCount() - 1) {
                viewWordHolder.mViewSubtext.setText(mContext.getResources()
                        .getString(R.string.round_subtext_last_word));
            } else {
                viewWordHolder.mViewSubtext.setText(mContext.getResources()
                        .getString(R.string.round_subtext_passed));
            }

            return convertView;
        }
    }

    private class CheckedWordsAdapter extends BaseAdapter {
        private ArrayList<Word> mWords;
        private int mIndexOfLastWord;

        private CheckedWordsAdapter(ArrayList<Word> words, int indexOfLastWord) {
            mWords = words;
            mIndexOfLastWord = indexOfLastWord;
        }

        @Override
        public int getCount() {
            return mWords.size();
        }

        @Override
        public Word getItem(int position) {
            return mWords.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewWordHolder viewWordHolder = new ViewWordHolder();

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_word, null, false);

                viewWordHolder.mViewText = convertView.findViewById(R.id.item_word_text);
                viewWordHolder.mViewSubtext = convertView.findViewById(R.id.item_word_subtext);

                convertView.setTag(viewWordHolder);
            } else {
                viewWordHolder = (ViewWordHolder) convertView.getTag();
            }

            viewWordHolder.mViewText.setText(mWords.get(position).getText());

            if (position == mIndexOfLastWord) {
                viewWordHolder.mViewSubtext.setText(String.format(Util.getLocale(), "%s"
                        , mContext.getResources().getString(R.string.round_subtext_checked)));
            } else {
                viewWordHolder.mViewSubtext.setText(mContext.getResources()
                        .getString(R.string.round_subtext_checked));
            }

            return convertView;
        }
    }

    public RoundWordsAdapter(Context context, ArrayList<Word> uncheckedWords,
                             ArrayList<Word> checkedWords, int lastWordIndex) {
        mCheckedWordsAdapter = new CheckedWordsAdapter(checkedWords, lastWordIndex);
        mUncheckedWordsAdapter = new UncheckedWordsAdapter(uncheckedWords, lastWordIndex);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewWordHolder {
        private TextView mViewText;
        private TextView mViewSubtext;
    }

}
