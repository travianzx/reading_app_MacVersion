package com.example.xinzhang.reading_app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

//import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class networkTesting extends IntentService
{

    private static final String TAG = "com.example.xinzhang.reading_app";
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG= "OSMG";



    public networkTesting() {
        super("networkTesting");
    }

    @Override
    protected void onHandleIntent(Intent intent) {



        Log.i("IntentService launched", "Executing work: " + intent);
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("Job done", "Done: " + intent);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(PARAM_OUT_MSG, "the result from networkTesting");
        sendBroadcast(broadcastIntent);

    }

}
