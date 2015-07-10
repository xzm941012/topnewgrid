package com.example.topnewgrid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import http.Manage;
import io.rong.imkit.RongIM;

/**
 * Created by 真爱de仙 on 2015/1/18.
 */
public class Activity_bieren extends FragmentActivity implements View.OnClickListener{

    private AppApplication app;
    Thread thread;
    private Fragment_bieren fragment_bieren=new Fragment_bieren();
    private User user;
    public static String id;
    private ImageView touxiang1;
    private ImageDownloader imageDownloader;
    private ImageView fanhui;
    private TextView name,locate,sex,joinTime,fabuActivity,joinActivity,tuku,seneMessage,joinFriend,jianjie;
    private TextView getFriend,sendMessage;
    private Handler handler,handler2,handler3,handler4,handler5;
    public static OtherUser otherUser;
    private int hasFriend=0; //0不是好友 1是好友
    private ProgressDialog dialog;
    private List<Huodong>  huodongList;
    private PullToRefreshListView listView;
    private Handler dialogHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bieren4);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        id=bundle.getString("id");

        dialog = new ProgressDialog(Activity_bieren.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initHandler();
        initUser();
        initLayout();
        initListener();
        initParam();


    }
    private void initParam(){

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //name,locate,sex,joinTime,fabuActivity,joinActivity,tuku,seneMessage,joinFriend;
                    dialogHandler.sendEmptyMessage(0);
                    otherUser=Manage.FindUserById(id);

                    dialogHandler.sendEmptyMessage(1);
                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                    editor.putString(id+"",gson.toJson(otherUser));
                    editor.commit();
                    if(thread.isInterrupted()){
                        return;
                    }
                    handler2.sendEmptyMessage(1);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        thread.start();
    }
    private void initHandler(){
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                FragmentTransaction ts=getSupportFragmentManager().beginTransaction();
                ts.replace(R.id.frameLayout2, fragment_bieren) .commitAllowingStateLoss();            }
        };
        dialogHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        dialog.show();
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            }
        };
    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();
        List<OtherUser>otherUserList1=user.getFriendList();
        if(otherUserList1==null){
            otherUserList1=new ArrayList<>();
        }
        for(OtherUser otherUser1:otherUserList1){
            if((otherUser1.getId()+"").equals(id)){
                hasFriend=1;
            }
        }
    }

    private void initLayout(){
        getFriend=(TextView)findViewById(R.id.textView206);
        sendMessage=(TextView)findViewById(R.id.textView205);
        //locate=(TextView)findViewById(R.id.textView5);
        if(hasFriend==1){
            getFriend.setText("取消关注");
        }
    }
    private void initListener(){
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // Cancel task.
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(thread!=null){
                        thread.interrupt();
                        dialog.dismiss();
                        finish();
                    }
                }
                return false;
            }
        });
        findViewById(R.id.sendMessage).setOnClickListener(this);
        findViewById(R.id.relativeLayout25).setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessage:
                if (user != null) {
                    RongIM.getInstance().startPrivateChat(Activity_bieren.this, id, "与" + otherUser.getUserName() + "的聊天");
                } else {
                    startActivityForResult(new Intent(Activity_bieren.this, Activity_login.class), 1);
                }
                break;
            case R.id.relativeLayout25:
                if (user != null) {
                    FoucousFriendTask task = new FoucousFriendTask();
                    task.execute(user.getUserId(), id);
                } else {
                    startActivityForResult(new Intent(Activity_bieren.this, Activity_login.class), 1);
                }
                break;
        }
    }
    class FoucousFriendTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String id=params[0];
            String friendId=params[1];
            String result=null;
            if(id.equals(friendId)){
                result="不能关注自己";
                return result;
            }
            try {
                result=Manage.FocusingFriend(id,friendId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result){
            dialog.dismiss();
            System.out.println(result);
            app.zhuangtai=2;
            if(result.contains("成功")) {
                if(hasFriend==0) {
                    Toast.makeText(Activity_bieren.this, "关注成功", Toast.LENGTH_SHORT).show();
                    getFriend.setText("取消关注");
                    user.getFriendList().add(otherUser);
                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.mySharedPreferences.edit();
                    editor.putString(user.getUserId()+"",gson.toJson(user.getFriendList()));
                    editor.commit();
                    hasFriend=1;

                }else{
                    Toast.makeText(Activity_bieren.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                    getFriend.setText("加关注");
                    List<OtherUser>friendList2=new ArrayList<OtherUser>();

                    for(OtherUser otherUser1:user.getFriendList()){
                        if(!(otherUser1.getId()+"").equals(id)){
                            friendList2.add(otherUser1);
                        }
                    }
                    user.setFriendList(friendList2);
                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.mySharedPreferences.edit();
                    editor.putString(user.getUserId()+"",gson.toJson(friendList2));
                    editor.commit();
                    hasFriend=0;
                }

            }else if(result.contains("不能关注自己")){
                Toast.makeText(Activity_bieren.this, "不能关注自己", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Activity_bieren.this, "操作异常", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected void onPreExecute(){
            dialog.show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...

            if(thread!=null){
                if(thread.isAlive()){
                    thread.interrupt();
                }
                finish();
                dialog.dismiss();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
