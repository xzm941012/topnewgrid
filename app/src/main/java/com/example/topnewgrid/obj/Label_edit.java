package com.example.topnewgrid.obj;

import android.graphics.Bitmap;

/**
 * Created by Õæ°®deÏÉ on 2015/6/2.
 */
public class Label_edit {
    int type; //1=Í¼Æ¬ 0=ÎÄ×Ö
    String wenzi;
    String bitmapPath;
    String urlPath;

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWenzi() {
        return wenzi;
    }

    public void setWenzi(String wenzi) {
        this.wenzi = wenzi;
    }

    public String getBitmapPath() {
        return bitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        this.bitmapPath = bitmapPath;
    }
}
