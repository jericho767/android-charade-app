package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import data.PlayList;
import common.Util;
import sprobe.training.miniproject.R;

public class ListAddActivity extends AppCompatActivity {
    private TextInputLayout mLayoutName;
    private TextInputEditText mViewName;
    private Button mBtnSubmit;

    private View.OnClickListener listenerAddList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = mViewName.getText().toString();
            String validateName = validateName(name);

            if (!validateName.isEmpty()) {
                mLayoutName.setError(validateName);
            } else {
                mLayoutName.setError(null);

                // Pass bundle to next activity
                Bundle bundle = new Bundle();
                bundle.putInt("id", storeList(name));
                Util.nextActivity(ListAddActivity.this,
                        new ListIndexActivity(), bundle);
            }
        }

        private String validateName(String name) {
            if (name.length() < PlayList.NAME_LENGTH_MIN
                    || name.length() > PlayList.NAME_LENGTH_MAX) {
                return PlayList.ERROR_NAME_LENGTH();
            } else {
                return "";
            }
        }

        private int storeList(String name) {
            return PlayList.store(new PlayList(name));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);
        Util.addToolbar(this, true);

        // Fetch views // TODO: Move to a method
        this.mLayoutName = findViewById(R.id.name_layout);
        this.mViewName = findViewById(R.id.name);
        this.mBtnSubmit = findViewById(R.id.submit);

        // Bind listeners
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
