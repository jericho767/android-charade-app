package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import adapter.RoundWordsAdapter;
import common.Util;
import data.Game;
import sprobe.training.miniproject.R;

public class RoundEndActivity extends AppCompatActivity {

    private TextView mLabelUncheckedWords;
    private TextView mLabelCheckedWords;
    private ListView mListUncheckedWords;
    private ListView mListCheckedWords;
    private Toolbar mToolbar;

    private Game mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_end);
        Util.goFullscreen(this);
        Util.addToolbar(this, false);

        fetchViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("game") == null) {
            // TODO: Do I really have to do this? I mean look at this? Is this right?
            Util.nextActivity(this, new ListListActivity());
        } else {
            mGame = Util.jsonToGame(bundle.getString("game"));
        }

        // Set round parameters
        setRoundEndCountUncheckedWords(mLabelUncheckedWords);
        setRoundEndCountCheckedWords(mLabelCheckedWords);
        Util.setToolbarTitle(this, String.format(Util.getLocale(),
                getResources().getString(R.string.round_title_end),
                mGame.getCurrentRoundNumber()));

        RoundWordsAdapter adapter = new RoundWordsAdapter(this,
                mGame.getUncheckedWordsInCurrentRound(),
                mGame.getCheckedWordsInCurrentRound(),
                mGame.getCurrentWordIndex());
        mListUncheckedWords.setAdapter(adapter.getUncheckedWordsAdapter());
        mListCheckedWords.setAdapter(adapter.getCheckedWordsAdapter());
    }

    private void fetchViews() {
        mLabelUncheckedWords = findViewById(R.id.round_label_unchecked_words);
        mLabelCheckedWords = findViewById(R.id.round_label_checked_words);
        mListUncheckedWords = findViewById(R.id.round_unchecked_words);
        mListCheckedWords = findViewById(R.id.round_checked_words);
        mToolbar = findViewById(R.id.toolbar);
    }

    private void setRoundEndCountUncheckedWords(TextView viewLabelUncheckedWords) {
        viewLabelUncheckedWords.setText(String.format(Util.getLocale(),
                getResources().getString(R.string.round_title_unchecked_words) + " (%d)",
                mGame.getUncheckedWordsInCurrentRound().size()));
    }

    private void setRoundEndCountCheckedWords(TextView viewLabelCheckedWords) {
        viewLabelCheckedWords.setText(String.format(Util.getLocale(),
                getResources().getString(R.string.round_title_checked_words) + " (%d)",
                mGame.getCheckedWordsInCurrentRound().size()));
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
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_download).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
        menu.findItem(R.id.action_upload).setVisible(false);

        MenuItem actionPlay = menu.findItem(R.id.action_play);
        actionPlay.setTitle(getResources().getString(R.string.round_label_button_start_round));
        actionPlay.setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_play) {
            // TODO: Implement click
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
