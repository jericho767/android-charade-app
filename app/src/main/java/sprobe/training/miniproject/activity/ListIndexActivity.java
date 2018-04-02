package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import adapter.PlayListWordsAdapter;
import common.Util;
import database.DBPlayList;
import database.DBWord;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class ListIndexActivity extends AppCompatActivity {

    private EditText mViewItemName;
    private Toast mToast;
    private ImageButton mBtnSubmit;
    private ListView mListWords;
    private DBPlayList mPlayList;
    private DatabaseHelper db;
    private PlayListWordsAdapter mPlayListAdapter;
    private AlertDialog.Builder mDeleteDialog;

    private View.OnClickListener listenerAddListItem = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String itemName = mViewItemName.getText().toString();
            String validateItemName = validateItemName(itemName);

            if (!validateItemName.isEmpty()) {
                mToast = Util.showToast(ListIndexActivity.this,
                        mToast, validateItemName);
            } else {
                mToast = Util.showToast(ListIndexActivity.this, mToast
                        , getResources().getString(R.string.playlist_message_item_added));

                mPlayListAdapter.addItem(new DBWord(mViewItemName.getText().toString()));
                mViewItemName.setText(null);
            }
        }

        private String validateItemName(String name) {
            if (name.length() < DBWord.TEXT_LENGTH_MIN
                    || name.length() > DBWord.TEXT_LENGTH_MAX) {
                return String.format(
                        getResources().getString(R.string.err_msg_char_length)
                        , getResources().getString(R.string.playlist_label_list_item)
                        , DBWord.TEXT_LENGTH_MIN, DBWord.TEXT_LENGTH_MAX);
            } else {
                return "";
            }
        }
    };

    private void storeWords() {
        for(DBWord word : mPlayListAdapter.getWords()) {
            if (word.getId() == 0) {
                long wordId = db.insertWord(word.getText(), mPlayList.getId());

                if (wordId == -1) {
                    Log.wtf("INSERT|WORD|ERROR: ", word.toString());
                } else {
                    Log.i("INSERT|WORD|SUCCESS: ", word.toString());
                }
            }
        }
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_index);
        Util.addToolbar(this, true);

        setDialog();
        fetchViews();
        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        db = new DatabaseHelper(this);

        // Get the bundle from the intent
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getLong(Util.BUNDLE_KEYS.PLAYLIST_ID, 0) == 0) {
            Util.nextActivity(this, new ListListActivity());
        } else if (bundle != null) {
            mPlayList = db.selectPlayListById(bundle.getLong(Util.BUNDLE_KEYS.PLAYLIST_ID));

            // Set name in the toolbar
            Util.setToolbarTitle(this, mPlayList.getName());

            // Set list
            mPlayListAdapter = new PlayListWordsAdapter(this
                    , db.selectWordsFromPlayList(mPlayList.getId()));
            mListWords.setAdapter(mPlayListAdapter);
        }

        // Show keyboard when there are no items on the list
        if (mPlayListAdapter.getCount() == 0) {
            mViewItemName.requestFocus();
            Util.showKeyboard(this);
        }

        bindListeners();
    }

    private void setDialog() {
        DialogInterface.OnClickListener listenerDelete = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePlayList();
            }
        };

        mDeleteDialog = Util.createAlertDialog(this
                , getResources().getString(R.string.button_delete)
                , getResources().getString(R.string.msg_delete)
                , getResources().getString(R.string.button_delete)
                , Util.dialogListenerShowConfirmDialog(this, listenerDelete)
                , getResources().getString(R.string.button_cancel)
                , Util.dialogListenerDismissDialog());
    }

    private void deletePlayList() {
        int rowsAffected = db.deletePlayListById(mPlayList.getId());

        if (rowsAffected == 1) {
            Util.nextActivity(this, new ListListActivity());
            Toast.makeText(this
                    , getResources().getString(R.string.playlist_message_deleted)
                    , Toast.LENGTH_LONG).show();
        } else if (rowsAffected > 1) {
            Log.wtf("IMPOSSIBLE: ", "Deleted id is: " + mPlayList.getId());
        } else {
            Log.wtf("HUH: ", "ID trying to be deleted is: " + mPlayList.getId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeWords();
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
            if (mPlayListAdapter.getCount() > 0) {
                Bundle bundle = new Bundle();
                bundle.putLong(Util.BUNDLE_KEYS.PLAYLIST_ID, mPlayList.getId());
                Util.nextActivity(this, new GameActivity(), bundle);
            } else {
                Util.showToast(this, mToast
                        , getResources().getString(R.string.game_message_no_words));
            }
        } else if (id == R.id.action_delete) {
            mDeleteDialog.show();
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
