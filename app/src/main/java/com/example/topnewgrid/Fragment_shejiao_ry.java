package com.example.topnewgrid;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;

//import android.support.v4.view.ViewPager;

/**
 * Created by 真爱de仙 on 2014/11/13.
 */
public class Fragment_shejiao_ry extends Fragment {
    private ConversationListFragment f1;
    private Fragment_message f2;
    private List<View> viewList;
    private ListView haoyouList;
    private View view;
    private ViewPager viewPager; // 对应的viewPager
    private ArrayList<Fragment> fragmentList;
    private ArrayList<String>titleList;

    private AppApplication app;
    private User user;
    private Handler handler;
    private Gson gson = new Gson();
    private FragmentTransaction ft;

    private RadioButton huihuaBt,haoyouBt;

    // 用来实现 UI 线程的更新。
    Handler mHandler;




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("ExampleFragment--onCreate");
    }
    private void initHandler(){

    }
    private void initListener(){

        view.findViewById(R.id.textView293).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 RongIM.getInstance().startFriendSelect(getActivity());
            }
        });
        huihuaBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.framelayout, f1);
                ft.commit();
            }
        });
        haoyouBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.framelayout, f2);
                ft.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        System.out.println("ExampleFragment--onCreateView");

            mHandler = new Handler();

        if(view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        view=inflater.inflate(R.layout.fragment_shejiao_ry, container, false);
        initUser();
        initLayout();
        initHandler();
        initUser();
        initLayout();
        initListener();
        ft = this.getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.framelayout, f1);
        ft.commit();
        return  view;
    }
    /*
    public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            Toast.makeText(getActivity(), "JobPagerAdapter getItem", Toast.LENGTH_SHORT).show();
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
    */
    private void initUser() {
        app = (AppApplication) getActivity().getApplication();
        user = app.getUser();
    }
    public void initLayout(){
        ((SegmentedGroup)view.findViewById(R.id.segmented2)).setTintColor(getResources().getColor(R.color.radiobutton),getResources().getColor(R.color.activitybarblue));
        f1=new ConversationListFragment();
        f2=new Fragment_message();
        huihuaBt=(RadioButton)view.findViewById(R.id.button21);
        haoyouBt=(RadioButton)view.findViewById(R.id.button22);
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
