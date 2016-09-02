package com.rizzhivaykini.rsswidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by GrkRise on 23.08.2016.
 */

public class RssWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "RssWidgetProvider";
    public static final String WIDGET_NEXT = "widget_button_next";
    public static final String WIDGET_PREV = "widget_button_prev";



    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnable");

        Calendar TIME = Calendar.getInstance();
        TIME.setTimeInMillis(System.currentTimeMillis());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, TIME.getTimeInMillis(), 60 * 1000, loadIntent(context));

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for (int i = 0; i < appWidgetIds.length; i++){
            Intent intent = new Intent(context, UpdaterRssService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.rss_widget);
            remoteViews.setRemoteAdapter(R.id.stack, intent);
            remoteViews.setOnClickPendingIntent(R.id.left, loadPrevIntent(appWidgetIds[i], context));
            remoteViews.setOnClickPendingIntent(R.id.right, loadNextIntent(appWidgetIds[i], context));
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "onUpdate");

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WIDGET_NEXT) || intent.getAction().equals(WIDGET_PREV)){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.rss_widget);
            if(intent.getAction().equals(WIDGET_NEXT)){
                remoteViews.showNext(R.id.stack);
            } else if (intent.getAction().equals(WIDGET_PREV)){
                remoteViews.showPrevious(R.id.stack);
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        Log.d(TAG, "onReceive");
        super.onReceive(context, intent);



    }


    @Override
    public void onDisabled(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(loadIntent(context));
        Log.d(TAG, "onDisable");

    }

    private PendingIntent loadIntent(Context context){
        Log.d(TAG, "LoadIntent");
        Intent intent = new Intent(context, RssLoadService.class);
        intent.setAction(RssLoadService.LOAD_RSS);
        return PendingIntent.getService(context, 0 , intent, 0);
    }

    private static PendingIntent loadPrevIntent(int appWidgetId, Context context){
        Log.d(TAG, "PrevIntent");
        Intent intent = new Intent(context, RssWidgetProvider.class);
        intent.setAction(WIDGET_PREV);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    private static PendingIntent loadNextIntent(int appWidgetId, Context context){
        Log.d(TAG, "NextIntent");
        Intent intent = new Intent(context, RssWidgetProvider.class);
        intent.setAction(WIDGET_NEXT);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
