package com.autodesk.icp.community.mobile.stomp;

import java.util.Map;

public interface ListenerSubscription {
    public void onMessage(Map<String, String> headers, String body);
}