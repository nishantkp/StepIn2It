package com.stepin2it;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stepin2it.utils.IConstants;
import com.stepin2it.utils.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity {
    @BindView(R.id.txt_user_email_id)
    TextView txtUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(DashBoardActivity.this);

        txtUserEmailId = findViewById(R.id.txt_user_email_id);

        String userName
                = PreferenceHelper.getInstance(DashBoardActivity.this).readString(IConstants.IPreference.PREF_USER_NAME);
        txtUserEmailId.setText(userName);
    }
}
