package com.MyDay.myday1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class other extends AppCompatActivity {
    /**
     * 일정의 기타 부분 구현 ==> 사용자가 직접 일정 입력해서 리스트에 가지고 오도록!
     */
    Intent it;
    EditText editmemo;
    Button button,button2;
    Button btn3, btn1, btn2, btn4;
    InputMethodManager imm;
    TextView tv1;
    int color=100;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        it=getIntent();

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        color = pref.getInt("key2", -8331542);
        editmemo=findViewById(R.id.edit);
        imm.hideSoftInputFromWindow(editmemo.getWindowToken(),0);
        editmemo.setTextIsSelectable(true); //커서 보이게
        editmemo.setShowSoftInputOnFocus(true); //키보드 숨겨짐
        button=findViewById(R.id.finish);
        button2=findViewById(R.id.cancel);
        button.setBackgroundColor(color);
        button.setAlpha(0.66f);
        button2.setBackgroundColor(color);
        button2.setAlpha(0.75f);
        tv1 = (TextView)findViewById(R.id.saying);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);


        Drawable iv_btn=btn1.getBackground();
        ColorFilter filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
        //btn1.setAlpha(0.66f);

        iv_btn=btn2.getBackground();
        iv_btn.setColorFilter(filter);
       // btn2.setAlpha(0.75f);

        iv_btn=btn3.getBackground();
        iv_btn.setColorFilter(filter);
       // btn3.setAlpha(0.84f);

        iv_btn=btn4.getBackground();
        iv_btn.setColorFilter(filter);
        //btn4.setAlpha(0.93f);
        iv_btn=tv1.getBackground();
        filter=new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
        tv1.setTextColor(Color.BLACK);
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

    public void btnOther(View v)
    {
        int id=v.getId();
        if (id == R.id.finish) {
            final String TEXT = editmemo.getText().toString();
            it.putExtra("TEXT", TEXT);
            setResult(RESULT_OK, it);
            finish();
        } else if (id == R.id.cancel) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}

