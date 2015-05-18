package com.autodesk.icp.community.mobile.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.autodesk.icp.community.mobile.activity.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleTextView;
    TextView mDesTextView;
    TextView mDateTextView;
    ImageView mReadIndicatorImageView;

    public NotificationViewHolder(View v) {
        super(v);

        this.mTitleTextView = (TextView) v.findViewById(R.id.notification_title);
        this.mDateTextView = (TextView) v.findViewById(R.id.notification_time);
        this.mDesTextView = (TextView) v.findViewById(R.id.notification_desc);
        this.mReadIndicatorImageView = (ImageView)v.findViewById(R.id.notification_read_icon);

        this.mReadIndicatorImageView.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_redpoint));
    }


    public void setNotificationReaded() {
        this.mReadIndicatorImageView.setImageDrawable(null);
    }
}