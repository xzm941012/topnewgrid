package com.example.topnewgrid;

/**
 * Created by 真爱de仙 on 2015/5/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.util.LevalUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import http.PostUrl;


public class Activity_ReadPerson extends Activity   {
    private List<LatLng> list=new ArrayList<LatLng>();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc=true;
    private LocationClient mLocClient;
    private InfoWindow mInfoWindow;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions defaultOptions;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read_person);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        list=Activity_fujin.list;
        System.out.println("list size:"+list.size());

        initMap();
        initLoc();
        initClickPic();

    }

    protected void initMap() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.showZoomControls(false);//隐藏缩放控件
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(14);
        mBaiduMap.animateMapStatus(u);
        mBaiduMap.setMapStatus(u);
        mBaiduMap.setMyLocationEnabled(true);
    }




    // 设置相关参数
    private void initLoc() {
        mLocClient = new LocationClient(this.getApplicationContext());
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if(location==null) return;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(90).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    MapStatusUpdate	u = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(u);
                    isFirstLoc=false;
                }
                Log.i("1", ll.latitude+" "+ll.longitude);
                //添加头像
                for(int i=0;i<list.size();i++)
                    addPic(list.get(i), R.drawable.taxi1, i+"");//最后1位为传入的司机id
            }

            public void onReceivePoi(BDLocation location) {
                if(location==null) return;


            }
        });


        setLocationOption();
        mLocClient.start();

    }
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //	option.setScanSpan(5000); // 设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }

    //添加头像
    public void addPic(LatLng ll,int pic,String id){
        mBaiduMap.addOverlay(new MarkerOptions().position(ll)
                .icon(BitmapDescriptorFactory
                        .fromResource(pic)).title(id));
    }

    // 点击头像事件
    private void initClickPic() {
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                String id=marker.getTitle();//点击头像的id
                final OtherUser item = Activity_fujin.friendList.get(Integer.parseInt(id));
               // T.showShort(Activity_ReadPerson.this, id);
                //Log.i("idii", id);

			/*   点击头像后在头像上显示按钮,listener为按钮点击事件*/
			 	/*Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);		*/
                LayoutInflater inflater=getLayoutInflater();
                View view=inflater.inflate(R.layout.label_fujin, null);
                TextView name = null;
                TextView dongtai=null;
                TextView locate;
                CircleImageView touxiang;
                name=(TextView)view.findViewById(R.id.textView107);
                touxiang=(CircleImageView)view.findViewById(R.id.profile_image);
                locate=(TextView)view.findViewById(R.id.textView126);
                name.setText(item.getUserName());
                locate.setText("");
                int level=item.getLevel()/10;
                ((TextView)view.findViewById(R.id.textView291)).setText("LV"+ LevalUtil.getLevel(item.getLevel()));

                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getId() + ".jpg" ,touxiang,defaultOptions);
                LatLng ll = marker.getPosition();

                OnInfoWindowClickListener	listener = new OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        Intent intent=new Intent();
                        intent.setClass(Activity_ReadPerson.this,Activity_bieren.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("id",item.getId()+"");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                };

                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -47, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);//显示按钮
                //mBaiduMap.hideInfoWindow();//隐藏按钮
                return true;
            }
        });

    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
