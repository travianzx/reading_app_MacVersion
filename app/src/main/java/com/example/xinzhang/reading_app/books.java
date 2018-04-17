package com.example.xinzhang.reading_app;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class books {


    private int uid;
    private String name;
    private String[] chapters;

    public books(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChapters(String[] chapters) {
        this.chapters = chapters;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String[] getChapters() {
        return chapters;
    }




}
