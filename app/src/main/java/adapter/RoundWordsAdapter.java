package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import data.Word;
import sprobe.training.miniproject.R;

public class RoundWordsAdapter {
    private LayoutInflater mInflater;
    private CheckedWordsAdapter mCheckedWordsAdapter;
    private UncheckedWordsAdapter mUncheckedWordsAdapter;
    private static final String SUBTEXT_CHECKED = "CHECKED"; // TODO: Move to strings.xml
    private static final String SUBTEXT_PASSED = "PASSED"; // TODO: Move to strings.xml
    private static final String SUBTEXT_LAST_WORD = "LAST WORD"; // TODO: Move to strings.xml

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
            // TODO: Remove log
            Log.wtf("ADAPTER | INDEX OF THE LAST WORD: ", mIndexOfLastWord + "");
            if (position == mIndexOfLastWord && !word.isChecked()) {
                viewWordHolder.mViewSubtext.setText(SUBTEXT_LAST_WORD);
            } else {
                viewWordHolder.mViewSubtext.setText(SUBTEXT_PASSED);
            }
            return convertView;
        }
    }

    private class CheckedWordsAdapter extends BaseAdapter {
        private List<Word> mWords;

        private CheckedWordsAdapter(List<Word> mWords) {
            this.mWords = mWords;
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
            viewWordHolder.mViewSubtext.setText(SUBTEXT_CHECKED);
            return convertView;
        }
    }

    public RoundWordsAdapter(Context context, List<Word> uncheckedWords,
                             List<Word> checkedWords, int lastWordIndex) {
        this.mCheckedWordsAdapter = new CheckedWordsAdapter(checkedWords);
        this.mUncheckedWordsAdapter = new UncheckedWordsAdapter(uncheckedWords, lastWordIndex);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewWordHolder {
        private TextView mViewText;
        private TextView mViewSubtext;
    }

}
