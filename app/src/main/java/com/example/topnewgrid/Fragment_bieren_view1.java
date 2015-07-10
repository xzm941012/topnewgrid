package com.example.topnewgrid;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import http.PostUrl;

//import android.support.v4.view.ViewPager;

/**
 * Created by 真爱de仙 on 2014/11/13.
 */
public class Fragment_bieren_view1 extends Fragment {

    public static Fragment_bieren_view1 fragment_geren_view1;
    private AppApplication app;
    private DisplayImageOptions defaultOptions;
    private User user;
    View view;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView touxiangImageview;
    TextView sex;
    OtherUser otherUser;
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
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnLoading(R.drawable.avatar_default).showImageOnFail(R.drawable.avatar_default).build();
        Fragment_bieren_view1 fragment_geren_view1;
        initUser();
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
        touxiangImageview=(ImageView)view.findViewById(R.id.imageView90);
        imageLoader.displayImage("http://"+ PostUrl.Media+":8080/upload/"+otherUser.getId()+".jpg", touxiangImageview, defaultOptions);
        sex=(TextView)view.findViewById(R.id.textView257);
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
    private void initListener(){
        view.findViewById(R.id.foucusLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Activity_foucused.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("id",otherUser.getId()+"");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
    }
    public void setJianjie(String jianjie){
        ((TextView)view.findViewById(R.id.textView259)).setText(jianjie);
    }
    private void initParam(){

    }
    private void initHandler(){
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

}
