package com.MyDay.myday1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoList2 extends AppCompatActivity{

    private String curDate=MainActivity.DATE;

    public static ArrayList<ListViewItem> sendArr = new ArrayList<ListViewItem>();

    private ListView listView;
    private MyAdapter Adapter;
    private String doingnow, start_time, finish_time; //지금 하고 있는 것
    Button btn3, btn1, btn2, btn4,add_button;
    private TextView output;
    int color=-8331542;
    private Button completebtn,stopbtn;
    public static boolean flag=false;
    public static Activity todolist2;
    TextView tv1;
    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    boolean choose=false;

    public static int cur_Status; //현재의 상태를 저장할변수를 초기화함.
    int myCount = 1;
    public static long myBaseTime;
    long myPauseTime;

    public NotificationManager manager;
    public NotificationCompat.Builder builder;
    SharedPreferences pref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_list2);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        output = findViewById(R.id.time_out);
        completebtn = findViewById(R.id.completebtn);
        stopbtn = findViewById(R.id.stopbtn);
        add_button=findViewById(R.id.add);

        tv1 = findViewById(R.id.saying);
        pref=getSharedPreferences("pref", MODE_PRIVATE);
        color = pref.getInt("key2", -8331542);

        todolist2=ToDoList2.this;
        loadTimeData();

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(color);
        }

        Drawable iv_btn=btn1.getBackground();
        ColorFilter filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        iv_btn.setColorFilter(filter);
        //btn1.setAlpha(0.66f);

        iv_btn=btn2.getBackground();
        iv_btn.setColorFilter(filter);
       // btn2.setAlpha(0.75f);

        iv_btn=btn3.getBackground();
        iv_btn.setColorFilter(filter);
        //btn3.setAlpha(0.84f);

        iv_btn=btn4.getBackground();
        iv_btn.setColorFilter(filter);
        //btn4.setAlpha(0.93f);

        iv_btn=completebtn.getBackground();
        iv_btn.setColorFilter(filter);

        iv_btn=stopbtn.getBackground();
        iv_btn.setColorFilter(filter);


        iv_btn=tv1.getBackground();
        iv_btn.setColorFilter(filter);
        tv1.setTextColor(Color.BLACK);

        completebtn.setAlpha(0.75f);
        stopbtn.setAlpha(0.75f);

        completebtn.setTextColor(Color.BLACK);
        stopbtn.setTextColor(Color.BLACK);

        int list = ((MainActivity)MainActivity.context).list;
        Resources resources = getResources();
        String []arr = resources.getStringArray(R.array.goodsaying);
        tv1 = (TextView)findViewById(R.id.saying);
        tv1.setText(arr[list]);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeData();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeData();
                Intent intent = new Intent(getApplicationContext(), list_3page.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeData();
                Intent intent = new Intent(getApplicationContext(), colorchange.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==true){
                    Toast.makeText(getApplicationContext(),"완료 후 추가해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ToDoList1.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

        if(ToDoList1.list.size()==0)
            loadData2();
        Adapter = new MyAdapter(this, R.layout.to_do_list2_listview, ToDoList1.list);
        saveData2();
        loadData();
        listView = findViewById(R.id.list);
        listView.setAdapter(Adapter);
        listView.invalidate();

    }

    /**
     * 타이머를 백그라운드로 구현하지 못해서 sharedpreferences를 이용해 상태를 저장하였습니다.
     * 사용자가 이 화면을 나가서 다시 여기로 돌아와도 타이머가 계속 돌아가도록 하기 위해(타이머 상태가 유지되기 위해) 구현하였습니다.
     */
    public void loadTimeData(){
        SharedPreferences preferences = getSharedPreferences("StopWatch", MODE_PRIVATE);
        String json=preferences.getString("Time",null);
        SharedPreferences preferences1=getSharedPreferences("myBaseTime",MODE_PRIVATE);
        SharedPreferences preferences2=getSharedPreferences("myPauseTime", MODE_PRIVATE);

        String temp;
        if(json!=null) {
            temp = json.substring(1, 13);
            output.setText(temp);
            myBaseTime=preferences1.getLong("data1", 0);

            if (json.charAt(0) == '1') {
                myPauseTime=preferences2.getLong("data2",0);
                temp = json.substring(13);
                doingnow = temp;
                choose = true;
                flag = false;
                stopbtn.setText("시작");
                cur_Status = Pause;
            } else if (json.charAt(0) == '2') {

                myTimer.sendEmptyMessage(0);
                temp = json.substring(13);
                doingnow = temp;
                choose = true;
                flag = true;
                stopbtn.setText("일시정지");
                cur_Status = Run;
            } else {
                choose = false;
                flag = false;
                cur_Status = Init;
            }
            //"00 : 00 : 00"
        }
    preferences=getSharedPreferences("Start",MODE_PRIVATE);
        start_time=preferences.getString("start_time",null);
    }


    public void saveTimeData(){
        SharedPreferences preferences=getSharedPreferences("StopWatch",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String str=(String)output.getText();
        String temp="0";

        if(choose==true){
          SharedPreferences preferences2=getSharedPreferences("myBaseTime",MODE_PRIVATE);
           SharedPreferences.Editor editor2=preferences2.edit();
            editor2.putLong("data1",myBaseTime);
            editor2.commit();

            if(flag==true) { //완료 버튼 혹은 일시정지 버튼 안눌렀을 때 다른 액티비티로 간 경우
                temp="2";
            }
            else {
                temp = "1"; //일시정지 버튼 누르고 다른 액티비티로 간 경우
                preferences2=getSharedPreferences("myPauseTime",MODE_PRIVATE);
                editor2=preferences2.edit();
                editor2.putLong("data2",myPauseTime);
                editor2.commit();

            }
        }

        String s=temp+str+doingnow;
        editor.putString("Time", s); //key, value를 이용하여 저장하는 형태
        editor.commit(); //최종 커밋

        preferences=getSharedPreferences("Start",MODE_PRIVATE);
        editor=preferences.edit();
        str=start_time;
        editor.putString("start_time",str);
        editor.commit();
    }


    public void loadData2()  {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences2", MODE_PRIVATE);
        String json = preferences.getString(curDate+"2", null);
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                ToDoList1.list.clear();
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    ToDoList1.list.add(url);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    //데이터 호출
    public void loadData() {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(curDate, null);

        Type type = new TypeToken<ArrayList<ListViewItem>>() {
        }.getType();
        if (gson.fromJson(json, type) != null) {
            sendArr = gson.fromJson(json, type);
        }

    }

    private void saveData1() {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sendArr);
        editor.putString(curDate, json);
        editor.apply();
    }

    public void saveData2() {
        SharedPreferences preferences = getSharedPreferences("sharedpreferences2", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ToDoList1.list);
        editor.putString(curDate+"2", json);
        editor.apply();
    }

    /**
     *  사용자가 앱을 강제 종료했을 때 타이머를 계속 유지하기 위해서
     *     ForcedTerminationService 클래스와 같이 사용
     */
    @Override
    public void onResume() {

        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("naminsik"));
        saveData2();
        saveData1();

    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            saveTimeData();

            //CallYourMethod(message); 실행시킬 메소드를 전달 받은 데이터를 담아 호출하려면 이렇게 한다.
        }
    };
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
        saveData1();
        saveData2();
    }
    @Override
    public void onStop(){
        super.onStop();
        saveTimeData();
        saveData1();
        saveData2();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        saveTimeData();
    }

    class MyAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> string;
        private int layout;
        AlertDialog.Builder alert;

        public MyAdapter(Context context, int alayout, ArrayList<String> string) {
            this.context = context;
            this.string = string;
            layout = alayout;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return string.size();
        }

        @Override
        public Object getItem(int position) {
            return string.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            if (convertView == null) convertView = inflater.inflate(layout, parent, false);

            TextView txt = (TextView) convertView.findViewById(R.id.listtext);
            String what=string.get(position);
            txt.setText(string.get(position));

            if(doingnow!=null&&what.equals(doingnow)){
                txt.setTextColor(color);
            }
            txt.setSelected(true);

            Button first = (Button) convertView.findViewById(R.id.first);
            first.setOnClickListener(this);

            Drawable iv_btn=first.getBackground();
            ColorFilter filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
            iv_btn.setColorFilter(filter);


            final View finalConvertView = convertView;

            first.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choose == false) {
                        alert = new AlertDialog.Builder(context);
                        alert.setTitle("프로그램");
                        alert
                                .setMessage("시작할까?")
                                .setCancelable(false)
                                .setPositiveButton("시작", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (flag == true || choose == true) {
                                            Toast.makeText(getApplicationContext(), "이미 실행 중 입니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            doingnow = (String) Adapter.getItem(pos);
                                            TextView tt1 = (TextView) finalConvertView.findViewById(R.id.listtext);
                                            tt1.setTextColor(color);
                                            choose = true;
                                            flag = true;
                                            myBaseTime = SystemClock.elapsedRealtime();
                                            cur_Status=Run;
                                             myTimer.sendEmptyMessage(0);

                                            long now = System.currentTimeMillis();
                                            Date mDate = new Date(now);
                                            SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
                                            start_time = simpleDate.format(mDate);

                                            boolean noti = PreferenceManager.getBoolean(ToDoList2.this, "alert");
                                            if (noti) showNoti();
                                        }
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"완료 버튼을 누르세요.",Toast.LENGTH_SHORT).show();
                    }
                }

            });

            Button last;
            last = convertView.findViewById(R.id.last);
            iv_btn=last.getBackground();
            filter=new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
            iv_btn.setColorFilter(filter);

            last.setOnClickListener(this);
            last.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int count;
                    final int[] checked = new int[1];
                    count = Adapter.getCount();

                    alert = new AlertDialog.Builder(context);
                    alert.setTitle("완료");
                    alert
                            .setMessage("다 했어?")
                            .setCancelable(false)
                            .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (count > 0) {
                                        checked[0] = pos;
                                        if (checked[0] > -1 && checked[0] < count) {
                                            TextView tt2 = (TextView) finalConvertView.findViewById(R.id.listtext);
                                            String text=(String)tt2.getText();
                                            if(text.equals(doingnow) && flag==true) {
                                                Toast.makeText(getApplicationContext(), "완료 버튼을 누르세요.", Toast.LENGTH_SHORT).show();
                                            }
                                            else {//   Log.i("인덱스: ", String.valueOf(checked[0]));
                                                finalConvertView.setBackgroundColor(Color.WHITE);
                                                ToDoList1.list.remove(checked[0]);
                                               // saveData2();

                                                Adapter.notifyDataSetChanged();
                                            }
                                        }
                                    } else {
                                        listView.setBackgroundColor(Color.CYAN);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });

            return convertView;
        }

        @Override
        public void onClick(View v) {

        }

    }



    //스탑워치 구현
    public void myonclick(View v) {
        int id = v.getId();
        if (id == R.id.stopbtn) {
            if (choose) {
                switch (cur_Status) {
                    case Init:
                        flag = true;
                        myBaseTime = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        stopbtn.setText("일시정지");
                        cur_Status = Run;

                           /* Intent intent = new Intent(ToDoList2.this, MyTimerService.class);
                            bindService(intent, connection, BIND_AUTO_CREATE);

                            new Thread(new GetTimerThread()).start();*/
                        break;

                    case Run: //움직이고 있을 때 멈춘다
                        myPauseTime = SystemClock.elapsedRealtime();
                        flag = false;
                        //unbindService(connection);
                        cur_Status = Pause;
                        myTimer.removeMessages(0);

                        stopbtn.setText("시작");

                        break;

                    case Pause: //시작한다
                        flag = true;
                        long now = SystemClock.elapsedRealtime();
                        myBaseTime += (now - myPauseTime);
                        cur_Status = Run;
                        myTimer.sendEmptyMessage(0);

                            /*intent = new Intent(ToDoList2.this, MyTimerService.class);
                            bindService(intent, connection, BIND_AUTO_CREATE);
                            new Thread(new GetTimerThread()).start();*/

                        stopbtn.setText("일시정지");
                        break;
                }
                return;
            }

            if (choose == true) {
                cur_Status = 4;
                choose = false;
                if (flag == true) {
                    // unbindService(connection);
                    flag = false;
                    myBaseTime = SystemClock.elapsedRealtime();

                }
                stopbtn.setText("일시정지");
                myTimer.removeMessages(0);

                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
                finish_time = simpleDate.format(mDate);

                /**
                 *sendArr에 저장 (한 일, 시작 시각, 나중시각)
                 */
                ListViewItem item = new ListViewItem(doingnow, start_time + "," + finish_time);
                sendArr.add(item);

                doingnow = null;
                listView.setAdapter(Adapter);
                saveData1();

                output.setText("00 : 00 : 00");
                myCount = 1;
            }
        } else if (id == R.id.completebtn) {
            if (choose == true) {
                cur_Status = 4;
                choose = false;
                if (flag == true) {
                    // unbindService(connection);
                    flag = false;
                    myBaseTime = SystemClock.elapsedRealtime();

                }
                stopbtn.setText("일시정지");
                myTimer.removeMessages(0);

                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm:ss");
                finish_time = simpleDate.format(mDate);

                /**
                 *sendArr에 저장 (한 일, 시작 시각, 나중시각)
                 */
                ListViewItem item = new ListViewItem(doingnow, start_time + "," + finish_time);
                sendArr.add(item);

                doingnow = null;
                listView.setAdapter(Adapter);
                saveData1();

                output.setText("00 : 00 : 00");
                myCount = 1;
            }
        }
    }

    Handler myTimer = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            output.setText(getTimeOut());
            myTimer.sendEmptyMessage(0);
        }
    };

    String getTimeOut() {
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;

        long sec = outTime / 1000;
        long min = sec / 60;
        long hour = min / 60;
        sec = sec % 60;

        String real_outTime = String.format("%02d : %02d : %02d", hour, min, sec);
        return real_outTime;
    }


    //상단 바에 현재 일정 보여주는 코드

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private boolean isScreenOn(){
        PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        return pm.isInteractive();
    }

    //상단 바에 현재 일정 보여주는 코드
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public void showNoti() {
        builder=null;
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        @SuppressLint("WrongConstant") PendingIntent pintent=PendingIntent.getActivity(getApplicationContext(),0,intent,Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel("channel1","Channel1",NotificationManager.IMPORTANCE_DEFAULT)
            );

        }
        builder = new NotificationCompat.Builder(ToDoList2.this,"channel1")
                .setContentTitle("일정")
                .setSmallIcon(R.drawable.ic_done_black_24dp)
                .setContentText(doingnow)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
               // .setAutoCancel(true)
                .setShowWhen(true);
        if(!isScreenOn()) builder.setContentIntent(pintent);
        manager.notify(1,builder.build());
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
