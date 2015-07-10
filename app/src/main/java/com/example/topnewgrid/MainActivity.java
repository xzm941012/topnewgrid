package com.example.topnewgrid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.activity.Fragment_haoyouList;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.ResultUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.HuodongItem;
import http.Httppost;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
//晚上资料只是确认自己的标签，更具标签选择活动标签
    private AppApplication app;
    private User user;
    int twoThouch=0;

    private int i=0;
    private List<HuodongItem> data=new ArrayList<HuodongItem>();
    private Fragment_shejiao_ry fm1=new Fragment_shejiao_ry();
    private Fragment_haoyouList fm2=new Fragment_haoyouList();
    private Fragment_huodong2 fm3=new Fragment_huodong2();
    private Fragment_geren fm4=new Fragment_geren();

    private RadioButton rb1,rb2,rb3,rb4;
    private ImageView touxiang;
    private TextView fabu;

    private ChannelManage channelManage;



    // 用来实现 UI 线程的更新。
    Handler mHandler;

    private Httppost httppost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        init();

    }
    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("yunxingle");
    }

    private void init(){
        initMessage();
        initUser();
        initLayout();
        initListener();
        /*
        FragmentTransaction ts=getSupportFragmentManager().beginTransaction();
        ts.replace(R.id.frameLayout, fm3) .commit();
        */
        findViewById(R.id.bt3).performClick();
    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();
    }
    private void initLayout(){
    }
    private void initMessage(){
        SharedPreferences message_unread_SharedPreferences=getSharedPreferences("unreadmessage",
                Activity.MODE_PRIVATE);
        String result= message_unread_SharedPreferences.getString("unread","");
        if(!result.equals("")){
            System.out.println("未读列表不为空");
            ((CircleImageView)findViewById(R.id.profile_image)).setImageDrawable(getResources().getDrawable(R.color.subscribe_item_selected_stroke));
        }else{
            System.out.println("未读列表为空");
            ((CircleImageView)findViewById(R.id.profile_image)).setImageDrawable(null);
        }
    }
    private void initListener(){
        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt2).setOnClickListener(this);
        findViewById(R.id.bt3).setOnClickListener(this);
        findViewById(R.id.bt4).setOnClickListener(this);
        /*
        findViewById(R.id.fabuhuodong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    startActivity(new Intent(MainActivity.this, com.example.topnewgrid.choosephotos.choosephotos.MainActivity.class));
                }else{
                    startActivityForResult(new Intent(MainActivity.this,Activity_login.class),1);
                }
            }
        });
        findViewById(R.id.textView88).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    startActivityForResult(new Intent(MainActivity.this, Activity_fabu_xuqiu.class),1);
                }else{
                    startActivityForResult(new Intent(MainActivity.this,Activity_login.class),1);
                }
            }
        });
        */
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case ResultUtil.XIUGAIJIANJIE:
                Fragment_geren_view1.fragment_geren_view1.setJianjie(data.getStringExtra("jianjie"));
                break;
            case 1:
                /*
                System.out.println("登陆或者退出");
                startActivity(new Intent(MainActivity.this,Activity_login.class));
                finish();
                */
                initUser();
                initLayout();
                Fragment_huodong2 fm2=new Fragment_huodong2();
                FragmentTransaction ts=getSupportFragmentManager().beginTransaction();
                ts=getSupportFragmentManager().beginTransaction();
                ts.replace(R.id.frameLayout, fm2) .commitAllowingStateLoss();

                break;
            case 3:
                System.out.println("你刚刚发布了活动?");
                break;
            case 4:
                initUser();
                System.out.println(user==null);
                initLayout();
                Fragment_huodong2 fm3=new Fragment_huodong2();
                FragmentTransaction ts1=getSupportFragmentManager().beginTransaction();
                ts1.replace(R.id.frameLayout, fm3) .commitAllowingStateLoss();
                Intent intent=new Intent();
                intent.putExtras(data.getExtras());
                intent.setClass(MainActivity.this,Activity_huodong_xiangqing.class);
                startActivity(intent);
                break;
            case 5:
                Fragment_huodong2 fm4=new Fragment_huodong2();
                FragmentTransaction ts2=getSupportFragmentManager().beginTransaction();
                ts2.replace(R.id.frameLayout, fm4) .commitAllowingStateLoss();
                break;
        }
        if(requestCode==333){
            System.out.println("fragment剪切完成");
            Fragment_geren_view1.fragment_geren_view1.setPicToView(data);
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
            case R.id.bt1:
                if(i!=1) {
                    i=1;
                    ((CircleImageView) findViewById(R.id.profile_image)).setImageDrawable(null);
                    ((ImageView) findViewById(R.id.imageView126)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_processing_outline_press));
                    ((TextView) findViewById(R.id.textView139)).setTextColor(getResources().getColor(R.color.bt));
                    ((ImageView) findViewById(R.id.imageView128)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_multipe_outline));
                    ((TextView) findViewById(R.id.textView140)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView127)).setImageDrawable(getResources().getDrawable(R.drawable.ic_compass_outline));
                    ((TextView) findViewById(R.id.textView137)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView130)).setImageDrawable(getResources().getDrawable(R.drawable.ic_account));
                    ((TextView) findViewById(R.id.textView138)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    FragmentTransaction ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.frameLayout, fm1).commit();
                }
                break;
            case R.id.bt2:
                if(i!=2) {
                    i = 2;
                    ((ImageView) findViewById(R.id.imageView126)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_processing_outline));
                    ((TextView) findViewById(R.id.textView139)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView128)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_multipe_outline_press));
                    ((TextView) findViewById(R.id.textView140)).setTextColor(getResources().getColor(R.color.bt));
                    ((ImageView) findViewById(R.id.imageView127)).setImageDrawable(getResources().getDrawable(R.drawable.ic_compass_outline));
                    ((TextView) findViewById(R.id.textView137)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView130)).setImageDrawable(getResources().getDrawable(R.drawable.ic_account));
                    ((TextView) findViewById(R.id.textView138)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    FragmentTransaction ts2 = getSupportFragmentManager().beginTransaction();
                    ts2.replace(R.id.frameLayout, fm2).commit();
                }
                break;
            case  R.id.bt3:
                if(i!=3) {
                    i = 3;
                    ((ImageView) findViewById(R.id.imageView126)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_processing_outline));
                    ((TextView) findViewById(R.id.textView139)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView128)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_multipe_outline));
                    ((TextView) findViewById(R.id.textView140)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView127)).setImageDrawable(getResources().getDrawable(R.drawable.ic_compass_outline_press));
                    ((TextView) findViewById(R.id.textView137)).setTextColor(getResources().getColor(R.color.bt));
                    ((ImageView) findViewById(R.id.imageView130)).setImageDrawable(getResources().getDrawable(R.drawable.ic_account));
                    ((TextView) findViewById(R.id.textView138)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    FragmentTransaction ts3 = getSupportFragmentManager().beginTransaction();
                    ts3.replace(R.id.frameLayout, fm3).commit();
                }
                break;
            case R.id.bt4:
                if(i!=4) {
                    i = 4;
                    ((ImageView) findViewById(R.id.imageView126)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_processing_outline));
                    ((TextView) findViewById(R.id.textView139)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView128)).setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_multipe_outline));
                    ((TextView) findViewById(R.id.textView140)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView127)).setImageDrawable(getResources().getDrawable(R.drawable.ic_compass_outline));
                    ((TextView) findViewById(R.id.textView137)).setTextColor(getResources().getColor(R.color.subscribe_item_text_color_normal_night));
                    ((ImageView) findViewById(R.id.imageView130)).setImageDrawable(getResources().getDrawable(R.drawable.ic_account_press));
                    ((TextView) findViewById(R.id.textView138)).setTextColor(getResources().getColor(R.color.bt));
                    FragmentTransaction ts4 = getSupportFragmentManager().beginTransaction();
                    ts4.replace(R.id.frameLayout, fm4).commit();
                }
                break;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            if(twoThouch==1){
                finish();
            }else{
                Toast.makeText(MainActivity.this,"再次点击返回键退出",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        twoThouch=1;
                        try {
                            Thread.sleep(3000);
                            twoThouch=0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}