package com.root.testnotif;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseToken extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.d("TEST", "onTokenRefresh: " + FirebaseInstanceId.getInstance().getToken());
        SharedPref.getInstance(getApplicationContext())
                .put(SharedPref.Key.STRING_FIREBASE_TOKEN, FirebaseInstanceId.getInstance().getToken());

        Intent broadcast = new Intent("firebase-token");
        broadcast.putExtra("update-ui", true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
    }
}
