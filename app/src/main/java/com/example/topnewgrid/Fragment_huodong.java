package com.example.topnewgrid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.topnewgrid.adapter.HuodongCardAdapter2;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.PagerSlidingTabStrip;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.HuodongItem;
import http.Manage;
import http.PostUrl;
import info.hoang8f.android.segmented.SegmentedGroup;
import shake.ShakeActivity;

//import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by 真爱de仙 on 2014/11/13.
 */
public class Fragment_huodong extends Fragment {
    private DisplayImageOptions defaultOptions;
    private DisplayImageOptions defaultOptions2;

    BDLocation location;

    int[]re,fu;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private List<HuodongItem> data = new ArrayList<HuodongItem>();
    private Button remenBt,fujinBt;//热门和附近按钮
    private View view, dengluView;
    private RadioGroup radioGroup;
    private AppApplication app;
    private ImageDownloader imageDownloader;
    private String[] title;
    private View[] view10;
    private ImageView guanliItem; //管理活动栏目按钮
    private List<View> viewList;
    private ViewPager viewPager; // 对应的viewPager
    private HuodongCardAdapter2[] adapter;
    Gson gson;
    private PopupWindow pop;
    private Integer type=0; //0热门,1附近
    private PullToRefreshListView[] listViews;//每个viewpager对应的listview
    private Integer[] pageNum;
    private TextView guanli;
    private PagerAdapter pagerAdapter;
    private Spinner fenlei;//分类选择
    private String fangshi="";
    private TextView fabu;
    View tabs;

    LayoutInflater inflater2;
    Handler handler, handler2, handler3,handler4,handler5,handler6,handler7,handler8,handler9,prossdialog1,prossdialog2;

    LocationClient mLocClient;
    private Calendar startCalendar,endCalendar;
    private String district="";      //城市
    private Double longitude=0.0,latitude=0.0;   //经度,纬度

    private User user;

    private List<String> titleList;
    private ProgressDialog dialog;
    private Handler dialogHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("ExampleFragment--onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        gson=new Gson();
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        defaultOptions2 = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();

        inflater2 = LayoutInflater.from(getActivity());
        System.out.println("ExampleFragment--onCreateView");
        view = inflater.inflate(R.layout.ceshi, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);

        initPopupWindow();
        initHandler();
        initUser();
        mLocClient = new LocationClient(getActivity().getApplicationContext());
        mLocClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(final BDLocation location) {
                if (location == null) return;
                longitude=location.getLongitude();
                latitude=location.getLatitude();
                district=location.getAddrStr();
                System.out.println("定位完成："+longitude+" "+latitude+" "+district);
                if(longitude!=null&&latitude!=null&&district!=null){
                    mLocClient.stop();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Manage.GPSUser(user.getUserId(),location.getLongitude()+"",location.getLatitude()+"",location.getCity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

        });
        //System.out.println("运行了fragment_huodong");
        setLocationOption();
        mLocClient.start();
        initLayout();
        initListenner();
        initTitle();
       /* HuodongItem hd=new HuodongItem(getResources().getDrawable(R.drawable.ic_launcher_touxiang),getResources().getDrawable(R.drawable.tupian),"熊志鸣","2014-10-12","1.2KM","晨跑进行时","每天早上6点麦鹭广场","20/无限制","学习|运动");
        data.add(hd);
        HuodongAdapter adapter = new HuodongAdapter(getActivity(), data);
        ListView listview=(ListView)view.findViewById(R.id.listView);
        listview.setAdapter(adapter);
        */




        //initListenner();
        return view;
    }
    private void initPopupWindow() {
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.label_popupwindows, null);
        view1.findViewById(R.id.relativeLayout42).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                startActivityForResult(new Intent(getActivity(),Activity_choose_huodong.class),1);
            }
        });
        view1.findViewById(R.id.relativeLayout43).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                Intent intent=new Intent(getActivity(),Activity_fujin.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",user.getUserId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        view1.findViewById(R.id.relativeLayout64).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                startActivity(new Intent(getActivity(), ShakeActivity.class));
            }
        });
        pop = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
            }
        });
    }
    private void initTitle(){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager3);
        viewPager.setOffscreenPageLimit(1);
        //   title=app.getTitle();
        view10 = new View[10];


        pagerAdapter = new PagerAdapter() {

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
                System.out.println("position是" + position);
                final View views = viewList.get(position);
                /*
                if(type==0){
                    if(re[position]==1){
                        return viewList.get(position);
                    }else{
                        re[position]=1;
                    }
                }else{
                    if(fu[position]==1){
                        return viewList.get(position);
                    }else{
                        fu[position]=1;
                    }
                }
                */
                /*
                if(listViews[position]!=null){
                    container.addView(viewList.get(position));
                    return viewList.get(position);
                }
                */
                /*
                Message ms=new Message();
                ms.what=2;
                ms.obj=viewList.get(position).findViewById(R.id.textView202);
                dialogHandler.sendMessage(ms);
                */
                listViews[position] = (PullToRefreshListView) viewList.get(position).findViewById(R.id.listView4);
                listViews[position].setMode(PullToRefreshBase.Mode.BOTH);
                listViews[position].getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
                listViews[position].getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
                listViews[position].getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));

                /*
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user!=null) {
                            startActivity(new Intent(getActivity(), com.example.topnewgrid.choosephotos.choosephotos.MainActivity.class));
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("你还没有登陆，无法发布活动")
                                    .setCancelable(false)
                                    .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            startActivity(new Intent(getActivity(), Activity_geren.class));
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
                */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Message dialogms=new Message();
                            dialogms.obj=views.findViewById(R.id.progressBarCircularIndetermininate);
                            prossdialog1.sendMessage(dialogms);
                            System.out.println("开始读取活动列表");
                            //final List<Huodong> huodongList = Manage.findbyTag(titleList.get(position), type+"", "1",longitude+"",latitude+"",district);
                            List<Huodong> huodongList;
                            String huodongload= AppApplication.huodong_load_SharedPreferences.getString(user.getUserId()+""+titleList.get(position),"");
                            if(!huodongload.equals("")){
                                huodongList=gson.fromJson(huodongload,new TypeToken<List<Huodong>>(){}.getType());
                                adapter[position] = new HuodongCardAdapter2(huodongList, getActivity());
                                Message ms = new Message();
                                ms.what = position;
                                ms.obj = adapter[position];
                                handler.sendMessage(ms);
                            }

                            listViews[position].setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                    if(firstVisibleItem==1||firstVisibleItem==0){
                                        Log.e("log", "滑到顶部");
                                        dialogHandler.sendEmptyMessage(5);
                                    }else{
                                        dialogHandler.sendEmptyMessage(4);
                                    }

                                }
                            });

                            listViews[position].setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), Activity_huodong_xiangqing.class);
                                    Bundle mBundle = new Bundle();
                                    mBundle.putString("id",adapter[position].URLS.get(arg2-1).getHuodongId() + "");
                                    mBundle.putString("authorId",adapter[position].URLS.get(arg2-1).getAuthorId()+"");
                                    intent.putExtras(mBundle);
                                    startActivityForResult(intent,1);

                                }
                            });
                            if(position==0){
                                if(type==1) {
                                    //huodongList = Manage.RecommendActivity(user.getUserId(), AppApplication.location.getCity(), fangshi, AppApplication.location.getLongitude() + "", AppApplication.location.getLatitude() + "", "10");
                                    huodongList = Manage.findActivityByTag(fangshi, "", 0 + "", "1",  "",  "", "");

                                }else{
                                    huodongList = Manage.findActivityByTag(fangshi, "", 0 + "", "1", "", "", "");
/*
                                    while(AppApplication.location==null){

                                    }
                                    huodongList = Manage.RecommendActivity(user.getUserId(), AppApplication.location.getCity(), fangshi, "","", "10");
                               */
                                }
                            }else {
                                if (type == 1) {
                                    huodongList = Manage.findActivityByTag(fangshi, titleList.get(position), type + "", "1", AppApplication.location.getLongitude() + "", AppApplication.location.getLatitude() + "", AppApplication.location.getCity());
                                } else {
                                    huodongList = Manage.findActivityByTag(fangshi, titleList.get(position), type + "", "1", "", "", "");
                                }
                            }
                            if(huodongList==null){
                                huodongList=new ArrayList<Huodong>();
                                Message ms=new Message();
                                ms.what=3;
                                ms.obj=viewList.get(position).findViewById(R.id.textView202);
                                dialogHandler.sendMessage(ms);

                                System.out.println("显示文字");
                                dialogms=new Message();
                                dialogms.obj=views.findViewById(R.id.progressBarCircularIndetermininate);
                                prossdialog2.sendMessage(dialogms);
                            }
                            System.out.println(huodongList.size());
                            adapter[position] = new HuodongCardAdapter2(huodongList, getActivity());
                            System.out.println("adapter初始化完毕");
                            Message ms = new Message();
                            ms.what = position;
                            ms.obj = adapter[position];
                            handler.sendMessage(ms);
                            dialogms=new Message();
                            dialogms.obj=views.findViewById(R.id.progressBarCircularIndetermininate);
                            prossdialog2.sendMessage(dialogms);
                            SharedPreferences.Editor editor = AppApplication.huodong_load_SharedPreferences.edit();
                            editor.putString(user.getUserId()+""+titleList.get(position),gson.toJson(huodongList));
                            editor.commit();
                            listViews[position].setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                                @Override
                                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                List<Huodong> huodongList2;
                                                if(position==0){
                                                    huodongList2 = Manage.findActivityByTag(fangshi, "", 0 + "",   1+"", "", "", "");
                                                }else {
                                                    if (type == 1) {
                                                        huodongList2 = Manage.findActivityByTag(fangshi, titleList.get(position), type + "", "1", AppApplication.location.getLongitude() + "", AppApplication.location.getLatitude() + "", AppApplication.location.getCity());
                                                    } else {
                                                        huodongList2 = Manage.findActivityByTag(fangshi, titleList.get(position), type + "", "1", "", "", "");
                                                    }
                                                }
                                                if(huodongList2==null){
                                                    handler9.sendEmptyMessage(position);
                                                    return;
                                                }
                                                Message ms1=new Message();
                                                ms1.what=2;
                                                ms1.obj=viewList.get(position).findViewById(R.id.textView202);
                                                dialogHandler.sendMessage(ms1);
                                                Message ms=new Message();
                                                ms.what=position;
                                                ms.obj=huodongList2;
                                                handler4.sendMessage(ms);
                                                SharedPreferences.Editor editor = AppApplication.huodong_load_SharedPreferences.edit();
                                                editor.putString(user.getUserId()+""+titleList.get(position),gson.toJson(huodongList2));
                                                editor.commit();
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
                                                System.out.println("longitude:"+longitude+" "+"latitude:"+latitude);
                                                List<Huodong> huodongList2;
                                                if(position==0){
                                                    huodongList2 = Manage.findActivityByTag(fangshi, "", 0 + "",  pageNum[position] + 1+"", "", "", "");

                                                }else {
                                                    if (type == 1) {
                                                        huodongList2 = Manage.findActivityByTag(fangshi, titleList.get(position), type + "", pageNum[position] + 1 + "", AppApplication.location.getLongitude() + "", AppApplication.location.getLatitude() + "", AppApplication.location.getCity());
                                                    } else {
                                                        huodongList2 = Manage.findActivityByTag(fangshi, titleList.get(position), type + "", pageNum[position] + 1 + "", "", "", "");
                                                    }
                                                }
                                                if(huodongList2==null){
                                                    handler9.sendEmptyMessage(position);
                                                    return;
                                                }
                                                Message ms=new Message();
                                                ms.what=2;
                                                ms.obj=viewList.get(position).findViewById(R.id.textView202);
                                                dialogHandler.sendMessage(ms);
                                                System.out.println("隐藏文字");
                                                ms=new Message();
                                                ms.what=position;
                                                ms.obj=huodongList2;
                                                handler2.sendMessage(ms);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
    }
    private void initUser() {
        app = (AppApplication) getActivity().getApplication();
        user = app.getUser();
        if (user != null) {


            System.out.println("有user");
            if (user.getHuodongTitle() == null) {
                System.out.println("没有标签");
                if (user.getProfession() == null) {
                    String[] array = getResources().getStringArray(R.array.huodongTitlenotnull);
                    titleList = new ArrayList<String>();// 每个页面的Title数据
                    for (String arrays : array) {
                        titleList.add(arrays);
                    }
                } else if (user.getProfession().contains("学生")) {
                    String[] array = getResources().getStringArray(R.array.huodongTitlenotnull);
                    titleList = new ArrayList<String>();// 每个页面的Title数据
                    for (String arrays : array) {
                        titleList.add(arrays);
                    }
                } else if (user.getProfession().contains("上班族")) {
                    String[] array = getResources().getStringArray(R.array.huodongTitlenotnull);
                    titleList = new ArrayList<String>();// 每个页面的Title数据
                    for (String arrays : array) {
                        titleList.add(arrays);
                    }
                } else {
                    String[] array = getResources().getStringArray(R.array.huodongTitlenotnull);
                    titleList = new ArrayList<String>();// 每个页面的Title数据
                    for (String arrays : array) {
                        titleList.add(arrays);
                    }
                }
                listViews=new PullToRefreshListView[titleList.size()];
                re=new int[titleList.size()];
                fu=new int[titleList.size()];
                for(int res=0;res<re.length;res++){
                    re[res]=0;
                    fu[res]=0;
                }

                pageNum=new Integer[titleList.size()];
                for(Integer i=0;i<pageNum.length;i++){
                    pageNum[i]=1;
                }
                view10 = new View[titleList.size()];
                adapter = new HuodongCardAdapter2[titleList.size()];
                viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
                for (int i = 0; i < titleList.size(); i++) {
                    View view = inflater2.inflate(R.layout.ceshi_02, null);
                    viewList.add(view);
                }
            } else {
                System.out.println("有标签");
                titleList = user.getHuodongTitle();
                List<String >titleList2=new ArrayList<>();
                for(int i=0;i<titleList.size();i++){
                    if(titleList.get(i).contains("全部")||titleList.get(i).contains("推荐"))
                    {
                        titleList2.add( "全部");
                    }else{
                        titleList2.add(titleList.get(i));
                    }
                }
                titleList=titleList2;
                pageNum=new Integer[titleList.size()];
                for(Integer i=0;i<pageNum.length;i++){
                    pageNum[i]=1;
                }
                listViews=new PullToRefreshListView[titleList.size()];
                re=new int[titleList.size()];
                fu=new int[titleList.size()];
                for(int res=0;res<re.length;res++){
                    re[res]=0;
                    fu[res]=0;
                }
                view10 = new View[titleList.size()];
                adapter = new HuodongCardAdapter2[titleList.size()];
                viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
                for (int i = 0; i < titleList.size(); i++) {
                    View view = inflater2.inflate(R.layout.ceshi_02, null);
                    viewList.add(view);
                }
            }
        } else {
            String[] array = getResources().getStringArray(R.array.huodongTitlenull);
            titleList = new ArrayList<String>();// 每个页面的Title数据
            for (String arrays : array) {
                titleList.add(arrays);
            }
            pageNum=new Integer[titleList.size()];
            for(Integer i=0;i<pageNum.length;i++){
                pageNum[i]=1;
            }
            listViews=new PullToRefreshListView[titleList.size()];
            re=new int[titleList.size()];
            fu=new int[titleList.size()];
            for(int res=0;res<re.length;res++){
                re[res]=0;
                fu[res]=0;
            }
            view10 = new View[titleList.size()];
            adapter = new HuodongCardAdapter2[titleList.size()];
            viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
            for (int i = 0; i < titleList.size(); i++) {
                View view = inflater2.inflate(R.layout.ceshi_02, null);
                viewList.add(view);
            }
        }
    }


    public void initLayout() {
        tabs=view.findViewById(R.id.relativeLayout);
        ((SegmentedGroup)view.findViewById(R.id.segmented2)).setTintColor(getResources().getColor(R.color.radiobutton),getResources().getColor(R.color.activitybarblue));
        fenlei=(Spinner)view.findViewById(R.id.spinner);
        fabu=(TextView)view.findViewById(R.id.textView70);
        String[] str={"筛选","活动","求助"};
        ArrayAdapter adapter=new ArrayAdapter(getActivity(),R.layout.spinner,str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource();
        fenlei.setAdapter(adapter);
        fujinBt=(Button)view.findViewById(R.id.button23);
        remenBt=(Button)view.findViewById(R.id.button21);
        guanliItem = (ImageView) view.findViewById(R.id.imageView);

    }

    private void initListenner() {
        view.findViewById(R.id.imageView234).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Activity_find.class));
            }
        });
        view.findViewById(R.id.buttonFloat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), Activity_fabu_huodong_step3.class),1);
            }
        });
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.showAsDropDown(v, 0, 30);
                }

                startActivityForResult(new Intent(getActivity(), Activity_fabu_xuqiu2.class),1);
                */
                //startActivityForResult(new Intent(getActivity(), Activity_fabu_huodong_step1.class),1);
            }
        });
        fenlei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        fangshi = "";
                        initUser();
                        initTitle();
                        break;
                    case 1:
                        fangshi = "活动";
                        initUser();
                        initTitle();
                        break;
                    case 2:
                        fangshi = "需求";
                        initUser();
                        initTitle();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        guanliItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop.isShowing()) {
                    pop.dismiss();
                } else {
                    pop.showAsDropDown(v, 0, 30);
                }
            }

        });
        /*
        guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    getActivity().startActivityForResult(new Intent(getActivity(), Activity_guanli.class), 1);
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("你还没有登陆，无法发布活动")
                            .setCancelable(false)
                            .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivityForResult(new Intent(getActivity(), Activity_geren.class),1);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();

                    getActivity().startActivityForResult(new Intent(getActivity(), Activity_login.class), 1);
                }
            }
        });
               */
        fujinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("你的坐标是:" + AppApplication.location.getLongitude() + "     " + AppApplication.location.getLatitude() + "    " + AppApplication.location.getCity());
                for(PullToRefreshListView listView:listViews){
                    listView=null;
                }
                for(int res=0;res<re.length;res++){
                    re[res]=0;
                    fu[res]=0;
                }
                type=1;
                handler5.sendMessage(new Message());
            }
        });
        remenBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(PullToRefreshListView listView:listViews){
                    listView=null;
                }
                for(int res=0;res<re.length;res++){
                    re[res]=0;
                    fu[res]=0;
                }
                type=0;
                handler5.sendMessage(new Message());
            }
        });
        /*
        view.findViewById(R.id.imageView17).setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mLocClient.registerLocationListener(new BDLocationListener() {

                    @Override
                    public void onReceiveLocation(BDLocation location) {
                        System.out.println("000");
                        if (location == null) return;
                        DialogUtil.showDialog(getActivity(), location.getCity() + "," + location.getAddrStr(), true);
                    }

                });
                setLocationOption();
                mLocClient.start();
            }
        });
        */

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                System.out.println("跳转回来了1");
                initUser();
                initTitle();
                break;
            case 2:
                System.out.println("跳转回来了2");
                initUser();
                initTitle();
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("完善一下资料，获取个性化订阅")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivityForResult(new Intent(getActivity(), Activity_choose_sex.class),1);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                */
                break;
            case 3:
                System.out.println("跳转回来了3");
                Bundle b=data.getExtras();
                String groupId=b.getString("groupid");
                System.out.println("groupid:"+groupId);
                /*
                Intent intent = new Intent();
                intent.setClass(getActivity(), Activity_huodong_xiangqing.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id", groupId);
                intent.putExtras(mBundle);
                startActivity(intent);
                */

                break;
            case 4:
                Intent intent=new Intent();
                intent.putExtras(data.getExtras());
                intent.setClass(getActivity(),Activity_huodong_xiangqing.class);
                startActivity(intent);
                break;
        }
    }
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);                //打开gps
        option.setCoorType("bd09ll");        //设置坐标类型
        option.setIsNeedAddress(true);//设置地址信息，默认无地址信息
        option.setAddrType("all");

        option.setScanSpan(1000);    //设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }

    private void initHandler() {
        prossdialog1=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ((View)msg.obj).setVisibility(View.VISIBLE);
            }
        };
        prossdialog2=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ((View)msg.obj).setVisibility(View.GONE);
            }
        };
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
                    case 2:
                        ((View)msg.obj).setVisibility(View.GONE);
                        break;
                    case 3:
                        ((View)msg.obj).setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        tabs.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        tabs.setVisibility(View.VISIBLE);
                        break;
                    case 6:

                }
            }
        };
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
               // viewList.get(msg.what).findViewById(R.id.textView202).setVisibility(View.VISIBLE);
                ((PullToRefreshListView)(viewList.get(msg.what).findViewById(R.id.listView4))).setAdapter((HuodongCardAdapter2) msg.obj);
            }
        };
        handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                adapter[msg.what].URLS.addAll((List<Huodong>) msg.obj);
                adapter[msg.what].notifyDataSetChanged();
                listViews[msg.what].onRefreshComplete();
                pageNum[msg.what]++;
            }
        };
        handler3 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ((ImageView) view.findViewById(R.id.imageView17)).setImageBitmap((Bitmap) msg.obj);
            }
        };
        handler4 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                adapter[msg.what].URLS=((List<Huodong>) msg.obj);
                adapter[msg.what].notifyDataSetChanged();
                listViews[msg.what].onRefreshComplete();
                pageNum[msg.what]=1;
            }
        };
        handler5=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                viewPager.setAdapter(pagerAdapter);
            }
        };
        handler6=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(getActivity());
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)msg.obj));
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
        handler8=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(isAdded()) {
                    ((CircleImageView) msg.obj).setImageDrawable(getResources().getDrawable(R.drawable.avatar_default));
                }
            }
        };
        handler9=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listViews[msg.what].onRefreshComplete();
            }
        };


    }


    /* private void initListenner(){
         ListView listView=(ListView)view.findViewById(R.id.listView);
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                 // TODO Auto-generated method stub
                 //触发的事件
                 Intent intent=new Intent();
                 intent.setClass(getActivity(),Activity_huodong_xiangqing.class);

                 startActivity(intent);
             }
         });
     }
 */
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        System.out.println("ExampleFragment--onPause");
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("ExampleFragment--onResume");
    }

        @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mLocClient.stop();
        System.out.println("ExampleFragment--onStop");
    }
    public class HuodongLoadAdapter extends BaseAdapter {

        public List<Huodong> URLS ;
        private LayoutInflater mInflater;
        private Context context;
        public HuodongLoadAdapter(List<Huodong> URLS, Context context) {
            super();
            this.URLS=URLS;
            this.context=context;
            this.mInflater = LayoutInflater.from(context);
        }
        public int getCount() {
            return URLS.size();
        }

        public Huodong getItem(int position) {
            return URLS.get(position);
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
            if(s[0].contains("mp4")||s[0].contains("wmv")||s[0].contains("avi")||s[0].contains("3gp")){
                holder.bofang.setVisibility(View.VISIBLE);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0].split("\\.")[0]+".jpg",holder.tupian,defaultOptions2);
            }else {
                //imageDownloader.download("http://" + PostUrl.Url + ":8080/upload/" + s[0], holder.tupian);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0], holder.tupian, defaultOptions2);
            }
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getAuthorId() + ".jpg" ,holder.touxiang,defaultOptions);
            /*
            if(item.getChuangjianTime().split(" ").length==2){
                    holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()).split(" ")[0]);
                }else {
                    holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()));
                }
                */
            holder.time.setText(item.getContent());
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
                        holder.distance.setText("<100m");
                    }
                }
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
                    if(!item.getRecompense().equals("无")&&!item.getRecompense().equals("免费"))
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
                recompense=(TextView)view.findViewById(R.id.textView165);
            }
        }

    }
}
