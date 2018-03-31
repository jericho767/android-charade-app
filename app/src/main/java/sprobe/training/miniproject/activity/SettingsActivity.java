package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import common.Util;
import sprobe.training.miniproject.R;

public class SettingsActivity extends AppCompatActivity {
    private Toast mToast;
    private TextView mTimeLimit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Util.addToolbar(this, true);

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
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.findItem(R.id.action_upload).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);

        super.onPrepareOptionsMenu(menu);
        return true;
    }
}
