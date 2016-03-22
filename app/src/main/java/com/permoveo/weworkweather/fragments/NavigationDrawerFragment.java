package com.permoveo.weworkweather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.permoveo.weworkweather.R;

/**
 * Created by byfieldj on 3/18/16.
 */
public class NavigationDrawerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_nav_drawer, container, false);

        return v;
    }
}
