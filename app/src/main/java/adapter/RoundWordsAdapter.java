package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private Context mContext;
    private CheckedWordsAdapter mCheckedWordsAdapter;
    private UncheckedWordsAdapter mUncheckedWordsAdapter;
    private static final String SUBTEXT_CHECKED = "CHECKED";
    private static final String SUBTEXT_PASSED = "PASSED";

    private class UncheckedWordsAdapter extends BaseAdapter {
        private List<Word> mWords;

        public UncheckedWordsAdapter(List<Word> mWords) {
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
            viewWordHolder.mViewSubtext.setText(SUBTEXT_PASSED);
            return null;
        }
    }

    private class CheckedWordsAdapter extends BaseAdapter {
        private List<Word> mWords;

        public CheckedWordsAdapter(List<Word> mWords) {
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
            return null;
        }
    }

    public RoundWordsAdapter(Context context, List<Word> uncheckedWords, List<Word> checkedWords) {
        this.mContext = context;
        this.mCheckedWordsAdapter = new CheckedWordsAdapter(checkedWords);
        this.mUncheckedWordsAdapter = new UncheckedWordsAdapter(uncheckedWords);

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewWordHolder {
        private TextView mViewText;
        private TextView mViewSubtext;
    }

}
