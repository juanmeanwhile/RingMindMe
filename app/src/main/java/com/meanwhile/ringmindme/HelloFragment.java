package com.meanwhile.ringmindme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mengujua on 17/5/16.
 */
public class HelloFragment extends Fragment {

    public static HelloFragment newInstance() {
        HelloFragment f = new HelloFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hello, container, false);
        return v;
    }
}
