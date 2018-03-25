package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import common.Util;
import data.Game;
import sprobe.training.miniproject.R;

public class GameActivity extends AppCompatActivity {
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

    private static final String MESSAGE_GAME_END = "That's all folks!";
    private static final String MESSAGE_GAME_PAUSED = "Game is paused you imbecile.";

    private Toast mToast;

    private View.OnTouchListener mListenerGameWord = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            view.performClick();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mIsTimerOn) {
                    pauseGame();
                } else {
                    resumeGame();
                }
            }

            return false;
        }
    };

    private View.OnClickListener mListenerBtnPass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mIsTimerOn) {
                if (mGame.canPass()) {
                    mGame.pass();
                    mViewCountPass.setText(String.valueOf(mGame
                            .getPassedWordsInCurrentRound().size()));

                    mBtnPass.setText(getPassButtonText(mGame.getRemainingPasses()));

                    Game.Word word = mGame.getWord();

                    if (word == null) {
                        endGame();
                    } else {
                        mViewGameWord.setText(word.getText());
                    }
                } else {
                    Util.showToast(GameActivity.this, mToast
                            , Game.MESSAGE_CANNOT_PASS);
                }
            } else {
                Util.showToast(GameActivity.this, mToast, MESSAGE_GAME_PAUSED);
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

                Game.Word word = mGame.getWord();

                if (word == null) {
                    endGame();
                } else {
                    mViewGameWord.setText(word.getText());
                }
            } else {
                Util.showToast(GameActivity.this, mToast, MESSAGE_GAME_PAUSED);
            }
        }
    };

    private View.OnClickListener mListenerViewGameOverlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Game.Word word = mGame.getWord();

            if (word != null) {
                mGame.startRound();
                startTimer();

                view.setVisibility(View.GONE);
                mViewGameWord.setText(word.getText());
                mViewGameWord.setVisibility(View.VISIBLE);
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

        // TODO: Replace this with actual values
        int numberOfPasses = 3;
        String[] words = {"One", "Two", "Three", "Four"};
        boolean includePasses = true;
        int timelimit = 5000;
        mRemainingMilliseconds = timelimit;
        mIsTimerOn = false;

        // Fetch views
        mViewCountCheck = findViewById(R.id.count_check);
        mViewCountPass = findViewById(R.id.count_pass);
        mViewGameWord = findViewById(R.id.game_word);
        mViewGameTimer = findViewById(R.id.game_timer);
        mBtnPass = findViewById(R.id.game_button_pass);
        mBtnCheck = findViewById(R.id.game_button_check);
        mViewGameWordOverlay = findViewById(R.id.game_overlay_text);

        // Initialize the game
        mGame = new Game(numberOfPasses, words, includePasses);

        mBtnPass.setText(getPassButtonText(numberOfPasses));
        setTextOfTimer(mRemainingMilliseconds);
        setCountDownTimer();

        // Bind listeners
        Util.hideTheSystemUiWhenShown(this);
        mBtnCheck.setOnClickListener(mListenerBtnCheck);
        mBtnPass.setOnClickListener(mListenerBtnPass);
        mViewGameWordOverlay.setOnClickListener(mListenerViewGameOverlay);
        mViewGameWord.setOnTouchListener(mListenerGameWord);
    }

    private void endGame() {
        // TODO: Implement
        mViewGameWord.setText("");
        mViewGameWord.setVisibility(View.GONE);
        mViewGameWordOverlay.setText(MESSAGE_GAME_END);
        mViewGameWordOverlay.setVisibility(View.VISIBLE);
    }

    private void endRound() {
        // TODO: Implement
        Log.wtf("END ROUND", "Round ends. " + mRemainingMilliseconds);
    }

    private String getPassButtonText(int remainingPasses) {
        return String.format(Util.getLocale(), "%s (%d)",
                getResources().getString(R.string.button_pass), remainingPasses);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    private void setCountDownTimer() {
        mCountDownTimer = new CountDownTimer(mRemainingMilliseconds,
                1000) {
            public void onTick(long millisUntilFinished) {
                mRemainingMilliseconds = millisUntilFinished - (millisUntilFinished % 1000);
//                mRemainingMilliseconds = millisUntilFinished;
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
        mViewGameWord.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_lg));
        mViewGameWord.setText(mGame.getCurentWord().getText());
        continueTimer();
    }

    private void pauseGame() {
        Log.wtf("[PAUSED] TIME LEFT: ", String.valueOf(mRemainingMilliseconds));
        mViewGameWord.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_sm));
        mViewGameWord.setText(getResources().getString(R.string.button_game_resume));
        pauseTimer();
    }

    private void continueTimer() {
        setCountDownTimer();
        mCountDownTimer.start();
    }

    private void startTimer() {
        mIsTimerOn = true;
        mCountDownTimer.start();
    }

    private void pauseTimer() {
        mIsTimerOn = false;
        mCountDownTimer.cancel();
    }

}
