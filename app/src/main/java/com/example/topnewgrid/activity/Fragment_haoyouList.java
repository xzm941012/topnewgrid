package com.example.topnewgrid.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.topnewgrid.Activity_bieren;
import com.example.topnewgrid.Activity_joinUser;
import com.example.topnewgrid.R;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import http.Manage;
import http.PostUrl;

/**
 * Created by 真爱de仙 on 2015/2/1.
 */
public class Fragment_haoyouList extends Fragment {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions defaultOptions;
    private AppApplication app;
    private User user;
    private Handler handler,handler2,handler3,dialoghandler;
    private ImageDownloader imageDownloader;
    private FriendAdapter adapter;
    private View view;
    private List<OtherUser> friendList;
    private List<OtherUser> friendList2;
    private PullToRefreshListView listView;
    EditText sousuo;
    int ifFirst=0;
    ProgressBarCircularIndeterminate prossdialog;

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
        view = inflater.inflate(R.layout.ry_fragment_haoyoulist, container, false);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        initHandler();
        initUser();
        initLayout();
        initListener();
        friendList=user.getFriendList();
        if(friendList!=null){
            System.out.println("直接通过数据库加载好友信息");
            adapter=new FriendAdapter(getActivity(),friendList);
            Message ms=new Message();
            ms.obj=adapter;
            handler.sendMessage(ms);
            if(friendList.size()==0){
                /*
                ms=new Message();
                ms.what=1;
                ms.obj=view.findViewById(R.id.textView203);
                handler3.sendMessage(ms);
                */
                initParama();
            }
        }else{
            initParama();
        }
        ifFirst=1;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppApplication app1=(AppApplication)(getActivity().getApplication());
        if(ifFirst!=0) {
            if (app1.zhuangtai == 1) {
                friendList=user.getFriendList();
                if(friendList!=null) {
                    System.out.println("直接通过数据库加载好友信息");
                    adapter = new FriendAdapter(getActivity(), friendList);
                    Message ms = new Message();
                    ms.obj = adapter;
                    handler.sendMessage(ms);
                }
                app.zhuangtai=0;
            }
        }
    }

    private void initParama(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialoghandler.sendEmptyMessage(1);
                    friendList = Manage.ViewUserByFriend(user.getUserId(), "", "0");
                    if(friendList==null){
                        friendList=new ArrayList<OtherUser>();
                        Message ms=new Message();
                        ms.what=1;
                        ms.obj=view.findViewById(R.id.textView203);
                        handler3.sendMessage(ms);
                    }else{
                        Message ms=new Message();
                        ms.what=2;
                        ms.obj=view.findViewById(R.id.textView203);
                        handler3.sendMessage(ms);
                    }
                    dialoghandler.sendEmptyMessage(2);
                    user.setFriendList(friendList);
                    Gson gson=new Gson();
                    SharedPreferences.Editor editor = AppApplication.mySharedPreferences.edit();
                    editor.putString(user.getUserId()+"",gson.toJson(friendList));
                    editor.commit();
                    SharedPreferences.Editor editor2 = AppApplication.userSharedPreferences.edit();
                    for(OtherUser otherUser:friendList) {
                        editor2.putString(otherUser.getId() + "", gson.toJson(otherUser));
                    }
                    editor2.commit();
                    adapter=new FriendAdapter(getActivity(),friendList);
                    Message ms=new Message();
                    ms.obj=adapter;
                    handler.sendMessage(ms);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initHandler(){
        dialoghandler=new Handler(){
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
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ((PullToRefreshListView)view.findViewById(R.id.listView4)).setAdapter((FriendAdapter)msg.obj);
                listView.onRefreshComplete();
            }
        };
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageDownloader = new ImageDownloader(getActivity());
                imageDownloader.download("http://" + PostUrl.Media + ":8080/upload/" + msg.what + ".jpg", ((CircleImageView)msg.obj));
            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        ((View)msg.obj).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        ((View)msg.obj).setVisibility(View.GONE);
                        break;
                }
            }
        };
    }
    private void initListener(){
        sousuo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                friendList2 = new ArrayList<OtherUser>();
                String sousuoText = sousuo.getText().toString().trim();
                if(sousuoText.equals("")){
                    adapter=new FriendAdapter(getActivity(),friendList);
                    Message ms=new Message();
                    ms.obj=adapter;
                    handler.sendMessage(ms);
                }else {
                    for (OtherUser otherUser : friendList) {
                        if (otherUser.getUserName().contains(sousuoText)) {
                            friendList2.add(otherUser);
                        }
                    }
                    adapter = new FriendAdapter(getActivity(), friendList2);
                    Message ms = new Message();
                    ms.obj = adapter;
                    handler.sendMessage(ms);
                }
            }
        });
        view.findViewById(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initParama();
            }
        });
        view.findViewById(R.id.textView292).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), Activity_joinUser.class),1);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),Activity_bieren.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",adapter.mItems.get(position-1).getId()+"");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initParama();
            }
        });
    }
    private void initUser() {
        app = (AppApplication) getActivity().getApplication();
        user = app.getUser();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                friendList=user.getFriendList();
                if(friendList!=null){
                    adapter=new FriendAdapter(getActivity(),friendList);
                    Message ms=new Message();
                    ms.obj=adapter;
                    handler.sendMessage(ms);
                    if(friendList.size()==0){
                        handler3.sendEmptyMessage(1);
                    }else{
                        handler3.sendEmptyMessage(2);
                    }
                }else{

                }
                break;
        }
    }
    public void initLayout(){
        sousuo=(EditText)view.findViewById(R.id.placeedit);
        prossdialog=(ProgressBarCircularIndeterminate)view.findViewById(R.id.progressBarCircularIndetermininate);
        listView=(PullToRefreshListView)view.findViewById(R.id.listView4);
    }
    public class FriendAdapter extends BaseAdapter {

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

            TextView name = null;
            TextView dongtai=null;;
            final CircleImageView touxiang;
            TextView locate=null;

            if(convertView == null){
                convertView = mInflater.inflate(R.layout.label_haoyou, null);
            }
            name=(TextView)convertView.findViewById(R.id.textView107);
            touxiang=(CircleImageView)convertView.findViewById(R.id.profile_image);
            locate=(TextView)convertView.findViewById(R.id.textView126);
            /*
            if(!item.getLocate().equals("")) {
                locate.setText(item.getLocate());
            }
            */
            if(!item.getInformation().equals("")) {
                locate.setText(item.getInformation());
            }
            name.setText(item.getUserName());
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getId() + ".jpg" ,touxiang,defaultOptions);
            return convertView;
        }
    }

}


