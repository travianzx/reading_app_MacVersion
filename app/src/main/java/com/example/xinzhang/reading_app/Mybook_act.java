package com.example.xinzhang.reading_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Mybook_act extends AppCompatActivity {

    private ListView mListView;
    public final static String EXTRA_MESSAGE = "bookname";
    ProgressBar pb;
    public DatabaseHandler_single_book db;
    String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook_act);
        mListView = (ListView) findViewById(R.id.book_list_view);
        pb = (ProgressBar) findViewById(R.id.progressBarForMybookAct);
        db = new DatabaseHandler_single_book(this);
        loadingInfo loading = new loadingInfo();
        loading.execute();

    }

    class loadingInfo extends AsyncTask<Void, Void, String[]>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(Void... voids) {

            return db.showTableInfo();
        }

        @Override
        protected void onProgressUpdate(Void... voids) {


        }

        @Override
        protected void onPostExecute(String[] sb) {
            //super.onPostExecute(sb);
            pb.setVisibility(View.INVISIBLE);


            ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, sb);
            values = sb;
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                Intent i = new Intent(getApplicationContext(), reading_book_Activity.class);

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    //Toast.makeText(Mybook_act.this, "" + position, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Mybook_act.this, values[position], Toast.LENGTH_SHORT).show();
                    i.putExtra(EXTRA_MESSAGE, values[position]);
                    startActivity(i);

                }

            });

        }
    }


}
