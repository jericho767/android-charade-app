package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import adapter.PlayListWordsAdapter;
import common.Util;
import data.Garbage;
import sprobe.training.miniproject.R;

public class ListIndexActivity extends AppCompatActivity {

    private EditText mViewItemName;
    private Garbage mGarbage;
    private Toast mToast;
    private ImageButton mBtnSubmit;
    private ListView mListWords;
    private PlayListWordsAdapter mPlayListAdapter;

    private View.OnClickListener listenerAddListItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String itemName = mViewItemName.getText().toString();
            String validateItemName = validateItemName(itemName);

            if (!validateItemName.isEmpty()) {
                mToast = Util.showToast(ListIndexActivity.this,
                        mToast, validateItemName);
            } else {
                mToast = Util.showToast(ListIndexActivity.this,
                        mToast, Garbage.SUCCESS_ITEM_ADD());

                storeListItem(itemName);

                mGarbage.getItems().add(mViewItemName.getText().toString());
                mPlayListAdapter.notifyDataSetChanged();
                mViewItemName.setText(null);
            }
        }

        private String validateItemName(String name) {
            if (name.length() < Garbage.ITEM_LENGTH_MIN
                    || name.length() > Garbage.ITEM_LENGTH_MAX) {
                return Garbage.ERROR_ITEM_LENGTH();
            } else {
                return "";
            }
        }

        private void storeListItem(String listItem) {
            // TODO: Oh crap! ABORT MISSION! I REPEAT! ABORT MISSION!
        }
    };

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_index);
        Util.addToolbar(this, true);

        fetchViews();
        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        // Get the bundle from the intent
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getInt("id", 0) == 0) {
            Util.nextActivity(this, new ListListActivity());
        } else if (bundle != null) {
            mGarbage = Garbage.getList(bundle.getInt("id"));

            // Set name in the toolbar
            Util.setToolbarTitle(this, mGarbage.getName());

            // Set list
            mPlayListAdapter = new PlayListWordsAdapter(this
                    , mGarbage.getItems());
            mListWords.setAdapter(mPlayListAdapter);
        }

        // Show keyboard when there are no items on the list
        if (mGarbage.getItems().size() == 0) {
            mViewItemName.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        bindListeners();
    }

    private void fetchViews() {
        mViewItemName = findViewById(R.id.item_name);
        mBtnSubmit = findViewById(R.id.submit);
        mListWords = findViewById(R.id.list_items);
    }

    private void bindListeners() {
        mBtnSubmit.setOnClickListener(listenerAddListItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_play) {
            Util.nextActivity(this, new GameActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mToast.cancel();
        Util.nextActivity(this, new ListListActivity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.findItem(R.id.action_upload).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);

        super.onPrepareOptionsMenu(menu);
        return true;
    }

}
