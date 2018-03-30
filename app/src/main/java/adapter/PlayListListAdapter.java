package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import database.DBPlayList;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class PlayListListAdapter extends BaseAdapter {
    private ArrayList<DBPlayList> mPlayLists;
    private LayoutInflater mInflater;
    private Context mContext;
    private DatabaseHelper db;

    public PlayListListAdapter(Context context, ArrayList<DBPlayList> playLists) {
        mContext = context;
        db = new DatabaseHelper(mContext);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPlayLists = playLists;
    }

    @Override
    public int getCount() {
        return mPlayLists.size();
    }

    @Override
    public DBPlayList getItem(int i) {
        return mPlayLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mPlayLists.get(i).getId();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewPlayListHolder viewPlayListHolder = new ViewPlayListHolder();

        if (view == null) {
            view = mInflater.inflate(R.layout.item_playlist, null, false);
            viewPlayListHolder.mViewName = view.findViewById(R.id.playlist_name);
            viewPlayListHolder.mViewWordCount = view.findViewById(R.id.playlist_word_count);
            view.setTag(viewPlayListHolder);
        } else {
            viewPlayListHolder = (ViewPlayListHolder) view.getTag();
        }

        DBPlayList playList = mPlayLists.get(i);
        viewPlayListHolder.mViewName.setText(playList.getName());
        viewPlayListHolder.mViewWordCount.setText(String
                .format(mContext.getResources().getString(R.string.playlist_label_word_count)
                , db.selectWordsFromPlayList(playList.getId()).size()));

        return view;
    }

    private class ViewPlayListHolder {
        private TextView mViewName;
        private TextView mViewWordCount;
    }
}
