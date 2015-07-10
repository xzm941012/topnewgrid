package com.example.topnewgrid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ’Ê∞Ædeœ… on 2015/6/1.
 */
public class Activity_fenlei extends FragmentActivity {
    List<String> titleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fenlei);
        titleList=new ArrayList<>();
        String titles= getIntent().getStringExtra("title");
        System.out.println("titles:"+titles);
        titleList.add(titles);
        Fragment_fenlei fm3=new Fragment_fenlei();
        FragmentTransaction ts1=getSupportFragmentManager().beginTransaction();
        ts1.replace(R.id.frameLayout, fm3) .commitAllowingStateLoss();

    }

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }
}
