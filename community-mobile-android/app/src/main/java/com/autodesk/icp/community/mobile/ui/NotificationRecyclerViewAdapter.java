package com.autodesk.icp.community.mobile.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.autodesk.icp.community.mobile.activity.R;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private List<NotificationCard> notificationCards;

    public NotificationRecyclerViewAdapter(List<NotificationCard> cards) {
        this.notificationCards = cards;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_card, viewGroup, false);

        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(R.id.notifcation_swipeLayout);

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, v.findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        Button del = (Button)v.findViewById(R.id.notification_delete_button);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItemAtPosition(i));
            }
        });

        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NotificationViewHolder viewHolder, int pos) {
        final NotificationCard card = notificationCards.get(pos);

        viewHolder.mTitleTextView.setText(card.getTitle());
        viewHolder.mDesTextView.setText(card.getDescription());
        viewHolder.mDateTextView.setText(card.getTimestamp());
        if (card.isRead()) {
            viewHolder.setNotificationReaded();
        }
    }

    @Override
    public int getItemCount() {
        return notificationCards.size();
    }

    public void remove(NotificationCard card) {
        int position = getItemPosition(card);
        if (position != -1) {
            notificationCards.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(NotificationCard card, final int position) {
        notificationCards.add(position, card);
        notifyItemInserted(position);
    }

    public int getItemPosition(NotificationCard card) {
        return notificationCards.indexOf(card);
    }

    public NotificationCard getItemAtPosition(int position) {
        return notificationCards.get(position);
    }

    public void refresh() {
        notifyDataSetChanged();
    }
}