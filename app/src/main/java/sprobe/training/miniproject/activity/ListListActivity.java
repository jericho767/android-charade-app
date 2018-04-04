package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import adapter.PlayListListAdapter;
import common.Util;
import database.DBPlayList;
import database.DBWord;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class ListListActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private FloatingActionButton mFabAdd;
    private ListView mListPlayList;
    private Toast mExitAppToast;
    private String mUploadedFileContents;

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
        db = new DatabaseHelper(this);

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
        } else if (id == R.id.action_upload) {
            Util.askFilePermissions(this);
            Util.openFileChooser(this);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPause() {
        super.onPause();
        mExitAppToast.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Util.RESULT_LOAD_LIST_FILE && resultCode == RESULT_OK && data != null) {
            Uri selectedFile = data.getData();
            String selectedFileExtension = "";

            if (selectedFile != null) {
                selectedFileExtension = Util.getFileExtensionFromUri(selectedFile);
            }

            if (Util.isAcceptableFileExtension(selectedFileExtension)) {
                String selectedFilePath = Util.getPath(this, selectedFile);

                try {
                    if (selectedFilePath != null) {
                        File file = new File(selectedFilePath);
                        FileInputStream fin = new FileInputStream(file);
                        mUploadedFileContents = Util.convertStreamToString(fin);
                        fin.close();
                        insertLists();
                    }
                } catch (NullPointerException | IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Toast.makeText(this
                        , getResources().getString(R.string.err_msg_wrong_uploaded_file)
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertLists() {
        if (mUploadedFileContents != null) {
            ArrayList<DBPlayList> dbPlayLists = new Gson().fromJson(mUploadedFileContents
                    , Util.PLAYLISTS_TYPE);

            long playListId;

            for (DBPlayList playList : dbPlayLists) {
                playListId = db.insertPlaylist(
                        Util.createPlayListNameFromUpload(playList.getName()));

                for (DBWord word : playList.getWords()) {
                    db.insertWord(word.getText(), playListId);
                }
            }

            Toast.makeText(this, String.format(
                    getResources().getString(R.string.playlist_message_uploaded)
                        , dbPlayLists.size())
                    , Toast.LENGTH_LONG).show();

            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onBackPressed() {
        if (mExitAppToast.getView().isShown()) {
            mExitAppToast.cancel();
            finishAffinity();
        } else {
            Util.showToast(this, mExitAppToast
                    , getResources().getString(R.string.app_message_on_close));
        }
    }

}
