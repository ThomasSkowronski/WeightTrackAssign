package com.example.weighttracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;

public class databaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "weightEntries";
    private static final int DB_VER = 1;
    private static final String TABLE = "entries";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String WEIGHT = "weight";

    public databaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "create table "+TABLE+"( "+ID;
        sqlCreate += " integer primary key autoincrement, "+DATE;
        sqlCreate += " int, "+WEIGHT+" real )";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE);
        onCreate(db);
    }

    public void insert (Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlIns = "insert into "+TABLE;
        sqlIns +=" values( null, " + entry.getDate();
        sqlIns +=", "+entry.getWeight()+" )";

        db.execSQL(sqlIns);
        db.close();
    }

    public void deleteId (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlDel = "delete from "+TABLE;
        sqlDel += " where "+ID+" = "+id;

        db.execSQL(sqlDel);
        db.close();
    }

    //public void updateID () { }

    public ArrayList<Entry> selectAll() {
        String sqlSel = "select * from " + TABLE + " order by "+DATE+" desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery( sqlSel, null);

        ArrayList<Entry> entries = new ArrayList<Entry>();
        while (c.moveToNext()) {
            Entry currentEntry = new Entry(Integer.parseInt(c.getString(0) ), Long.parseLong(c.getString(1)), c.getDouble(2));
            entries.add(currentEntry);
        }
        db.close();
        return entries;
    }

    public Entry selectId (int id) {
        String sqlSel = "select * from "+TABLE;
        sqlSel += " where "+ID+" = "+id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sqlSel, null);

        Entry entry = null;
        if (c.moveToFirst()) {
            entry = new Entry(Integer.parseInt(c.getString(0)) , Long.parseLong(c.getString(1)), c.getDouble(2));
        }

        return entry;
    }

    public Entry mostRecent () {
        String sqlSel = "select * from "+TABLE;
        sqlSel += " order by "+DATE+" desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sqlSel, null);

        Entry entry = null;
        if (c.moveToFirst()) {
            entry = new Entry(Integer.parseInt(c.getString(0)) , Long.parseLong(c.getString(1)), c.getDouble(2));
        }

        return entry;
    }
}
