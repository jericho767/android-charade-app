package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import adapter.PlayListListAdapter;
import common.Util;
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

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

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
            mExitAppToast.setText(getResources().getString(R.string.app_message_on_close));
            mExitAppToast.show();
        }
    }

}
