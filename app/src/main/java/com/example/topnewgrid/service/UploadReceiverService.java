package com.example.topnewgrid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.baidu.location.BDLocation;
import com.example.topnewgrid.Activity_fabu_receiver;
import com.example.topnewgrid.R;
import com.example.topnewgrid.choosephotos.util.Bimp;
import com.example.topnewgrid.choosephotos.util.DialogUtil;
import com.example.topnewgrid.choosephotos.util.PictureNarrowUtils;
import com.example.topnewgrid.choosephotos.util.UploadFile;
import com.example.topnewgrid.obj.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.Manage;

/**
 * Created by 真爱de仙 on 2015/6/7.
 */
public class UploadReceiverService extends Service {
    Thread thread;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    String contentup;
    User user;
    String huodongID,huodongTitle;
    BDLocation location;
    String nowTime;
    public List<String> videoArray=new ArrayList<>();
    public List<String> imageArray=new ArrayList<>();
    int notifyId;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    break;
                case 3:
                    Map<String,String>map1=(HashMap)msg.obj;
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
        contentup= Activity_fabu_receiver.activity_fabu_receiver.contentup;
        notifyId=Activity_fabu_receiver.activity_fabu_receiver.notifyId;
        videoArray=Activity_fabu_receiver.activity_fabu_receiver.videoArray;
        imageArray=Activity_fabu_receiver.activity_fabu_receiver.imageArray;
        user=Activity_fabu_receiver.activity_fabu_receiver.user;
        nowTime=Activity_fabu_receiver.activity_fabu_receiver.nowTime;
        location=Activity_fabu_receiver.activity_fabu_receiver.location;
        huodongID=Activity_fabu_receiver.activity_fabu_receiver.huodongID;
        huodongTitle=Activity_fabu_receiver.activity_fabu_receiver.huodongTitle;
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                loading();
                            /*
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            */

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
                mBuilder.setContentTitle(contentup)
                        .setContentText("进度:")
                        .setTicker("开始上传");// 通知首次出现在通知栏，带上升动画效果的
                mBuilder.setProgress(100, 100, true); // 这个方法是显示进度条  设置为true就是不确定的那种进度条效果
                mNotificationManager.notify(notifyId, mBuilder.build());
                int i=1;
                try {
                    StringBuffer array1=new StringBuffer("");
                    StringBuffer array2=new StringBuffer("");

                    if(imageArray!=null&&imageArray.size()!=0){
                        System.out.println("开始上传图片");
                        for(String a:imageArray){
                            Map<String,String> map=new HashMap<>();
                            System.out.println("地址："+a);

                            map.put("imageNum",i+"");
                            mBuilder.setContentText("进度:正在上传第"+i+"张图片");
                            mNotificationManager.notify(notifyId, mBuilder.build());
                            File mediaFile = new File(a);
                            System.out.println("绝对地址："+mediaFile.getAbsolutePath());
                            String str=a.substring(a.lastIndexOf("/") + 1, a.lastIndexOf("."));
                            System.out.println("str："+str);
                            UploadFile.uploadStreamFile(PictureNarrowUtils.SDPATH + str + ".JPEG", "receiverimageid" + user.getUserId() + "time" + nowTime + i + ".JPEG");
                            array1.append("receiverimageid"+user.getUserId()+"time"+nowTime+i+".JPEG"+";");
                            i++;
                        }
                    }

                    if(videoArray!=null&&videoArray.size()!=0){
                        for(String a:videoArray){
                            System.out.println("开始上传视频");
                            File mediaFile = new File(a);
                            System.out.println("视频绝对地址为:"+mediaFile.getAbsolutePath());
                            System.out.println("视频上传地址为:"+mediaFile.getName()+"user"+user.getUserId()+nowTime+";");
                            UploadFile.uploadVedioFile(mediaFile.getAbsolutePath(), "receivervideoid"+user.getUserId()+"time"+nowTime+i+"."+mediaFile.getName().split("\\.")[1],handler);
                            array2.append("receivervideoid"+user.getUserId()+"time"+nowTime+i+"."+mediaFile.getName().split("\\.")[1]+";");
                            i++;
                        }
                    }

                    String result;
                    if(location!=null) {
                        result = Manage.addBroadcast(contentup, huodongID + "", array1.toString(), array2.toString(), location.getAddrStr(), location.getLatitude() + "", location.getLongitude() + "");
                    }else {
                        result = Manage.addBroadcast(contentup, huodongID + "", array1.toString(), array2.toString(),"","","");
                    }

                    if(result.contains("添加成功")){
                        Manage.sendHuodongReceiver(huodongID,contentup,huodongTitle,user.getUserId()+"",user.getName());
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
                        mBuilder.setContentText("上传完成")
                                .setTicker("上传完成");// 通知首次出现在通知栏，带上升动画效果的
                        mNotificationManager.notify(notifyId, mBuilder.build());
                        mBuilder.setAutoCancel(true);
                    }
                } catch (Exception e) {
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
                    mBuilder.setContentText("上传失败")
                            .setTicker("上传完成");// 通知首次出现在通知栏，带上升动画效果的
                    mNotificationManager.notify(notifyId, mBuilder.build());
                    mBuilder.setAutoCancel(true);
                    System.out.println("错误:"+e.toString());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
    public void loading() {
        for (int i=0;i<imageArray.size();i++) {
            try {
                String path=imageArray.get(i);
                Bitmap bm = Bimp.revitionImageSize(path);
                if(bm!=null){
                    System.out.println("bitmap not null");
                }
                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                PictureNarrowUtils.saveBitmap(bm, "" + newStr);
                Message message = new Message();
                //upLoadhand.sendEmptyMessage(0x8);
            } catch (IOException e) {
                DialogUtil.showDialog(getApplicationContext(), e.toString(), true);
            }
        }

    }
}
