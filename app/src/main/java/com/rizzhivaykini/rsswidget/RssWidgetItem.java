package com.rizzhivaykini.rsswidget;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;






/**
 * Created by GrkRise on 23.08.2016.
 */

public class RssWidgetItem {
    private  String title;
    private  String desc;

    public RssWidgetItem(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }


}
