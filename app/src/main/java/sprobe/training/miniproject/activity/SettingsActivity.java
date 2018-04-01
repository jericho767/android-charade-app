package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import common.Util;
import sprobe.training.miniproject.R;

public class SettingsActivity extends AppCompatActivity {
    private Toast mToast;
    private EditText mTimeLimit;
    private EditText mNumPasses;
    private ToggleButton mIncludePassed;
    private TextView mErrorMessage;
    private Button mButtonSave;

    private SharedPreferences mSharedPref;

    private View.OnClickListener mListenerSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mErrorMessage.setVisibility(View.GONE);
            String errMsg = validation();

            if (errMsg == null) {
                saveSharedPref();
                Util.nextActivity(SettingsActivity.this, new ListListActivity());
            } else {
                showErrorMessage(errMsg);
            }
        }

        private String validation() {
            String strTimeLimit = mTimeLimit.getText().toString();
            String strNumPasses = mNumPasses.getText().toString();

            if (strTimeLimit.length() == 0) {
                return String.format(getResources().getString(R.string.err_msg_required)
                        , "Time limit");
            } else if (strNumPasses.length() == 0) {
                return String.format(getResources().getString(R.string.err_msg_required)
                        , "Number of passes");
            }

            try {
                int timeLimit = Integer.parseInt(strTimeLimit);

                if (timeLimit < Util.SHARED_PREF.SETTINGS_MIN_TIME_LIMIT
                        || timeLimit > Util.SHARED_PREF.SETTINGS_MAX_TIME_LIMIT) {
                    return String.format(getResources().getString(R.string.err_msg_length)
                            , "Time limit"
                            , Util.SHARED_PREF.SETTINGS_MIN_TIME_LIMIT
                            , Util.SHARED_PREF.SETTINGS_MAX_TIME_LIMIT);
                }
            } catch (NumberFormatException e) {
                return String.format(getResources().getString(R.string.err_msg_invalid)
                        , "Time limit");
            }

            try {
                int numPasses = Integer.parseInt(strNumPasses);

                if (numPasses < Util.SHARED_PREF.SETTINGS_MIN_NUM_PASSES
                        || numPasses > Util.SHARED_PREF.SETTINGS_MAX_NUM_PASSES) {
                    return String.format(getResources().getString(R.string.err_msg_length)
                            , "Number of passes"
                            , Util.SHARED_PREF.SETTINGS_MIN_NUM_PASSES
                            , Util.SHARED_PREF.SETTINGS_MAX_NUM_PASSES);
                }
            } catch (NumberFormatException e) {
                return String.format(getResources().getString(R.string.err_msg_invalid)
                        , "Number of passes ");
            }

            return null;
        }

        private void showErrorMessage(String message) {
            mErrorMessage.setText(message);
            mErrorMessage.setVisibility(View.VISIBLE);
        }
    };

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Util.addToolbar(this, true);

        mSharedPref = this.getSharedPreferences(Util.SHARED_PREF.KEY, MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        fetchViews();
        getSharedPref();
        bindListeners();
    }

    private void saveSharedPref() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(Util.SHARED_PREF.SETTINGS_KEY_TIME_LIMIT
                , Integer.parseInt(mTimeLimit.getText().toString()));
        editor.putInt(Util.SHARED_PREF.SETTINGS_KEY_NUM_PASSES
                , Integer.parseInt(mNumPasses.getText().toString()));
        editor.putBoolean(Util.SHARED_PREF.SETTINGS_KEY_INCLUDE_PASSED
                , mIncludePassed.isChecked());
        editor.apply();
    }

    private void getSharedPref() {
        int timeLimit = mSharedPref.getInt(Util.SHARED_PREF.SETTINGS_KEY_TIME_LIMIT
                , Util.SHARED_PREF.SETTINGS_DEFAULT_TIME_LIMIT);
        int numPasses = mSharedPref.getInt(Util.SHARED_PREF.SETTINGS_KEY_NUM_PASSES
                , Util.SHARED_PREF.SETTINGS_DEFAULT_NUM_PASSES);
        boolean includePassed = mSharedPref.getBoolean(Util.SHARED_PREF.SETTINGS_KEY_INCLUDE_PASSED
                , Util.SHARED_PREF.SETTINGS_DEFAULT_INCLUDE_PASSED);

        mTimeLimit.setText(String.valueOf(timeLimit));
        mNumPasses.setText(String.valueOf(numPasses));
        mIncludePassed.setChecked(includePassed);
    }

    private void fetchViews() {
        mTimeLimit = findViewById(R.id.settings_time_limit);
        mNumPasses = findViewById(R.id.settings_num_pass);
        mIncludePassed = findViewById(R.id.settings_include_passed_words);
        mErrorMessage = findViewById(R.id.settings_error_message);
        mButtonSave = findViewById(R.id.settings_save);
    }

    private void bindListeners() {
        mButtonSave.setOnClickListener(mListenerSave);
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
        menu.findItem(R.id.action_play).setVisible(false);
        menu.findItem(R.id.action_upload).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_delete_items).setVisible(false);

        super.onPrepareOptionsMenu(menu);
        return true;
    }
}
