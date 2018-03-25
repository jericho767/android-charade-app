package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import common.Util;
import sprobe.training.miniproject.R;

public class MainActivity extends AppCompatActivity {
    private final int WAIT_SUB_TEXT_SHOW = 2000;
    private final int WAIT_SPLASH_TO_FINISH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.goFullscreen(this);
        setContentView(R.layout.activity_main);
        this.proceedToHomeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.goFullscreen(this);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void proceedToHomeScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView subText = findViewById(R.id.sub_text);
                subText.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Util.nextActivity(MainActivity.this, new ListListActivity());
                    }
                }, WAIT_SPLASH_TO_FINISH);
            }
        }, WAIT_SUB_TEXT_SHOW);
    }

}
