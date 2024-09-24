package com.MyDay.myday1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {
    public Fragment2(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.frag2, container, false);
        return layout;
    }
}
