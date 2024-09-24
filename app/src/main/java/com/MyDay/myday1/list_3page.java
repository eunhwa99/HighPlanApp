package com.MyDay.myday1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
ArrayList<String> keys

키값 불러오기:
keys = PreferenceManager.getArray(this, "key_list");

키값 저장하기:
PreferenceManager.setArray(this, "key_list", keys);

cf)key값들 저장한 데이터베이스 키값은 "key_list"루 했다
cf)key값들은 String형태로 저장함
cf)나머지는 평소 ArrayList사용하는것처럼 사용하면 됨
 */

public class list_3page extends Activity {
    /**
     * 일기 리스트
     */
    private Context context;
    ListView listview;
    FrameLayout frame;
    List<Data> lists;
    Button btn4, btn2, btn1, btn3;
    FloatingActionButton fab;
    String date1="", date2="";
    int day=0, emotii=0, emojic=0, day_write=0, year_a, month_a, day_a, color=0;
    listAdapter adapter;
    ArrayList<Data> list;
    ArrayList<String> keys;
    TextView tv1;
    Data data;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_3);
        context=this;
        Intent intent = getIntent();
        list = new ArrayList<Data>();
        adapter = new listAdapter(list);
        btn4 = findViewById(R.id.btn4);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        listview = findViewById(R.id.listv);
        listview.setAdapter(adapter);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        color = pref.getInt("key2", -8331542);

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(color);
        }

        Drawable iv_btn=btn1.getBackground();
        ColorFilter filter=new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
      //  btn1.setAlpha(0.66f);

        iv_btn=btn2.getBackground();
        iv_btn.setColorFilter(filter);
      //  btn2.setAlpha(0.75f);

        iv_btn=btn3.getBackground();
        iv_btn.setColorFilter(filter);
       // btn3.setAlpha(0.84f);

        iv_btn=btn4.getBackground();
        iv_btn.setColorFilter(filter);
      //  btn4.setAlpha(0.93f);
        keys = PreferenceManager.getArray(this, "key_list");
        for(int i = keys.size() - 1; i > -1; i--){
            day = Integer.parseInt(keys.get(i));
            emotii = PreferenceManager.getInt(context, day + "E");
            list.add(new Data(day, emotii));
        }


        //일기 추가
     fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(getApplicationContext(), NewWriting.class);
             startActivity(intent);
             overridePendingTransition(0,0);
             finish();
         }
     });//floating action button 사용
        fab.setBackgroundColor(color);
        fab.setRippleColor(color);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        PaintDrawable pd = new PaintDrawable(color);
        pd.setAlpha(70);
        listview.setSelector(pd);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), colorchange.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ToDoList2.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        PaintDrawable pd2 = new PaintDrawable(color);
        pd2.setAlpha(50);
        listview.setSelector(pd2);

        //리스트에 있는 일기들 중 하나를 선택했을 때 ==> 일기 읽기 위함
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ReadDiary.class);
                day_write=list.get(position).getDate();
                intent.putExtra("day_check", day_write);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        int list = ((MainActivity)MainActivity.context).list;
        Resources resources = getResources();
        String []arr = resources.getStringArray(R.array.goodsaying);
        tv1 = (TextView)findViewById(R.id.saying);
        tv1.setText(arr[list]);
        iv_btn=tv1.getBackground();
        filter=new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
        tv1.setTextColor(Color.BLACK);

        if(exist()){
            fab.setVisibility(View.INVISIBLE);
        }
         else fab.setVisibility(View.VISIBLE);
        //일기버튼 추가 or 안보이게
    }
    class listAdapter extends BaseAdapter{
        List<Data> lists;

        public listAdapter(List<Data> lists){
            this.lists = lists;
        }
        @Override
        public int getCount(){
            return lists.size();

        }

        @Override
        public Object getItem(int i) {
            return lists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Context c = viewGroup.getContext();
            if(view==null){
                LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_list, viewGroup, false);

            }
            TextView date = view.findViewById(R.id.date_show);
            ImageView iv = view.findViewById(R.id.iv_img);


            data = lists.get(i);
            day_write=data.getDate();
            year_a = day_write/10000;
            month_a = (day_write/100)%100;
            day_a = day_write%100;

            date.setText("20"+String.valueOf(year_a)+"년"+String.valueOf(month_a)+"월"+String.valueOf(day_a)+"일");
            emojic = data.getEmot();

            if(emojic == 1){
                iv.setImageResource(R.drawable.laugh);
            }
            else if(emojic == 2){
                iv.setImageResource(R.drawable.smile);
            }
            else if(emojic == 3){
                iv.setImageResource(R.drawable.emoji);
            }
            else if(emojic == 4){
                iv.setImageResource(R.drawable.angry);
            }

            return view;
        }
    }

    private boolean exist(){
        Date current = Calendar.getInstance().getTime();

        SimpleDateFormat YEAR = new SimpleDateFormat("yy", Locale.getDefault());
        SimpleDateFormat MONTH = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat DAY = new SimpleDateFormat("dd", Locale.getDefault());

        String year = YEAR.format(current);
        String month = MONTH.format(current);
        String day = DAY.format(current);

        String today = year + month + day;

        keys = PreferenceManager.getArray(this, "key_list");

        for(int i = keys.size() - 1; i > -1; i--){
            if(keys.get(i).equals(today))
                return true;
        }
        return false;
    }


    //뒤로가기 버튼 눌렀을때 홈으로 이동하기 메소드
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();

    }

}
