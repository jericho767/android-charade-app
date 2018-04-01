package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import data.Word;
import sprobe.training.miniproject.R;

public class RoundWordsAdapter {
    private LayoutInflater mInflater;
    private CheckedWordsAdapter mCheckedWordsAdapter;
    private UncheckedWordsAdapter mUncheckedWordsAdapter;
    private Context mContext;
    private Word mLastWord;

    public CheckedWordsAdapter getCheckedWordsAdapter() {
        return mCheckedWordsAdapter;
    }

    public UncheckedWordsAdapter getUncheckedWordsAdapter() {
        return mUncheckedWordsAdapter;
    }

    private class UncheckedWordsAdapter extends BaseAdapter {
        private ArrayList<Word> mWords;

        private UncheckedWordsAdapter(ArrayList<Word> words) {
            mWords = words;
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
            return mWords.get(position).getId();
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

            if (position == 0 && mLastWord.getId() == word.getId()) {
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

        private CheckedWordsAdapter(ArrayList<Word> words) {
            mWords = words;
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
            return mWords.get(position).getId();
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

            if (position == 0 && mLastWord.getId() == word.getId()) {
                viewWordHolder.mViewSubtext.setText(String.format("%s | %s"
                        , mContext.getResources().getString(R.string.round_subtext_checked)
                        , mContext.getResources().getString(R.string.round_subtext_last_word)));
            } else {
                viewWordHolder.mViewSubtext.setText(mContext.getResources()
                        .getString(R.string.round_subtext_checked));
            }

            return convertView;
        }
    }

    public RoundWordsAdapter(Context context, ArrayList<Word> uncheckedWords,
                             ArrayList<Word> checkedWords, Word lastWord) {
        mCheckedWordsAdapter = new CheckedWordsAdapter(checkedWords);
        mUncheckedWordsAdapter = new UncheckedWordsAdapter(uncheckedWords);
        mLastWord = lastWord;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewWordHolder {
        private TextView mViewText;
        private TextView mViewSubtext;
    }

}
