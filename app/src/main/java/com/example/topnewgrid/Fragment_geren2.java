package com.example.topnewgrid;


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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.adapter.HuodongCardAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.GraphicsBitmapUtils;
import com.example.topnewgrid.util.UploadFile;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class Fragment_geren2 extends Fragment implements Handler.Callback {
    View view;
    public static Fragment_geren2 fragment_geren;
    private ArrayList<Fragment> fragmentList;
    private List<String> titleList=new ArrayList<>();
    private TextView tuichuButton;
    private AppApplication app;
    private User user;
    TextView sex;
    TabHost tabHost;
    int i=0;
    View headView;
    private ListView mainListView;
    Thread thread;
    private ChannelManage channelManage;
    private RelativeLayout touxiang;
    private ImageView touxiangImageview;
    private ImageDownloader imageDownloader;
    private Handler handler,handler2;
    public static OtherUser otherUser;
    private ProgressDialog dialog;
    private Handler dialogHandler;
    IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
    IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
    BroadcastReceiver  videoBroadcastReceiver = new BroadcastReceiver() {

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
    List<String> imageArray=new ArrayList<>();

    /** Called when the activity is first created. */

    ImageView view_pic;
    Button view_btn;
    EditText view_et;
    // 线程通知上传成功
    final Handler upLoadhand = new Handler(this);
    String[] arrayString = { "拍照", "相册" };
    String title = "上传照片";

    // 上传的地址
    String uploadUrl ="http://"+PostUrl.Media+":8080/Server/android/uploadImage.jsp?";
    String filename;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 333;// 结果

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
        fragment_geren=this;
        System.out.println("ExampleFragment--onCreateView");
        if(view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        view=inflater.inflate(R.layout.fragment_listview, container, false);
        LayoutInflater flater = LayoutInflater.from(getActivity());

        headView = flater.inflate(R.layout.activity_xiugai5, null);
        fragmentList=new ArrayList<>();



        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initHandler();
        initUser();
        initLayout();
        initListener();
        initParam();
        initBradcast();


        return  view;
    }
    private void initBradcast() {




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
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                touxiangImageview.setImageBitmap((Bitmap)msg.obj);
                user.setTouxiang((Bitmap)msg.obj);
                System.out.println("设置user完成");
                app.getUser().setTouxiang((Bitmap) msg.obj);
                Gson gson=new Gson();
                SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                editor.putString("user",gson.toJson(user));
                System.out.println("保存user完成");
            }
        };
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        ((TextView)headView.findViewById(R.id.textView227)).setText(otherUser.getUserName());
                        sex=(TextView)headView.findViewById(R.id.textView92);
                        ((TextView)headView.findViewById(R.id.textView89)).setText(otherUser.getLevel() + "人关注");
                        ((TextView)headView.findViewById(R.id.textView90)).setText(otherUser.getRegisterTime());
                        ((TextView)headView.findViewById(R.id.textView92)).setText(otherUser.getSex());
                        //((TextView)view.findViewById(R.id.textView112)).setText(otherUser.getProfession());
                        //((TextView)view.findViewById(R.id.textView261)).setText(otherUser.getLevel() + "");
                        //((TextView)view.findViewById(R.id.textView231)).setText(otherUser.getPublicNum()+"");
                        //((TextView)view.findViewById(R.id.textView234)).setText(otherUser.getJoinNum()+"");
                        //((TextView)findViewById(R.id.textView77)).setText(AppApplication.location.getAddrStr());
                        ((TextView)headView.findViewById(R.id.textView96)).setText(otherUser.getInformation() + "");
                        break;
                }
            }
        };
    }
    private void initParam(){
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //name,locate,sex,joinTime,fabuActivity,joinActivity,tp.
                    otherUser=Manage.FindUserById(user.getUserId());
                    app.setOtherUser(otherUser);
                    //dialogHandler.sendEmptyMessage(1);
                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                    editor.putString(user.getUserId()+"",gson.toJson(otherUser));
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
    private void initUser(){
        app = (AppApplication) getActivity().getApplication();
        channelManage=ChannelManage.getManage(AppApplication.getApp().getSQLHelper());
        user=app.getUser();
        filename=user.getUserId()+".jpg";
    }

    private void initLayout(){
        tabHost=(TabHost)headView.findViewById(R.id.tabHost1);
        tabHost.addTab(tabHost.newTabSpec("ziliao").setIndicator("资料",null).setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("dongtai").setIndicator("动态",null).setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("canyu").setIndicator("参与",null).setContent(R.id.tab3));
        tabHost.addTab(tabHost.newTabSpec("xiaoxi").setIndicator("消息",null).setContent(R.id.tab4));
        mainListView=(ListView)view.findViewById(R.id.listView7);
        mainListView.addHeaderView(headView);
        List<Huodong>  huodongList=new ArrayList<>();
        HuodongCardAdapter adapter=new HuodongCardAdapter(huodongList,getActivity());
        mainListView.setAdapter(adapter);
        tuichuButton=(TextView)headView.findViewById(R.id.textView263);
        //touxiang=(RelativeLayout)findViewById(R.id.relativeLayout7);
        touxiangImageview=(ImageView)headView.findViewById(R.id.imageView220);
        /*
        if(user.getTouxiang()!=null) {
            touxiangImageview.setImageBitmap(user.getTouxiang());
        }
        */

        System.out.println("http://"+ PostUrl.Media+":8080/upload/"+user.getUserId()+".jpg");

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

        //imageDownloader.download("http://"+ PostUrl.Url+":8080/upload/"+user.getUserId()+".jpg", touxiangImageview);
    }

    private void initListener(){

        tuichuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        headView.findViewById(R.id.imageView220).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                i=1;
                Toast.makeText(getActivity(),"选择一个头像",Toast.LENGTH_LONG).show();
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
        thread.interrupt();

        System.out.println("ExampleFragment--onPause");
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

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.obj != null) {
            Drawable drawable = new BitmapDrawable(getResources(),(Bitmap) msg.obj);
            //app.getUser().setTouxiang((Bitmap) msg.obj);
            touxiangImageview.setImageDrawable(drawable);
            user.setTouxiang((Bitmap) msg.obj);
            Gson gson=new Gson();
            SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
            editor.putString("user",gson.toJson(user));
            Toast.makeText(getActivity(), "头像上传成功", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getActivity(), "头像上传失败", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());

    // 对话框
    DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    startCamearPicCut(dialog);// 开启照相
                    break;
                case 1:
                    startImageCaptrue(dialog);// 开启图库
                    break;
                default:
                    break;
            }
        }

        private void startCamearPicCut(DialogInterface dialog) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            // 调用系统的拍照功能
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra("camerasensortype", 2);// 调用前置摄像头
            intent.putExtra("autofocus", true);// 自动对焦
            intent.putExtra("fullScreen", false);// 全屏
            intent.putExtra("showActionIcons", false);
            // 指定调用相机拍照后照片的储存路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
        }

        private void startImageCaptrue(DialogInterface dialog) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }
    };




    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("接收到请求");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    startPhotoZoom(data.getData(), 150);
                }
                break;

            case PHOTO_REQUEST_CUT:
                System.out.println("剪切完成");
                if (data != null) {
                    setPicToView(data);
                }
                break;
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

    // 将进行剪裁后的图片显示到UI界面上
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
    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {

            return titleList.get(position);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
