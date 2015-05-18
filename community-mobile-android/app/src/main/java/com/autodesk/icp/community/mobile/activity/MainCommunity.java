package com.autodesk.icp.community.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.autodesk.icp.community.common.model.Notification;
import com.autodesk.icp.community.common.util.JSONReadWriteHelper;
import com.autodesk.icp.community.mobile.fragement.ForumFragment;
import com.autodesk.icp.community.mobile.fragement.NotificationsFragment;
import com.autodesk.icp.community.mobile.fragement.SettingsFragment;
import com.autodesk.icp.community.mobile.stomp.ListenerSubscription;
import com.autodesk.icp.community.mobile.stomp.ListenerWSNetwork;
import com.autodesk.icp.community.mobile.stomp.Stomp;
import com.autodesk.icp.community.mobile.stomp.Subscription;
import com.autodesk.icp.community.mobile.ui.BadgeView;
import com.autodesk.icp.community.mobile.ui.NotificationCard;
import com.autodesk.icp.community.mobile.util.NotificationBadgeManager;
import com.autodesk.icp.community.mobile.util.SessionManager;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainCommunity extends BaseActivity {

    public static MainCommunity instance = null;
    public static Stomp stomp = null;

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @InjectView(R.id.viewpagertab)
    SmartTabLayout smartTabLayout;

    private FragmentPagerItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_community);
        ButterKnife.inject(this);

        instance = this;

        adapter = new FragmentPagerItemAdapter(
                getFragmentManager(), FragmentPagerItems.with(this)
                .add("Notification", NotificationsFragment.class)
                .add("Forum", ForumFragment.class)
                .add("Settings", SettingsFragment.class)
                .create());

        viewPager.setAdapter(adapter);
        smartTabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter pager) {
                ImageView icon = (ImageView) LayoutInflater.from(MainCommunity.this).inflate(R.layout.layout_tab_icon, container, false);

                switch (position) {
                    case 0:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_white_24dp));
                        break;
                    case 1:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_people_white_24dp));
                        break;
                    case 2:
                        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on_white_24dp));
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }

                return icon;
            }
        });
        smartTabLayout.setViewPager(viewPager);


        connectToMsgBroker();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            Intent intent = new Intent();
            intent.setClass(MainCommunity.this, Exit.class);
            startActivity(intent);
        }

        return false;
    }

    public void exit_settings(View v) {
        Intent intent = new Intent(MainCommunity.this, ExitFromSettings.class);
        startActivity(intent);
    }

    private void connectToMsgBroker() {
        new Thread() {
            @Override
            public void run() {
                SessionManager sm = new SessionManager(MainCommunity.this);
                Map<String, String> headersSetup = new HashMap<String, String>();
                headersSetup.put("Cookie", "JSESSIONID=".concat(sm.getJSESSIONID()));
                String endpoint = MainCommunity.this.getConfigurationManager()
                        .getProperties("server.uri.ws")
                        .concat("/message");

                stomp = new Stomp(endpoint, headersSetup, new ListenerWSNetwork() {
                    @Override
                    public void onState(int state) {
                        if (Stomp.CONNECTED == state) {
                            subscribe();
                        }
                    }
                });

                Map<String, String> connectHeasers = new HashMap<String, String>();
                connectHeasers.put("client-id", sm.getUserDetails().getName());
                stomp.connect(connectHeasers);
            }
        }.start();

    }

    private void subscribe() {
        SessionManager sm = new SessionManager(MainCommunity.this);

        Map<String, String> subscribeHeasers = new HashMap<String, String>();
        String username = sm.getUserDetails().getName();
        subscribeHeasers.put("activemq.subscriptionName", username);

        stomp.subscribe(new Subscription("/topic/notification", new ListenerSubscription() {
                    @Override
                    public void onMessage(Map<String, String> headers, final String body) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                NotificationsFragment fragment = (NotificationsFragment) adapter.getPage(0);

                                NotificationCard card = new NotificationCard();
                                Notification notification = JSONReadWriteHelper.deSerializeJSON(body, Notification.class);
                                card.setTitle(notification.getTitle());
                                card.setDescription(notification.getDescription());
                                card.setTimestamp(notification.getTimestamp());

                                fragment.getAdpter().add(card, 0);

                                View target = findViewById(R.id.notification_head);
                                NotificationBadgeManager.getInstance().increse(target);
                            }
                        });
                    }
                }),
                subscribeHeasers);
    }
}
