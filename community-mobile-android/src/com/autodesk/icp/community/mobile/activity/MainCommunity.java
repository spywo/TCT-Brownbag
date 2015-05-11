package com.autodesk.icp.community.mobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.autodesk.icp.community.common.model.ServiceResponse;
import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.util.JSONReadWriteHelper;
import com.autodesk.icp.community.mobile.stomp.ListenerSubscription;
import com.autodesk.icp.community.mobile.stomp.ListenerWSNetwork;
import com.autodesk.icp.community.mobile.stomp.Stomp;
import com.autodesk.icp.community.mobile.stomp.Subscription;
import com.autodesk.icp.community.mobile.ui.Item;
import com.autodesk.icp.community.mobile.ui.ItemListFragment;
import com.autodesk.icp.community.mobile.util.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;

public class MainCommunity extends BaseActivity {

    public static MainCommunity instance = null;

    private ViewPager mTabPager;
    private ImageView mTabImg;
    private ImageView mTab1, mTab2, mTab3, mTab4;
    private int zero = 0;
    private int currIndex = 0;
    private int one;
    private int two;
    private int three;
    private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;
    private boolean menu_display = false;
    private PopupWindow menuWindow;
    private LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_community);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        instance = this;

        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mTab1 = (ImageView)findViewById(R.id.img_notification);
        mTab2 = (ImageView)findViewById(R.id.img_address);
        mTab3 = (ImageView)findViewById(R.id.img_friends);
        mTab4 = (ImageView)findViewById(R.id.img_settings);
        mTabImg = (ImageView)findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));

        Display currDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        currDisplay.getSize(size);
        int displayWidth = size.x;
        one = displayWidth / 4;
        two = one * 2;
        three = one * 3;

        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.main_tab_notification, null);
        View view2 = mLi.inflate(R.layout.main_tab_address, null);
        View view3 = mLi.inflate(R.layout.main_tab_friends, null);
        View view4 = mLi.inflate(R.layout.main_tab_settings, null);

        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);

        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(views.get(position));
                return views.get(position);
            }
        };

        mTabPager.setAdapter(mPagerAdapter);

        subscribe();
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mTabPager.setCurrentItem(index);
        }
    };

    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                        mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                        mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                        mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
                    }
                    break;
                case 1:
                    mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, one, 0, 0);
                        mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                        mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                        mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
                    }
                    break;
                case 2:
                    mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, two, 0, 0);
                        mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                        mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                        mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
                    }
                    break;
                case 3:
                    mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, three, 0, 0);
                        mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                        mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                        mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(150);
            mTabImg.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            if (menu_display) {
                menuWindow.dismiss();
                menu_display = false;
            } else {
                Intent intent = new Intent();
                intent.setClass(MainCommunity.this, Exit.class);
                startActivity(intent);
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!menu_display) {

                inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.main_menu, null);

                menuWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                menuWindow.showAtLocation(this.findViewById(R.id.maincommunity), Gravity.BOTTOM
                                                                                 | Gravity.CENTER_HORIZONTAL, 0, 0);
                mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
                mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);

                mCloseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        Intent intent = new Intent();
                        intent.setClass(MainCommunity.this, Exit.class);
                        startActivity(intent);
                        menuWindow.dismiss();
                    }
                });
                menu_display = true;
            } else {
                menuWindow.dismiss();
                menu_display = false;
            }

            return false;
        }
        return false;
    }

    public void btnmainright(View v) {
        Intent intent = new Intent(MainCommunity.this, MainTopRightDialog.class);
        startActivity(intent);
    }

    public void startchat(View v) {
        Intent intent = new Intent(MainCommunity.this, ChatActivity.class);
        startActivity(intent);
    }

    public void exit_settings(View v) {
        Intent intent = new Intent(MainCommunity.this, ExitFromSettings.class);
        startActivity(intent);
    }

    public void btn_shake(View v) {
        Intent intent = new Intent(MainCommunity.this, ShakeActivity.class);
        startActivity(intent);
    }

    private void subscribe() {
        new Thread() {
            /*
             * (non-Javadoc)
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                SessionManager sm = new SessionManager(MainCommunity.this);
                Map<String, String> headersSetup = new HashMap<String, String>();
                headersSetup.put("Cookie", "JSESSIONID=".concat(sm.getJSESSIONID()));
                Stomp stomp = new Stomp("ws://10.148.202.55:8080/community/message",
                                        headersSetup,
                                        new ListenerWSNetwork() {
                                            @Override
                                            public void onState(int state) {
                                                // TODO Auto-generated method stub

                                            }
                                        });

                Map<String, String> connectHeasers = new HashMap<String, String>();
                connectHeasers.put("client-id", sm.getUserDetails().getName());
                stomp.connect(connectHeasers);

                Map<String, String> subscribeHeasers = new HashMap<String, String>();
                subscribeHeasers.put("activemq.subscriptionName", sm.getUserDetails().getName());
                stomp.subscribe(new Subscription("/topic/notification", new ListenerSubscription() {

                    @Override
                    public void onMessage(Map<String, String> headers, final String body) {

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                ItemListFragment lf = (ItemListFragment)getSupportFragmentManager().findFragmentById(R.id.notificationList);
                                Item item = JSONReadWriteHelper.deSerializeJSON(body, Item.class);
                                lf.getItems().add(0, item);

                                lf.getAdpater().notifyDataSetChanged();

                            }
                        });

                    }
                }),
                                subscribeHeasers);
            }
        }.start();

    }
}
