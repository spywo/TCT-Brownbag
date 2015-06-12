package com.autodesk.icp.community.mobile.util;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationManager;

import com.autodesk.icp.community.common.util.JSONReadWriteHelper;
import com.autodesk.icp.community.mobile.activity.MainCommunity;
import com.autodesk.icp.community.mobile.activity.R;
import com.autodesk.icp.community.mobile.fragement.NotificationsFragment;
import com.autodesk.icp.community.mobile.stomp.ListenerSubscription;
import com.autodesk.icp.community.mobile.stomp.Subscription;
import com.autodesk.icp.community.mobile.ui.NotificationCard;
import com.autodesk.icp.community.mobile.util.SessionManager;
import com.autodesk.icp.community.mobile.util.MyApp;
import com.autodesk.icp.community.mobile.stomp.Stomp;
import com.autodesk.icp.community.mobile.SQLiteDB.DbOpenHelper;
import com.autodesk.icp.community.mobile.SQLiteDB.NotificationService;
import com.autodesk.icp.community.mobile.SQLiteDB.NotificationDao;

import java.util.HashMap;
import java.util.Map;

public class ReceiverService extends Service {
    private static final String TAG = "ReceiverService";
    private Context context;
    private MyApp app;
    private Stomp stomp;
    public static int FLAG = 1;

    public ReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        //Toast.makeText(this, "Receiver Service created", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onCreate");
        ///////
        context = getApplicationContext();
        //////

    }

    public void CreateInform(String body) {
        Intent intent = new Intent(context,MainCommunity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //??????
        Notification notification = new Notification(R.drawable.ic_app_logo, "content", System.currentTimeMillis());
        notification.setLatestEventInfo(context, "click to check", "click to check detail", pendingIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(100, notification);
    }

    public void NotifyParentActivity(String body){
        Message msg = new Message();
        msg.what = FLAG;
        msg.obj = body;
        MainCommunity.handler.sendMessage(msg);
    }

    public void writeDB(String body){
        NotificationService nService = new NotificationDao(ReceiverService.this);
        Object[] row = {body, NotificationDao.UNREDFLAG};
        nService.addNotification(row);
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Receiver Service Stoped", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onDestroy");

    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(this, "Receiver Service Start", Toast.LENGTH_LONG).show();
        Log.i(TAG, "onStart");

        ////////////////
        SessionManager sm = new SessionManager(ReceiverService.this);
        Map<String, String> subscribeHeasers = new HashMap<String, String>();
        String username = sm.getUserDetails().getName();
        subscribeHeasers.put("activemq.subscriptionName", username);
/*        MainCommunity.stomp.
        app = (MyApp)getApplication();
        stomp = app.getStomp();*/
        MainCommunity.stomp.subscribe(new Subscription("/topic/notification", new ListenerSubscription() {
                    @Override
                    public void onMessage(Map<String, String> headers, final String body) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                CreateInform(body);
                                NotifyParentActivity(body);
                                writeDB(body);
                            }
                        });
                    }
                }),
                subscribeHeasers);
        ///////////////

    }
}
