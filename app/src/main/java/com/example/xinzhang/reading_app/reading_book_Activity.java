package com.example.xinzhang.reading_app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private TextView textviewForChapterbutton;
    private Toolbar toolbarWithTwoTextView;
    private Button backbutton;
    private String chapterName;
    private ScrollView scrollView;

    private void loadText(String s) {

        //final Resources resource = getApplicationContext().getResources();
        //InputStream in = resource.openRawResource(R.raw.ludingji_4);
        //InputStream is = getAssets().open(s+ "/" + s + "_1.txt");
        File enduserFiledir = this.getFilesDir();
        FileInputStream ins = null;
        try {
            ins = new FileInputStream(enduserFiledir.getAbsolutePath() +"/"+"books"+"/"+ s + "/"+ s + "_1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
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

    private void loadSingleChapter(int n) throws IOException {

        //final Resources resource = getApplicationContext().getResources();
        //InputStream in = resource.openRawResource(R.raw.ludingji_4);
        InputStream is = getAssets().open(chapterName + "/" + chapterName + "_"+ n +".txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            scrollView.post(new Runnable()
                            {
                                @Override
                                public void run() {
                                    scrollView.scrollTo(0,0);
                                }
                            }

            );
            tv.setText(sb);
            backbutton.setVisibility(View.INVISIBLE);
            textviewForChapterbutton.setVisibility(View.VISIBLE);
            mDrawerList.setVisibility(View.INVISIBLE);
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
        toolbarWithTwoTextView = (Toolbar)findViewById(R.id.toolbar_profile);
        textviewForChapterbutton = (TextView)findViewById(R.id.textViewForChapter);
        backbutton = (Button) findViewById(R.id.button6);
        scrollView = (ScrollView) findViewById(R.id.scrollview);

        textviewForChapterbutton.setVisibility(View.VISIBLE);
        backbutton.setVisibility(View.INVISIBLE);
        mDrawerList.setVisibility(View.INVISIBLE);

        textviewForChapterbutton.setOnClickListener(clicktextviewForChapterbutton);
        backbutton.setOnClickListener(clickForBackbutton);
        tv.setOnClickListener(tvClickevent);
        Intent intent = getIntent();
        //System.out.println(intent.getExtras().getString("bookname"));
        String message = intent.getStringExtra(Mybook_act.EXTRA_MESSAGE);
        db = new DatabaseHandler_single_book(this);
        getbookAsyTask gt = new getbookAsyTask();
        gt.execute(message);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            |   View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            |   View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            |   View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            |   View.SYSTEM_UI_FLAG_FULLSCREEN
                            |   View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    private View.OnClickListener clickForBackbutton = new View.OnClickListener()
    {
        public void onClick(View v)
        {   // set viewlist to INVISIBLE and chapterbutton on
            if(textviewForChapterbutton.getVisibility()==View.INVISIBLE && mDrawerList.getVisibility()==View.VISIBLE) {
                textviewForChapterbutton.setVisibility(View.VISIBLE);
                mDrawerList.setVisibility(View.INVISIBLE);
                backbutton.setVisibility(View.INVISIBLE);
            }
        }
    };

    private View.OnClickListener clicktextviewForChapterbutton = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if(backbutton.getVisibility()==View.INVISIBLE && mDrawerList.getVisibility()==View.INVISIBLE)
            {
                backbutton.setVisibility(View.VISIBLE);
                mDrawerList.setVisibility(View.VISIBLE);
                textviewForChapterbutton.setVisibility(View.INVISIBLE);
                System.out.println("chapter button clicked ");
            }
        }

    };

    private View.OnClickListener tvClickevent = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if(mDrawerList.getVisibility()==View.VISIBLE && backbutton.getVisibility()==View.VISIBLE)
            {
                mDrawerList.setVisibility(View.INVISIBLE);
                backbutton.setVisibility(View.INVISIBLE);
                textviewForChapterbutton.setVisibility(View.VISIBLE);
            }
        }
    };

    private void addDrawerItems(String s) throws IOException {
        //InputStream chapterlist = getAssets().open(s+ "/" + s + "_essayfile.txt");

        FileInputStream ins = new FileInputStream(this.getFilesDir().getPath() +"/"+"books"+"/"+ s + "/"+ s + "_essayfile.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        List<String> sb = new ArrayList<String>();
        String line;
        int j = 0;
        // The J is number of chapters
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
                //Toast.makeText(getApplicationContext(), "Value is: " + position, Toast.LENGTH_SHORT).show();
                try {
                    loadSingleChapter(position+1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            chapterName = db.showBookInfo(strings[0]);
            return chapterName;
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

            loadText(s);
            pg.setVisibility(View.INVISIBLE);
        }
    }


}
