package com.stepin2it.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.i("OnMessageReceived()");
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Timber.i("Received messages : %s", remoteMessage.getData().toString());
        }
    }
}
