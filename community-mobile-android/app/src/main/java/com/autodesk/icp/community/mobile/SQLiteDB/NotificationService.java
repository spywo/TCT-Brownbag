package com.autodesk.icp.community.mobile.SQLiteDB;

import java.util.List;
import java.util.Map;

/**
 * Created by zengj on 5/21/2015.
 */
public interface NotificationService {
    public boolean addNotification(Object[] params);
    public boolean deleteNotification(Object[] params);
    public boolean updateNotification(Object[] params);
    public Map<String, String> viewNotification(String[] selectionArgs);
    public List<Map<String, String>> listNotificationMaps(String[] selectionArgs);
}
