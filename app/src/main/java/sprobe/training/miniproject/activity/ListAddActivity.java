package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import common.Util;
import database.DBPlayList;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class ListAddActivity extends AppCompatActivity {
    private TextInputLayout mLayoutName;
    private TextInputEditText mViewName;
    private Button mBtnSubmit;
    private DatabaseHelper db;

    private View.OnClickListener listenerAddList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = mViewName.getText().toString();
            String validateName = validateName(name, ListAddActivity.this);

            if (!validateName.isEmpty()) {
                mLayoutName.setError(validateName);
            } else {
                mLayoutName.setError(null);

                long playListId = storeList(name);

                if (playListId == -1) {
                    Toast.makeText(ListAddActivity.this
                            , getResources().getString(R.string.err_msg_crash)
                            , Toast.LENGTH_SHORT).show();
                    Util.nextActivity(ListAddActivity.this
                            , new ListListActivity());
                } else {
                    // Pass bundle to next activity
                    Bundle bundle = new Bundle();
                    bundle.putLong(Util.BUNDLE_KEYS.PLAYLIST_ID, playListId);
                    Util.nextActivity(ListAddActivity.this
                            , new ListIndexActivity(), bundle);
                }
            }
        }

        private long storeList(String name) {
            return db.insertPlaylist(name);
        }
    };

    public static String validateName(String name, AppCompatActivity activity) {
        if (name.length() < DBPlayList.NAME_LENGTH_MIN
                || name.length() > DBPlayList.NAME_LENGTH_MAX) {
            return String.format(Util.getLocale()
                    , activity.getResources().getString(R.string.err_msg_char_length)
                    , activity.getResources().getString(R.string.playlist_label_name)
                    , DBPlayList.NAME_LENGTH_MIN, DBPlayList.NAME_LENGTH_MAX);
        } else {
            return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);
        Util.addToolbar(this, true);
        db = new DatabaseHelper(this);

        fetchViews();
        bindListeners();
    }

    private void fetchViews() {
        mLayoutName = findViewById(R.id.name_layout);
        mViewName = findViewById(R.id.name);
        mBtnSubmit = findViewById(R.id.submit);
    }

    private void bindListeners() {
        mBtnSubmit.setOnClickListener(listenerAddList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Util.nextActivity(this, new ListListActivity());
    }

}
