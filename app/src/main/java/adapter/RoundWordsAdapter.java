package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import common.Util;
import data.Word;
import sprobe.training.miniproject.R;

public class RoundWordsAdapter {
    private LayoutInflater mInflater;
    private CheckedWordsAdapter mCheckedWordsAdapter;
    private UncheckedWordsAdapter mUncheckedWordsAdapter;
    private String mSubtextChecked;
    private String mSubtextPassed;
    private String mSubtextLastWord;

    public CheckedWordsAdapter getCheckedWordsAdapter() {
        return mCheckedWordsAdapter;
    }

    public UncheckedWordsAdapter getUncheckedWordsAdapter() {
        return mUncheckedWordsAdapter;
    }

    private class UncheckedWordsAdapter extends BaseAdapter {
        private List<Word> mWords;
        private int mIndexOfLastWord;

        private UncheckedWordsAdapter(List<Word> mWords, int indexOfLastWord) {
            this.mWords = mWords;
            this.mIndexOfLastWord = indexOfLastWord;
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

            if (position == mIndexOfLastWord) {
                viewWordHolder.mViewSubtext.setText(mSubtextLastWord);
            } else {
                viewWordHolder.mViewSubtext.setText(mSubtextPassed);
            }

            return convertView;
        }
    }

    private class CheckedWordsAdapter extends BaseAdapter {
        private List<Word> mWords;
        private int mIndexOfLastWord;

        private CheckedWordsAdapter(List<Word> mWords, int indexOfLastWord) {
            this.mWords = mWords;
            this.mIndexOfLastWord = indexOfLastWord;
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
                viewWordHolder.mViewSubtext.setText(String.format(Util.getLocale(),
                        "%s | %s", mSubtextChecked, mSubtextLastWord));
            } else {
                viewWordHolder.mViewSubtext.setText(mSubtextChecked);
            }

            return convertView;
        }
    }

    public RoundWordsAdapter(Context context, List<Word> uncheckedWords,
                             List<Word> checkedWords, int lastWordIndex,
                             String subtextChecked, String subtextPassed,
                             String mSubtextLastWord) {
        this.mCheckedWordsAdapter = new CheckedWordsAdapter(checkedWords, lastWordIndex);
        this.mUncheckedWordsAdapter = new UncheckedWordsAdapter(uncheckedWords, lastWordIndex);

        this.mSubtextChecked = subtextChecked;
        this.mSubtextPassed = subtextPassed;
        this.mSubtextLastWord = mSubtextLastWord;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewWordHolder {
        private TextView mViewText;
        private TextView mViewSubtext;
    }

}