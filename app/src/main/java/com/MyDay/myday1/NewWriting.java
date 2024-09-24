package com.MyDay.myday1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewWriting extends AppCompatActivity {
    /**
     * 일기 구현 부분
     */
    private Context context;
    int data_date;
    String KEY_date;
    ArrayList<String> keys;
    Button btn3, btn1, btn2, btn4, save_diary;
    int color=0;

    //날씨
    TextView tv_weather;
    ImageView Iv_weather;
    String degree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_page);
        TextView tv = (TextView)findViewById(R.id.today_date);
        Date current = Calendar.getInstance().getTime();

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        save_diary= (Button)findViewById(R.id.save_diary);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        color = pref.getInt("key2", -8331542);


        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(color);
        }


        Drawable iv_btn=btn1.getBackground();
        ColorFilter filter=new PorterDuffColorFilter(color,PorterDuff.Mode.SRC_IN);
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

        iv_btn=save_diary.getBackground();
        iv_btn.setColorFilter(filter);

        SimpleDateFormat YEAR = new SimpleDateFormat("yy", Locale.getDefault());
        SimpleDateFormat MONTH = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat DAY = new SimpleDateFormat("dd", Locale.getDefault());

        String year = YEAR.format(current);
        String month = MONTH.format(current);
        String day = DAY.format(current);

        tv.setText(year + "/" + month + "/" + day);

        int year_int = Integer.parseInt(year);
        int month_int = Integer.parseInt(month);
        int day_int = Integer.parseInt(day);

        data_date = year_int * 10000 + month_int * 100 + day_int;
        KEY_date = Integer.toString(data_date);

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

        //날씨 설정
        tv_weather = (TextView)findViewById(R.id.today_weather);
        Iv_weather = (ImageView)findViewById(R.id.weather);
        String api = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=2714051000";

        DownloadWebpageTask task = new DownloadWebpageTask();
        task.execute(api);
    }

    public void save_Diary(View v){
        context = this;

        //이모티콘 저장
        RadioGroup rg_emoji = (RadioGroup)findViewById(R.id.emotes);
        RadioButton rb_emoji;
        int emoji_code;

        if(rg_emoji.getCheckedRadioButtonId() == R.id.laughing){
            rb_emoji = (RadioButton)findViewById(R.id.laughing);
            emoji_code = 1;
        }
        else if(rg_emoji.getCheckedRadioButtonId() == R.id.smiling){
            rb_emoji = (RadioButton)findViewById(R.id.smiling);
            emoji_code = 2;
        }
        else if(rg_emoji.getCheckedRadioButtonId() == R.id.angry){
            rb_emoji = (RadioButton)findViewById(R.id.angry);
            emoji_code = 3;
        }
        else if(rg_emoji.getCheckedRadioButtonId() == R.id.crying){
            rb_emoji = (RadioButton)findViewById(R.id.crying);
            emoji_code = 4;
        }
        else{
            Toast.makeText(this, "오늘의 기분을 골라주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
        String EKEY = KEY_date + "E";
        PreferenceManager.setInt(this, EKEY, emoji_code);

        //내용 저장
        String DKEY = KEY_date + "D";
        EditText et = (EditText)findViewById(R.id.document);
        String document = et.getText().toString();
        PreferenceManager.setString(this, DKEY, document);

        //키값 저장
        save_key(KEY_date);

        //돌아가기
        Intent intent = new Intent(getApplicationContext(), list_3page.class);
        intent.putExtra("da_te", data_date);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }


    //일기 저장장
   private void save_key(String k){
        keys = PreferenceManager.getArray(this, "key_list");
        for(int i = 0; i < keys.size(); i++){
            if(keys.get(i).equals(k)) return;
        }
        keys.add(k);
        PreferenceManager.setArray(this, "key_list", keys);
    }



    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        // ctrl + o

        /**
         * 일기 적을 때 날씨 아이콘 설정해 주는 부분
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            int flag1=0, flag2=0;
            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));

                // 현재 이벤트 확인
                int eventType = xpp.getEventType();

                String start_tag = "";
                String txt = "";
                String end_tag = "";
                int weather_code=1;
                boolean bSet_itemCode = false;
                boolean bSet_itemCode1 = false;//이모티콘 사용
                while ((eventType != XmlPullParser.END_DOCUMENT)&&((flag1==0)||(flag2==0))){

                    if(eventType == XmlPullParser.START_DOCUMENT){

                    }else if(eventType == XmlPullParser.START_TAG){
                        start_tag = xpp.getName();
                        if (start_tag.equals("temp")){
                            bSet_itemCode = true;
                        }
                        if (start_tag.equals("wfKor")){
                            bSet_itemCode1 = true;
                        }

                    }else if(eventType == XmlPullParser.TEXT){
                        // 엘리먼트 내용 확인
                        if (bSet_itemCode){
                            txt = xpp.getText();
                            tv_weather.setText(txt+" ℃");
                            Log.i("온도",txt);
                            degree = txt;
                            flag1=1;
                            String DeKEY = KEY_date + "De";
                            PreferenceManager.setString(getApplicationContext(), DeKEY, degree);
                            bSet_itemCode = false;
                        }
                        if (bSet_itemCode1){
                            txt = xpp.getText();
                            flag2=1;
                            String WKEY = KEY_date + "W";
                            if(txt.equals("맑음")){
                                Iv_weather.setImageResource(R.drawable.sun);
                                weather_code = 1;
                            }
                            else if(txt.equals("구름 많음")){
                                Iv_weather.setImageResource(R.drawable.cloudy);
                                weather_code = 2;
                            }
                            else if(txt.equals("흐림")){
                                Iv_weather.setImageResource(R.drawable.cloud);
                                weather_code = 3;
                            }
                            else if(txt.equals("비")){
                                Iv_weather.setImageResource(R.drawable.rain);
                                weather_code = 4;
                            }
                            else if(txt.equals("눈")){
                                Iv_weather.setImageResource(R.drawable.snow);
                                weather_code = 5;
                            }
                            else if(txt.equals("비/눈")){
                                Iv_weather.setImageResource(R.drawable.rain_snow);
                                weather_code = 6;
                            }
                            else if(txt.equals("소나기")){
                                Iv_weather.setImageResource(R.drawable.rain);
                                weather_code = 7;
                            }
                            PreferenceManager.setInt(getApplicationContext(), WKEY, weather_code);
                            bSet_itemCode1 = false;
                        }

                    }else if(eventType == XmlPullParser.END_TAG){
                        end_tag = xpp.getName();
                    }
                    eventType = xpp.next();
                }


            }catch(Exception e){

            }


        }

        @Override
        protected String doInBackground(String... urls) {
            try{
                String txt = (String)downloadUrl((String) urls[0]);
                return txt;
            }catch (IOException e){
                Log.e("결과", e.toString());
                return "다운로드 실패";
            }

        }

        private  String downloadUrl(String api) throws IOException{
            HttpURLConnection conn = null;
            try{
                URL url = new URL(api);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf,"utf-8"));
                String line = null;
                String page ="";

                while ((line = bufferedReader.readLine()) != null){
                    page += line;
                }
                return page;
            }finally {
                conn.disconnect();
            }
        }
    }

//뒤로가기 버튼 눌렀을때 홈으로 이동하기 메소드
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), list_3page.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();

    }

}

