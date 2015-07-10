package com.example.topnewgrid;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.topnewgrid.adapter.EditSeeAdapter2;
import com.example.topnewgrid.adapter.ReceiverAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.Label_edit;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.AndroidClass;
import com.example.topnewgrid.util.GraphicsBitmapUtils;
import com.example.topnewgrid.util.LevalUtil;
import com.example.topnewgrid.util.UploadFile;
import com.example.topnewgrid.view.ListViewForScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import demo.MyLocation;
import drawerlistitem.PinglunItem;
import http.Httppost;
import http.Manage;
import http.PostUrl;
import io.rong.imkit.RongIM;


/**
 * Created by 真爱de仙 on 2015/1/18.
 */
public class Activity_huodong_xiangqing extends FragmentActivity implements com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener  {
    DialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    View headView;
    Thread thread;
    private DisplayImageOptions defaultOptions;
    private ReceiverAdapter pinglunAdapter;
    private ListView pinglun;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private ArrayList<Fragment> fragmentList;
    List<PinglunItem>pinglunItemList;
    PinglunItem pinglunItem;
    private View view1, view2, view3,view4,view5;
    public static List<View> viewList,viewList2;
    public static List<String>pathList;
    private ViewPager viewPager; // 对应的viewPager
    private ViewPager viewPager2;
    private String huodongID;
    private Huodong huodong;
    private Handler handler,handler2,handler3,handler4,handler5;
    private ImageDownloader imageDownloader;
    private ImageView fatie,touxiang;
    private User user;
    private AppApplication app;
    private Integer arg=0;  //是否是自己发布的活动 0:报名 1:取消报名 2:管理
    private View baoming;
    private TextView baomingtext,jianjie,time,locate,city,peopleNum;
    EditText username;
    private int flag=0;  //0:报名 1:取消
    private GridView joinpeople;
    private String huodongTitle;
    private String authorId;
    private int jieshu=0;//0 活动结束了
    private int needs=0;//0啥都不需要 1需要图片 2需要简历
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    String uploadUrl ="http://"+PostUrl.Media+":8080/Server/android/uploadImage.jsp?";

    private String locationAdress="";
    private String locationPoi_Adress="";
    private String locationPoi="";
    private String locationx="";
    private String locationy="";
    private MyLocation location=new MyLocation();

    private List<String> titleList=new ArrayList<>();
    String[] arrayString = { "拍照", "相册" };
    String title = "上传照片";
    File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    String filename;

    private ProgressDialog dialog;
    private Handler dialogHandler;
    int ifFirst=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xiangqing_huodong6);
        LayoutInflater flater = LayoutInflater.from(Activity_huodong_xiangqing.this);

        headView = flater.inflate(R.layout.label_huodong_head, null);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        titleList.add("基本资料");
        titleList.add("动态");
        Bundle bundle = getIntent().getExtras();
        huodongID=bundle.getString("id");
        System.out.println("groupid" + huodongID);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        fragmentList=new ArrayList<>();

        initUser();
        initLayout1();
        initListener1();
        initHandler();
        initParam();
    }
    public Huodong getHuodong(){
        return huodong;
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
                switch(msg.what){
                    case 0:
                    {
                        viewPager2.setAdapter((PagerAdapter)msg.obj);
                        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                        tabs.setViewPager(viewPager2);
                        break;
                    }
                    case 1:
                    {
                        viewPager.setAdapter((PagerAdapter)msg.obj);
                        break;
                    }
                    case 2:
                        viewPager.setVisibility(View.GONE);
                        break;
                    case 3:

                        break;
                    case 4:
                        Toast.makeText(Activity_huodong_xiangqing.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dialogHandler.sendEmptyMessage(0);
                                    String result=Manage.enrollByActivityId(arg+"",user.getUserId()+"",huodong.getHuodongId()+"","");
                                    dialogHandler.sendEmptyMessage(1);
                                    System.out.println(result);
                                    if(result.contains("成功")){
                                        arg=1;
                                        user.addPublicActivity(huodongID);
                                        Gson gson=new Gson();
                                        SharedPreferences.Editor editor = AppApplication.activitySharedPreferences.edit();
                                        editor.putString(user.getUserId()+"",gson.toJson(user.getPublicActivity()));
                                        editor.commit();

                                        Message ms=new Message();
                                        ms.what=1;
                                        handler2.sendMessage(ms);
                                        RongIM.getInstance().joinGroup(huodongID,huodong.getTitle(),new RongIM.OperationCallback() {
                                            @Override
                                            public void onSuccess() {
                                                System.out.println("加入群组成功");
                                            }

                                            @Override
                                            public void onError(ErrorCode errorCode) {
                                                System.out.println("加入群组失败");
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        break;
                    case  6:
                        //((ViewPager)findViewById(R.id.viewpager3)).setCurrentItem(1);
                        break;
                }
            }
        };
        handler2=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                    {
                        baomingtext.setText("关注");
                        break;
                    }
                    case 1:
                    {
                        baomingtext.setText("取消关注");
                        break;
                    }
                    case 2:
                    {
                        baomingtext.setText("发动态");
                        break;
                    }
                    case 3:
                        //username.setText((String)msg.obj);
                        break;
                    case 4:
                        jianjie.setText((String)msg.obj);
                        break;
                    ////time,locate,city,peopleNum;
                    case 5:
                        time.setText((String)msg.obj);
                        break;
                    case 6:
                        locate.setText((String)msg.obj);
                        break;
                    case 7:
                        peopleNum.setText((String)msg.obj);
                        break;
                    case 8:

                        break;
                    case 9:
                        ((TextView)findViewById(R.id.textView16)).setText("查看关注的人");
                        break;
                    case 10:
                        ((TextView)findViewById(R.id.textView16)).setText("发消息");
                        break;
                    case 11:
                        baomingtext.setText("已结束");
                        break;
                }
            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(Activity_huodong_xiangqing.this);
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)msg.obj));
            }
        };
        handler4=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                 /*
                FragmentTransaction ts=getSupportFragmentManager().beginTransaction();
                ts.replace(R.id.frameLayout3, new Fragment_huodong_xiangqing_view1()) .commit();
                */
                Gson gson=new Gson();
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + huodong.getAuthorId()+".jpg",(ImageView)headView.findViewById(R.id.imageView),defaultOptions);
                if(huodong.getContent().toString().contains("type")) {
                    List<Label_edit> edits = gson.fromJson(huodong.getContent(), new TypeToken<List<Label_edit>>() {
                    }.getType());
                    EditSeeAdapter2 editSeeAdapter2 = new EditSeeAdapter2(Activity_huodong_xiangqing.this, edits);
                    ((ListViewForScrollView) headView.findViewById(R.id.listView2)).setAdapter(editSeeAdapter2);
                }
                        ((TextView) headView.findViewById(R.id.textView162)).setText(huodong.getAuthorName());
                ((TextView)headView.findViewById(R.id.textView86)).setText(huodong.getLocate());
                ((TextView)headView.findViewById(R.id.textView84)).setText("["+huodong.getFenlei()+"]");
                ((TextView)headView.findViewById(R.id.textView164)).setText(huodong.getAuthorInformation());
                ((TextView)headView.findViewById(R.id.textView87)).setText(huodong.getTitle());
                ((TextView)headView.findViewById(R.id.textView284)).setText(huodong.getChuangjianTime());
                ((TextView)headView.findViewById(R.id.textView285)).setText(huodong.getPinglunNum()+"");
                ((TextView)headView.findViewById(R.id.textView286)).setText(huodong.getJoinMun()+"");

                ((TextView)headView.findViewById(R.id.textView287)).setText("LV"+ LevalUtil.getLevel(huodong.getLevel()));
                //int leavel=huodong.get
                if(huodong.getStyle().trim().equals("需求")){
                    if(huodong.getRecompense().equals("金额")){
                        ((TextView)headView.findViewById(R.id.textView85)).setText("￥"+huodong.getRecompense());
                    }else if(huodong.getRecompense().equals("无悬赏")||huodong.getRecompense().equals("无")){
                        //((TextView)view.findViewById(R.id.textView2560)).setText("免费");
                    }else{
                        ((TextView)headView.findViewById(R.id.textView85)).setText("￥"+huodong.getRecompense());
                    }
                }else{
                    //findViewById(R.id.textView191).setVisibility(View.GONE);
                }
                if(pinglunItem==null){
                    headView.findViewById(R.id.pinglunLayout).setVisibility(View.GONE);
                    //headView.findViewById(R.id.relativeLayout57).setVisibility(View.GONE);
                }else{
                    ((TextView)headView.findViewById(R.id.textView211)).setText("("+huodong.getPinglunNum()+""+")");
                    ((TextView)headView.findViewById(R.id.textView212)).setText(pinglunItem.getName()+"");
                    ((TextView)headView.findViewById(R.id.textView213)).setText(pinglunItem.getContent()+"");
                    imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + pinglunItem.getUserId()+".jpg",(ImageView)headView.findViewById(R.id.profile_image),defaultOptions);
                }
            }
        };
        handler5=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        pinglun.addHeaderView(headView,null,false);
                        pinglun.setAdapter((ReceiverAdapter)msg.obj);
                        break;
                    case 1:
                        pinglun.addHeaderView(headView,null,false);
                        pinglun.setAdapter((ReceiverAdapter)msg.obj);
                        findViewById(R.id.dongtailayout).setVisibility(View.VISIBLE);
                        break;
                }

            }
        };
    }
    private void initListener1(){
        findViewById(R.id.textView200).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

            });
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
        pinglun.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem==0){
                    Log.e("log", "滑到顶部");
                    findViewById(R.id.relativeLayout70).setVisibility(View.GONE);
                }else if(firstVisibleItem==1){
                    findViewById(R.id.relativeLayout70).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.textView297)).setText(pinglunAdapter.getItem(firstVisibleItem-1).getJiange());
                }
                else if(!pinglunAdapter.getItem(firstVisibleItem-1).getJiange().equals(pinglunAdapter.getItem(firstVisibleItem-2).getJiange())){
                    ((TextView)findViewById(R.id.textView297)).setText(pinglunAdapter.getItem(firstVisibleItem-1).getJiange());
                }
            }
        });
        pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent=new Intent();
                intent.setClass(Activity_huodong_xiangqing.this,Activity_receiver_pinglun.class);
                intent.putExtra("huodong",huodong);
                intent.putExtra("suiji",pinglunAdapter.getItem(position-1));
                startActivity(intent);
            }
        });
        headView.findViewById(R.id.relativeLayout57).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_huodong_xiangqing.this,Activity_pinglun.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",huodongID);
                mBundle.putString("userid",user.getUserId());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        headView.findViewById(R.id.relativeLayout57).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_huodong_xiangqing.this,Activity_pinglun.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",huodongID);
                mBundle.putString("userid",user.getUserId());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        headView.findViewById(R.id.imageView295).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_huodong_xiangqing.this,Activity_pinglun.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",huodongID);
                mBundle.putString("userid",user.getUserId());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        headView.findViewById(R.id.textView285).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_huodong_xiangqing.this,Activity_pinglun.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",huodongID);
                mBundle.putString("userid",user.getUserId());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        headView.findViewById(R.id.imageView297).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Activity_huodong_xiangqing.this,Activity_list_baoming.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",huodong.getHuodongId()+"");
                bundle.putInt("needs", needs);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        headView.findViewById(R.id.textView286).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Activity_huodong_xiangqing.this,Activity_list_baoming.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",huodong.getHuodongId()+"");
                bundle.putInt("needs", needs);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        headView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Activity_huodong_xiangqing.this,Activity_bieren.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",huodong.getAuthorId()+"");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        findViewById(R.id.imageView27).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Activity_huodong_xiangqing.this,Activity_bieren.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",huodong.getAuthorId()+"");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        */
        findViewById(R.id.xiaoxiLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    if (user.getUserId().equals(huodong.getAuthorId())) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Intent intent=new Intent();
                                    intent.setClass(Activity_huodong_xiangqing.this,Activity_list_baoming.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("id",huodong.getHuodongId()+"");
                                    bundle.putInt("needs", needs);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else{
                        RongIM.getInstance().startPrivateChat(Activity_huodong_xiangqing.this, huodong.getAuthorId(), "与"+huodong.getAuthorName()+"的聊天");
                    }
                }else{
                    startActivityForResult(new Intent(Activity_huodong_xiangqing.this,Activity_login.class),100);
                }
            }
        });
        /*
        findViewById(R.id.relativeLayout52).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setClass(Activity_huodong_xiangqing.this,Activity_receiver.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",huodongID);
                bundle.putString("userid",huodong.getAuthorId());
                bundle.putString("huodongTitle",huodongTitle);
                if(user!=null){
                    bundle.putString("myId",user.getUserId());
                }else{
                    bundle.putString("myId","");
                }
                intent.putExtras(bundle);
                startActivity(intent);

                if(user.getUserId().equals(huodong.getHuodongId())) {
                    startActivity(new Intent(Activity_huodong_xiangqing.this, libstreamactivity.class));
                }else{
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    //bundle.putString("path","http://121.41.74.72:8080/upload/"+item.getMediaPath().get(0));
                    bundle.putString("path","rtsp://192.168.1.102:8086");
                    intent.putExtras(bundle);
                    intent.setClass(Activity_huodong_xiangqing.this,VlcVideoActivity.class);
                    startActivity(intent);
                }

            }
        });
        */
    }


    private void initLayout1(){
        pinglun=(ListView)findViewById(R.id.listView3);

        ImageView open=(ImageView)findViewById(R.id.imageView26);
        fragmentManager = getSupportFragmentManager();
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    mMenuDialogFragment.show(fragmentManager, "ContextMenuDialogFragment");
                }else{
                    startActivityForResult(new Intent(Activity_huodong_xiangqing.this,Activity_login.class),100);
                }
            }
        });
        baoming=findViewById(R.id.baomingview);
        baomingtext=(TextView)findViewById(R.id.textView19);
        //touxiang=(ImageView)findViewById(R.id.profile_image);
        //username=(EditText)findViewById(R.id.editText34);
    }
    private void initUser(){
        app = (AppApplication) Activity_huodong_xiangqing.this.getApplication();
        user=app.getUser();
    }


    private void initParam(){

/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                List <User>userList= null;
                try {
                    userList = Manage.ViewEnrolluserByActivityID(huodongID, "1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(userList!=null&&userList.size()!=0) {
                    if (userList.size() >4) {
                        List<User> userList1 = new ArrayList<User>();
                        for (int i = 0; i < 4; i++) {
                            userList1.add(userList.get(i));
                        }
                        userList = userList1;
                    }
                    JoinPeopleAdapter joinPeopleAdapter = new JoinPeopleAdapter(Activity_huodong_xiangqing.this, userList);

                    Message ms=new Message();
                    ms.what=3;
                    ms.obj=joinPeopleAdapter;
                    handler.sendMessage(ms);
                }
            }
        }).start();
*/
        ifFirst=1;
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialogHandler.sendEmptyMessage(0);
                    huodong= Manage.viewActivityByID(huodongID);
                    //pinglunItemList=Manage.ViewCommentByActivity(huodongID,"1","活动");

                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.allActivitySharedPreferences.edit();
                    editor.putString(huodongID+"",gson.toJson(huodong));
                    editor.commit();
                    if(thread.isInterrupted()){
                        return;
                    }
                    huodongTitle=huodong.getTitle();
                    if(huodong.getFlag().contains("结束")){
                        jieshu=1;
                    }
                    if(huodong.getIfneedtupian()==1){
                        needs=1;
                    }else if(huodong.getStyle().equals("需求")){
                        needs=2;
                    }
                    if(user!=null) {
                        System.out.println("活动作者id是:" + huodong.getAuthorId());
                        System.out.println("用户id是:" + user.getUserId());
                        if (huodong.getAuthorId() .equals( user.getUserId())) {
                            arg = 2;

                        }else if(huodong.getEnrollUser()!=null){
                            if(huodong.getEnrollUser().contains(user.getUserId()+"")) {
                                arg = 1;
                            }
                        }
                    }
                    initLayout();
                    initListener();
                    pinglunItem=Manage.FindCommentByLike(huodongID,"活动");
                    handler4.sendEmptyMessage(0);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<PinglunItem> receiverList= null;
                            try {
                                receiverList = Manage.ViewBroadcastByActivity(huodongID, "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(thread.isInterrupted()){
                                return;
                            }
                            dialogHandler.sendEmptyMessage(1);
                            if(receiverList!=null) {
                                pinglunAdapter = new ReceiverAdapter(Activity_huodong_xiangqing.this, receiverList);
                                Message ms = new Message();
                                ms.obj = pinglunAdapter;
                                ms.what=0;
                                handler5.sendMessage(ms);
                            }else{
                                receiverList=new ArrayList<>();
                                pinglunAdapter = new ReceiverAdapter(Activity_huodong_xiangqing.this, receiverList);
                                if(thread.isInterrupted()){
                                    return;
                                }
                                Message ms = new Message();
                                ms.obj = pinglunAdapter;
                                ms.what=1;
                                handler5.sendMessage(ms);
                            }
                        }
                    }).start();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(Httppost.urlIsTrue(huodong.getAuthorId())){
                            Message ms=new Message();
                            ms.what=Integer.parseInt(huodong.getAuthorId());
                            ms.obj=touxiang;
                            handler3.sendMessage(ms);
                        }
                    }
                }).start();
                */

            }
        });
        thread.start();
    }
    private void initLayout(){
        if(user!=null) {
            if (user.getUserId().equals(huodong.getAuthorId())) {
                ArrayList<MenuObject> menuObjects = new ArrayList<MenuObject>();
                menuObjects.add(new MenuObject(R.drawable.icn_close));
                menuObjects.add(new MenuObject(R.drawable.icn_1, "查看评论"));
                menuObjects.add(new MenuObject(R.drawable.icn_2, "邀请别人加入"));
                menuObjects.add(new MenuObject(R.drawable.icn_3, "进入聊天室"));
                //menuObjects.add(new MenuObject(R.drawable.icn_4, "上传新的图片"));
                menuObjects.add(new MenuObject(R.drawable.icn_5, "分享"));
                //menuObjects.add(new MenuObject(R.drawable.icn_5, "结束活动"));
                mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), menuObjects);
            } else {
                ArrayList<MenuObject> menuObjects = new ArrayList<MenuObject>();
                menuObjects.add(new MenuObject(R.drawable.icn_close));
                menuObjects.add(new MenuObject(R.drawable.icn_1, "查看评论"));
                menuObjects.add(new MenuObject(R.drawable.icn_2, "邀请别人加入"));
                menuObjects.add(new MenuObject(R.drawable.icn_3, "进入聊天室"));
                //menuObjects.add(new MenuObject(R.drawable.icn_4, "添加收藏"));
                menuObjects.add(new MenuObject(R.drawable.icn_5, "分享"));

                mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), menuObjects);
            }
        }else{
            ArrayList<MenuObject> menuObjects = new ArrayList<MenuObject>();
            menuObjects.add(new MenuObject(R.drawable.icn_close));
            menuObjects.add(new MenuObject(R.drawable.icn_1, "查看评论"));
            menuObjects.add(new MenuObject(R.drawable.icn_2, "邀请别人加入"));
            menuObjects.add(new MenuObject(R.drawable.icn_3, "进入聊天室"));
            menuObjects.add(new MenuObject(R.drawable.icn_4, "添加收藏"));
            menuObjects.add(new MenuObject(R.drawable.icn_4, "分享"));

            mMenuDialogFragment = ContextMenuDialogFragment.newInstance((int) getResources().getDimension(R.dimen.tool_bar_height), menuObjects);
        }
        viewList=new ArrayList<View>();

        if(arg==0){
            Message ms=new Message();
            ms.what=0;
            handler2.sendMessage(ms);
        }else if(arg==2){
            Message ms=new Message();
            ms.what=2;
            handler2.sendMessage(ms);
        }else{
            Message ms=new Message();
            ms.what=1;
            handler2.sendMessage(ms);
        }
        if(jieshu==1){
            handler2.sendEmptyMessage(11);
        }

        viewList2=new ArrayList<View>();
        pathList=new ArrayList<String>();
        LayoutInflater inflater = getLayoutInflater();
        viewPager = (ViewPager)headView.findViewById(R.id.viewpager2);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_huodong_xiangqing.this,Activity_gallary.class));
            }
        });

        System.out.println("ok1");
        if(huodong.getFengmianUrl().equals("")||huodong.getFengmianUrl()==null){
            Message ms=new Message();
            ms.what=2;
            handler.sendMessage(ms);
        }else{
            String []path=huodong.getFengmianUrl().split(";");
            for(String a:path){
                if(!a.equals("")&&a!=null){
                    pathList.add(a);
                    View views=inflater.inflate(R.layout.fragment_tupian, null);
                    viewList.add(views);
                }
            }
        }
        System.out.println("ok2");


        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                // TODO Auto-generated method stub
                View views=viewList.get(position);
                ImageView imageView=(ImageView)views.findViewById(R.id.imageView33);

                if(pathList.get(position).contains("mp4")||pathList.get(position).contains("wmv")||pathList.get(position).contains("avi")||pathList.get(position).contains("3gp")) {
                    views.findViewById(R.id.jianjielayout).setVisibility(View.VISIBLE);
                    views.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            String url = "http://121.41.74.72:8080/upload/"+pathList.get(position).split("\\.")[0]+".mp4";
                            //String url = "http://121.41.74.72:8080/upload/"+"3.mp4";
                            Intent intent = new Intent();
                            intent.setClass(Activity_huodong_xiangqing.this, BBVideoPlayer.class);
                            intent.putExtra("url", url);

                            startActivity(intent);
                            */
                            Intent it = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse("http://"+PostUrl.Media+":8080/upload/"+pathList.get(position).split("\\.")[0]+".mp4");
                            it.setDataAndType(uri, "video/mp4");
                            startActivity(it);
                        }
                    });
                    imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + pathList.get(position).split("\\.")[0]+".jpg", imageView);
                }else {
                    views.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Activity_huodong_xiangqing.this,Activity_gallary.class));
                        }
                    });
                    imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + pathList.get(position), imageView);
                }

                container.addView(views);
                return views;
            }
        };

/*
        ms.obj=huodong.getContent();
        ms.what=4;
        handler2.sendMessage(ms);
        ms.what=5;
        ms.obj=huodong.getTime();
        handler2.sendMessage(ms);
        ms.what=6;
        ms.obj=huodong.getLocate();
        handler2.sendMessage(ms);
        ms.what=7;
        ms.obj=huodong.getPeopleNum();
        handler2.sendMessage(ms);
        */
        Message ms=new Message();
        ms.what=1;
        ms.obj= pagerAdapter;
        handler.sendMessage(ms);
        System.out.println("ok6");



    }
    private void initListener(){

        /*
        fatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity_huodong_xiangqing.this,Activity_pinglun.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",huodongID);
                mBundle.putString("userid",user.getUserId());
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.zhibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getUserId().trim().equals(huodong.getAuthorId().trim())){
                    startActivity(new Intent(Activity_huodong_xiangqing.this,libstreamactivity.class));
                }else{

                }
            }
        });
        */
        if (user!=null) {
            if (user.getUserId().equals(huodong.getAuthorId())) {
                handler2.sendEmptyMessage(9);
            }else {
                handler2.sendEmptyMessage(10);
            }
        }
        baoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jieshu==1){
                    Toast.makeText(Activity_huodong_xiangqing.this,"活动已结束",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(user!=null) {
                    if (arg==0) {
                        if (needs == 0||needs==2) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        dialogHandler.sendEmptyMessage(0);
                                        String result=Manage.enrollByActivityId(arg+"",user.getUserId()+"",huodong.getHuodongId()+"","");
                                        dialogHandler.sendEmptyMessage(1);
                                        System.out.println(result);
                                        if(result.contains("成功")){
                                            arg=1;
                                            user.addPublicActivity(huodongID);
                                            Gson gson=new Gson();
                                            SharedPreferences.Editor editor = AppApplication.activitySharedPreferences.edit();
                                            editor.putString(user.getUserId()+"",gson.toJson(user.getPublicActivity()));
                                            editor.commit();

                                            Message ms=new Message();
                                            ms.what=1;
                                            handler2.sendMessage(ms);
                                            /*
                                            RongIM.getInstance().joinGroup(huodongID,huodong.getTitle(),new RongIM.OperationCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    System.out.println("加入群组成功");
                                                }

                                                @Override
                                                public void onError(ErrorCode errorCode) {
                                                    System.out.println("加入群组失败");
                                                }
                                            });
                                            */

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }else if (needs == 1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_huodong_xiangqing.this);
                            builder.setMessage("该活动设置报名需要照片，请选择一张图片")
                                    .setCancelable(false)
                                    .setPositiveButton("选择照片", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            AlertDialog.Builder dialog1 = AndroidClass.getListDialogBuilder(
                                                    Activity_huodong_xiangqing.this, arrayString, title,
                                                    onDialogClick);
                                            dialog1.show();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();
                        }
                    }else if(arg==1){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    dialogHandler.sendEmptyMessage(0);
                                        System.out.println("aa");
                                        String result=Manage.enrollByActivityId(arg+"",user.getUserId()+"",huodong.getHuodongId()+"","");
                                    dialogHandler.sendEmptyMessage(1);
                                        System.out.println(result);
                                        arg=0;
                                        Message ms=new Message();
                                        ms.what=0;
                                        handler2.sendMessage(ms);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else{
                        Intent intent=new Intent();
                        intent.setClass(Activity_huodong_xiangqing.this,Activity_receiver.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("id",huodongID);
                        bundle.putString("userid",huodong.getAuthorId());
                        bundle.putString("huodongTitle",huodongTitle);
                        intent.putExtra("huodong",huodong);
                        if(user!=null){
                            bundle.putString("myId",user.getUserId());
                        }else{
                            bundle.putString("myId","");
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }else{
                    startActivityForResult(new Intent(Activity_huodong_xiangqing.this,Activity_login.class),100);
                }
            }
        });
    }
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



    public class JoinPeopleAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<User> mItems;

        public JoinPeopleAdapter(Context context, List<User> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mItems = data;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public User getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final User item=getItem(position);
            final CircleImageView usertouxiang;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.label_join_pople, null);
            }
            usertouxiang=(CircleImageView)convertView.findViewById(R.id.profile_image);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(Httppost.urlIsTrue(item.getUserId())){
                        Message ms=new Message();
                        ms.what=Integer.parseInt(item.getUserId());
                        ms.obj=usertouxiang;
                        handler3.sendMessage(ms);
                    }
                }
            }).start();

            return convertView;
        }
    }
    @Override
    public void onMenuItemClick(View view, int i) {
        if (arg != 2) {
            switch (i) {
                case 1:
                    Intent intent = new Intent(Activity_huodong_xiangqing.this, Activity_pinglun.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id", huodongID);
                    mBundle.putString("userid", user.getUserId());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    break;
                case 2:
                    Intent mintent = new Intent(Activity_huodong_xiangqing.this, Activity_yaoqing.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", huodong.getHuodongId() + "");
                    bundle.putString("name", huodong.getTitle() + "");
                    mintent.putExtras(bundle);
                    startActivity(mintent);
                    break;
                case 3:
                    RongIM.getInstance().startChatroom(Activity_huodong_xiangqing.this, huodong.getHuodongId()+"", huodong.getTitle());

                    break;
                case 4:
                    /*
                    if(user!=null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String result=Manage.CollectionActivity(user.getUserId()+"",huodongID+"");
                                    Message ms=new Message();
                                    ms.obj=result;
                                    ms.what=4;
                                    handler.sendMessage(ms);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    */
                    showShare();
                    break;

            }
        } else {
            switch (i) {
                case 1:
                    Intent intent = new Intent(Activity_huodong_xiangqing.this, Activity_pinglun.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("id", huodongID);
                    mBundle.putString("userid", user.getUserId());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    break;
                case 2:
                    Intent mintent = new Intent(Activity_huodong_xiangqing.this, Activity_yaoqing.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", huodong.getHuodongId() + "");
                    bundle.putString("name", huodong.getTitle() + "");
                    mintent.putExtras(bundle);
                    startActivity(mintent);
                    break;
                case 3:
                    RongIM.getInstance().startChatroom(Activity_huodong_xiangqing.this, huodong.getHuodongId()+"", huodong.getTitle());

                    break;
                case 4:
                    showShare();
                    break;
                /*
                case 5:
                    showShare();
                    break;
                case 6:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Manage.FinishActivity(huodong.getHuodongId()+"");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                    */
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppApplication app1=(AppApplication)(getApplication());
        if(ifFirst!=0){
            if(app.zhuangtai==1){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<PinglunItem> receiverList= null;
                        try {
                            receiverList = Manage.ViewBroadcastByActivity(huodongID, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(thread.isInterrupted()){
                            return;
                        }
                        dialogHandler.sendEmptyMessage(1);
                        if(receiverList!=null) {
                            pinglunAdapter = new ReceiverAdapter(Activity_huodong_xiangqing.this, receiverList);
                            Message ms = new Message();
                            ms.obj = pinglunAdapter;
                            ms.what=0;
                            handler5.sendMessage(ms);
                        }else{
                            receiverList=new ArrayList<>();
                            pinglunAdapter = new ReceiverAdapter(Activity_huodong_xiangqing.this, receiverList);
                            if(thread.isInterrupted()){
                                return;
                            }
                            Message ms = new Message();
                            ms.obj = pinglunAdapter;
                            ms.what=1;
                            handler5.sendMessage(ms);
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 1:
                /*
                System.out.println("登陆或者退出");
                initUser();
                if((user.getUserId()+"").equals(huodongID)){
                System.out.println("这是你的活动");
            }
                initLayout1();
                initListener1();
                initHandler();
                initParam();
                */
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("id",huodong.getHuodongId()+"");
                intent.putExtras(bundle);
                setResult(4,intent);
                finish();
                break;
            case 3:
                System.out.println("你刚刚发布了活动?");
                break;
        }
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
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(huodong.getTitle());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(huodong.getContent());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        if(huodong.getFengmianUrl().equals("")||huodong.getFengmianUrl()==null){
            oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");

        }else{
            String []path=huodong.getFengmianUrl().split(";");
            oks.setImageUrl("http://"+ PostUrl.Media+":8080/upload/"+pathList.get(0));
        }
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");

// 启动分享GUI
        oks.show(this);
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
                        filename=PostUrl.BAOMINGIMAGE+"huodong"+huodong.getHuodongId()+"user"+user.getUserId()+".jpg";
                        isUploadSuccess = uploadFile.defaultUploadMethod(photodata, filename);
                        if (isUploadSuccess) {
                            System.out.println("图片上传成功,图片地址为"+filename);
                            String result=Manage.enrollByActivityId(arg+"",user.getUserId()+"",huodong.getHuodongId()+"",filename);
                            System.out.println(result);
                            if(result.contains("成功")) {
                                arg = 1;
                                user.addPublicActivity(huodongID);
                                Gson gson = new Gson();
                                SharedPreferences.Editor editor = AppApplication.activitySharedPreferences.edit();
                                editor.putString(user.getUserId() + "", gson.toJson(user.getPublicActivity()));
                                editor.commit();

                                Message ms = new Message();
                                ms.what = 1;
                                handler2.sendMessage(ms);
                                RongIM.getInstance().joinGroup(huodongID, huodong.getTitle(), new RongIM.OperationCallback() {
                                    @Override
                                    public void onSuccess() {
                                        System.out.println("加入群组成功");
                                    }

                                    @Override
                                    public void onError(ErrorCode errorCode) {
                                        System.out.println("加入群组失败");
                                    }
                                });
                                //upLoadhand.obtainMessage(0, photo).sendToTarget();
                            }
                        } else {
                           // upLoadhand.obtainMessage(-1, null).sendToTarget();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

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
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
