package com.example.topnewgrid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import http.Httppost;
import http.Manage;

/**
 * Created by 真爱de仙 on 2015/1/18.
 */
public class Activity_regist extends Activity {
    private String textname;
    private String textemail;
    private String textpassword1;
    private String textgetPassword2;
    private Httppost httppost;
    private ImageView tuichu;
    private ProgressDialog dialog;
    private Handler dialogHandler;
    private Button zhuce;
    private Handler handler;
    private String phone;
    ChannelManage channelManage;
    private User user;

    private EditText name,password1,password2,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_regist2);
        phone=getIntent().getExtras().getString("phone");

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("注册中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initLayout();
        initHandler();
        initListener();

    }

    private void initHandler(){
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
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(Activity_regist.this, "用户已存在", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(Activity_regist.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 2:
                        Toast.makeText(Activity_regist.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 3:
                        Toast.makeText(Activity_regist.this, "未知错误", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 4:
                        Toast.makeText(Activity_regist.this, "email不能为空", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 5:
                        Toast.makeText(Activity_regist.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 6:
                        Toast.makeText(Activity_regist.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        };
    }
    private void initLayout(){
        tuichu=(ImageView)findViewById(R.id.imageView38);
        httppost=new Httppost();
        zhuce=(Button)findViewById(R.id.button6);
        name=(EditText)findViewById(R.id.editText2);
        password1=(EditText)findViewById(R.id.edt_password);
        password2=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText2);
        email.setText(phone);
        email.setEnabled(false);
    }
    private void initListener(){
        findViewById(R.id.imageView36).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().equals("")){
                    Message ms=new Message();
                    ms.what=4;
                    handler.sendMessage(ms);
                    return ;
                }
                if(password1.getText().toString().trim().equals("")||password2.getText().toString().trim().equals("")){
                    Message ms=new Message();
                    ms.what=5;
                    handler.sendMessage(ms);
                    return ;
                }
                if(name.getText().toString().trim().equals("")){
                    Message ms=new Message();
                    ms.what=6;
                    handler.sendMessage(ms);
                    return ;
                }
                if(!password1.getText().toString().equals(password2.getText().toString())){
                    Message ms=new Message();
                    ms.what=2;
                    handler.sendMessage(ms);
                    return ;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message ms=new Message();
                        dialogHandler.sendEmptyMessage(0);
                        String result= httppost.regist(email.getText().toString().trim(),password1.getText().toString().trim(),password2.getText().toString().trim(),name.getText().toString().trim());
                        System.out.println("regist:"+result);
                        if(result.contains("userExist")){
                            dialogHandler.sendEmptyMessage(1);
                            ms.what=0;
                            handler.sendMessage(ms);
                        }else {
                            System.out.println("1");
                            ms.what=1;
                            user=httppost.login(email.getText().toString().trim(), password1.getText().toString().trim());
                            System.out.println("login:"+user);
                            System.out.println("2");
                            if(user!=null){
                                String token=Manage.getToken(user.getUserId(),user.getName());
                                token = token.substring(token.indexOf("token") + 8, token.indexOf("\"}}"));
                                System.out.println("3");
                                System.out.println(token);
                                user.setToken(token);
                                //initUserParam();
                                AppApplication.initConnect(user);
                                dialogHandler.sendEmptyMessage(1);
                                System.out.println("4");
                                System.out.println("登陆获取到了user"+user.getUserId()+" "+user.getEmail()+" "+user.getName()+" "+user.getHuodongTitle());
                                ( (AppApplication) getApplication()).setUser(user);

                            }
                            Gson gson=new Gson();
                            SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                            editor.putString("user",gson.toJson(user));
                            editor.commit();
                            handler.sendEmptyMessage(1);
                            Intent aintent = new Intent();
                            setResult(1,aintent);
                            finish();
                        }
                    }
                }).start();
            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initUserParam(){
        List<OtherUser> friendList=null;
        List<Huodong> huodongList=null;
        try {
            huodongList= Manage.ViewActivityByUser("1", user.getUserId() + "", "1");
            friendList = Manage.ViewUserByFriend(user.getUserId(), "", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(huodongList!=null) {
            List<String>publicActivity=new ArrayList<String>();
            for(Huodong a:huodongList){
                publicActivity.add(a.getHuodongId()+"");
            }
            user.setPublicActivity(publicActivity);
            Gson gson=new Gson();
            SharedPreferences.Editor editor = AppApplication.activitySharedPreferences.edit();
            editor.putString(user.getUserId()+"",gson.toJson(user.getPublicActivity()));
            editor.commit();
        }

        if(friendList==null){
            friendList=new ArrayList<OtherUser>();
        }
        user.setFriendList(friendList);
        Gson gson=new Gson();
        SharedPreferences.Editor editor = AppApplication.mySharedPreferences.edit();
        editor.putString(user.getUserId()+"",gson.toJson(friendList));
        editor.commit();
        SharedPreferences.Editor editor2 = AppApplication.userSharedPreferences.edit();
        for(OtherUser otherUser:friendList) {
            editor2.putString(otherUser.getId() + "", gson.toJson(otherUser));
        }
        editor2.commit();
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
