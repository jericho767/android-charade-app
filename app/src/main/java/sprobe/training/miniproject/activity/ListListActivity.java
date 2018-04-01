package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import adapter.PlayListListAdapter;
import common.Util;
import database.DBPlayList;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class ListListActivity extends AppCompatActivity {
    private FloatingActionButton mFabAdd;
    private ListView mListPlayList;
    private Toast mExitAppToast;

    private View.OnClickListener listenerAddList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Util.nextActivity(ListListActivity.this, new ListAddActivity());
        }
    };

    private AdapterView.OnItemClickListener mListenerItemClick =
        new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DBPlayList playList = (DBPlayList) adapterView.getItemAtPosition(i);

                Bundle bundle = new Bundle();
                bundle.putLong(Util.BUNDLE_KEYS.PLAYLIST_ID, playList.getId());
                Util.nextActivity(ListListActivity.this
                        , new ListIndexActivity(), bundle);
            }
    };

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_list);
        Util.addToolbar(this, false);
        DatabaseHelper db = new DatabaseHelper(this);

        fetchViews();
        mExitAppToast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        PlayListListAdapter playListListAdapter = new PlayListListAdapter(this
                , db.selectAllPlayLists());
        mListPlayList.setAdapter(playListListAdapter);

        bindListeners();
    }

    private void fetchViews() {
        mFabAdd = findViewById(R.id.fab_add);
        mListPlayList = findViewById(R.id.lists);
    }

    private void bindListeners() {
        mFabAdd.setOnClickListener(listenerAddList);
        mListPlayList.setOnItemClickListener(mListenerItemClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_delete_items).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Util.nextActivity(this, new SettingsActivity());
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPause() {
        super.onPause();
        mExitAppToast.cancel();
    }

    @Override
    public void onBackPressed() {
        if (mExitAppToast.getView().isShown()) {
            mExitAppToast.cancel();
            this.finishAffinity();
        } else {
            Util.showToast(this, mExitAppToast
                    , getResources().getString(R.string.app_message_on_close));
        }
    }

}
