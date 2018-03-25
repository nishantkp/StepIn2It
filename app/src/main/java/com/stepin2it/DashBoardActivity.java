package com.stepin2it;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity {
    @BindView(R.id.txt_user_email_id) TextView txtUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(DashBoardActivity.this);

        txtUserEmailId = findViewById(R.id.txt_user_email_id);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IConstants.IBundle.BUNDLE_USER_NAME)) {
            String emailAddress = intent.getStringExtra(IConstants.IBundle.BUNDLE_USER_NAME);
            txtUserEmailId.setText(emailAddress);
        }
    }
}
