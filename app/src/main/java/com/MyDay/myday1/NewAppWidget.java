package com.MyDay.myday1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */

public class NewAppWidget extends AppWidgetProvider {

    public static String arr[];
    public static int list;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        arr= MainActivity.arr;
        list = MainActivity.list;
        for (int appWidgetId : appWidgetIds) {

            int prev_col;
            prev_col=MainActivity.color;
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            SharedPreferences sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
            int color = sharedPreferences.getInt("key2", -8331542);

            SharedPreferences shared = context.getSharedPreferences("saying", Context.MODE_PRIVATE);
            String sentence = shared.getString("sen", "");

            views.setTextColor(R.id.button1, color);
            views.setTextColor(R.id.button2, color);
            views.setTextColor(R.id.button3, color);
            views.setTextColor(R.id.button4, color);

            views.setTextViewText(R.id.saying, sentence);

            Intent intent5 = new Intent(context, MainActivity.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent5, 0);
            views.setOnClickPendingIntent(R.id.button1, pending);


            Intent intent2 = new Intent(context, ToDoList2.class);
            PendingIntent pending2 = PendingIntent.getActivity(context, 0, intent2, 0);
            views.setOnClickPendingIntent(R.id.button2, pending2);


            Intent intent3 = new Intent(context, list_3page.class);
            PendingIntent pending3 = PendingIntent.getActivity(context, 0, intent3, 0);
            views.setOnClickPendingIntent(R.id.button3, pending3);

            Intent intent4 = new Intent(context, colorchange.class);
            PendingIntent pending4 = PendingIntent.getActivity(context, 0, intent4, 0);
            views.setOnClickPendingIntent(R.id.button4, pending4);

            Intent newintent = new Intent(context, MainActivity.class);
            PendingIntent pending5 = PendingIntent.getActivity(context, 0, newintent, 0);
            views.setOnClickPendingIntent(R.id.saying, pending5);


            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }




    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName myWidget = new ComponentName(context.getPackageName(), NewAppWidget.class.getName());
        int[] widgetIds = appWidgetManager.getAppWidgetIds(myWidget);
        String action = intent.getAction();
        //업데이트 액션이 들어오면
        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            this.onUpdate(context, AppWidgetManager.getInstance(context), widgetIds); // onUpdate 호출
             }
             }


}

