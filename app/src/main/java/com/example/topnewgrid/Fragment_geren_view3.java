package com.example.topnewgrid;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.adapter.HuodongCardAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.ColorUtil;
import com.example.topnewgrid.util.TimeUtil;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.HuodongItem;
import http.Httppost;
import http.Manage;
import http.PostUrl;

//import android.support.v4.view.ViewPager;

/**
 * Created by 真爱de仙 on 2014/11/13.
 */
public class Fragment_geren_view3 extends Fragment {


    private AppApplication app;
    private User user;

    private String huodongID;
    private String huodongName;

    private List<HuodongItem> data=new ArrayList<HuodongItem>();
    private ImageDownloader imageDownloader;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions defaultOptions;
    private DisplayImageOptions defaultOptions2;
    int n=0;

    private RadioButton rb1,rb2,rb3;
    private Handler handler,handler2,handler3,handler4,handler7;
    private TextView find;
    private HuodongCardAdapter adapter;
    private int style=1; //1 发布 2参加 3收藏
    private String styles="";
    private ProgressDialog dialog;
    private Handler dialogHandler,prossdialoghandler;
    private PullToRefreshListView listView;
    private int i=1;
    private String id;
    ProgressBarCircularIndeterminate prossdialog;

    // 用来实现 UI 线程的更新。
    Handler mHandler;

    private Httppost httppost;
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        System.out.println("ExampleFragment--onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        System.out.println("ExampleFragment--onCreateView");
        view=inflater.inflate(R.layout.fragment_geren_view2, container, false);

        initUser();
        id=user.getUserId();
        style=2;
        initLayout();
        initHandler();
        initParam();
        initListener();
        return  view;
    }


    private void initUser(){
        app = (AppApplication) getActivity().getApplication();
        user=app.getUser();
    }

    private void initLayout(){
        prossdialog=(ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBarCircularIndetermininate);
        listView=(PullToRefreshListView)view.findViewById(R.id.listView41);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));

    }
    private void initListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getActivity(), Activity_huodong_xiangqing.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id",adapter.URLS.get(arg2-1).getHuodongId() + "");
                mBundle.putString("authorId",adapter.URLS.get(arg2-1).getAuthorId()+"");
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Huodong> huodongList;
                            if(style==1) {
                                huodongList= Manage.ViewActivityByUser(styles, id + "", "1");
                            }else if(style==2){
                                huodongList= Manage.ViewActivityByJoin(styles, id + "", "1");
                            }else{
                                huodongList= Manage.ViewActivityByCollection(styles, id + "", "1");
                            }
                            if(huodongList==null){
                                Message ms=new Message();
                                ms.what=2;
                                handler2.sendMessage(ms);
                                return;
                            }
                            Message ms=new Message();
                            ms.what=0;
                            ms.obj=huodongList;
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
                            List<Huodong> huodongList;
                            if(style==1) {
                                huodongList= Manage.ViewActivityByUser(styles, id + "", i+1+"");
                            }else if(style==2){
                                huodongList= Manage.ViewActivityByJoin(styles, id + "", i+1+"");
                            }else{
                                huodongList= Manage.ViewActivityByCollection(styles, id + "", i+1+"");
                            }
                            if(huodongList==null){
                                Message ms=new Message();
                                ms.what=2;
                                handler2.sendMessage(ms);
                                return;
                            }
                            i++;
                            Message ms=new Message();
                            ms.what=1;
                            ms.obj=huodongList;
                            handler4.sendMessage(ms);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    private void initParam(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    prossdialoghandler.sendEmptyMessage(1);
                    List<Huodong>  huodongList;
                    Gson gson=new Gson();
                    String huodongload= AppApplication.huodong_load_SharedPreferences.getString(user.getUserId()+""+"join","");
                    if(!huodongload.equals("")){
                        huodongList=gson.fromJson(huodongload,new TypeToken<List<Huodong>>(){}.getType());
                        adapter = new HuodongCardAdapter(huodongList,getActivity());
                        Message ms = new Message();
                        ms.obj = adapter;
                        handler.sendMessage(ms);
                    }
                    //dialogHandler.sendEmptyMessage(0);
                    if(style==1) {
                        huodongList= Manage.ViewActivityByUser(styles, id + "", "1");
                        //System.out.println("读取到的："+huodongList.get(0).toString());
                    }else if(style==2){
                        huodongList= Manage.ViewActivityByJoin(styles, id + "", "1");
                    }else{
                        huodongList= Manage.ViewActivityByCollection(styles, id + "", "1");
                    }
                    //dialogHandler.sendEmptyMessage(1);
                    if(huodongList!=null) {
                        adapter = new HuodongCardAdapter( huodongList,getActivity());
                        Message ms = new Message();
                        ms.obj = adapter;
                        handler.sendMessage(ms);
                    }else{
                        huodongList=new ArrayList<Huodong>();
                        adapter = new HuodongCardAdapter( huodongList,getActivity());
                        Message ms = new Message();
                        ms.obj = adapter;
                        handler.sendMessage(ms);
                        SharedPreferences.Editor editor = AppApplication.huodong_load_SharedPreferences.edit();
                        editor.putString(user.getUserId()+""+"join",gson.toJson(huodongList));
                        editor.commit();
                    }
                    prossdialoghandler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initHandler(){
        prossdialoghandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        prossdialog.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        prossdialog.setVisibility(View.GONE);
                        break;
                }
            }
        };
        dialogHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                      //  dialog.show();
                        break;
                    case 1:
                       // dialog.dismiss();
                        break;
                }
            }
        };
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                listView.setAdapter((HuodongCardAdapter)msg.obj);

            }
        };
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"添加失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"没有更多了",Toast.LENGTH_SHORT).show();
                        listView.onRefreshComplete();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),"点赞成功",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(getActivity());
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView) msg.obj));
            }
        };
        handler4=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        adapter.URLS=(List<Huodong>)msg.obj;
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                        break;
                    case 1:
                        adapter.URLS.addAll((List<Huodong>)msg.obj);
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                        break;
                    case 2:
                        listView.onRefreshComplete();
                        break;
                }
            }
        };
        handler7=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(getActivity());
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)msg.obj));
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
        System.out.println("ExampleFragment--onPause");
    }



    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("ExampleFragment--onResume");
    }

    @Override
    public void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
        System.out.println("ExampleFragment--onStop");
    }
    public class HuodongAdapter extends BaseAdapter {

        public List<Huodong> mItems ;
        private LayoutInflater mInflater;
        private Context context;
        public HuodongAdapter( Context context,List<Huodong> URLS) {
            super();
            this.mItems=URLS;
            this.context=context;
            this.mInflater = LayoutInflater.from(context);
        }
        public int getCount() {
            return mItems.size();
        }

        public Huodong getItem(int position) {
            return mItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder ;
            final Huodong item=getItem(position);
            imageDownloader=new ImageDownloader(context);
            ImageView tupian;
            final ImageView touxiang;
            ImageView background;
            TextView time;
            TextView name,colectNum,pinglunNum,neirong,biaoti,distance;
            TextView t1,t2,t3,t4;

            if (view == null) {
                view = mInflater.inflate(R.layout.label_card, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.bofang.setVisibility(View.GONE);
            /*
            view = mInflater.inflate(R.layout.label_card, null);
            holder = new ViewHolder(view);
            */

            holder.biaoti.setText(item.getTitle());
            holder.name.setText(item.getAuthorName());
            String s[] = item.getFengmianUrl().split(";");
            defaultOptions2 = new DisplayImageOptions.Builder()
                    .cacheInMemory().cacheOnDisc().showImageOnFail(ColorUtil.colors[n]).displayer(new SimpleBitmapDisplayer()).showImageOnLoading(ColorUtil.colors[n]).build();
            n++;
            if(n==ColorUtil.colors.length){
                n=0;
            }
            if(s[0].contains("mp4")||s[0].contains("wmv")||s[0].contains("avi")||s[0].contains("3gp")){
                holder.bofang.setVisibility(View.VISIBLE);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0].split("\\.")[0]+".jpg",holder.tupian,defaultOptions2);
            }else {
                //imageDownloader.download("http://" + PostUrl.Url + ":8080/upload/" + s[0], holder.tupian);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0], holder.tupian, defaultOptions2);
            }
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getAuthorId() + ".jpg" ,holder.touxiang,defaultOptions);
            if(item.getChuangjianTime().split(" ").length==2){
                holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()).split(" ")[0]);
            }else {
                holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()));
            }
            if (item.getDistance() != null) {
                if (!item.getDistance().equals("0")) {
                    String lengs = item.getDistance();
                    System.out.println("长度为:" + lengs);
                    double distances = Double.parseDouble(lengs);
                    int distanceInt = (int) distances;
                    if (distanceInt < 1000) {
                        holder.distance.setText(distanceInt + "m");
                    } else {
                        distanceInt = distanceInt / 100;
                        holder.distance.setText(distanceInt / 10 + "km");
                    }
                } else {
                    //holder.distance.setText("<100m");
                }
            }
            /*
            if (item.getSex().equals("男")) {
                holder.background.setImageDrawable(getResources().getDrawable(R.drawable.gender_male));
            } else if (item.getSex().equals("女")) {
                holder.background.setImageDrawable(getResources().getDrawable(R.drawable.gender_female));
            }
            /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getAuthorId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = holder.touxiang;
                            handler7.sendMessage(ms);
                        }else{
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = holder.touxiang;
                            handler8.sendMessage(ms);
                        }
                    }
                }).start();
                */
            if (!item.getStyle().equals("活动")) {
                holder.recompense.setVisibility(View.VISIBLE);
                holder.recompense.setText("$"+item.getRecompense());
            }
                /*
                view = mInflater.inflate(R.layout.label_huodong2, null);

                background = (ImageView) view.findViewById(R.id.imageView108);
                t1 = (TextView) view.findViewById(R.id.textView32);
                t2 = (TextView) view.findViewById(R.id.textView33);
                t3 = (TextView) view.findViewById(R.id.textView34);
                t4 = (TextView) view.findViewById(R.id.textView35);
                tupian = (ImageView) view.findViewById(R.id.imageView18);
                name = (TextView) view.findViewById(R.id.name);
                time = (TextView) view.findViewById(R.id.time2);
                //neirong=(TextView)view.findViewById(R.id.neirong);
                biaoti = (TextView) view.findViewById(R.id.textView31);
                biaoti.getPaint().setFakeBoldText(true);
                distance = (TextView) view.findViewById(R.id.textView6);
                touxiang = (ImageView) view.findViewById(R.id.imageView);

                name.setText(item.getAuthorName());
                //neirong.setText(item.getContent());
                biaoti.setText(item.getTitle());
                System.out.println("发布时间为" + item.getChuangjianTime());
                System.out.println("相隔时间为:" + TimeUtil.getInterval(item.getChuangjianTime()));
                time.setText(TimeUtil.getInterval(item.getChuangjianTime()));
                if (item.getDistance() != null) {
                    if (!item.getDistance().equals("0")) {
                        String lengs = item.getDistance();
                        System.out.println("长度为:" + lengs);
                        double distances = Double.parseDouble(lengs);
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
                }
                if (item.getSex().equals("男")) {
                    ((ImageView) view.findViewById(R.id.imageView121)).setImageDrawable(getResources().getDrawable(R.drawable.gender_male));
                } else if (item.getSex().equals("女")) {
                    ((ImageView) view.findViewById(R.id.imageView121)).setImageDrawable(getResources().getDrawable(R.drawable.gender_female));
                }
                if (item.getStyle().equals("活动")) {
                    background.setImageDrawable(getResources().getDrawable(R.color.activitycolor));
                    t1.setText(item.getFlag());
                    t2.setText("城市:" + item.getLocate());
                    t3.setText(item.getJoinMun() + "人报名");
                    t4.setText("简介:" + item.getContent());
                } else {
                    background.setImageDrawable(getResources().getDrawable(R.color.helpcolor));
                    t1.setText(item.getFlag());
                    t2.setText("城市:" + item.getLocate());
                    t3.setText("悬赏:" + item.getRecompense());
                    t4.setText("简介:" + item.getContent());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getAuthorId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = touxiang;
                            handler7.sendMessage(ms);
                        }
                    }
                }).start();

                if (item.getFengmianUrl() != null && !item.getFengmianUrl().equals("")) {
                    tupian.setVisibility(View.VISIBLE);
                    System.out.println("item.getFengmianUrl():" + item.getFengmianUrl());
                    String s[] = item.getFengmianUrl().split(";");

                    imageDownloader.download("http://" + PostUrl.Url + ":8080/upload/" + s[0], tupian);

                    if (item.getFengmianUrl() == null || item.getFengmianUrl().equals("")) {
                        //tupian.setVisibility(View.GONE);

                    }
                }
                */

            return view;
        }
        public final class ViewHolder {
            ImageView tupian;
            ImageView bofang;
            ImageView touxiang;
            ImageView background;
            TextView time;
            TextView name,colectNum,pinglunNum,neirong,biaoti,distance,recompense;
            TextView t1,t2,t3,t4;
            public ViewHolder(View view){
                bofang=(ImageView)view.findViewById(R.id.imageView238);
                tupian = (ImageView) view.findViewById(R.id.imageView153);
                name = (TextView) view.findViewById(R.id.textView162);
                time = (TextView) view.findViewById(R.id.textView164);
                biaoti = (TextView) view.findViewById(R.id.textView161);
                biaoti.getPaint().setFakeBoldText(true);
                distance = (TextView) view.findViewById(R.id.textView163);
                touxiang = (CircleImageView) view.findViewById(R.id.imageView);
                background=(ImageView)view.findViewById(R.id.imageView154);
                recompense=(TextView)view.findViewById(R.id.textView83);
            }
        }

    }
}
