package com.MyDay.myday1;

import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class colorchange extends AppCompatActivity {
    /**
     * 앱에 배경이랑 글자 색깔을 바꾸는 메뉴가 있는데 바꿔주는 코드입니다.
     */
    private Button button, btn1, btn2, btn3, btn4, col_change, button_info, use_button;
    private LinearLayout layout1, layout2, layout3;
    private TextView top;
    TextView tv1;
    int color = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);
        int list = ((MainActivity)MainActivity.context).list;
        /**
         * 여기는 다 버튼 구현하고 색 지정하는 등의 코드입니다. 이 부분은 다른 클래스 코드들과 거의 비슷합니다.
         */
        Resources resources = getResources();
        String []arr = resources.getStringArray(R.array.goodsaying);
        tv1 = (TextView)findViewById(R.id.saying);
        tv1.setText(arr[list]);
        button = (Button) findViewById(R.id.change);
        button_info = (Button)findViewById(R.id.information);
        use_button=(Button)findViewById(R.id.use);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        layout1 = (LinearLayout) findViewById(R.id.top);
        layout2 = (LinearLayout) findViewById(R.id.middle);
        layout3 = (LinearLayout) findViewById(R.id.bottom);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        color = pref.getInt("key2", -8331542);

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(color);
        }
        Drawable iv_btn=btn1.getBackground();
        ColorFilter filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
     //   btn1.setAlpha(0.66f);

        iv_btn=btn2.getBackground();
        iv_btn.setColorFilter(filter);
       // btn2.setAlpha(0.75f);

        iv_btn=btn3.getBackground();
        iv_btn.setColorFilter(filter);
      //  btn3.setAlpha(0.84f);

        iv_btn=btn4.getBackground();
        iv_btn.setColorFilter(filter);
       // btn4.setAlpha(0.93f);

        iv_btn = button_info.getBackground();
        iv_btn.setColorFilter(filter);

        iv_btn = button.getBackground();
        iv_btn.setColorFilter(filter);

        iv_btn = use_button.getBackground();
        iv_btn.setColorFilter(filter);

        iv_btn=tv1.getBackground();
        iv_btn.setColorFilter(filter);

        tv1.setTextColor(Color.BLACK);

        /**
         * 메뉴 중에 사용 방법 알려주는 버튼 있는데 그것을 구현한 리스너입니다.
         */
        use_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), How_Use.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        /**
         * 색깔 지정 해 주는 코드입니다.
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();

            }
        });


        /**
         * 개발자 정보라는 버튼을 구현한 코드입니다.
         */
        button_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder ad = new AlertDialog.Builder(colorchange.this);
                ad.setTitle("기타 정보");
                ad.setMessage(
                        "다이어리 및 일정 관리 앱\n"+
                        "\n로고 출처 : FlatIcon(https://www.flaticon.com/)"+
                        "\nCopyright (c) 2016 Petrov Kristiyan\n");

                ad.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }});

        /**
         * Home 버튼 눌렀을 때
         */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        /**
         * SHEDULE 버튼 눌렀을 때
         */
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ToDoList2.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        /**
         * Diary 버튼 눌렀을 때
         */
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), list_3page.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        /**
         * 일정 타이머로 시작할 때 상단 바에 뜨게 할 건지 안 할건지 설정해 주는 코드입니다.
         */
        Switch opt_change = findViewById(R.id.change_option);
        final TextView alert = findViewById(R.id.alert_info);
        boolean on_off = PreferenceManager.getBoolean(this, "alert");
        if(on_off == true){
            opt_change.setChecked(true);
            alert.setText(" 오늘 일정에 대한 알림을 받습니다.");
        }
        else{
            opt_change.setChecked(false);
            alert.setText(" 오늘 일정에 대한 알림을 받지 않습니다.");
        }
        opt_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    PreferenceManager.setBoolean(colorchange.this, "alert", true);
                    alert.setText(" 오늘 일정에 대한 알림을 받습니다.     ");
                }else{
                    PreferenceManager.setBoolean(colorchange.this, "alert", false);
                    alert.setText(" 오늘 일정에 대한 알림을 받지 않습니다.");
                }
            }
        });
    }





    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this);  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

        colors.add("#FFffab91");
        colors.add("#FFF48FB1");
        colors.add("#FFce93d8");
        colors.add("#FFb39ddb");
        colors.add("#FF9fa8da");
        colors.add("#FF90caf9");
        colors.add("#FF81d4fa");
        colors.add("#FF80deea");
        colors.add("#FF80cbc4");
        colors.add("#FFc5e1a5");
        colors.add("#FFe6ee9c");
        colors.add("#FFfff59d");
        colors.add("#FFffe082");
        colors.add("#FFffcc80");
        colors.add("#FFbcaaa4");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        // OK 버튼 클릭 시 이벤트
                        if(color == 0) return;

                        Drawable iv_btn=btn1.getBackground();
                        ColorFilter filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
                        iv_btn.setColorFilter(filter);
                        //   btn1.setAlpha(0.66f);

                        iv_btn=btn2.getBackground();
                        iv_btn.setColorFilter(filter);
                        // btn2.setAlpha(0.75f);

                        iv_btn=btn3.getBackground();
                        iv_btn.setColorFilter(filter);
                        //  btn3.setAlpha(0.84f);

                        iv_btn=btn4.getBackground();
                        iv_btn.setColorFilter(filter);

                        iv_btn=tv1.getBackground();
                        iv_btn.setColorFilter(filter);

                        tv1.setTextColor(Color.BLACK);


                        iv_btn=button.getBackground();
                        iv_btn.setColorFilter(filter);

                        iv_btn=button_info.getBackground();
                        iv_btn.setColorFilter(filter);

                        iv_btn=use_button.getBackground();
                        iv_btn.setColorFilter(filter);



                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("key2", color);
                        editor.commit();

                        if(Build.VERSION.SDK_INT >= 21){
                            getWindow().setStatusBarColor(color);
                        }

                        Intent intent = new Intent(colorchange.this, NewAppWidget.class);
                        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        intent.putExtra("color", color);
                        colorchange.this.sendBroadcast(intent);
                        Log.i("과연", "success");

                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
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
