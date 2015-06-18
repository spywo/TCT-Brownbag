package com.autodesk.icp.community.mobile.fragement;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.autodesk.icp.community.mobile.activity.NotificationDetails;
import com.autodesk.icp.community.mobile.activity.R;
import com.autodesk.icp.community.mobile.ui.NotificationCard;
import com.autodesk.icp.community.mobile.ui.NotificationRecyclerViewAdapter;
import com.autodesk.icp.community.mobile.util.RecyclerItemClickListener;
import com.autodesk.icp.community.mobile.util.SimpleDividerItemDecoration;
import com.autodesk.icp.community.mobile.util.SwipeDismissRecyclerViewTouchListener;
import com.google.zxing.WriterException;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItem;
import com.zxing.activity.CaptureActivity;
import com.autodesk.icp.community.mobile.util.BitmapUtil;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class SettingsFragment extends Fragment {

    private ImageView iv_qr_image;
    private RelativeLayout tmpLayout;
    protected int mScreenWidth ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_tab_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ImageView iv = (ImageView)getActivity().findViewById(R.id.namecard_qr);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Create2QR(arg0);
            }
        });

        ImageView iv1 = (ImageView)getActivity().findViewById(R.id.codescanning);
        iv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                openBarCodeScanner();
            }
        });

        iv_qr_image = (ImageView)getActivity().findViewById(R.id.iv_qr_image);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;

        iv_qr_image.setVisibility(View.GONE);
        tmpLayout = (RelativeLayout)getActivity().findViewById(R.id.container_iv_qr_image);
        tmpLayout.setVisibility(View.GONE);

        ScrollView scrollView = (ScrollView)getActivity().findViewById(R.id.containerScrollView);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    iv_qr_image.setVisibility(View.GONE);
                    tmpLayout.setVisibility(View.GONE);
                }

                return false;
            }
        });
    }

    public void openBarCodeScanner(){
        //open Camera
        try{
            Intent openCameraIntent = new Intent(getActivity(),CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Create2QR(View v){
        String content = "Test";
//      Bitmap bitmap = BitmapUtil.create2DCoderBitmap(uri, mScreenWidth/2, mScreenWidth/2);
        Bitmap bitmap;
        try {
            bitmap = BitmapUtil.createQRCode(content, mScreenWidth);

            if(bitmap != null){
                iv_qr_image.setImageBitmap(bitmap);
                iv_qr_image.setVisibility(View.VISIBLE);
                tmpLayout.setVisibility(View.VISIBLE);
            }

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
