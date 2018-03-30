package sprobe.training.miniproject.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import data.Garbage;
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
            if (name.length() < Garbage.NAME_LENGTH_MIN
                    || name.length() > Garbage.NAME_LENGTH_MAX) {
                return Garbage.ERROR_NAME_LENGTH();
            } else {
                return "";
            }
        }

        private int storeList(String name) {
            return Garbage.store(new Garbage(name));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);
        Util.addToolbar(this, true);

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
