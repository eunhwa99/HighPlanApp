package com.MyDay.myday1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TimeTable extends AppCompatActivity {

    //완료한 일정 이름 담을 배열
    private ArrayList<String>schedule1=new ArrayList<>();
    //시간 담을 배열
    private ArrayList<String>start_time1=new ArrayList<>();
    private ArrayList<String>finish_time1=new ArrayList<>();
    //미완료한 일정 이름 담을 배열
    private ArrayList<String>schedule2=new ArrayList<>();
    //시간 담을 배열
    private ArrayList<String>start_time2=new ArrayList<>();
    private ArrayList<String>finish_time2=new ArrayList<>();
    boolean flag;
    String[] TIME; //시작, 끝 시각
    private String curDate=MainActivity.DATE;
    private Intent it;
    //timetable 날짜로
    TextView tv_timetable;

    private ArrayList<ListViewItem> temp1=new ArrayList<>();
    private ArrayList<String>temp2=new ArrayList<>();

    //버튼 색 변경
    int color;
    Button btn3, btn1, btn2, btn4;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table);
        curDate=MainActivity.DATE;
        it=getIntent();
        String val=it.getStringExtra("chooseDate");
        getData(val);
        //버튼 색 변경
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        color = pref.getInt("key2", -8331542);

        SharedPreferences today = getSharedPreferences("today", MODE_PRIVATE);
        SharedPreferences.Editor editor = today.edit();
        editor.putString("today_date", curDate);
        editor.commit();


        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(color);
        }
        int year_int = (Integer.parseInt(val) / 10000)/100;
        int month_int = (Integer.parseInt(val) / 100) % 100;
        int day_int = Integer.parseInt(val) % 100;
        tv_timetable = (TextView)findViewById(R.id.today_date);
        tv_timetable.setText(year_int+"/"+month_int+"/"+day_int);

        Drawable iv_btn=btn1.getBackground();
        ColorFilter filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
        //btn1.setAlpha(0.66f);

        iv_btn=btn2.getBackground();
        iv_btn.setColorFilter(filter);
        //btn2.setAlpha(0.75f);

        iv_btn=btn3.getBackground();
        iv_btn.setColorFilter(filter);
        //btn3.setAlpha(0.84f);

        iv_btn=btn4.getBackground();
        iv_btn.setColorFilter(filter);
        //btn4.setAlpha(0.93f);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), list_3page.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), colorchange.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

    }


    public void setData(String str){
        //완료한 일정
        SharedPreferences preferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(str, null);

        Type type = new TypeToken<ArrayList<ListViewItem>>() {
        }.getType();
        if (gson.fromJson(json, type) != null) {
            temp1 = gson.fromJson(json, type);
        }

        //미완료한 일정
        preferences = getSharedPreferences("sharedpreferences2", MODE_PRIVATE);
        json = preferences.getString(str+"2", null);
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    temp2.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getData(String str) {
        Log.i("DATE: ",str);
        if(str!=null) {
            if (str.equals(curDate)) { //현재 날짜라면
                setData(str);
                makeList(temp1,temp2);
              //  makeList(ToDoList2.sendArr, ToDoList1.list);
            } else { //키값 찾아서 배열에 넣어주기
                setData(str);
                makeList(temp1, temp2);
            }

            /**
             * 색칠 메소드 호출
             */
            coloring();
        }
    }

    public void makeList(ArrayList<ListViewItem> list1, ArrayList<String> list2){
        for(int i=0;i<list1.size();i++) {

            ListViewItem listViewItem = list1.get(i);
            String Name = listViewItem.getName();
            String Time = listViewItem.getTime();
            TIME = Time.split(","); //문자열 분리

            Log.i("테스트", TIME[0] + ", " + TIME[1]);

            for (int j = 0; j < list2.size(); j++) { //list2에 있다는 거는 완료 못한 일정
                flag = false;
                String str1 = list2.get(j);
                if (str1.equals(Name)) { //완료 못한 일정
                    flag = true;
                    schedule2.add(Name);
                    start_time2.add(TIME[0]);
                    finish_time2.add(TIME[1]);
                    break;
                }
            }

            if (!flag) { //완료한 일정
                schedule1.add(Name);
                start_time1.add(TIME[0]);
                finish_time1.add(TIME[1]);
            }
        }
    }

    void coloring(){
        /**
         * time_table 일정에 맞게 색칠해주는 메소드
         * 완료한 일정은 진하게, 덜 완료한 거는 흐릿하게 색칠
         */
        TextView set_color;
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        int color = pref.getInt("key2",-8331542 );

        int start_h, start_m, finish_h, finish_m;

        for(int i = 0; i < start_time1.size(); i++){

            String[] start = start_time1.get(i).split(":");

            if(start[0]==null||start[1]==null) continue;

            start_h = Integer.parseInt(start[0]);
            start_m = Integer.parseInt(start[1]);
            start_m /= 10;

            String[] finish = finish_time1.get(i).split(":");

            if(finish[0]==null||finish[1]==null) continue;

            finish_h = Integer.parseInt(finish[0]);
            finish_m = Integer.parseInt(finish[1]);
            finish_m /= 10;

            int START = start_h * 10 + start_m, FINISH = finish_h * 10 + finish_m;

            for(int j = START; j <= FINISH; j++){
                 set_color = getTextView(j);
                set_color.setSelected(true);
                 set_color.setBackgroundColor(color);

                if(j == START){
                     set_color.setText(schedule1.get(i));
                }
            }
        }

        for(int i = 0; i < start_time2.size(); i++){
            String[] start = start_time2.get(i).split(":");

            if(start[0]==null||start[1]==null) continue;

            start_h = Integer.parseInt(start[0]);

            start_m = Integer.parseInt(start[1]);
            start_m /= 10;

            String[] finish = finish_time2.get(i).split(":");
            if(finish[0]==null||finish[1]==null) continue;
            finish_h = Integer.parseInt(finish[0]);
            finish_m = Integer.parseInt(finish[1]);
            finish_m /= 10;

            int START = start_h * 10 + start_m, FINISH = finish_h * 10 + finish_m;

            for(int j = START; j <= FINISH; j++){
                 set_color = getTextView(j);
                set_color.setSelected(true);
                 set_color.setBackgroundColor(color);
                 set_color.setAlpha((float)0.7);

                if(j == START){
                    set_color.setText(schedule2.get(i));
                }
            }
        }
    }


    //타임테이블 구현
    TextView getTextView(int idx){
        int h, m;
        h = idx / 10;
        m = idx % 10;

        TextView tv = null;

        if(h == 0){
            if(m == 0) tv = findViewById(R.id.t000);
            if(m == 1) tv = findViewById(R.id.t001);
            if(m == 2) tv = findViewById(R.id.t002);
            if(m == 3) tv = findViewById(R.id.t003);
            if(m == 4) tv = findViewById(R.id.t004);
            if(m == 5) tv = findViewById(R.id.t005);
        }
        if(h == 1){
            if(m == 0) tv = findViewById(R.id.t010);
            if(m == 1) tv = findViewById(R.id.t011);
            if(m == 2) tv = findViewById(R.id.t012);
            if(m == 3) tv = findViewById(R.id.t013);
            if(m == 4) tv = findViewById(R.id.t014);
            if(m == 5) tv = findViewById(R.id.t015);
        }
        if(h == 2){
            if(m == 0) tv = findViewById(R.id.t020);
            if(m == 1) tv = findViewById(R.id.t021);
            if(m == 2) tv = findViewById(R.id.t022);
            if(m == 3) tv = findViewById(R.id.t023);
            if(m == 4) tv = findViewById(R.id.t024);
            if(m == 5) tv = findViewById(R.id.t025);
        }
        if(h == 3){
            if(m == 0) tv = findViewById(R.id.t030);
            if(m == 1) tv = findViewById(R.id.t031);
            if(m == 2) tv = findViewById(R.id.t032);
            if(m == 3) tv = findViewById(R.id.t033);
            if(m == 4) tv = findViewById(R.id.t034);
            if(m == 5) tv = findViewById(R.id.t035);
        }
        if(h == 4){
            if(m == 0) tv = findViewById(R.id.t040);
            if(m == 1) tv = findViewById(R.id.t041);
            if(m == 2) tv = findViewById(R.id.t042);
            if(m == 3) tv = findViewById(R.id.t043);
            if(m == 4) tv = findViewById(R.id.t044);
            if(m == 5) tv = findViewById(R.id.t045);
        }
        if(h == 5){
            if(m == 0) tv = findViewById(R.id.t050);
            if(m == 1) tv = findViewById(R.id.t051);
            if(m == 2) tv = findViewById(R.id.t052);
            if(m == 3) tv = findViewById(R.id.t053);
            if(m == 4) tv = findViewById(R.id.t054);
            if(m == 5) tv = findViewById(R.id.t055);
        }
        if(h == 6){
            if(m == 0) tv = findViewById(R.id.t060);
            if(m == 1) tv = findViewById(R.id.t061);
            if(m == 2) tv = findViewById(R.id.t062);
            if(m == 3) tv = findViewById(R.id.t063);
            if(m == 4) tv = findViewById(R.id.t064);
            if(m == 5) tv = findViewById(R.id.t065);
        }
        if(h == 7){
            if(m == 0) tv = findViewById(R.id.t070);
            if(m == 1) tv = findViewById(R.id.t071);
            if(m == 2) tv = findViewById(R.id.t072);
            if(m == 3) tv = findViewById(R.id.t073);
            if(m == 4) tv = findViewById(R.id.t074);
            if(m == 5) tv = findViewById(R.id.t075);
        }
        if(h == 8){
            if(m == 0) tv = findViewById(R.id.t080);
            if(m == 1) tv = findViewById(R.id.t081);
            if(m == 2) tv = findViewById(R.id.t082);
            if(m == 3) tv = findViewById(R.id.t083);
            if(m == 4) tv = findViewById(R.id.t084);
            if(m == 5) tv = findViewById(R.id.t085);
        }
        if(h == 9){
            if(m == 0) tv = findViewById(R.id.t090);
            if(m == 1) tv = findViewById(R.id.t091);
            if(m == 2) tv = findViewById(R.id.t092);
            if(m == 3) tv = findViewById(R.id.t093);
            if(m == 4) tv = findViewById(R.id.t094);
            if(m == 5) tv = findViewById(R.id.t095);
        }
        if(h == 10){
            if(m == 0) tv = findViewById(R.id.t100);
            if(m == 1) tv = findViewById(R.id.t101);
            if(m == 2) tv = findViewById(R.id.t102);
            if(m == 3) tv = findViewById(R.id.t103);
            if(m == 4) tv = findViewById(R.id.t104);
            if(m == 5) tv = findViewById(R.id.t105);
        }
        if(h == 11){
            if(m == 0) tv = findViewById(R.id.t110);
            if(m == 1) tv = findViewById(R.id.t111);
            if(m == 2) tv = findViewById(R.id.t112);
            if(m == 3) tv = findViewById(R.id.t113);
            if(m == 4) tv = findViewById(R.id.t114);
            if(m == 5) tv = findViewById(R.id.t115);
        }
        if(h == 12){
            if(m == 0) tv = findViewById(R.id.t120);
            if(m == 1) tv = findViewById(R.id.t121);
            if(m == 2) tv = findViewById(R.id.t122);
            if(m == 3) tv = findViewById(R.id.t123);
            if(m == 4) tv = findViewById(R.id.t124);
            if(m == 5) tv = findViewById(R.id.t125);
        }
        if(h == 13){
            if(m == 0) tv = findViewById(R.id.t130);
            if(m == 1) tv = findViewById(R.id.t131);
            if(m == 2) tv = findViewById(R.id.t132);
            if(m == 3) tv = findViewById(R.id.t133);
            if(m == 4) tv = findViewById(R.id.t134);
            if(m == 5) tv = findViewById(R.id.t135);
        }
        if(h == 14){
            if(m == 0) tv = findViewById(R.id.t140);
            if(m == 1) tv = findViewById(R.id.t141);
            if(m == 2) tv = findViewById(R.id.t142);
            if(m == 3) tv = findViewById(R.id.t143);
            if(m == 4) tv = findViewById(R.id.t144);
            if(m == 5) tv = findViewById(R.id.t145);
        }
        if(h == 15){
            if(m == 0) tv = findViewById(R.id.t150);
            if(m == 1) tv = findViewById(R.id.t151);
            if(m == 2) tv = findViewById(R.id.t152);
            if(m == 3) tv = findViewById(R.id.t153);
            if(m == 4) tv = findViewById(R.id.t154);
            if(m == 5) tv = findViewById(R.id.t155);
        }
        if(h == 16){
            if(m == 0) tv = findViewById(R.id.t160);
            if(m == 1) tv = findViewById(R.id.t161);
            if(m == 2) tv = findViewById(R.id.t162);
            if(m == 3) tv = findViewById(R.id.t163);
            if(m == 4) tv = findViewById(R.id.t164);
            if(m == 5) tv = findViewById(R.id.t165);
        }
        if(h == 17){
            if(m == 0) tv = findViewById(R.id.t170);
            if(m == 1) tv = findViewById(R.id.t171);
            if(m == 2) tv = findViewById(R.id.t172);
            if(m == 3) tv = findViewById(R.id.t173);
            if(m == 4) tv = findViewById(R.id.t174);
            if(m == 5) tv = findViewById(R.id.t175);
        }
        if(h == 18){
            if(m == 0) tv = findViewById(R.id.t180);
            if(m == 1) tv = findViewById(R.id.t181);
            if(m == 2) tv = findViewById(R.id.t182);
            if(m == 3) tv = findViewById(R.id.t183);
            if(m == 4) tv = findViewById(R.id.t184);
            if(m == 5) tv = findViewById(R.id.t185);
        }
        if(h == 19){
            if(m == 0) tv = findViewById(R.id.t190);
            if(m == 1) tv = findViewById(R.id.t191);
            if(m == 2) tv = findViewById(R.id.t192);
            if(m == 3) tv = findViewById(R.id.t193);
            if(m == 4) tv = findViewById(R.id.t194);
            if(m == 5) tv = findViewById(R.id.t195);
        }
        if(h == 20){
            if(m == 0) tv = findViewById(R.id.t200);
            if(m == 1) tv = findViewById(R.id.t201);
            if(m == 2) tv = findViewById(R.id.t202);
            if(m == 3) tv = findViewById(R.id.t203);
            if(m == 4) tv = findViewById(R.id.t204);
            if(m == 5) tv = findViewById(R.id.t205);
        }
        if(h == 21){
            if(m == 0) tv = findViewById(R.id.t210);
            if(m == 1) tv = findViewById(R.id.t211);
            if(m == 2) tv = findViewById(R.id.t212);
            if(m == 3) tv = findViewById(R.id.t213);
            if(m == 4) tv = findViewById(R.id.t214);
            if(m == 5) tv = findViewById(R.id.t215);
        }
        if(h == 22){
            if(m == 0) tv = findViewById(R.id.t220);
            if(m == 1) tv = findViewById(R.id.t221);
            if(m == 2) tv = findViewById(R.id.t222);
            if(m == 3) tv = findViewById(R.id.t223);
            if(m == 4) tv = findViewById(R.id.t224);
            if(m == 5) tv = findViewById(R.id.t225);
        }
        if(h == 23){
            if(m == 0) tv = findViewById(R.id.t230);
            if(m == 1) tv = findViewById(R.id.t231);
            if(m == 2) tv = findViewById(R.id.t232);
            if(m == 3) tv = findViewById(R.id.t233);
            if(m == 4) tv = findViewById(R.id.t234);
            if(m == 5) tv = findViewById(R.id.t235);
        }

        return tv;
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
