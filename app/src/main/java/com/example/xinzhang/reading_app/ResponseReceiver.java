package com.example.xinzhang.reading_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResponseReceiver extends BroadcastReceiver {

    public static final String ACTION_RESP =
            "com.mamlambo.intent.action.MESSAGE_PROCESSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra(networkTesting.PARAM_OUT_MSG);
        Log.i("receiver got:", text);

    }


}
