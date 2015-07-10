package com.example.topnewgrid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.cyberplayer.sdk.BEngineManager;
import com.baidu.cyberplayer.sdk.BMediaController;
import com.baidu.cyberplayer.sdk.BVideoView;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.User;

/**
 * Created by 真爱de仙 on 2015/1/18.
 */
public class Activity_video extends Activity  {
    BVideoView mVV;
    BMediaController mVVCtl;

    private AppApplication app;
    String path;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        BEngineManager mgr =
                BCyberPlayerFactory.createEngineManager();
        mgr.initCyberPlayerEngine("5kGqGc0028p2D63Y0m1TrasG","N90irp48shAXRziv");

        setContentView(R.layout.activity_videoview);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        path=bundle.getString("path");
        Uri uri = Uri.parse(path);
        System.out.println("视频地址为:"+path);


        initHandler();
        initUser();
        initLayout();
        initListener();
        initParam();

    }
    private void initParam(){

    }
    private void initHandler(){

    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();

    }

    private void initLayout(){
        mVV=(BVideoView)findViewById(R.id.videoview);
        mVVCtl=(BMediaController)findViewById(R.id.controllerbar);
        mVV.setMediaController(mVVCtl);
        mVV.setDecodeMode(BVideoView.DECODE_SW); //可选择软解模式或硬解模式
        mVV.setVideoPath(path);
        mVV.start();


    }
    private void initListener(){

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
