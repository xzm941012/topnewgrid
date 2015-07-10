package com.example.topnewgrid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.topnewgrid.Activity_huodong_xiangqing;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.ReceiverMessage;
import com.example.topnewgrid.util.ResultUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 真爱de仙 on 2015/2/25.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<ReceiverMessage>receiverMessages;
        Gson gson=new Gson();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            System.out.println("titile:"+title);
            System.out.println("content:"+content);
            System.out.println("extra:"+extra);

            try {
                JSONObject object=new JSONObject(extra);
                String type=object.getString("type");
                //广播
                if(type.equals(ResultUtil.RECEIVER)){
                    String huodongId=object.getString("groupid");
                    Intent mIntent = new Intent(context, Activity_huodong_xiangqing.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",huodongId);
                    mIntent.putExtras(mBundle);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mIntent);
                }//邀请
                else if(type.equals(ResultUtil.YAOQING)){
                    String huodongId=object.getString("groupid");
                    Intent mIntent = new Intent(context, Activity_huodong_xiangqing.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",huodongId);
                    mIntent.putExtras(mBundle);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mIntent);
                }else if(type.equals(ResultUtil.HUIFU)){
                    String huodongId=object.getString("groupid");
                    Intent mIntent = new Intent(context, Activity_huodong_xiangqing.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id",huodongId);
                    mIntent.putExtras(mBundle);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(mIntent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            String result=AppApplication.message_unread_SharedPreferences.getString("unread","");
            if(!result.equals("")){
                System.out.println("未读列表不为空");
                receiverMessages=gson.fromJson(result, new TypeToken<List<ReceiverMessage>>(){}.getType());
            }else{
                System.out.println("未读列表为空");
                receiverMessages=new ArrayList<>();
            }
            System.out.println("用户收到了通知");
            Bundle bundle = intent.getExtras();
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONObject object= null;
            try {
                object = new JSONObject(extra);
                String type=object.getString("type");
                if(type.equals(ResultUtil.HUIFU)){
                    System.out.println("这是一个回复");
                    ReceiverMessage ms=new ReceiverMessage();
                    ms.setUserid(object.getString("userid"));
                    ms.setHuodongid(object.getString("groupid"));
                    ms.setUsername(object.getString("username"));
                    ms.setType(type);
                    ms.setContent(object.getString("pingluncontant"));
                    receiverMessages.add(ms);
                }else if(type.equals(ResultUtil.YAOQING)){
                    System.out.println("这是一个邀请");
                    ReceiverMessage ms=new ReceiverMessage();
                    ms.setUserid(object.getString("userid"));
                    ms.setType(type);
                    ms.setHuodongid(object.getString("groupid"));
                    ms.setUsername(object.getString("username"));
                    ms.setHuodongname(object.getString("huodongname"));
                    receiverMessages.add(ms);
                }else if(type.equals(ResultUtil.RECEIVER)){
                    System.out.println("这是一个广播");
                    ReceiverMessage ms=new ReceiverMessage();
                    ms.setUserid(object.getString("userid"));
                    ms.setType(type);
                    ms.setHuodongid(object.getString("groupid"));
                    ms.setUsername(object.getString("username"));
                    ms.setContent(object.getString("content"));
                    receiverMessages.add(ms);
                }
                if(receiverMessages.size()==0){
                    System.out.println("没有插广播");
                    SharedPreferences.Editor editor = AppApplication.message_unread_SharedPreferences.edit();
                    editor.putString("unread","");
                    editor.commit();
                }else{
                    System.out.println("插入广播");
                    SharedPreferences.Editor editor = AppApplication.message_unread_SharedPreferences.edit();
                    editor.putString("unread",gson.toJson(receiverMessages));
                    editor.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
