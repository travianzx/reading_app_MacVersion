package com.example.xinzhang.reading_app;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.IOException;

public class DownloadMoreBook extends AppCompatActivity {

    ProgressBar pg;
    ListView list;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_download_more_book);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String macaddress = wifiInfo.getMacAddress();
        System.out.println("Mac address: " + macaddress);

        list = findViewById(R.id.list);
        pg = findViewById(R.id.progressBarloadingbook);
        getBookListFromServer gB = new getBookListFromServer();
        gB.execute(new String[]{"for testing","testing again"});

    }

    private class getBookListFromServer extends AsyncTask<String[], Void, String[]>
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pg.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String[]... strings) {

            //step1
            //connect to server and retrieve info
            //booknamelist,uid,URL      String[] bookUID; String[] imageUrls; String[] bookNameList
            TranzClient tranz = new TranzClient();

            String [] s1= {"1","2"};
            return s1;
        }
        //for testing
        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            pg.setVisibility(ProgressBar.INVISIBLE);
            System.out.println("what we got is:  " + s[0]+" " + s[1]);
            String[] bookuid = {"open","ccc","download","open","download","open","ccc","download","open","download"} ;
            String[] imageurl = {"http://10.2.192.31/test.png","http://10.2.192.31/test.png",
                    "http://10.2.192.31/test.png","http://10.2.192.31/test.png","http://10.2.192.31/test.png","http://10.2.192.31/test.png","http://10.2.192.31/test.png",
                    "http://10.2.192.31/test.png","http://10.2.192.31/test.png","http://10.2.192.31/test.png"};
            String[] booknamelist = {"the one got rip","Mama","Dada","the longest one in title for testing","To be continued","the one got rip","Mama","Dada","the longest one in title for testing","To be continued"};

            //step2  //create listview
            CustomList adapter = new CustomList(context,imageurl,bookuid,booknamelist);
            list.setAdapter(adapter);


        }
    }



}
