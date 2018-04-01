package com.stepin2it.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stepin2it.R;
import com.stepin2it.data.PreferenceHelper;
import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.txv_settings_user_name)
    TextView txvSettingsUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Use butter-knife to bind the views
        ButterKnife.bind(SettingsActivity.this);

        // Get the user name from the stored preferences and set the text
        String userName =
                PreferenceHelper.getInstance(SettingsActivity.this).readString(IConstants.IPreference.PREF_USER_NAME);
        txvSettingsUserName.setText(userName);
    }

    /**
     * Start {@link LoginActivity} when user clicks on Logout option
     */
    @OnClick(R.id.btn_settings_logout)
    public void btnSettingsLogout() {
        // Remove all stores preferences
        PreferenceHelper.getInstance(SettingsActivity.this).logout();
        Intent logoutIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        // Destroy all the previous activities
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        // Finish current activity
        finish();
    }
}
