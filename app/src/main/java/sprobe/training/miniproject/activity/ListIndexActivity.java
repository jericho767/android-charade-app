package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import adapter.PlayListWordsAdapter;
import common.Util;
import database.DBPlayList;
import database.DBWord;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class ListIndexActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText mViewItemName;
    private Toast mToast;
    private ImageButton mBtnSubmit;
    private ListView mListWords;
    private DBPlayList mPlayList;
    private PlayListWordsAdapter mPlayListAdapter;
    private AlertDialog.Builder mDeleteDialog;
    private AlertDialog mEditNameDialog;

    private EditText mViewPlaylistName;
    private Button mBtnSave;
    private TextView mViewPlaylistNameError;

    private View.OnClickListener mListenerDeleteItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            long wordId = mPlayListAdapter.getItemId(position);
            int rowsAffected = 0;

            if (wordId != 0) {
                rowsAffected = db.deleteWordById(wordId);
            }

            if (wordId != 0 && rowsAffected == 0) {
                Util.showToast(ListIndexActivity.this, mToast
                        , getResources().getString(
                                R.string.playlist_message_delete_list_item_oops));
            } else {
                if (rowsAffected > 1) {
                    Log.wtf("IMPOSSIBLE: ", "Delete WORDS with id of " + wordId);
                }

                Util.showToast(ListIndexActivity.this, mToast
                        , getResources().getString(
                                R.string.playlist_message_deleted_list_item));
                mPlayListAdapter.deleteItem(position);
            }
        }
    };

    private View.OnClickListener mListenerAddListItem = new View.OnClickListener() {
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
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
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
                    , db.selectWordsByPlayListId(mPlayList.getId())
                    , mListenerDeleteItem);
            mListWords.setAdapter(mPlayListAdapter);
        }

        // Show keyboard when there are no items on the list
        if (mPlayListAdapter.getCount() == 0) {
            mViewItemName.requestFocus();
            Util.showKeyboard(this);
        }

        bindListeners();
    }

    private void downloadList() {
        ArrayList<Long> id = new ArrayList<>();
        id.add(mPlayList.getId());
        ArrayList<DBPlayList> dbPlayLists = db.selectPlayListById(id);
        String json = new Gson().toJson(dbPlayLists, Util.PLAYLISTS_TYPE);

        File directory;

        if (Environment.getExternalStorageState() == null) {
            directory = Environment.getDataDirectory();
        } else {
            directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
        }

        File file = new File(directory + File.separator +
                Util.generateFilename(mPlayList.getName()));

        try {
            while (!file.createNewFile()) {
                file = new File(directory + File.separator +
                        Util.generateFilename(mPlayList.getName()));
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(json.getBytes());
            outputStream.close();

            int originalToastDuration = mToast.getDuration();
            mToast.setDuration(Toast.LENGTH_LONG);

            Util.showToast(this, mToast, String.format(
                    getResources().getString(R.string.playlist_message_downloaded)
                    , file.getAbsolutePath()));
            mToast.setDuration(originalToastDuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        mBtnSubmit.setOnClickListener(mListenerAddListItem);
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
        } else if (id == R.id.action_download) {
            downloadList();
        } else if (id == R.id.action_settings) {
            editName();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("InflateParams")
    private void editName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog = getLayoutInflater().inflate(R.layout.dialog_edit_playlist_name, null);
        mViewPlaylistName = dialog.findViewById(R.id.playlist_name);
        mBtnSave = dialog.findViewById(R.id.playlist_edit_save);
        mViewPlaylistNameError = dialog.findViewById(R.id.playlist_name_error);

        mViewPlaylistName.setText(mPlayList.getName());
        mViewPlaylistName.setSelection(mViewPlaylistName.getText().length());

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPlaylistNameError.setVisibility(View.GONE);
                String newName = mViewPlaylistName.getText().toString();
                String error = ListAddActivity.validateName(newName
                        , ListIndexActivity.this);

                if (!error.isEmpty()) {
                    mViewPlaylistNameError.setText(error);
                    mViewPlaylistNameError.setVisibility(View.VISIBLE);
                } else {
                    storeWords();
                    db.updatePlayListById(mPlayList.getId(), newName);
                    mEditNameDialog.hide();
                    Util.refreshActivity(ListIndexActivity.this);
                }
            }
        });

        builder.setView(dialog);
        mEditNameDialog = builder.create();
        Util.showKeyboard(mEditNameDialog.getWindow());
        mEditNameDialog.show();
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
        // Use settings action menu for renaming playlist name
        MenuItem menuEdit = menu.findItem(R.id.action_settings);
        menuEdit.setIcon(R.drawable.ic_edit);
        menuEdit.setTitle(R.string.button_edit);
        menuEdit.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.findItem(R.id.action_upload).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);

        super.onPrepareOptionsMenu(menu);
        return true;
    }

}
