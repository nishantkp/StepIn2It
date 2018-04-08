package com.stepin2it.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.stepin2it.R;
import com.stepin2it.ui.activity.SplashScreenActivity;
import com.stepin2it.utils.IConstants;

import timber.log.Timber;

public class MusicPlayerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String serviceId = intent.getAction();
        if (serviceId.equals(IConstants.IActions.START_MUSIC_PLAYER)) {
            initForegroundService();
        } else if (serviceId.equals(IConstants.IActions.ACTION_PLAY)) {
            // Send the intent through broadcast
            Intent broadcastPlayIntent = new Intent();
            broadcastPlayIntent.setAction(IConstants.IActions.ACTION_PLAY);
            sendBroadcast(broadcastPlayIntent);
            Timber.i("Play Music");
        } else if (serviceId.equals(IConstants.IActions.ACTION_NEXT)) {
            Intent broadcastNextIntent = new Intent();
            broadcastNextIntent.setAction(IConstants.IActions.ACTION_NEXT);
            sendBroadcast(broadcastNextIntent);
            Timber.i("Play Next Music");
        } else if (serviceId.equals(IConstants.IActions.ACTION_PREVIOUS)) {
            Intent broadcastPreviousIntent = new Intent();
            broadcastPreviousIntent.setAction(IConstants.IActions.ACTION_PREVIOUS);
            sendBroadcast(broadcastPreviousIntent);
            Timber.i("Play Previous Music");
        } else if (serviceId.equals(IConstants.IActions.STOP_MUSIC_PLAYER)) {
            stopForeground(true);
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // Initialize foreground service to display Notification
    private void initForegroundService() {
        Timber.i("Received Start Foreground Intent ");
        /**
         * PendingIntent to open {@link SplashScreenActivity} when user clicks on notification
         */
        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        notificationIntent.setAction(IConstants.IActions.ACTION_MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        /**
         * PendingIntent to open {@link MusicPlayerService} when user clicks on previous music
         * button
         */
        Intent previousIntent = new Intent(this, MusicPlayerService.class);
        previousIntent.setAction(IConstants.IActions.ACTION_PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        /**
         * PendingIntent to open {@link MusicPlayerService} when user clicks on play music
         * button
         */
        Intent playIntent = new Intent(this, MusicPlayerService.class);
        playIntent.setAction(IConstants.IActions.ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        /**
         * PendingIntent to open {@link MusicPlayerService} when user clicks on next music
         * button
         */
        Intent nextIntent = new Intent(this, MusicPlayerService.class);
        nextIntent.setAction(IConstants.IActions.ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        // Notification builder
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Music Player")
                .setTicker("Music Player")
                .setContentText("My Music")
                .setSmallIcon(R.drawable.ic_internet)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous,
                        "Previous", previousPendingIntent)
                .addAction(android.R.drawable.ic_media_play, "Play",
                        playPendingIntent)
                .addAction(android.R.drawable.ic_media_next, "Next",
                        nextPendingIntent).build();
        startForeground(IConstants.INotification.ID_MUSIC_NOTIFICATION,
                notification);
    }
}
