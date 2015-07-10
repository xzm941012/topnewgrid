package com.example.topnewgrid;


import android.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.GraphicsBitmapUtils;
import com.example.topnewgrid.util.UploadFile;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import http.Httppost;
import http.Manage;
import http.PostUrl;
import mediachooser.MediaChooser;
import mediachooser.activity.HomeFragmentActivity;

//import android.support.v4.view.ViewPager;

/**
 * Created by 真爱de仙 on 2014/11/13.
 */
public class Fragment_geren_view1 extends Fragment implements Handler.Callback{

    public static Fragment_geren_view1 fragment_geren_view1;
    private AppApplication app;
    private User user;
    Thread thread;
    View view;
    int i=0;
    TextView sex;
    final Handler upLoadhand = new Handler(this);
    private static final int PHOTO_REQUEST_CUT = 333;// 结果
    Handler handler;
    private ImageView touxiangImageview;
    String uploadUrl ="http://"+ PostUrl.Media+":8080/Server/android/uploadImage.jsp?";
    String filename;
    OtherUser otherUser=Fragment_geren.otherUser;
    List<String> imageArray=new ArrayList<>();
    IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
    IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
    BroadcastReceiver videoBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getActivity(), "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
        }
    };
    BroadcastReceiver  imageBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("接受广播");
            Toast.makeText(getActivity(), "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
            imageArray = (intent.getStringArrayListExtra("list"));
            if (imageArray.size() != 0) {
                System.out.println("剪切图片");
                startPhotoZoom(Uri.fromFile(new File(imageArray.get(0))), 150);
            }

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("ExampleFragment--onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view=inflater.inflate(R.layout.fragment_geren_view1, container, false);
        fragment_geren_view1=this;
        otherUser=((AppApplication)(getActivity().getApplication())).getOtherUser();
        initUser();
        initHandler();
        initLayout();
        initParam();
        initListener();

        return  view;
    }
    public void setPicToView(Intent picdata) {
        System.out.println("将进行剪裁后的图片显示到UI界面上");
        if(picdata==null){
            return;
        }
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            final Bitmap photo = bundle.getParcelable("data");

            thread=new Thread() {

                @Override
                public void run() {
                    byte[] photodata = GraphicsBitmapUtils.Bitmap2Bytes(photo);
                    UploadFile uploadFile = new UploadFile(uploadUrl);
                    Boolean isUploadSuccess;
                    try {
                        upLoadhand.obtainMessage(0, photo).sendToTarget();
                        isUploadSuccess = uploadFile.defaultUploadMethod(photodata, filename);
                        System.out.println("头像上传结果:"+isUploadSuccess);
                        if(thread.isInterrupted()){
                            return;
                        }
                        if (isUploadSuccess) {
                            /*
                            try {
                                Manage.enrollByActivityId("0", "1", "1", uploadFile.result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            */
                        } else {
                            upLoadhand.obtainMessage(-1, null).sendToTarget();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            thread.start();

        }
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
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        System.out.println("开启裁剪图片");
        getActivity().startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void initUser(){
        app = (AppApplication) getActivity().getApplication();
        user=app.getUser();
        filename=user.getUserId()+".jpg";
    }

    private void initLayout(){
        view.findViewById(R.id.relativeLayout34).setVisibility(View.VISIBLE);
        touxiangImageview=(ImageView)view.findViewById(R.id.imageView90);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(Httppost.urlIsTrue(user.getUserId())) {
                    Bitmap bitmap = Httppost.readImg(user.getUserId());
                    Message ms = new Message();
                    ms.obj = bitmap;
                    handler.sendMessage(ms);
                }
            }
        }).start();
        sex=(TextView)view.findViewById(R.id.textView257);
        ((TextView)view.findViewById(R.id.textView98)).setText(otherUser.getUserName());
        ((TextView)view.findViewById(R.id.textView278)).setText(otherUser.getLevel()+"人关注");
        ((TextView)view.findViewById(R.id.textView208)).setText(otherUser.getRegisterTime());
        ((TextView)view.findViewById(R.id.textView257)).setText(otherUser.getSex());
        //((TextView)view.findViewById(R.id.textView112)).setText(otherUser.getProfession());
        ((TextView)view.findViewById(R.id.textView261)).setText(otherUser.getLevel() + "");
        //((TextView)view.findViewById(R.id.textView231)).setText(otherUser.getPublicNum()+"");
        //((TextView)view.findViewById(R.id.textView234)).setText(otherUser.getJoinNum()+"");
        //((TextView)findViewById(R.id.textView77)).setText(AppApplication.location.getAddrStr());
        ((TextView)view.findViewById(R.id.textView259)).setText(otherUser.getInformation()+"");
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(user!=null){
                    AppApplication.initConnect(user);
                    System.out.println("开始清理user");
                    app.clearUser();
                    System.out.println("清理user完毕");
                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                    editor.putString("user","");
                    editor.commit();
                    getActivity().finish();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void initListener(){
        view.findViewById(R.id.relativeLayout34).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
        view.findViewById(R.id.imageView90).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                i=1;
                Toast.makeText(getActivity(), "选择一个头像", Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
                /*
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(
                        getActivity(), arrayString, title,
                        onDialogClick);
                dialog.show();
                */
                MediaChooser.setSelectedMediaCount(0);
                MediaChooser.showOnlyImageTab();
                MediaChooser.setSelectionLimit(1);
                Intent intent = new Intent(getActivity(), HomeFragmentActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.foucusLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Activity_foucused.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",user.getUserId()+"");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.relativeLayout67).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sex.getText().toString().trim().equals("")){
                    final String[]a={"男","女"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("你只有一次修改的机会")
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());

                                    builder3.setItems(a, new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface arg0, int arg1)
                                        {
                                            if(arg1==0){
                                                sex.setText("男");
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Manage.AlterUser(user.getUserId()+"","男","","","");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            }else if(arg1==1) {
                                                sex.setText("女");
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Manage.AlterUser(user.getUserId()+"","女","","","");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            }
                                        }
                                    });
                                    builder3.show();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
            }
        });
        view.findViewById(R.id.relativeLayout68).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(getActivity(),Activity_xiugai_jianjie.class),100);
            }
        });
    }
    public void setJianjie(String jianjie){
        ((TextView)view.findViewById(R.id.textView259)).setText(jianjie);
    }
    private void initParam(){

    }
    @Override
    public boolean handleMessage(Message msg) {
        if (msg.obj != null) {
            Drawable drawable = new BitmapDrawable(getResources(),(Bitmap) msg.obj);

            touxiangImageview.setImageDrawable(drawable);

            Gson gson=new Gson();

            Toast.makeText(getActivity(), "头像上传成功", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getActivity(), "头像上传失败", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
    private void initHandler(){
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                touxiangImageview.setImageBitmap((Bitmap)msg.obj);

            }
        };
           }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        if(i!=1){
            getActivity().unregisterReceiver(imageBroadcastReceiver);
            getActivity().unregisterReceiver(videoBroadcastReceiver);
        }
       // thread.interrupt();

        System.out.println("ExampleFragment--onPause");
    }


    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        if(i!=1){
            getActivity().registerReceiver(videoBroadcastReceiver, videoIntentFilter);
            getActivity().registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        }else{
            i=0;
        }
        System.out.println("ExampleFragment--onResume");
    }

    @Override
    public void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
        System.out.println("ExampleFragment--onStop");
    }

}
