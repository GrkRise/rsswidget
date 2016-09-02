package com.rizzhivaykini.rsswidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdaterRssService extends RemoteViewsService {



    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsStackFactory(this.getApplicationContext(), intent) ;
    }

    class RemoteViewsStackFactory implements RemoteViewsService.RemoteViewsFactory{
        private List<RssWidgetItem> rssItems = new ArrayList<RssWidgetItem>();
        private Context mContext;
        private int mAppWidgetId;

        public RemoteViewsStackFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            rssItems = load(mAppWidgetId);
        }



        @Override
        public void onDestroy() {
            rssItems.clear();
        }

        @Override
        public int getCount() {
            return rssItems.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            remoteViews.setTextViewText(R.id.title, rssItems.get(i).getTitle());
            remoteViews.setTextViewText(R.id.news, rssItems.get(i).getDesc());

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private List<RssWidgetItem> load(int id){
            List<RssWidgetItem> rssItems = new ArrayList<RssWidgetItem>();
            try {
                SharedPreferences sp = mContext.getSharedPreferences(RssLoadService.RSS_PREFS, 0);
                String rss = sp.getString(RssLoadService.PREFIX + id, null);
                if(!TextUtils.isEmpty(rss)){
                    JSONArray jsonArray = new JSONArray(rss);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        rssItems.add(new RssWidgetItem(jsonObject.getString("title"),
                                jsonObject.getString("text")));
                    }
                }

                } catch (JSONException e) {
                    e.printStackTrace();
            }


            return rssItems;
        }

        public void onDataSetChanged(){
            rssItems = load(mAppWidgetId);
        }
    }

}
