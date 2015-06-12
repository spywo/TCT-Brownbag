package com.autodesk.icp.community.mobile.util;

import android.app.Application;
import com.autodesk.icp.community.mobile.stomp.Stomp;
/**
 * Created by zengj on 5/20/2015.
 */
public class MyApp extends Application {
    private Stomp stomp;

    public Stomp getStomp(){
        return stomp;
    }

    public void setStomp(Stomp stmp){
        stomp = stmp;
    }
}
