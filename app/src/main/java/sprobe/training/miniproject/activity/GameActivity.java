package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import common.Util;
import sprobe.training.miniproject.R;

public class GameActivity extends AppCompatActivity {
    private long mRemainingMilliseconds;

    private CountDownTimer mCountDownTimer;

    private TextView mViewGameWord;
    private Button mBtnPass;
    private Button mBtnCheck;
    private TextView mViewGameTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Util.goFullscreen(this);

        // Fetch views
        mViewGameWord = findViewById(R.id.game_word);
        mViewGameTimer = findViewById(R.id.game_timer);
        mBtnPass = findViewById(R.id.game_button_pass);
        mBtnCheck = findViewById(R.id.game_button_check);

        setCountDownTimer(121000);
        mCountDownTimer.start();

        // Bind listeners
        Util.hideTheSystemUiWhenShown(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Work on this crap. Crappy thing does not work.
        setCountDownTimer(mRemainingMilliseconds);
        mCountDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: Work on this crap. It does not work
        mCountDownTimer.cancel();
    }

    private void setCountDownTimer(long timeLimit) {
        mCountDownTimer = new CountDownTimer(timeLimit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRemainingMilliseconds = millisUntilFinished;
                mViewGameTimer.setText(Util.formatSecondsToTime(millisUntilFinished / 1000));
                System.out.print(mRemainingMilliseconds);
            }

            @Override
            public void onFinish() {
                // TODO: Show results
            }
        };
    }

}
