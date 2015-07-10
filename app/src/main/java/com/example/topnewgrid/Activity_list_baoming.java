package com.example.topnewgrid;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.ResultUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.HuodongItem;
import http.Httppost;
import http.Manage;
import http.PostUrl;
import io.rong.imkit.RongIM;


public class Activity_list_baoming extends FragmentActivity {
//晚上资料只是确认自己的标签，更具标签选择活动标签
    private AppApplication app;
    private User user;

    private String huodongID;
    private String huodongName;

    private List<HuodongItem> data=new ArrayList<HuodongItem>();
    private ImageDownloader imageDownloader;
    private ImageView tuichu;

    private RadioButton rb1,rb2;
    private Handler handler,handler2,handler3,handler4;
    private TextView find;
    private FriendAdapter adapter;
    private int style=1;
    private PullToRefreshListView listView;
    private int i=1;
    private int needs;
    private ProgressDialog dialog;
    private Handler dialogHandler;

    // 用来实现 UI 线程的更新。
    Handler mHandler;

    private Httppost httppost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_baoming);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        Bundle bundle = getIntent().getExtras();
        huodongID=bundle.getString("id");
        needs=bundle.getInt("needs");
        mHandler = new Handler();
        init();

    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    private void init(){
        initUser();
        initLayout();
        initHandler();
        initListener();
        initParam();

    }
    private void initParam(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialogHandler.sendEmptyMessage(0);
                    List<OtherUser> friendList=Manage.ViewEnrolluserByActivityID(huodongID, "1");
                    dialogHandler.sendEmptyMessage(1);
                    if(friendList!=null) {
                        adapter = new FriendAdapter(Activity_list_baoming.this, friendList);
                        Message ms = new Message();
                        ms.obj = adapter;
                        handler.sendMessage(ms);
                    }else{
                        friendList=new ArrayList<OtherUser>();
                        adapter = new FriendAdapter(Activity_list_baoming.this, friendList);
                        Message ms = new Message();
                        ms.obj = adapter;
                        handler.sendMessage(ms);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                        break;
                }
            }
        };
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                listView.setAdapter((FriendAdapter)msg.obj);

            }
        };
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(Activity_list_baoming.this, "添加成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(Activity_list_baoming.this,"添加失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(Activity_list_baoming.this,"没有更多了",Toast.LENGTH_SHORT).show();
                        listView.onRefreshComplete();
                        break;
                    case 3:
                        Toast.makeText(Activity_list_baoming.this,"点赞成功",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(Activity_list_baoming.this);
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView) msg.obj));
            }
        };
        handler4=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        adapter.mItems=(List<OtherUser>)msg.obj;
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                        break;
                    case 1:
                        adapter.mItems.addAll((List<OtherUser>)msg.obj);
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                        break;
                    case 2:
                        listView.onRefreshComplete();
                        break;
                }
            }
        };
    }
    private void initLayout(){
        find=(TextView)findViewById(R.id.textView108);
        tuichu=(ImageView)findViewById(R.id.imageView17);

        listView=(PullToRefreshListView)findViewById(R.id.listView41);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
    }
    private void initListener(){
        findViewById(R.id.imageView17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(needs!=2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_list_baoming.this);
                    String[] a = {"发消息", "查看资料"};
                    builder.setItems(a, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO 自动生成的方法存根
                            switch (arg1) {
                                case 0:
                                    if (user != null) {
                                        RongIM.getInstance().startPrivateChat(Activity_list_baoming.this, adapter.mItems.get(position - 1).getId() + "", "与" + adapter.mItems.get(position - 1).getUserName() + "的聊天");
                                    } else {
                                        startActivityForResult(new Intent(Activity_list_baoming.this, Activity_login.class), 1);
                                    }
                                    break;
                                case 1:
                                    Intent intent = new Intent();
                                    intent.setClass(Activity_list_baoming.this, Activity_bieren.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", adapter.mItems.get(position - 1).getId() + "");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;

                            }
                        }
                    });
                    builder.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_list_baoming.this);
                    String[] a = {"发消息", "查看资料","查看简历"};
                    builder.setItems(a, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO 自动生成的方法存根
                            switch (arg1) {
                                case 0:
                                    if (user != null) {
                                        RongIM.getInstance().startPrivateChat(Activity_list_baoming.this, adapter.mItems.get(position - 1).getId() + "", "与" + adapter.mItems.get(position - 1).getUserName() + "的聊天");
                                    } else {
                                        startActivityForResult(new Intent(Activity_list_baoming.this, Activity_login.class), 1);
                                    }
                                    break;
                                case 1:
                                    Intent intent = new Intent();
                                    intent.setClass(Activity_list_baoming.this, Activity_bieren.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", adapter.mItems.get(position - 1).getId() + "");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                case 2:

                            }
                        }
                    });
                    builder.show();
                }
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<OtherUser>friendList= Manage.ViewEnrolluserByActivityID(huodongID, "1");
                            if(friendList==null){
                                Message ms=new Message();
                                ms.what=2;
                                handler2.sendMessage(ms);
                                return;
                            }
                            Message ms=new Message();
                            ms.what=0;
                            ms.obj=friendList;
                            handler4.sendMessage(ms);
                            i=1;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<OtherUser>friendList= Manage.ViewEnrolluserByActivityID(huodongID, i+1+"");

                            if(friendList==null){
                                Message ms=new Message();
                                ms.what=2;
                                handler2.sendMessage(ms);
                                return;
                            }
                            i++;
                            Message ms=new Message();
                            ms.what=1;
                            ms.obj=friendList;
                            handler4.sendMessage(ms);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case ResultUtil.FINDUSER:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
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
    public void onDestroy() {
        super.onDestroy();
    }
    private class FriendAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<OtherUser> mItems;


        public FriendAdapter(Context context, List<OtherUser> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mItems = data;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public OtherUser getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final OtherUser item = getItem(position);
            imageDownloader=new ImageDownloader(Activity_list_baoming.this);
            TextView name = null;
            TextView locate=null;;
            final CircleImageView touxiang;
            TextView distance;
            final ImageView tupian;
            if(needs==0||needs==2) {
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.label_haoyou, null);
                }
                name = (TextView) convertView.findViewById(R.id.textView107);
                touxiang = (CircleImageView) convertView.findViewById(R.id.profile_image);
                locate=(TextView)convertView.findViewById(R.id.textView126);
                name.setText(item.getUserName());

                distance = (TextView) convertView.findViewById(R.id.textView109);
                if(!item.getLocate().equals("")){
                    locate.setText(item.getLocate());
                }
                if (item.getDistance() != 0) {
                    double lengs = item.getDistance();
                    System.out.println("长度为:" + lengs);
                    double distances = lengs;
                    int distanceInt = (int) distances;
                    if (distanceInt < 1000) {
                        distance.setText(distanceInt + "m");
                    } else {
                        distanceInt = distanceInt / 100;
                        distance.setText(distanceInt / 10 + "km");
                    }
                } else {
                    distance.setText("<100m");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getId() + "");
                            ms.obj = touxiang;
                            handler2.sendMessage(ms);
                        }
                    }
                }).start();
            }else if(needs==1){
                String filename=PostUrl.BAOMINGIMAGE+"huodong"+huodongID+"user"+item.getId();
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.lable_list_join_tupian, null);
                    tupian=(ImageView)convertView.findViewById(R.id.tupian);
                    name = (TextView) convertView.findViewById(R.id.name);
                    touxiang = (CircleImageView) convertView.findViewById(R.id.imageView);
                    locate=(TextView)findViewById(R.id.time2);
                    distance = (TextView) convertView.findViewById(R.id.juli);

                    name.setText(item.getUserName());
                    if(!item.getLocate().equals("")){
                        locate.setText(item.getLocate());
                    }
                    if (item.getDistance() != 0) {
                        double lengs = item.getDistance();
                        System.out.println("长度为:" + lengs);
                        double distances = lengs;
                        int distanceInt = (int) distances;
                        if (distanceInt < 1000) {
                            distance.setText(distanceInt + "m");
                        } else {
                            distanceInt = distanceInt / 100;
                            distance.setText(distanceInt / 10 + "km");
                        }
                    } else {
                        distance.setText("<100m");
                    }
                    imageDownloader.download("http://"+ PostUrl.Media+":8080/upload/"+filename+ ".jpg", tupian);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (Httppost.urlIsTrue(item.getId() + "")) {
                                Message ms = new Message();
                                ms.what = Integer.parseInt(item.getId() + "");
                                ms.obj = touxiang;
                                handler3.sendMessage(ms);
                            }
                        }
                    }).start();
                }
                }

            return convertView;
        }
    }
}