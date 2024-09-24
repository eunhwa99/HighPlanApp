package com.MyDay.myday1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class How_Use extends AppCompatActivity {
    /**
     * 사용 방법 버튼 눌렀을 때 나오는 화면입니다.
     */
    ViewPager vp;
    int color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_slide);

        vp = (ViewPager)findViewById(R.id.viewPager);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        color = pref.getInt("key2", -8331542);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(color);
        }


    }

    View.OnClickListener movePageListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int tag = (int)v.getTag();
            vp.setCurrentItem(tag);
        }
    };


    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), colorchange.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();

    }

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
                case 3:
                    return new Fragment4();
                case 4:
                    return new Fragment5();
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 5;
        }
    }
}

