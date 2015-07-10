package com.example.topnewgrid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.ResultUtil;

import java.util.ArrayList;
import java.util.List;

import http.Manage;

/**
 * Created by 真爱de仙 on 2015/1/18.
 */
public class Activity_xiugai_jianjie extends Activity {

    List<String> imageArray=new ArrayList<>();
    private AppApplication app;
    String filename;

    EditText jianjie;

    private User user;
    private ProgressDialog dialog;
    private Handler dialogHandler;
    String uploadUrl ="http://121.41.74.72:8080/SoLoMo/android/uploadImage.jsp?";


    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xiugai_jianjie);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initUser();
        initHandler();
        initLayout();
        initListener();


    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();
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
                        setResult(3);
                        finish();
                        break;

                    default:
                        Toast.makeText(Activity_xiugai_jianjie.this, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }
    private void initLayout(){
       jianjie=(EditText)findViewById(R.id.editText38);
    }

    private void initListener(){


        findViewById(R.id.jianjielayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jianjie.requestFocus();
                InputMethodManager imm3 = (InputMethodManager) jianjie.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        findViewById(R.id.imageView241).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.textView253).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHandler.sendEmptyMessage(0);
                final String jianjieText=jianjie.getText().toString().trim();

                if(jianjieText.equals("")){
                    Toast.makeText(Activity_xiugai_jianjie.this,"简介不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Manage.AlterUser(user.getUserId() + "", "", "", "", jianjieText);
                            dialogHandler.sendEmptyMessage(1);
                            Intent intent=new Intent();
                            intent.putExtra("jianjie",jianjieText);
                            setResult(ResultUtil.XIUGAIJIANJIE,intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialogHandler.sendEmptyMessage(1);
                        }
                        dialogHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
