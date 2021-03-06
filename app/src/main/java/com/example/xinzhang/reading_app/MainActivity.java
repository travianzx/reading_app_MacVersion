package com.example.xinzhang.reading_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_INTERNET = 200;
    private static final int REQUEST_INTERNALSTORAGE = 300;
    private static final int REQUEST_WIFISTATUS = 300;

    private String FILEPATH;

    Button button_mybook;
    Button button_act2;
    Button button_setting;
    Button button_help;

    TextView topText;

    DatabaseHandler_single_book db;

    private ResponseReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_mybook = (Button) findViewById(R.id.button2);
        button_setting = (Button) findViewById(R.id.button3);

        button_mybook.setOnClickListener( clickButton );
        button_setting.setOnClickListener(settingButton);
        startWeather();

        //InputStream in_text = getAssets().open("shujianenchoulu" + "/" + "shujianenchoulu_1.txt");

        inti_readingAppForInternalStorage();

        db = new DatabaseHandler_single_book(this);
        StringBuffer databaseStr = new StringBuffer();

/*       List<reading_book_table> contacts = db.getAllContacts();
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

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
        Intent msgIntent = new Intent(this, networkTesting.class);
        msgIntent.putExtra(networkTesting.PARAM_IN_MSG, "theStringfromMainclass");
        startService(msgIntent);

    }


    private View.OnClickListener clickButton = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent act2 = new Intent(getApplicationContext(),Mybook_act.class);
            act2.putExtra("url_book",FILEPATH);
            startActivity(act2);
        }

    };

    private View.OnClickListener settingButton = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent act3 = new Intent(getApplicationContext(),reading_setting.class);
            startActivity(act3);
        }

    };

    // check user permission include :Internet,
    private void startWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            //Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();

        } else {
            // Permission is missing and must be requested.
            requestInternetPermission();
        }

    }

    private boolean inti_readingAppForInternalStorage() {
        // 1. check internal storasge usage make sure it is enough for all books which will take more than 50 MB .
        // if smaller than 10 MB, app stop.
        // 2. check config file exsited or not make one if not here, also check whether inti-books stored or not.
        // 3.

        startWeather();
        File enduserFiledir = this.getFilesDir();
        System.out.println("End user file :" + enduserFiledir.getAbsolutePath());
        if(enduserFiledir == null)
        {
            System.out.println("enduserFiledir is null" + "ERROR ~~ ");
            //this.onStop();
        }

        FILEPATH = enduserFiledir.getAbsolutePath();
        //String filelist[] = enduserFiledir.list();
        System.out.println("Total space size :" + enduserFiledir.getTotalSpace()/8/1024/1024);
        System.out.println("Total Usablespace size :" + enduserFiledir.getUsableSpace()/8/1024/1024);

        if(enduserFiledir.getUsableSpace()/8/1024/1024 < 10)
        {
            Toast.makeText(MainActivity.this,"Low internal storage",Toast.LENGTH_SHORT).show();
            this.onStop();
        }else
        {   //create config file or edit it
            ///data/user/0/com.example.xinzhang.reading_app/files/ludingji/ludingji_1.txt
            File configFile = new File (FILEPATH + "appUserConfig.ini");
            if(configFile.exists())
            {   //beware that this If-else condition does not check init-book folders and content.
                System.out.println("the config exsited, init finshed");

                return true;
            }else
            {
                System.out.println("the config missing");
                try {
                        configFile.createNewFile();
                        System.out.println(", then the config file created by method inti_readingAppForInternalStorage");

                } catch (IOException e) {
                        e.printStackTrace();
                }
                if(configFile.canRead() && configFile.canWrite())
                {
                    System.out.println("Config file is readable and writable");
                    if(new File(FILEPATH + "/books" + "ludingji").isDirectory() && new File(FILEPATH + "/books" + "shujianenchoulu").isDirectory())
                    {
                        System.out.println("inti books folder found ");
                    }else
                    {
                        new File(FILEPATH+"/books").mkdir();
                        System.out.println("Books folder creating");
                        AssetManager assetManager = getAssets();
                        //create 2 folder for ludingji , shujianenchoulu if those two not existed
                        if(new File(FILEPATH+"/books"+"/ludingji").mkdir() && new File(FILEPATH+"/books"+"/shujianenchoulu").mkdir())
                        {
                            try {
                                init_createTwoBooks(FILEPATH+"/books"+"/ludingji",FILEPATH+"/books"+"/shujianenchoulu");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if(new File(FILEPATH+"/books").exists())
                    { System.out.println("Books folder created, ");     }

                    return true;
                }
            }
        }
        return false;
    }

    //because some the deveices run Android 6 or even higher version,
    //Check For Permissions need to be implemented at run time.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == REQUEST_INTERNALSTORAGE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(MainActivity.this, "internal storage access permission was granted", Toast.LENGTH_SHORT).show();
                //startCamera();

            } else {
                // Permission request was denied.
                Toast.makeText(MainActivity.this, "Internal storage permission was denied, app installation  or Application quit", Toast.LENGTH_SHORT).show();
                super.onStop();
            }

        }
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

    private void requestWIFIStatusPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This permission is important to access to Network status.")
                .setTitle("Important permission required");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_WIFISTATUS);
            }
        });
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
    }

    private void init_createTwoBooks(String url_ludingji,String url_shujianenchoulu) throws IOException
    {
        String [] booklist_lu = getAssets().list("ludingji");
        String [] booklist_shu = getAssets().list("shujianenchoulu");
        for(String lu : booklist_lu)
        {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = getAssets().open("ludingji/"+lu);
                File outFile = new File(url_ludingji, lu);
                System.out.println("ludingji Path:" + outFile.toURI().getPath());

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + lu, e);
            }
        }
        for(String shu : booklist_shu)
        {
            InputStream in2 = null;
            OutputStream out2 = null;
            try {
                in2 = getAssets().open("shujianenchoulu/"+shu);
                File outFile = new File(url_shujianenchoulu, shu);
                System.out.println("shujianenchoulu Path:" + outFile.toURI().getPath());
                out2 = new FileOutputStream(outFile);

                copyFile(in2, out2);
                in2.close();
                in2 = null;
                out2.flush();
                out2.close();
                out2 = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + shu, e);
            }
        }
        System.out.println("Two books init finished");
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[32768];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
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
