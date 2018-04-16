package com.example.xinzhang.reading_app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class reading_book_Activity extends AppCompatActivity {

    private static final String TAG = "com.example.xinzhang.reading_app";
    TextView tv;
    ProgressBar pg;

    private void loadText() throws IOException {

        //final Resources resource = getApplicationContext().getResources();
        //InputStream in = resource.openRawResource(R.raw.ludingji_4);

        InputStream is = getAssets().open("ludingji/ludingji_4.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
            sb.append(line);
            }
            tv.setText(sb);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book_);
        tv = (TextView) findViewById(R.id.textView);
        pg = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Mybook_act.EXTRA_MESSAGE);
        tv.setText(message);

        // AsyncTask
        try {
            loadText();
        } catch (IOException e) {
            e.printStackTrace();
        }


        pg.setVisibility(View.INVISIBLE);


    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String>
    {


        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

}
