package com.autodesk.icp.community.mobile.fragement;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autodesk.icp.community.mobile.activity.NotificationDetails;
import com.autodesk.icp.community.mobile.activity.R;
import com.autodesk.icp.community.mobile.ui.NotificationCard;
import com.autodesk.icp.community.mobile.ui.NotificationRecyclerViewAdapter;
import com.autodesk.icp.community.mobile.util.RecyclerItemClickListener;
import com.autodesk.icp.community.mobile.util.SimpleDividerItemDecoration;
import com.autodesk.icp.community.mobile.util.SwipeDismissRecyclerViewTouchListener;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItem;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_tab_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

}
