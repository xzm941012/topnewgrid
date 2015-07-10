package com.example.topnewgrid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.ColorUtil;
import com.example.topnewgrid.util.TimeUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.PinglunItem;
import http.Manage;
import http.PostUrl;


/**
 * Created by 真爱de仙 on 2015/1/21.
 */
public class Activity_receiver_pinglun extends Activity {

    private AppApplication app;
    private User user;

    private DisplayImageOptions defaultOptions;
    private DisplayImageOptions defaultOptions2;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private PullToRefreshListView pinglun;
    private ImageDownloader imageDownloader;
    private String huodongID,userID;
    private TextView fasong;
    private Handler handler,handler2,handler3,handler4,handler5,handler6,handler7;
    private EditText content;
    private Huodong huodong;
    private PinglunItem pinglunItem;
    private PinglunAdapter pinglunAdapter;
    private Boolean arg=false;     //是否是自己发布的
    private Integer i=1;
    private int huifuId=0;
    Thread thread;
    private ProgressDialog dialog;
    private Handler dialogHandler;
    String[]month={"一","二","三","四","五","六","七","八","九","十","十一","十二"};
    View convertView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_receiver_pinglun);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(ColorUtil.colors[15]).showImageOnLoading(ColorUtil.colors[15]).build();
        defaultOptions2 = new DisplayImageOptions.Builder()
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
                        Toast.makeText(Activity_receiver_pinglun.this,"添加成功",Toast.LENGTH_SHORT).show();
                        content.clearFocus();
                        content.setText("");
                        break;
                    case 1:
                        Toast.makeText(Activity_receiver_pinglun.this,"添加失败",Toast.LENGTH_SHORT).show();
                        content.clearFocus();
                        content.setText("");
                        break;
                    case 2:
                        Toast.makeText(Activity_receiver_pinglun.this,"没有更多了",Toast.LENGTH_SHORT).show();
                        pinglun.onRefreshComplete();
                        break;
                    case 3:
                        Toast.makeText(Activity_receiver_pinglun.this,"点赞成功",Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(Activity_receiver_pinglun.this);
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
                        case 3:
                            imageDownloader = new ImageDownloader(Activity_receiver_pinglun.this);
                            imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)findViewById(R.id.touxiang_pinglun)));
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
        handler6=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(Activity_receiver_pinglun.this);
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)msg.obj));
            }
        };
        handler7=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Map<String,Object> map=(HashMap<String,Object>)(msg.obj);
                        imageDownloader = new ImageDownloader(Activity_receiver_pinglun.this);
                        imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + map.get("picturePath") ,(ImageView) map.get("view"));
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }

            }
        };
    }
    private void initHeaderView(){
        LayoutInflater mInflater = getLayoutInflater();
        PinglunItem item = pinglunItem;
        TextView name = null;
        TextView content=null;
        TextView time=null;
        TextView num=null;

        convertView = mInflater.inflate(R.layout.label_suiji, null);
        ImageView[]imageviews2=new ImageView[10];
        imageviews2[0]=(ImageView)convertView.findViewById(R.id.lin11);
        imageviews2[1]=(ImageView)convertView.findViewById(R.id.lin12);
        imageviews2[2]=(ImageView)convertView.findViewById(R.id.lin13);
        imageviews2[3]=(ImageView)convertView.findViewById(R.id.lin21);
        imageviews2[4]=(ImageView)convertView.findViewById(R.id.lin22);
        imageviews2[5]=(ImageView)convertView.findViewById(R.id.lin23);
        imageviews2[6]=(ImageView)convertView.findViewById(R.id.lin31);
        imageviews2[7]=(ImageView)convertView.findViewById(R.id.lin32);
        imageviews2[8]=(ImageView)convertView.findViewById(R.id.lin33);
        content = (TextView) convertView.findViewById(R.id.textView157);
        //time = (TextView) convertView.findViewById(R.id.textView27);
        //num = (TextView) convertView.findViewById(R.id.textView29);
        content.setText(item.getContent());
        //content.getPaint().setFakeBoldText(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ((TextView)(convertView.findViewById(R.id.textView151))).getPaint().setFakeBoldText(true);
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getUserId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getUserId() + "");
                            handler3.sendMessage(ms);
                        }
                    }
                }).start();
                */
        if(item.getMediaPath()!=null&&!("").equals(item.getMediaPath())){
            final List<String>mediaPath=item.getMediaPath();
            System.out.println("有视频，视频数量为:"+mediaPath.size());
            ImageView videoView=(ImageView)convertView.findViewById(R.id.imageView162);
            videoView.setVisibility(View.VISIBLE);
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" +mediaPath.get(0).split("\\.")[0]+".jpg" ,videoView,defaultOptions);
            convertView.findViewById(R.id.imageView162).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                            Intent it = new Intent(Intent.ACTION_VIEW);
                            //intent.putExtra("isHW", boolean isHW);

                            Uri uri = Uri.parse("rtsp://192.168.1.102:8086");
                            it.setClassName("com.baidu.cyberplayer.engine","com.baidu.cyberplayer.engine.PlayingActivity");
                            it.setData(uri);
                            startActivity(it);


                            BEngineManager mgr = BCyberPlayerFactory.createEngineManager();
                            //判断 CyberPlayerEngine 是否已安装
                            Boolean isInstalled = mgr.EngineInstalled();
//如果未安装，则执行安装
                            if(!isInstalled){
                                mgr.installAsync(new BEngineManager.OnEngineListener(){
                                    @Override
                                    public boolean onPrepare() {
                                        return true;
                                    }
                                    @Override
                                    public int onDownload(int total, int current) {
// TODO Auto-generated method stub
                                        return 0;
                                    }
                                    @Override
                                    public int onPreInstall() {
                                        return 0;
                                    }
                                    @Override
                                    public void onInstalled(int result) {
// TODO Auto-generated method stub
                                    }
                                });
                            }


                    Intent it = new Intent(Intent.ACTION_VIEW);
                    //Uri uri = Uri.parse("http://121.41.74.72:8080/upload/"+item.getMediaPath().get(0));
                    Uri uri = Uri.parse("http://121.41.74.72:8080/upload/"+"sample_100kbit.mp4");
                    it.setDataAndType(uri, "video/mp4");
                    startActivity(it);
/*
                            Intent intent=new Intent();
                            Bundle bundle=new Bundle();
                            //bundle.putString("path","http://121.41.74.72:8080/upload/"+item.getMediaPath().get(0));
                            bundle.putString("path","rtsp://192.168.1.102:8086");
                            intent.putExtras(bundle);
                            intent.setClass(Activity_receiver.this,VlcVideoActivity.class);
                            startActivity(intent);

                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putString("path","http://121.41.74.72:8080/upload/"+mediaPath.get(0));
                    //bundle.putString("path","rtsp://192.168.1.102:8086");
                    intent.putExtras(bundle);
                    intent.setClass(Activity_receiver_pinglun.this,VlcVideoActivity.class);
                    startActivity(intent);


                    String url = "http://121.41.74.72:8080/upload/"+mediaPath.get(0).split("\\.")[0]+".mp4";
                    //String url = "http://121.41.74.72:8080/upload/"+"3.mp4";
                    Intent intent = new Intent();
                    intent.setClass(Activity_receiver_pinglun.this, BBVideoPlayer.class);
                    intent.putExtra("url", url);

                    startActivity(intent);
                    */
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("http://"+PostUrl.Media+":8080/upload/"+mediaPath.get(0).split("\\.")[0]+".mp4");
                    it.setDataAndType(uri, "video/mp4");
                    startActivity(it);
                }
            });
        }else if(item.getPicturePath()!=null&&!("").equals(item.getPicturePath())){
            List<String>pictureList=item.getPicturePath();
            System.out.println("有图片，图片数量为:"+pictureList.size());
            for(int i=0;i<pictureList.size();i++){
                imageviews2[i].setVisibility(View.VISIBLE);
                Map<String,Object> map=new HashMap<>();
                map.put("view",imageviews2[i]);
                map.put("picturePath",pictureList.get(i));
                Message ms=new Message();
                ms.what=0;
                ms.obj=map;
                handler7.sendMessage(ms);
            }
        }
        String time1 = TimeUtil.getInterval(item.getTime());
        if(time1.contains("刚刚")||time1.contains("秒前")||time1.contains("分钟前")||time1.contains("小时前")){
            convertView.findViewById(R.id.textView151).setVisibility(View.VISIBLE);
            ((TextView)(convertView.findViewById(R.id.textView151))).setText("今天");
        }else if(time1.contains("1天")){
            convertView.findViewById(R.id.textView151).setVisibility(View.VISIBLE);
            ((TextView)(convertView.findViewById(R.id.textView151))).setText("昨天");
        }else if(time1.contains("2天")){
            convertView.findViewById(R.id.textView151).setVisibility(View.VISIBLE);
            ((TextView)(convertView.findViewById(R.id.textView151))).setText("前天");
        }else{
            time1=time1.split(" ")[0];
            convertView.findViewById(R.id.textView160).setVisibility(View.VISIBLE);
            ((TextView)(convertView.findViewById(R.id.textView160))).setText(time1.split("-")[0]+"年");
            convertView.findViewById(R.id.textView159).setVisibility(View.VISIBLE);
            ((TextView)(convertView.findViewById(R.id.textView159))).setText(month[Integer.parseInt(time1.split("-")[1].trim())-1]+"月");
            convertView.findViewById(R.id.textView158).setVisibility(View.VISIBLE);
            ((TextView)(convertView.findViewById(R.id.textView158))).setText(time1.split("-")[2]);
        }


    }
    private void initParam(){
        Bundle bundle = getIntent().getExtras();
        huodong=(Huodong)getIntent().getSerializableExtra("huodong");
        pinglunItem=(PinglunItem)getIntent().getSerializableExtra("suiji");
        huodongID=huodong.getHuodongId()+"";
        ((TextView)findViewById(R.id.textView4)).setText(huodong.getAuthorName());
        ((TextView)findViewById(R.id.textView5)).setText(huodong.getLocationAdress());
        initHeaderView();
        ListView mListView = pinglun.getRefreshableView();
        mListView.addHeaderView(convertView);
        imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + huodong.getAuthorId() + ".jpg",((CircleImageView)findViewById(R.id.touxiang_pinglun)),defaultOptions2);

        userID=user.getUserId();
        System.out.println("活动id是："+huodongID);
        System.out.println("用户id是：" + userID);
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialogHandler.sendEmptyMessage(0);
                    List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(pinglunItem.getPinglunId()+"","1","广播");
                    //
                    // System.out.println("广播评论是"+"size:"+pinglunItemList.size()+" "+"content:"+pinglunItemList.get(0));
                    dialogHandler.sendEmptyMessage(1);
                    if(thread.isInterrupted()){
                        return;
                    }
                    if(pinglunItemList!=null) {
                        pinglunAdapter = new PinglunAdapter(Activity_receiver_pinglun.this, pinglunItemList);
                        Message ms = new Message();
                        ms.obj = pinglunAdapter;
                        handler.sendMessage(ms);
                    }else{
                        pinglunItemList=new ArrayList<PinglunItem>();
                        pinglunAdapter = new PinglunAdapter(Activity_receiver_pinglun.this, pinglunItemList);
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
        findViewById(R.id.imageView201).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
        pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_receiver_pinglun.this);

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
                                            intent.setClass(Activity_receiver_pinglun.this,Activity_bieren.class);
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
        */
        pinglun.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(pinglunItem.getPinglunId()+"","1","广播");
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
                            List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(pinglunItem.getPinglunId()+"",i+1+"","广播");
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
                           if((Manage.addComment(content.getText().toString(),pinglunItem.getPinglunId()+"",userID,"广播")).contains("成功")){
                               Message ms=new Message();
                               ms.what=0;
                               handler2.sendMessage(ms);

                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {
                                           List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(pinglunItem.getPinglunId()+"","","广播");
                                           pinglunAdapter=new PinglunAdapter(Activity_receiver_pinglun.this,pinglunItemList);
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
                                           List<PinglunItem>pinglunItemList=Manage.ViewCommentByActivity(pinglunItem.getPinglunId()+"","","广播");
                                           pinglunAdapter=new PinglunAdapter(Activity_receiver_pinglun.this,pinglunItemList);
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
            final ViewHolder holder ;
            final CircleImageView touxiang;
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.label_pinglun, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
                holder.name.setText(item.getName());
                holder.content.setText(item.getContent());
                imageViews.add(holder.touxiang);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getUserId() + ".jpg" ,holder.touxiang,defaultOptions2);
            holder.time.setText(TimeUtil.getInterval(item.getTime()));
                if(item.getNum()!=null) {
                    holder.num.setText(item.getNum() + "人赞");
                }else{
                    holder.num.setText("0" + "人赞");
                }
            return convertView;
        }
        public final class ViewHolder {
            TextView name ;
            TextView content;
            TextView time;
            TextView num;
            CircleImageView touxiang;
            public ViewHolder(View view){
                name = (TextView) view.findViewById(R.id.textView4);
                content = (TextView) view.findViewById(R.id.textView5);
                touxiang = (CircleImageView) view.findViewById(R.id.touxiang_pinglun);
                time=(TextView)view.findViewById(R.id.textView27);
                num=(TextView)view.findViewById(R.id.textView29);
            }
        }
    }
}
