package com.example.xinzhang.reading_app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.xinzhang.reading_app.books;

public class reading_book_Activity extends AppCompatActivity {

    private static final String TAG = "com.example.xinzhang.reading_app";
    TextView tv;
    ProgressBar pg;
    books book;
    public DatabaseHandler_single_book db;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;


    private void loadText(String s) throws IOException {

        //final Resources resource = getApplicationContext().getResources();
        //InputStream in = resource.openRawResource(R.raw.ludingji_4);

        InputStream is = getAssets().open(s+ "/" + s + "_1.txt");

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
        setContentView(R.layout.activity_reading_book_ );
        mDrawerList = (ListView)findViewById(R.id.navList);
        tv = (TextView) findViewById(R.id.textView);
        pg = (ProgressBar) findViewById(R.id.progressBar);
        Intent intent = getIntent();
        //System.out.println(intent.getExtras().getString("bookname"));
        String message = intent.getStringExtra(Mybook_act.EXTRA_MESSAGE);


        db = new DatabaseHandler_single_book(this);

        getbookAsyTask gt = new getbookAsyTask();
        gt.execute(message);
        pg.setVisibility(View.INVISIBLE);

    }

    private void addDrawerItems(String s) throws IOException {
        InputStream chapterlist = getAssets().open(s+ "/" + s + "_essayfile.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(chapterlist));
        List<String> sb = new ArrayList<String>();
        String line;
        int j=0;
        while ((line = reader.readLine()) != null) {
            sb.add(line);
            j++;
        }
        System.out.println(j);
        //String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        String osArray[] = new String[j];
        System.out.println("how many chapater:" + sb.size());
        for(int i =0; i < j ;i++)
        {
            osArray[i] = sb.get(i);
            System.out.println(osArray[i]);
        }

        mAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class getbookAsyTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("getbookAsyTask : the input value is "+ strings[0]);

            return db.showBookInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                addDrawerItems(s);
                System.out.println("addDrawerItems launched");
            }catch(IOException e)
            {
                System.out.println(e.getMessage());
            }

            try {
                 loadText(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pg.setVisibility(View.INVISIBLE);
        }
    }

}
