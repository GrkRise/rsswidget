package com.rizzhivaykini.rsswidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by GrkRise on 23.08.2016.
 */

public class ConfigureActivity extends Activity {
    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editText;




    public static final String WIDGET_PREFERENCES = "widget_pref";
    public static final String WIDGET_PREFIX = "_rss_url";
    public static final String WIDGET_RSS_URI = "http://www.championat.com/xml/rss.xml";

 //   public static final String WIDGET_RSS_URI = "https://habrahabr.ru/rss/feed/posts/6266e7ec4301addaf92d10eb212b4546/";

//    public static final String WIDGET_RSS_URI = "https://news.mail.ru/rss/90/";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        editText = (EditText) findViewById(R.id.rss_uri);
        editText.setText(WIDGET_RSS_URI);

        Log.i("LOG_TAG", "onCreateConfig");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if(widgetID == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        setResult(RESULT_CANCELED);

        findViewById(R.id.push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push();
            }
        });

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });


    }


    public void push() {
        SharedPreferences sp = getSharedPreferences(WIDGET_PREFERENCES, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(WIDGET_RSS_URI + widgetID, editText.getText().toString());
        editor.commit();
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_OK, resultValue);
        Log.d("LOG_TAG", "finish config" + widgetID);
        finish();
    }

    public void delete(){
        editText.setText("");
    }


    static String loadRssUrl(Context context, int appWidgetId){
        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_PREFERENCES, 0);
        return sharedPreferences.getString(WIDGET_RSS_URI + appWidgetId, null);
    }


}
