package com.autodesk.icp.community.mobile.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autodesk.icp.community.mobile.activity.R;

public class ItemView extends RelativeLayout {
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mTimestampTextView;
    private ImageView mImageView;

    public static ItemView inflate(ViewGroup parent) {
        ItemView itemView = (ItemView)LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,
                                                                                       parent,
                                                                                       false);
        return itemView;
    }

    public ItemView(Context c) {
        this(c, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.item_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        mTitleTextView = (TextView)findViewById(R.id.item_titleTextView);
        mDescriptionTextView = (TextView)findViewById(R.id.item_descriptionTextView);
        mTimestampTextView = (TextView)findViewById(R.id.item_timestampTextView);
        mImageView = (ImageView)findViewById(R.id.item_imageView);
    }

    public void setItem(Item item) {
        mTitleTextView.setText(item.getTitle());
        mDescriptionTextView.setText(item.getDescription());
        mTimestampTextView.setText(String.valueOf(item.getTimestamp()));
        // TODO: set up image URL
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public TextView getDescriptionTextView() {
        return mDescriptionTextView;
    }
}
