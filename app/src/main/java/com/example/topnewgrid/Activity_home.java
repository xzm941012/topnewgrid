package com.example.topnewgrid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.User;

import cn.jpush.android.api.JPushInterface;


public class Activity_home extends FragmentActivity implements View.OnClickListener{
//晚上资料只是确认自己的标签，更具标签选择活动标签
    private AppApplication app;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        initUser();
        ImageView imageView=(ImageView)findViewById(R.id.imageView101);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.enlarge);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        start();
    }
    private void start(){
        if(isNetworkConnected()){
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(4000);
                        if(user!=null){
                            startActivity(new Intent(Activity_home.this,MainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(Activity_home.this,Activity_login.class));
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }else{
            //弹出对话框 让用户设置网络
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("设置网络");
            builder.setMessage("网络错误请设置网络");
            builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = null;
                    // 先判断当前系统版本
                    if(android.os.Build.VERSION.SDK_INT > 13 ){     //3.2以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),1);
                    } else {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS),1);
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("yunxingle");
    }
    private boolean isNetworkConnected(){
        ConnectivityManager cm =	(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info =cm.getActiveNetworkInfo();
        if(info!=null&&info.isConnected()){
            return true;
        }else {
            return false ;
        }
    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();
    }
    private void initLayout(){

    }
    private void initListener(){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                start();
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}