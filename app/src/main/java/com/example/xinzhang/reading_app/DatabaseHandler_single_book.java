package com.example.xinzhang.reading_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler_single_book extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "readingbook";

    public DatabaseHandler_single_book(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Contacts table name
    private static final String TABLE_CONTACTS = "readingBookTab";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_numberOfChapters = "numberOfChapters";
    private static final String KEY_fileLocation = "fileLocation";

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_numberOfChapters + " INTEGER," + KEY_fileLocation + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        System.out.println("Table is created");
    }

        void addBook(reading_book_table rt) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, rt.get_id());
            values.put(KEY_NAME, rt.get_name()); // Contact Name
            values.put(KEY_numberOfChapters, rt.getNumberOfChapters());
            values.put(KEY_fileLocation, rt.getFileLocation());

            // Inserting Row
            db.insert(TABLE_CONTACTS, null, values);
            db.close(); // Closing database connection
        }


    public List<reading_book_table> getAllContacts() {
        List<reading_book_table> contactList = new ArrayList<reading_book_table>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reading_book_table rt = new reading_book_table();
                rt.set_id(Integer.parseInt(cursor.getString(0)));
                rt.set_name(cursor.getString(1));
                rt.setNumberOfChapters(Integer.parseInt(cursor.getString(2)));
                rt.setFileLocation(cursor.getString(3));

                // Adding contact to list
                contactList.add(rt);
            } while (cursor.moveToNext());
        }
        // return contact list
        db.close();
        return contactList;
    }


    public void initReadingBookTable()
    {
        //init ludingji book
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, 100011);
        values.put(KEY_NAME, "鹿鼎记"); // Contact Name
        values.put(KEY_numberOfChapters, 50);
        values.put(KEY_fileLocation, "ludingji");

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);

        // init shujianenchoulu
        values.put(KEY_ID, 100012);
        values.put(KEY_NAME, "书剑恩仇录"); // Contact Name
        values.put(KEY_numberOfChapters, 20);
        values.put(KEY_fileLocation, "shujianenchoulu");

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        //onCreate(db);
        //Log.d("onUpgrade table exec..."," never updated table");

    }

    public void deletebook(reading_book_table rt)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(rt.get_id()) });
        db.close();
    }

    public StringBuffer showDataBaseinfo()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuffer str = new StringBuffer();

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                str.append(c.getString(0));
                c.moveToNext();
            }
        }
        db.close();
        return str;
    }

    public long getProfilesCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_CONTACTS);
        db.close();
        return count;
    }

    public String[] showTableInfo()
    {
        String[] str = new String[(int)getProfilesCount()];
        // be careful this database connection will be close if move behind connection-init

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT name FROM readingBookTab", null);

        int i = 0;
        if (c.moveToFirst()) {
            do {
                str[i] = c.getString(0);
                i++;
            } while (c.moveToNext());
        }

        db.close();
        return str;
    }

    public String showBookInfo(String sname)
    {
        // be careful this database connection will be close if move behind connection-init
        String result = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT fileLocation FROM readingBookTab where name = '" + sname +"'", null);
        if (c.moveToFirst()) {
            do {
                result = c.getString(0);
            } while (c.moveToNext());
        }

        db.close();
        return result;
    }



}
