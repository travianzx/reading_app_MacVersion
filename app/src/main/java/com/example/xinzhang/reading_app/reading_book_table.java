package com.example.xinzhang.reading_app;

import android.database.sqlite.SQLiteOpenHelper;
public class reading_book_table {

    int _id;
    String _name;
    int numberOfChapters;
    String fileLocation;

    /*
         Class for single book
    */

    public reading_book_table()
    {

    }

    public reading_book_table(int id, String name, int nc, String fL)
    {
        this._id = id;
        this._name = name;
        this.numberOfChapters = nc;
        this.fileLocation = fL;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void setNumberOfChapters(int numberOfChapters) {
        this.numberOfChapters = numberOfChapters;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public int getNumberOfChapters() {
        return numberOfChapters;
    }

    public String getFileLocation() {
        return fileLocation;
    }



}
