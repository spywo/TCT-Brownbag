package com.autodesk.icp.community.mobile.SQLiteDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by zengj on 5/21/2015.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static String name = "TCTminiSocial.db";
    private static int version = 1;
    private SQLiteDatabase db;

    public DbOpenHelper(Context context) {
            super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String sql = "create table Notification(id integer primary key autoincrement,body varchar(2048), redflag integer)";
            this.db = db;
            db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            //String sql="alter table person add sex varchar(8)";
            //db.execSQL(sql);
    }
}
