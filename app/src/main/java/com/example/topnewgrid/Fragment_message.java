package com.example.topnewgrid;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.imageviewloader.ImageDownloader;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.ReceiverMessage;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.util.ResultUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import http.PostUrl;

/**
 * Created by 真爱de仙 on 2015/2/1.
 */
public class Fragment_message extends Fragment {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions defaultOptions;
    private AppApplication app;
    private User user;
    private Handler handler,handler2;
    private ImageDownloader imageDownloader;
    private MessageAdapter adapter;
    private View view;
    private List<OtherUser> friendList;
    private PullToRefreshListView listView;
    List<ReceiverMessage>receiverMessages;;
    List<ReceiverMessage>receiverMessages2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        initHandler();
        initUser();
        initLayout();
        initListener();
        initParama();
        return view;
    }
    private void initParama(){
        Gson gson=new Gson();
        String result2= AppApplication.message_unread_SharedPreferences.getString("unread","");
        String result= AppApplication.message_unread_SharedPreferences.getString("read","");
        if(!result.equals("")){
            System.out.println("已读信息不为空");
            receiverMessages=gson.fromJson(result, new TypeToken<List<ReceiverMessage>>(){}.getType());
        }else{
            System.out.println("已读信息为空");
            receiverMessages=new ArrayList<>();
        }
        if(!result2.equals("")){
            System.out.println("未读信息不为空");
            System.out.println(result2);
            receiverMessages2=gson.fromJson(result2, new TypeToken<List<ReceiverMessage>>(){}.getType());
            System.out.println(receiverMessages2);
        }else{
            System.out.println("未读信息为空");
            receiverMessages2=new ArrayList<>();
        }
        SharedPreferences.Editor editor = AppApplication.message_unread_SharedPreferences.edit();
        editor.putString("unread","");
        editor.commit();
        receiverMessages.addAll(receiverMessages2);
        if(receiverMessages.size()!=0){
            System.out.println("消息列表不为空");
            System.out.println(receiverMessages);
            adapter=new MessageAdapter(getActivity(),receiverMessages);
            handler.sendEmptyMessage(0);
            SharedPreferences.Editor editor1 = AppApplication.message_unread_SharedPreferences.edit();
            editor1.putString("read",gson.toJson(receiverMessages));
            editor1.commit();
        }else{
            System.out.println("消息列表为空");
            SharedPreferences.Editor editor1 = AppApplication.message_unread_SharedPreferences.edit();
            editor1.putString("read","");
            editor1.commit();
        }
    }
    private void initHandler(){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ((PullToRefreshListView)view.findViewById(R.id.listView4)).setAdapter(adapter);
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
    }
    private void initListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String huodongId=receiverMessages.get(position-1).getHuodongid();
                Intent mIntent = new Intent(getActivity(), Activity_huodong_xiangqing.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id",huodongId);
                mIntent.putExtras(mBundle);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(mIntent);
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

    }
    public void initLayout(){
        listView=(PullToRefreshListView)view.findViewById(R.id.listView4);
    }
    public class MessageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<ReceiverMessage> mItems;


        public MessageAdapter(Context context, List<ReceiverMessage> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mItems = data;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public ReceiverMessage getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ReceiverMessage item = getItem(position);

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
            if (item.getType().equals(ResultUtil.HUIFU)){
                name.setText(item.getUsername());
                locate.setText(item.getContent());
            }else if(item.getType().equals(ResultUtil.YAOQING)){
                name.setText(item.getUsername());
                locate.setText(item.getHuodongname());
            }else if(item.getType().equals(ResultUtil.RECEIVER)){
                name.setText(item.getUsername());
                locate.setText(item.getContent());
            }
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getUserid() + ".jpg", touxiang, defaultOptions);
            return convertView;
        }
    }

}


