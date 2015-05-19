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
import android.widget.TextView;

import com.autodesk.icp.community.mobile.activity.NotificationDetails;
import com.autodesk.icp.community.mobile.activity.R;
import com.autodesk.icp.community.mobile.ui.NotificationCard;
import com.autodesk.icp.community.mobile.ui.NotificationRecyclerViewAdapter;
import com.autodesk.icp.community.mobile.util.NotificationBadgeManager;
import com.autodesk.icp.community.mobile.util.RecyclerItemClickListener;
import com.autodesk.icp.community.mobile.util.SimpleDividerItemDecoration;
import com.autodesk.icp.community.mobile.util.SwipeDismissRecyclerViewTouchListener;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItem;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class NotificationsFragment extends Fragment {
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_tab_notification, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.notification_recycleview);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(1000);
        mRecyclerView.getItemAnimator().setRemoveDuration(400);
        mRecyclerView.getItemAnimator().setMoveDuration(1000);
        mRecyclerView.getItemAnimator().setChangeDuration(1000);

        final NotificationRecyclerViewAdapter recyclerViewAdapter = new NotificationRecyclerViewAdapter(new ArrayList<NotificationCard>());
        mRecyclerView.setAdapter(recyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        for (View childView : view.getTouchables()) {
                            if (childView.getId() == R.id.notification_delete_button) {
                                NotificationBadgeManager.getInstance().decrease();
                                recyclerViewAdapter.remove(recyclerViewAdapter.getItemAtPosition(position));
                                return;
                            }
                        }

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), NotificationDetails.class);
                                NotificationCard nc = getAdpter().getItemAtPosition(position);
                                nc.setIsRead(true);
                                getAdpter().refresh();

                                NotificationBadgeManager.getInstance().decrease();

                                intent.putExtra("details", nc.getDescription());
                                startActivity(intent);
                            }
                        });
                    }
                })
        );
        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    NotificationCard nc = getAdpter().getItemAtPosition(position);
                                    getAdpter().remove(nc);
                                    if (!nc.isRead()) {
                                        NotificationBadgeManager.getInstance().decrease();
                                    }
                                }
                            }
                        });
//        mRecyclerView.setOnTouchListener(touchListener);
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public NotificationRecyclerViewAdapter getAdpter() {
        return ((NotificationRecyclerViewAdapter) getmRecyclerView().getAdapter());
    }
}
