package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import common.Util;
import data.PlayList;
import sprobe.training.miniproject.R;

public class ListListActivity extends AppCompatActivity {
    private FloatingActionButton mFabAdd;
    private Toast mExitAppToast;
    private final String MESSAGE_EXIT_APP = "Tap again i dare you. I DOUBLE DARE YOU!";

    private View.OnClickListener listenerAddList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Util.nextActivity(ListListActivity.this, new ListAddActivity());
        }
    };

    private View.OnLongClickListener listenerLongClickList = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            // TODO: Implement on long click
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_list);
        Util.addToolbar(this, false);

        // Fetch views
        ListView listView = findViewById(R.id.lists);
        mFabAdd = findViewById(R.id.fab_add);

        mExitAppToast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        ArrayList<PlayList> playLists = PlayList.getLists();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Util.getNamesFromPlayLists(playLists));
        listView.setAdapter(adapter);

        // Bind listeners
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

        if (id == R.id.action_settings) {
            return true;
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
            mExitAppToast.setText(this.MESSAGE_EXIT_APP);
            mExitAppToast.show();
        }
    }

}
