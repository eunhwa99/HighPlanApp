package com.MyDay.myday1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Fragment1 extends Fragment {
    /**
     * Fragment1~Fragment5: 사용 방법 버튼 눌렀을 때 나오는 화면 구현한 건데, 이미지를 넘기는 방식을 구현하기 위해서 작성하였습니다.
     */
    public Fragment1(){}
    int color;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
       LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.frag1, container, false);
        return layout;
    }
}
