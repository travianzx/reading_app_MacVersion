package com.example.xinzhang.reading_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_INTERNET = 200;
    Button button_mybook;
    Button button_act2;
    Button button_act3;
    Button button_help;

    TextView topText;
    reading_book_table readingT;

    DatabaseHandler_single_book db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_mybook = (Button) findViewById(R.id.button2);
        button_mybook.setOnClickListener( clickButton );

        db = new DatabaseHandler_single_book(this);
        StringBuffer databaseStr = new StringBuffer();

/*        readingT = new reading_book_table(001+Result,"example01",98,"Location");
        db.addContact(readingT);*/


/*        List<reading_book_table> contacts = db.getAllContacts();
        for (reading_book_table cn : contacts) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,numberOfchapter: " + cn.getNumberOfChapters() + ", location: "+cn.getFileLocation();
            // Writing Contacts to log
            //Log.d("Name: ", log);
            System.out.println(log);
        }*/
        //db.initReadingBookTable();

        List<reading_book_table> contacts = db.getAllContacts();
        for(reading_book_table cn : contacts)
        {
            System.out.println("name: "+cn.get_name());
        }

        databaseStr = db.showDataBaseinfo();
        if(databaseStr.indexOf("readingBookTab") > 0)
        {
            System.out.println("Found the readingBookTab tablet");
            System.out.println("How many row: " + db.getProfilesCount());
            if(db.getProfilesCount() < 1)
            {
                db.initReadingBookTable();
                System.out.println("Init readingBookTab table launched");
            }
        }
    }


    private View.OnClickListener clickButton = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent act2 = new Intent(getApplicationContext(),Mybook_act.class);
            startActivity(act2);
        }

    };

    private void startWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Toast.makeText(MainActivity.this, "startCamera", Toast.LENGTH_SHORT).show();

        } else {
            // Permission is missing and must be requested.
            requestInternetPermission();
        }
    }


    //because some the deveices run Android 6 or even higher version,
    //Check For Permissions need to be implemented at run time.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == REQUEST_INTERNET) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(MainActivity.this, "INTERNET access permission was granted", Toast.LENGTH_SHORT).show();
                //startCamera();

            } else {
                // Permission request was denied.
                Toast.makeText(MainActivity.this, "INTERNET access permission was denied", Toast.LENGTH_SHORT).show();

            }


        }
        // END_INCLUDE(onRequestPermissionsResult)

    }

    private void requestInternetPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This permission is important to record audio.")
                .setTitle("Important permission required");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
            }
        });
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("OnPause exec....");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("OnResume exec....");
    }
}
