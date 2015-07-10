package com.example.topnewgrid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.GraphicsBitmapUtils;
import com.example.topnewgrid.util.ResultUtil;
import com.example.topnewgrid.util.UploadFile;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import http.Manage;
import mediachooser.MediaChooser;
import mediachooser.activity.HomeFragmentActivity;

/**
 * Created by 真爱de仙 on 2015/1/18.
 */
public class Activity_wanshang_ziliao extends Activity {

    List<String> imageArray=new ArrayList<>();
    private AppApplication app;
    String filename;
    TextView sex;
    TextView jianjie;

    Thread thread;
    private User user;
    private ProgressDialog dialog;
    private Handler dialogHandler;
    BroadcastReceiver imageBroadcastReceiver;
    String uploadUrl ="http://121.41.74.72:8080/SoLoMo/android/uploadImage.jsp?";


    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wanshan_ziliao);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initUser();
        initHandler();
        initLayout();
        initListener();
        MediaChooser.setSelectionLimit(1);
        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(Activity_wanshang_ziliao.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                imageArray=(intent.getStringArrayListExtra("list"));
                if(imageArray.size()!=0) {
                    startPhotoZoom(Uri.fromFile(new File(imageArray.get(0))), 150);
                }
            }
        };
        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);

    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();
        filename=user.getUserId()+".jpg";
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
                    case 2:
                        if (msg.obj != null) {
                            Drawable drawable = new BitmapDrawable(getResources(),(Bitmap) msg.obj);
                            ((ImageView)findViewById(R.id.imageView220)).setImageDrawable(drawable);
                        }else
                        {
                            Toast.makeText(Activity_wanshang_ziliao.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        Toast.makeText(Activity_wanshang_ziliao.this, "提交成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(Activity_wanshang_ziliao.this, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }
    private void initLayout(){
        sex=(TextView)findViewById(R.id.textView78);
        jianjie=(TextView)findViewById(R.id.textView);
    }
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 110);
        intent.putExtra("outputY", 110);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 3);
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
        findViewById(R.id.imageView220).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaChooser.setSelectedMediaCount(0);
                MediaChooser.showOnlyImageTab();
                MediaChooser.setSelectionLimit(1);
                Intent intent = new Intent(Activity_wanshang_ziliao.this, HomeFragmentActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sexlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[]a={"男","女"};
                AlertDialog.Builder builder3 = new AlertDialog.Builder(Activity_wanshang_ziliao.this);

                builder3.setItems(a, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        if(arg1==0){
                            sex.setText("男");
                        }else if(arg1==1) {
                            sex.setText("女");
                        }
                    }
                });
                builder3.show();
            }
        });
        findViewById(R.id.jianjielayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Activity_wanshang_ziliao.this,Activity_xiugai_jianjie.class),999);

            }
        });
        findViewById(R.id.imageView236).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.textView248).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHandler.sendEmptyMessage(0);
                thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sexText=sex.getText().toString().trim();
                        String jianjieText=jianjie.getText().toString().trim();
                        if(!sexText.contains("点击选择性别")){
                            if(jianjieText!=null&&!jianjieText.equals("")){
                                try {
                                    Manage.AlterUser(user.getUserId() + "", sexText, "", "", jianjieText);
                                    dialogHandler.sendEmptyMessage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    Manage.AlterUser(user.getUserId() + "", sexText, "", "", "");
                                    dialogHandler.sendEmptyMessage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            if(jianjieText!=null&&!jianjieText.equals("")){
                                try {
                                    Manage.AlterUser(user.getUserId() + "", "", "", "", jianjieText);
                                    dialogHandler.sendEmptyMessage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    Manage.AlterUser(user.getUserId() + "", "", "", "", "");
                                    dialogHandler.sendEmptyMessage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if(thread.isInterrupted()){
                            return;
                        }
                        user.setSex(sexText);
                        user.setJianjie(jianjieText);
                        ((AppApplication) getApplication()).setUser(user);
                        Gson gson=new Gson();
                        SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                        editor.putString("user",gson.toJson(user));
                        editor.commit();
                        startActivity(new Intent(Activity_wanshang_ziliao.this,MainActivity.class));
                        finish();
                    }
                });
                thread.start();

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
        if(resultCode== ResultUtil.XIUGAIJIANJIE){
            jianjie.setText(data.getStringExtra("jianjie"));
        }
        switch (requestCode) {
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }
    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            final Bitmap photo = bundle.getParcelable("data");

            new Thread() {

                @Override
                public void run() {
                    byte[] photodata = GraphicsBitmapUtils.Bitmap2Bytes(photo);
                    UploadFile uploadFile = new UploadFile(uploadUrl);
                    Boolean isUploadSuccess;
                    try {
                        isUploadSuccess = uploadFile.defaultUploadMethod(photodata, filename);
                        System.out.println("头像上传结果:"+isUploadSuccess);
                        if (isUploadSuccess) {
                            dialogHandler.obtainMessage(2, photo).sendToTarget();
                        } else {
                            dialogHandler.obtainMessage(-1, null).sendToTarget();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }.start();



        }
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
        unregisterReceiver(imageBroadcastReceiver);
    }
}
