package sprobe.training.miniproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import data.Word;
import sprobe.training.miniproject.R;

public class GameActivity extends AppCompatActivity {
    private long mTimeLimit = 20000; // TODO: Set it right
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
    private static final String MESSAGE_GAME_ENDED = "Game is already over champ.";

    private Toast mToast;

    private View.OnTouchListener mListenerGameTimer = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (mGame.getCurrentRoundNumber() < 1) {
                Word word = mGame.getWord();

                if (word != null) {
                    startRound(mViewGameWordOverlay, word);
                }
            } else {
                view.performClick();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mIsTimerOn) {
                        pauseGame();
                    } else {
                        resumeGame();
                    }
                }
            }
            return false;
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
                    Util.showToast(GameActivity.this, mToast,
                            Game.MESSAGE_CANNOT_PASS_NO_MORE_WORDS);
                } else {
                    Util.showToast(GameActivity.this, mToast
                            , Game.MESSAGE_CANNOT_PASS);
                }
            } else {
                if (mRemainingMilliseconds == 0) {
                    Util.showToast(GameActivity.this, mToast, MESSAGE_GAME_ENDED);
                } else {
                    Util.showToast(GameActivity.this, mToast, MESSAGE_GAME_PAUSED);
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
                    Util.showToast(GameActivity.this, mToast, MESSAGE_GAME_ENDED);
                } else {
                    Util.showToast(GameActivity.this, mToast, MESSAGE_GAME_PAUSED);
                }
            }
        }
    };
    /*
    private View.OnClickListener mListenerViewGameOverlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Word word = mGame.getWord();

            if (word != null) {
                startRound(view, word);
            }
        }
    };
    */
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
        mRemainingMilliseconds = mTimeLimit;
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
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("game") == null) {
            mGame = new Game(numberOfPasses, words, includePasses);
        } else {
            mGame = Util.jsonToGame(bundle.getString("game"));
        }

        readyRound();

        // Bind listeners
        Util.hideTheSystemUiWhenShown(this);
        mBtnCheck.setOnClickListener(mListenerBtnCheck);
        mBtnPass.setOnClickListener(mListenerBtnPass);
        /*mViewGameWordOverlay.setOnClickListener(mListenerViewGameOverlay);*/
        mViewGameTimer.setOnTouchListener(mListenerGameTimer);
    }

    private void endGame() {
        // TODO: Implement CORRECTLY? RIGHTEOUSLY? WITH ALL THE BEST YOU'VE GOT!
        mViewGameWord.setText("");
        mViewGameWord.setVisibility(View.GONE);
        mViewGameWordOverlay.setText(MESSAGE_GAME_END);
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
        Log.wtf("GAME | INDEX OF THE LAST WORD: ", mGame.getCurrentWordIndex() + "");
        Bundle bundle = new Bundle();
        bundle.putString("game", Util.gameToJson(mGame));
        Util.nextActivity(this, new RoundEndActivity(), bundle);
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
        mViewGameWord.setText(mGame.getCurrentWord().getText());
        continueTimer();
    }

    private void pauseGame() {
        mViewGameWord.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_sm));
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
