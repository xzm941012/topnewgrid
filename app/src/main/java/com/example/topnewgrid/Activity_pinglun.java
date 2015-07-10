package com.example.topnewgrid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.R;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.TimeUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.PinglunItem;
import http.Manage;
import http.PostUrl;

/**
 * Created by 真爱de仙 on 2015/1/21.
 */
public class Activity_pinglun extends Activity {

    private AppApplication app;
    private User user;
    Thread thread;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions defaultOptions;
    private PullToRefreshListView pinglun;
    private ImageDownloader imageDownloader;
    private String huodongID,userID;
    private TextView fasong;
    private Handler handler,handler2,handler3,handler4,handler5;
    private EditText content;
    private PinglunAdapter pinglunAdapter;
    private Boolean arg=false;     //是否是自己发布的
    private Integer i=1;
    private int huifuId=0;
    private ProgressDialog dialog;
    private Handler dialogHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pinglun);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initUser();
        initLayout();
        initHandler();
        initParam();
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
                        break;
                }
            }
        };
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                pinglun.setAdapter((PinglunAdapter)msg.obj);

            }
        };
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(Activity_pinglun.this,"添加成功",Toast.LENGTH_SHORT).show();
                        content.clearFocus();
                        content.setText("");
                        break;
                    case 1:
                        Toast.makeText(Activity_pinglun.this,"添加失败",Toast.LENGTH_SHORT).show();
                        content.clearFocus();
                        content.setText("");
                        break;
                    case 2:
                        Toast.makeText(Activity_pinglun.this,"没有更多了",Toast.LENGTH_SHORT).show();
                        pinglun.onRefreshComplete();
                        break;
                    case 3:
                        Toast.makeText(Activity_pinglun.this,"点赞成功",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(Activity_pinglun.this);
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)msg.obj));
            }
        };
        handler4=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                    switch (msg.what){
                        case 0:
                            pinglunAdapter.mItems=(List<PinglunItem>)msg.obj;
                            pinglunAdapter.notifyDataSetChanged();
                            pinglun.onRefreshComplete();
                            break;
                        case 1:
                            pinglunAdapter.mItems.addAll((List<PinglunItem>)msg.obj);
                            pinglunAdapter.notifyDataSetChanged();
                            pinglun.onRefreshComplete();
                            break;
                        case 2:
                            pinglun.onRefreshComplete();
                            break;
                    }
                }
        };
        handler5=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                content.setText("@"+pinglunAdapter.mItems.get(msg.what).getName()+": ");
                huifuId=pinglunAdapter.mItems.get(msg.what).getUserId();
            }
        };
    }
    private void initParam(){
        Bundle bundle = getIntent().getExtras();
        huodongID=bundle.getString("id");
        userID=user.getUserId();
        System.out.println("活动id是："+huodongID);
        System.out.println("用户id是：" + userID);

        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialogHandler.sendEmptyMessage(0);
                    List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(huodongID,"1","活动");
                    dialogHandler.sendEmptyMessage(1);
                    if(thread.isInterrupted()){
                        return;
                    }
                    if(pinglunItemList!=null) {
                        pinglunAdapter = new PinglunAdapter(Activity_pinglun.this, pinglunItemList);
                        Message ms = new Message();
                        ms.obj = pinglunAdapter;
                        handler.sendMessage(ms);
                    }else{
                        pinglunItemList=new ArrayList<PinglunItem>();
                        pinglunAdapter = new PinglunAdapter(Activity_pinglun.this, pinglunItemList);
                        Message ms = new Message();
                        ms.obj = pinglunAdapter;
                        handler.sendMessage(ms);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    private void initLayout(){
        fasong=(TextView)findViewById(R.id.fasong);
        pinglun=(PullToRefreshListView)findViewById(R.id.listView3);
        pinglun.setMode(PullToRefreshBase.Mode.BOTH);
        pinglun.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        pinglun.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        pinglun.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        content=(EditText)findViewById(R.id.editText3);


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
        findViewById(R.id.imageView45).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_pinglun.this);

                builder.setItems(getResources().getStringArray(R.array.ItemArray), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        // TODO 自动生成的方法存根
                        switch (arg1){
                            case 0:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if((Manage.LikesComment(pinglunAdapter.mItems.get(position-1).getPinglunId()+"",user.getUserId(),"0")).contains("成功")){
                                                Message ms=new Message();
                                                ms.what=3;
                                                handler2.sendMessage(ms);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                break;
                            case 1:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Intent intent=new Intent();
                                            intent.setClass(Activity_pinglun.this,Activity_bieren.class);
                                            Bundle bundle=new Bundle();
                                            bundle.putString("id",pinglunAdapter.mItems.get(position-1).getUserId()+"");
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                break;
                            case 2:
                                handler5.sendEmptyMessage(position-1);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        pinglun.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(huodongID,"1","活动");
                            if(pinglunItemList==null){
                                Message ms=new Message();
                                ms.what=2;
                                handler2.sendMessage(ms);
                                return;
                            }
                            Message ms=new Message();
                            ms.what=0;
                            ms.obj=pinglunItemList;
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
                            List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(huodongID,i+1+"","活动");
                            if(pinglunItemList==null){
                                Message ms=new Message();
                                ms.what=2;
                                handler2.sendMessage(ms);
                                return;
                            }
                            i++;
                            Message ms=new Message();
                            ms.what=1;
                            ms.obj=pinglunItemList;
                            handler4.sendMessage(ms);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           System.out.println("aa");
                           if(huifuId!=0){
                               String neirong=content.getText().toString().trim();
                               if(neirong.indexOf("@")!=-1&&neirong.indexOf(": ")!=-1){
                                   Manage.sendPinglunReply(user.getUserId(),huodongID,neirong.substring(neirong.indexOf(": ")+2,neirong.length()),user.getName(),huifuId+"");
                               }
                               huifuId=0;
                           }
                           if((Manage.addComment(content.getText().toString(),huodongID,userID,"活动")).contains("成功")){
                               Message ms=new Message();
                               ms.what=0;
                               handler2.sendMessage(ms);

                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {
                                           List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(huodongID,"","活动");
                                           pinglunAdapter=new PinglunAdapter(Activity_pinglun.this,pinglunItemList);
                                           Message ms=new Message();
                                           ms.obj=pinglunAdapter;
                                           handler.sendMessage(ms);
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }).start();
                           }else{
                               Message ms=new Message();
                               ms.what=1;
                               handler2.sendMessage(ms);

                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {
                                           List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(huodongID,"","活动");
                                           pinglunAdapter=new PinglunAdapter(Activity_pinglun.this,pinglunItemList);
                                           Message ms=new Message();
                                           ms.obj=pinglunAdapter;
                                           handler.sendMessage(ms);
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }).start();
                           }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
    public class PinglunAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        public List<PinglunItem> mItems;
        public List<ImageView>imageViews;

        private Context context;

        public PinglunAdapter(Context context, List<PinglunItem> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mItems = data;
            this.context=context;
            imageViews=new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public PinglunItem getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final PinglunItem item = getItem(position);


            TextView name ;
            TextView content;
            TextView time;
            TextView num;
            final CircleImageView touxiang;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.label_pinglun, null);
                name = (TextView) convertView.findViewById(R.id.textView4);
                content = (TextView) convertView.findViewById(R.id.textView5);
                touxiang = (CircleImageView) convertView.findViewById(R.id.touxiang_pinglun);
                time=(TextView)convertView.findViewById(R.id.textView27);
                num=(TextView)convertView.findViewById(R.id.textView29);
                name.setText(item.getName());
                content.setText(item.getContent());
                imageViews.add(touxiang);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getUserId()+"" + ".jpg" ,touxiang,defaultOptions);

                time.setText(TimeUtil.getInterval(item.getTime()));
                if(item.getNum()!=null) {
                    num.setText(item.getNum() + "人赞");
                }else{
                    num.setText("0" + "人赞");
                }
            }

            return convertView;
        }
    }
}
