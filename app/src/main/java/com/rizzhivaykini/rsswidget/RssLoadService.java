package com.rizzhivaykini.rsswidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.rizzhivaykini.rsswidget.saxrssreader.RssFeed;
import com.rizzhivaykini.rsswidget.saxrssreader.RssItem;
import com.rizzhivaykini.rsswidget.saxrssreader.RssReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GrkRise on 25.08.2016.
 */

public class RssLoadService extends IntentService {
    private List<RssItem> rssItems;

    private static final String TAG = "RssLoadService";
    public static final String LOAD_RSS = "LOAD_RSS";
    public static final String RSS_PREFS = "rss_pref";
    public static final String PREFIX = "_rss";

    public RssLoadService() {
        super("RssLoadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "OnHandleIntent");
        if(intent.getAction().equals(LOAD_RSS)){
            Log.d(TAG, "LoadRss");
        }

        ComponentName componentName = new ComponentName(this, RssWidgetProvider.class);
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        for (final int id  :appWidgetIds){

            String rss_uri = ConfigureActivity.loadRssUrl(this, id);
            try {
                rssItems = downloadRss(rss_uri);
                for (int i = 0; i < rssItems.size(); i++){
                    Log.d("RSS_TAG", rssItems.get(i).getTitle());
                }
                saveRss(id, rssItems);
                appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.stack);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }


    }

    public void saveRss(int id, List<RssItem> items){
        JSONArray jsonArray = new JSONArray();
        try {
            for (final RssItem rssItem : items){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", rssItem.getTitle());
                jsonObject.put("text", android.text.Html.fromHtml(rssItem.getDescription().toString()));
                jsonArray.put(jsonObject);
            }
            saveRss(this, id, jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveRss(Context context, int id, String string){
        SharedPreferences.Editor editor = context.getSharedPreferences(RSS_PREFS, 0).edit();
        editor.putString(PREFIX + id, string);
        editor.commit();
    }
    private List<RssItem> downloadRss(String urlString) throws IOException, SAXException{
        Log.d(TAG, "download" + urlString);
        URL url = new URL(urlString);
        RssFeed feed = RssReader.read(url);
        return  feed.getRssItems();
    }
}
