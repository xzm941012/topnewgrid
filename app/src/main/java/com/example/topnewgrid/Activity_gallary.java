package com.example.topnewgrid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import http.PostUrl;

/**
 * Created by ’Ê∞Ædeœ… on 2015/5/31.
 */
public class Activity_gallary extends Activity {
    List<View> viewList,viewList2;
    List<String>pathList;
    LayoutInflater inflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallary);
        inflater=getLayoutInflater();
        viewList=Activity_huodong_xiangqing.viewList;
        System.out.println("viewList:"+viewList.size());
        pathList=Activity_huodong_xiangqing.pathList;
        System.out.println("pathList:"+pathList.size());
        PagerAdapter pagerAdapter = new PagerAdapter() {

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

                View views=inflater.inflate(R.layout.fragment_tupian_gallary, null);
                ImageView imageView=(ImageView)views.findViewById(R.id.imageView33);

                if(pathList.get(position).contains("mp4")||pathList.get(position).contains("wmv")||pathList.get(position).contains("avi")||pathList.get(position).contains("3gp")) {
                    views.findViewById(R.id.jianjielayout).setVisibility(View.VISIBLE);
                    views.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*
                            String url = "http://121.41.74.72:8080/upload/"+pathList.get(position).split("\\.")[0]+".mp4";
                            //String url = "http://121.41.74.72:8080/upload/"+"3.mp4";
                            Intent intent = new Intent();
                            intent.setClass(Activity_huodong_xiangqing.this, BBVideoPlayer.class);
                            intent.putExtra("url", url);

                            startActivity(intent);
                            */
                            Intent it = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse("http://"+ PostUrl.Media+":8080/upload/"+pathList.get(position).split("\\.")[0]+".mp4");
                            it.setDataAndType(uri, "video/mp4");
                            startActivity(it);
                        }
                    });
                    imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + pathList.get(position).split("\\.")[0]+".jpg", imageView);
                }else {
                    imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + pathList.get(position), imageView);
                }

                container.addView(views);
                return views;
            }
        };
        ((ViewPager)findViewById(R.id.viewpager2)).setAdapter(pagerAdapter);
    }
}
