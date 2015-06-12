package com.autodesk.icp.community.mobile.SQLiteDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.autodesk.icp.community.mobile.SQLiteDB.NotificationService;
import com.autodesk.icp.community.mobile.SQLiteDB.DbOpenHelper;
/**
 * Created by zengj on 5/21/2015.
 */
public class NotificationDao implements NotificationService {
    private DbOpenHelper helper = null;
    public static int REDFLAG = 1;
    public static int UNREDFLAG = 0;

    public NotificationDao(Context context) {
            helper = new DbOpenHelper(context);
    }

    @Override
    public boolean addNotification(Object[] params) {
            boolean flag = false;
            SQLiteDatabase database = null;
            try {
                    String sql = "insert into Notification(body, redflag) values(?,?)";
                    database = helper.getWritableDatabase();
                    database.execSQL(sql, params);
                    flag = true;
            } catch (Exception e) {
                    e.printStackTrace();
            } finally {
                    if (database != null) {

                        database.close();
                    }
            }
            return flag;
    }

    @Override
    public boolean deleteNotification(Object[] params) {
            boolean flag = false;
            SQLiteDatabase database = null;
            try {
                String sql = "delete from Notification where id=?";
                database = helper.getWritableDatabase();
                database.execSQL(sql, params);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (database != null) {
                database.close();
                }
            }
            return flag;
    }

    @Override
    public boolean updateNotification(Object[] params) {
            boolean flag = false;
            SQLiteDatabase database = null;
            try {

                String sql = "update Notification set redflag=? where id=?";
                database = helper.getWritableDatabase();

                database.execSQL(sql, params);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (database != null) {
                database.close();
                }
            }
            return flag;
    }

    @Override
    public Map<String, String> viewNotification(String[] selectionArgs) {
            Map<String, String> map = new HashMap<String, String>();
            SQLiteDatabase database = null;
            try {
                String sql = "select * from Notification where id=?";

                database = helper.getReadableDatabase();

                Cursor cursor = database.rawQuery(sql, selectionArgs);

                int colums = cursor.getColumnCount();
                while (cursor.moveToNext()) {
                    for (int i = 0; i < colums; i++) {
                        String cols_name = cursor.getColumnName(i);
                        String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));
                        if (cols_value == null) {
                                cols_value = "";
                        }
                        map.put(cols_name, cols_value);
                    }
                }
            } catch (Exception e) {
                    e.printStackTrace();
            } finally {
                    if (database != null) {
                            database.close();
                    }
            }
            return map;
    }

    @Override
    public List<Map<String, String>> listNotificationMaps(String[] selectionArgs) {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            String sql = "select * from Notification";
            SQLiteDatabase database = null;
            try {
                    database = helper.getReadableDatabase();
                    //Cursor cursor = database.rawQuery(sql, selectionArgs);
                    Cursor cursor = database.query("Notification", null,null,null,null,null,null);
                    int colums = cursor.getColumnCount();
                    while (cursor.moveToNext()) {
                        Map<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < colums; i++) {
                            String cols_name = cursor.getColumnName(i);
                            String cols_value = cursor.getString(cursor.getColumnIndex(cols_name));
                            if (cols_value == null) {
                                    cols_value = "";
                            }
                            map.put(cols_name, cols_value);
                        }
                        list.add(map);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
            } finally {
                    if (database != null) {
                        database.close();
                    }
            }
            return list;
    }
}
