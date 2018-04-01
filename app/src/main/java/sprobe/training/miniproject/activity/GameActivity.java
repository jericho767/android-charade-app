package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import common.Util;
import data.Game;
import data.Word;
import database.DBWord;
import database.DatabaseHelper;
import sprobe.training.miniproject.R;

public class GameActivity extends AppCompatActivity {
    private long mTimeLimit;
    private int mNumPasses;
    private boolean mIncludePassed;
    private long mRemainingMilliseconds;
    private CountDownTimer mCountDownTimer;
    private boolean mIsTimerOn;
    private Game mGame;

    private TextView mViewGameWord;
    private Button mBtnPass;
    private Button mBtnCheck;
    private TextView mViewGameTimer;
    private TextView mViewGameWordOverlay;
    private TextView mViewCountPass;
    private TextView mViewCountCheck;

    private Toast mToast;

    private View.OnClickListener mListenerGameTimer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mGame.getCurrentRoundNumber() < 1) {
                Word word = mGame.getWord();

                if (word != null) {
                    startRound(mViewGameWordOverlay, word);
                }
            } else {
                if (mIsTimerOn) {
                    pauseGame();
                } else {
                    resumeGame();
                }
            }
        }
    };

    private View.OnClickListener mListenerBtnPass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mIsTimerOn) {
                int pollOfWordsCount = mGame.pollOfWordsCount();

                if (mGame.canPass() && pollOfWordsCount > 1) {
                    mGame.pass();
                    mViewCountPass.setText(String.valueOf(mGame
                            .getPassedWordsInCurrentRound().size()));

                    mBtnPass.setText(getPassButtonText(mGame.getRemainingPasses()));

                    Word word = mGame.getWord();

                    if (word == null) {
                        endGame();
                    } else {
                        mViewGameWord.setText(word.getText());
                    }
                } else if (pollOfWordsCount < 2) {
                    Util.showToast(GameActivity.this, mToast
                            , getResources().getString(R.string.game_message_last_word));
                } else {
                    Util.showToast(GameActivity.this, mToast
                            , getResources().getString(R.string.game_message_cannot_pass));
                }
            } else {
                if (mRemainingMilliseconds == 0) {
                    Util.showToast(GameActivity.this, mToast
                            , getResources().getString(R.string.game_message_ended));
                } else {
                    Util.showToast(GameActivity.this, mToast
                            , getResources().getString(R.string.game_message_paused));
                }
            }
        }
    };

    private View.OnClickListener mListenerBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mIsTimerOn) {
                mGame.check();
                mViewCountCheck.setText(String.valueOf(mGame
                        .getCheckedWordsInCurrentRound().size()));

                Word word = mGame.getWord();

                if (word == null) {
                    endGame();
                } else {
                    mViewGameWord.setText(word.getText());
                }
            } else {
                if (mRemainingMilliseconds == 0) {
                    Util.showToast(GameActivity.this, mToast
                            , getResources().getString(R.string.game_message_ended));
                } else {
                    Util.showToast(GameActivity.this, mToast
                            , getResources().getString(R.string.game_message_paused));
                }
            }
        }
    };

    @Override
    @SuppressLint({"ShowToast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Util.goFullscreen(this);

        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        fetchViews();
        getSharedPref();

        mRemainingMilliseconds = mTimeLimit;
        mIsTimerOn = false;

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString(Util.BUNDLE_KEYS.GAME_JSON) == null) {
            ArrayList<DBWord> words = new ArrayList<>();

            if (bundle != null && bundle.getLong(Util.BUNDLE_KEYS.PLAYLIST_ID) > 0) {
                DatabaseHelper db = new DatabaseHelper(this);
                words = db.selectWordsFromPlayList(bundle.getLong(Util.BUNDLE_KEYS.PLAYLIST_ID));
            }

            if (words.size() == 0) {
                Util.showToast(this, mToast
                        , getResources().getString(R.string.game_message_list_not_found));
                Util.nextActivity(this, new ListListActivity());
            }

            // Initialize the game
            mGame = new Game(mNumPasses, words, mIncludePassed);
        } else {
            mGame = Util.jsonToGame(bundle.getString(Util.BUNDLE_KEYS.GAME_JSON));
        }

        readyRound();
        bindListeners();
    }

    private void bindListeners() {
        Util.hideTheSystemUiWhenShown(this);
        mBtnCheck.setOnClickListener(mListenerBtnCheck);
        mBtnPass.setOnClickListener(mListenerBtnPass);
        mViewGameTimer.setOnClickListener(mListenerGameTimer);
    }

    private void fetchViews() {
        mViewCountCheck = findViewById(R.id.count_check);
        mViewCountPass = findViewById(R.id.count_pass);
        mViewGameWord = findViewById(R.id.game_word);
        mViewGameTimer = findViewById(R.id.game_timer);
        mBtnPass = findViewById(R.id.game_button_pass);
        mBtnCheck = findViewById(R.id.game_button_check);
        mViewGameWordOverlay = findViewById(R.id.game_overlay_text);
    }

    private void getSharedPref() {
        SharedPreferences sharedPref = this.getSharedPreferences(Util.SHARED_PREF.KEY
                , MODE_PRIVATE);
        mNumPasses = sharedPref.getInt(Util.SHARED_PREF.SETTINGS_KEY_NUM_PASSES
                , Util.SHARED_PREF.SETTINGS_DEFAULT_NUM_PASSES);
        mIncludePassed = sharedPref.getBoolean(Util.SHARED_PREF.SETTINGS_KEY_INCLUDE_PASSED
                , Util.SHARED_PREF.SETTINGS_DEFAULT_INCLUDE_PASSED);

        int timeLimitInSeconds = sharedPref.getInt(Util.SHARED_PREF.SETTINGS_KEY_TIME_LIMIT
                , Util.SHARED_PREF.SETTINGS_DEFAULT_TIME_LIMIT);
        mTimeLimit = Long.parseLong(String.valueOf(timeLimitInSeconds * 1000));
    }

    private void endGame() {
        mViewGameWord.setText("");
        mViewGameWord.setVisibility(View.GONE);
        mViewGameWordOverlay.setText(String.format(Util.getLocale(), "%s"
                , getResources().getString(R.string.game_message_end)));
        mViewGameWordOverlay.setVisibility(View.VISIBLE);
    }

    private void readyRound() {
        mViewGameWord.setVisibility(View.INVISIBLE);
        mViewGameWordOverlay.setText(getResources().getString(R.string.button_game_start));
        mViewGameWordOverlay.setVisibility(View.VISIBLE);

        mBtnPass.setText(getPassButtonText(mGame.getMaxPasses()));
        setTextOfTimer(mTimeLimit);
        setCountDownTimer();
    }

    private void startRound(View viewGameOverlay, Word word) {
        mGame.startRound();
        viewGameOverlay.setVisibility(View.GONE);
        mViewGameWord.setText(word.getText());
        mViewGameWord.setVisibility(View.VISIBLE);

        startTimer();
    }

    private void endRound() {
        mToast.cancel();
        Bundle bundle = new Bundle();
        bundle.putString(Util.BUNDLE_KEYS.GAME_JSON, Util.gameToJson(mGame));
        Util.nextActivity(this, new RoundEndActivity(), bundle);
    }

    private String getPassButtonText(int remainingPasses) {
        return String.format(Util.getLocale(), "%s (%d)"
                , getResources().getString(R.string.button_pass), remainingPasses);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    private void setCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mRemainingMilliseconds
                , 1000) {
            public void onTick(long millisUntilFinished) {
                mRemainingMilliseconds = millisUntilFinished - (millisUntilFinished % 1000);
                setTextOfTimer(mRemainingMilliseconds);
            }

            public void onFinish() {
                setTextOfTimer(0);
                endRound();
            }
        };
    }

    private void setTextOfTimer(long milliseconds) {
        mViewGameTimer.setText(Util.formatSecondsToTime(milliseconds / 1000));
    }

    private void resumeGame() {
        mViewGameWord.setTextSize(TypedValue.COMPLEX_UNIT_PX
                , getResources().getDimension(R.dimen.font_lg));
        mViewGameWord.setText(mGame.getCurrentWord().getText());
        continueTimer();
    }

    private void pauseGame() {
        mViewGameWord.setTextSize(TypedValue.COMPLEX_UNIT_PX
                , getResources().getDimension(R.dimen.font_sm));
        mViewGameWord.setText(getResources().getString(R.string.button_game_resume));
        pauseTimer();
    }

    private void continueTimer() {
        mIsTimerOn = true;
        setCountDownTimer();
        mCountDownTimer.start();
    }

    private void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsTimerOn = true;
                mCountDownTimer.start();
            }
        }, 1000);
    }

    private void pauseTimer() {
        mIsTimerOn = false;
        mCountDownTimer.cancel();
    }

}
