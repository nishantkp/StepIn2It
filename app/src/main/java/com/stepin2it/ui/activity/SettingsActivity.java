package com.stepin2it.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.stepin2it.R;
import com.stepin2it.data.PreferenceHelper;
import com.stepin2it.service.MusicPlayerService;
import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.txv_settings_user_name)
    TextView txvSettingsUserName;

    @BindView(R.id.btn_start_service)
    Button tvStartService;

    @BindView(R.id.btn_stop_service)
    Button tvStopService;

    @BindView(R.id.tv_show_action)
    TextView tvShowAction;

    // Intent filter
    private IntentFilter mIntentFilter;

    // Broadcast receiver
    private MyBroadcastReceiver myBroadcastReceiver;

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

        // Create an IntentFilter for broadcast receiver
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(IConstants.IActions.ACTION_PLAY);
        mIntentFilter.addAction(IConstants.IActions.ACTION_PREVIOUS);
        mIntentFilter.addAction(IConstants.IActions.ACTION_NEXT);

        myBroadcastReceiver = new MyBroadcastReceiver();
    }

    // Register broadcast receiver
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcastReceiver, mIntentFilter);
    }

    // Unregister broadcast receiver
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);
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

    // Start the service when user clicks on start service button from Settings layout
    @OnClick(R.id.btn_start_service)
    public void btnStartService() {
        Intent startMusicPlayer = new Intent(SettingsActivity.this, MusicPlayerService.class);
        startMusicPlayer.setAction(IConstants.IActions.START_MUSIC_PLAYER);
        startService(startMusicPlayer);
    }

    // Stop the service when user clicks on stop service button from Settings layout
    @OnClick(R.id.btn_stop_service)
    public void btnStopService() {
        Intent stopMusicPlayer = new Intent(SettingsActivity.this, MusicPlayerService.class);
        stopMusicPlayer.setAction(IConstants.IActions.STOP_MUSIC_PLAYER);
        stopService(stopMusicPlayer);
    }


    // Broadcast receiver class
    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Get the action from intent to take specific action upon specific broadcast
            String intentId = intent.getAction();
            if (intentId.equals(IConstants.IActions.ACTION_PLAY)) {
                Timber.i("Play music broadcast");
                tvShowAction.setText("PLAY MUSIC");
            } else if (intentId.equals(IConstants.IActions.ACTION_NEXT)) {
                Timber.i("Play next music broadcast");
                tvShowAction.setText("PLAY NEXT MUSIC");
            } else if (intentId.equals(IConstants.IActions.ACTION_PREVIOUS)) {
                Timber.i("Play previous music broadcast");
                tvShowAction.setText("PLAY PREVIOUS MUSIC");
            }
        }
    }
}
