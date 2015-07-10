package com.example.topnewgrid.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.example.topnewgrid.Activity_fabu_huodong_step3;
import com.example.topnewgrid.Activity_huodong_xiangqing;
import com.example.topnewgrid.R;
import com.example.topnewgrid.adapter.EditSeeAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.choosephotos.util.Bimp;
import com.example.topnewgrid.choosephotos.util.DialogUtil;
import com.example.topnewgrid.choosephotos.util.PictureNarrowUtils;
import com.example.topnewgrid.choosephotos.util.UploadFile;
import com.example.topnewgrid.obj.Label_edit;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.Manage;
import http.PostUrl;

/**
 * Created by 真爱de仙 on 2015/5/30.
 */

public class UploadService extends Service {
    Context context;
    Thread thread;
    String filename;
    private User user;
    private AppApplication app;
    EditSeeAdapter adapter;
    int notifyId;
    NotificationManager mNotificationManager;
    String uploadUrl ="http://"+ PostUrl.Media+":8080/Server/android/uploadImage.jsp?";
    NotificationCompat.Builder mBuilder;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler3=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    break;
                case 3:
                    Map<String,String> map1=(HashMap)msg.obj;
                    String videoNum=map1.get("videoNum");
                    mBuilder.setProgress(100, Integer.parseInt(videoNum), false);
                    mNotificationManager.notify(notifyId, mBuilder.build());
                    break;
                case 2:
                    break;
                default:
                    break;

            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        app = (AppApplication) getApplication();
        user=app.getUser();
        thread=new Thread() {
            @Override
            public void run() {
                List<String> imageArray= Activity_fabu_huodong_step3.activity_fabu_huodong_step3.imageArray;
                List<String> videoArray= Activity_fabu_huodong_step3.activity_fabu_huodong_step3.videoArray;
                String locationAdress=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.locationAdress;
                String locationPoi_Adress=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.locationPoi_Adress;
                String locationPoi=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.locationPoi;
                String locationx=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.locationx;
                String locationy=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.locationy;
                final String title=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.titleup;
                String jineup=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.jineup;
                Double longitude=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.longitude;
                Double latitude=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.latitude;
                String district=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.district;
                final String fenleiText=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.fenleiTextup;
                notifyId=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.notifyId;
                adapter=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.adapter;
                File tempFile=Activity_fabu_huodong_step3.activity_fabu_huodong_step3.tempFile;
                mNotificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notifyId=app.titleBuild++;
                mNotificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                mBuilder = new NotificationCompat.Builder(getApplication());
                // mBuilder.addAction(R.drawable.ic_text_holo_dark,"取消",null);
                mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                        .setContentIntent(PendingIntent.getActivity(getApplication(), 1, new Intent(), 0))
                                // .setNumber(number)//显示数量
                        .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(true)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                                // requires VIBRATE permission
                        .setSmallIcon(R.drawable.ic_launcher);
                mBuilder.setContentTitle(title)
                        .setContentText("进度:")
                        .setTicker("开始上传");// 通知首次出现在通知栏，带上升动画效果的
                mBuilder.setProgress(100, 100, true); // 这个方法是显示进度条  设置为true就是不确定的那种进度条效果
                mNotificationManager.notify(notifyId, mBuilder.build());
                StringBuffer asd=new StringBuffer();
                // asd.append("标题:"+title+"  "+"分类:"+fenleiText+"  "+"内容:"+neirongText+"  "+"结束时间:"+endTimeText+"  "+"人数:"+renshuText+"  "+"需要图片:"+iftupiantext+"  "+"userId:"+user.getUserId()+"详细地址"+xiangxiLocate2);

                if (!title.equals("") ) {
                    String picturePath = "";
                    try {
                        loading();

                        StringBuffer str = new StringBuffer();
                        Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置显示格式
                        String nowTime="";
                        nowTime= df.format(dt);//用DateFormat的format()方法在dt中获取并以yyyy/MM/dd HH:mm:ss格式显示
                        filename="imageid"+user.getUserId() + nowTime + 1 + ".jpg";
                        com.example.topnewgrid.util.UploadFile uploadFile = new com.example.topnewgrid.util.UploadFile(uploadUrl);
                        /*
                        uploadFile.defaultUploadMethod(photodata, filename);
                        str.append(filename+";");
                        */
                        int i=2;

                        if(videoArray!=null&&videoArray.size()!=0){
                            for(String a:videoArray){
                                mBuilder.setContentText("进度:正在上传视频");
                                System.out.println("开始上传视频");
                                File mediaFile = new File(a);
                                System.out.println("视频绝对地址为:"+mediaFile.getAbsolutePath());
                                System.out.println("视频上传地址为:"+mediaFile.getName()+"user"+user.getUserId()+nowTime+";");

                                UploadFile.uploadVedioFile(mediaFile.getAbsolutePath(), "video" + user.getUserId() + nowTime + i + mediaFile.getName(), handler3);
                                str.append("video"+user.getUserId()+nowTime+i+mediaFile.getName()+";");
                                i++;
                            }
                        }else  if(imageArray!=null&&imageArray.size()!=0){
                            /*
                            Message ms=new Message();
                            ms.what=0;
                            handler2.sendMessage(ms);
                            */
                            //uploadFile.defaultUploadMethod(photodata, filename);
                            //str.append(filename+";");
                            mBuilder.setContentText("进度:正在上传封面");
                            mNotificationManager.notify(notifyId, mBuilder.build());
                            String upstr=tempFile.getAbsolutePath().substring(tempFile.getAbsolutePath().lastIndexOf("/") + 1, tempFile.getAbsolutePath().lastIndexOf("."));
                            UploadFile.uploadStreamFile(PictureNarrowUtils.SDPATH + upstr+ ".JPEG", "imageid"+user.getUserId()+"time"+nowTime+i+".JPEG");
                            str.append("imageid"+user.getUserId()+"time"+nowTime+i+".JPEG"+";");
                            i++;
                        }
                        i++;
/*
                        if(photoList.size()!=0){
                            int tupianNum=1;
                            for(Item item:photoList){
                                mBuilder.setContentText("进度:正在上传第"+tupianNum+"张图片");
                                mNotificationManager.notify(notifyId, mBuilder.build());
                                tupianNum++;
                                File mediaFile = new File(item.getPhotoPath());
                                String upstr=mediaFile.getAbsolutePath().substring(mediaFile.getAbsolutePath().lastIndexOf("/") + 1, mediaFile.getAbsolutePath().lastIndexOf("."));
                                UploadFile.uploadStreamFile(PictureNarrowUtils.SDPATH + upstr+ ".JPEG", "imageid"+user.getUserId()+"time"+nowTime+i+".JPEG");
                                str.append("imageid"+user.getUserId()+"time"+nowTime+i+".JPEG"+";");
                                i++;
                            }
                        }
                        */
                        mBuilder.setContentText("进度:正在上传图文详情");
                        mNotificationManager.notify(notifyId, mBuilder.build());
                        List<Label_edit> edits2=adapter.mItems;
                        for(Label_edit label_edit:edits2){
                            if(label_edit.getType()==1){
                                File mediaFile = new File(label_edit.getBitmapPath());
                                String upstr=mediaFile.getAbsolutePath().substring(mediaFile.getAbsolutePath().lastIndexOf("/") + 1, mediaFile.getAbsolutePath().lastIndexOf("."));
                                UploadFile.uploadStreamFile(PictureNarrowUtils.SDPATH + upstr+ ".JPEG", "imageid"+user.getUserId()+"time"+nowTime+i+".JPEG");
                                label_edit.setUrlPath("imageid"+user.getUserId()+"time"+nowTime+i+".JPEG"+"");
                                i++;
                            }
                        }
                        Gson gson=new Gson();
                        String neirongText=gson.toJson(edits2);
                        String groupId="";
                        mBuilder.setContentText("进度:正在上传第活动信息图片");
                        mNotificationManager.notify(notifyId, mBuilder.build());

                        if(jineup.equals("金额")) {
                            groupId = Manage.addActivity(title, fenleiText, str.toString(), neirongText, "", "", "", user.getName(), user.getUserId(), longitude + "", latitude + "", district, "需求",Activity_fabu_huodong_step3.activity_fabu_huodong_step3.editText4,"", "", "0", user.getSex(), locationx, locationy, locationAdress, locationPoi, locationPoi_Adress, user.getSex());
                        }else{
                            groupId = Manage.addActivity(title, fenleiText, str.toString(), neirongText, "", "","", user.getName(), user.getUserId(), longitude + "", latitude + "", district, "需求", Activity_fabu_huodong_step3.activity_fabu_huodong_step3.textView169,"", "", "0", user.getSex(), locationx, locationy, locationAdress, locationPoi, locationPoi_Adress, user.getSex());
                        }
                        SharedPreferences edit_xiangqing=getSharedPreferences("edit",
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = edit_xiangqing.edit();
                        editor.putString(user.getUserId()+"",gson.toJson(edits2));
                        editor.commit();
                        groupId=groupId.trim();
                        SharedPreferences.Editor editor2 = edit_xiangqing.edit();
                        editor2.putString(user.getUserId()+"","");
                        editor2.commit();
                        System.out.println("groupId"+groupId);
                        int groupids=Integer.parseInt(groupId);
                        groupId=groupids+"";
                        System.out.println("活动id为"+groupId);
                        //System.out.println(Manage.createGroup("1",groupId,title));
                        /*
                        Intent intent=new Intent();
                        Bundle b=new Bundle();
                        b.putString("groupid",groupId);
                        intent.putExtras(b);
                        setResult(3,intent);
                        */
                        /*
                        Intent intent = new Intent();
                        intent.setClass(Activity_fabu_huodong_step3.this, Activity_huodong_xiangqing.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("id",groupId + "");
                        mBundle.putString("authorId",user.getUserId()+"");
                        intent.putExtras(mBundle);
                        startActivity(intent);
                        */
                        //Activity_fabu_huodong_step1.activity_fabu_huodong_step1.finish();
                        mBuilder = new NotificationCompat.Builder(getApplication());
                        // mBuilder.addAction(R.drawable.ic_text_holo_dark,"取消",null);
                        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                                .setContentIntent(PendingIntent.getActivity(getApplication(), 1, new Intent(), 0))
                                        // .setNumber(number)//显示数量
                                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                                        // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                        // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                                        // requires VIBRATE permission
                                .setSmallIcon(R.drawable.ic_launcher);
                        mBuilder.setContentTitle(title)
                                .setContentText("上传完成")
                                .setTicker("上传完成");// 通知首次出现在通知栏，带上升动画效果的
                        //mNotificationManager.notify(notifyId, mBuilder.build());
                        mBuilder.setAutoCancel(true);
                        Intent resultIntent = new Intent(getApplicationContext(), Activity_huodong_xiangqing.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("id",groupId + "");
                        mBundle.putString("authorId",user.getUserId()+"");
                        resultIntent.putExtras(mBundle);
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);
                        mNotificationManager.notify(notifyId, mBuilder.build());
                    } catch (Exception e) {
                        System.out.println("错误:"+e.toString());
                        mBuilder = new NotificationCompat.Builder(getApplication());
                        // mBuilder.addAction(R.drawable.ic_text_holo_dark,"取消",null);
                        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                                .setContentIntent(PendingIntent.getActivity(getApplication(), 1, new Intent(), 0))
                                        // .setNumber(number)//显示数量
                                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                                        // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                        // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                                        // requires VIBRATE permission
                                .setSmallIcon(R.drawable.ic_launcher);
                        mBuilder.setContentTitle(title)
                                .setContentText("上传失败")
                                .setTicker("上传失败");// 通知首次出现在通知栏，带上升动画效果的
                        //mNotificationManager.notify(notifyId, mBuilder.build());
                        mBuilder.setAutoCancel(true);
                        mNotificationManager.notify(notifyId, mBuilder.build());
                    }
                }
            }
        };
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
    public void loading() {
        /*
        for (int i=0;i<photoList.size();i++) {
            try {
                String path=photoList.get(i).getPhotoPath();
                Bitmap bm = Bimp.revitionImageSize(path);
                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                PictureNarrowUtils.saveBitmap(bm, "" + newStr);

                //upLoadhand.sendEmptyMessage(0x8);
            } catch (IOException e) {
                DialogUtil.showDialog(Activity_fabu_huodong_step3.this,e.toString(), true);
            }
        }
        */
        for (int i=0;i<adapter.mItems.size();i++) {
            try {
                if(adapter.mItems.get(i).getType()==1) {
                    String path = adapter.mItems.get(i).getBitmapPath();
                    Bitmap bm = Bimp.revitionImageSize(path);
                    String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                    PictureNarrowUtils.saveBitmap(bm, "" + newStr);
                }

                //upLoadhand.sendEmptyMessage(0x8);
            } catch (IOException e) {
                DialogUtil.showDialog(getApplicationContext(), e.toString(), true);
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

