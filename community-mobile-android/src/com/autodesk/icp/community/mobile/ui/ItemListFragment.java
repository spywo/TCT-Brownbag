package com.autodesk.icp.community.mobile.ui;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ItemListFragment extends ListFragment {
    private final ArrayList<Item> items = new ArrayList<Item>();

    private ArrayAdapter<Item> adpater = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        adpater = new ItemAdapter(getActivity(), items);
        setListAdapter(adpater);

        return v;
    }

    /**
     * @return the adpater
     */
    public ArrayAdapter<Item> getAdpater() {
        return adpater;
    }

    /**
     * @param adpater
     *            the adpater to set
     */
    public void setAdpater(ArrayAdapter<Item> adpater) {
        this.adpater = adpater;
    }

    /**
     * @return the items
     */
    public ArrayList<Item> getItems() {
        return items;
    }
}
