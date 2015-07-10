package demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.topnewgrid.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class LocationMain extends Activity implements OnGetGeoCoderResultListener 
,OnGetPoiSearchResultListener, OnGetSuggestionResultListener,OnGetShareUrlResultListener, 
BaiduMap.OnMarkerClickListener  {
	GeoCoder mSearch = null;
EditText cityEditText;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	private InfoWindow mInfoWindow;
	BitmapDescriptor mCurrentMarker;
	MyLocation location=new MyLocation();
	String poi="";
	String url="";
	Boolean isPoi=false;
	public BDLocation location1;
	MapView mMapView;
	BaiduMap mBaiduMap;
	Button savescreenbtButton,readButton;
	Button ActivityButton;
	private LatLng currentPt;
	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位

	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;

	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;
	private ShareUrlSearch mShareUrlSearch = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location);
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch.setOnGetShareUrlResultListener(this);
		
		cityEditText=(EditText)findViewById(R.id.city1);
		savescreenbtButton=(Button)findViewById(R.id.savescreen1);
		requestLocButton = (Button) findViewById(R.id.button1);
		readButton=(Button) findViewById(R.id.read);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("普通");
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);
		
	readButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {				
				//try {
					Intent intent=new Intent();
					intent.setClass(LocationMain.this, ReadLocation.class);
					intent.putExtra("location", location);
					startActivity(intent);
				/*} catch (Exception e) {
				
					e.printStackTrace();
					Log.e("Exception", e.toString());
				}*/
				//finish();
			}
		});

		savescreenbtButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				// 截图，在SnapshotReadyCallback中保存图片到 sd 卡
				mBaiduMap.snapshot(new SnapshotReadyCallback() {
					public void onSnapshotReady(Bitmap snapshot) {
						File file = new File("/mnt/sdcard/activityLocation.png");
						FileOutputStream out;
						try {
							out = new FileOutputStream(file);
							if (snapshot.compress(
									Bitmap.CompressFormat.PNG, 100, out)) {
								out.flush();
								out.close();
							}
							Toast.makeText(getApplicationContext(),
									"屏幕截图成功，图片存在: " + file.toString(),
									Toast.LENGTH_SHORT).show();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				Toast.makeText(getApplicationContext(), "正在截取屏幕图片...",
						Toast.LENGTH_SHORT).show();

			
				
			}
		});
		

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate u=MapStatusUpdateFactory.zoomTo(15);
		mBaiduMap.animateMapStatus(u);
	
		mBaiduMap.setMapStatus(u);
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
			//	touchType = "单击";
				currentPt = point;
				//Toast.makeText(getApplicationContext(), "单击"+currentPt.latitude+","+currentPt.longitude+currentPt, Toast.LENGTH_SHORT).show();
				LatLng ptCenter = new LatLng(currentPt.latitude, currentPt.longitude);
				// 反Geo搜索
				/*mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));*/
				// Marker marker=null;marker.setTitle("dfg");
				/*mShareUrlSearch.requestLocationShareUrl(new LocationShareURLOption().location(currentPt).snippet("测试分享点")
						.name(marker.getTitle()));*/
			
			
			
			}
			
			 public boolean onMapPoiClick(MapPoi arg0) {
				/* PoiInfo info = getPoiResult().getAllPoi().get(i);
					currentAddr = info.address;
					mShareUrlSearch
							.requestPoiDetailShareUrl(new PoiDetailShareURLOption()
									.poiUid(info.uid));*/
				 
				 poi=arg0.getName();
				 isPoi=true;
				 currentPt = arg0.getPosition();
					//Toast.makeText(getApplicationContext(), "单击"+currentPt.latitude+","+currentPt.longitude+currentPt, Toast.LENGTH_SHORT).show();
					LatLng ptCenter = new LatLng(currentPt.latitude, currentPt.longitude);
					// 反Geo搜索
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(ptCenter));
				   //名称
					/*Toast.makeText(getApplicationContext(),  arg0.getName()+":"+adress, Toast.LENGTH_LONG)
					.show();*/
					
				
				    return false;
				   } 
		});
		mBaiduMap.setOnMarkerClickListener(this);
		/*mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {			
			@Override
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				OnInfoWindowClickListener listener = null;			
					button.setText("设为活动地");
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							LatLng ll = marker.getPosition();
							LatLng llNew = new LatLng(ll.latitude + 0.005,
									ll.longitude + 0.005);
							marker.setPosition(llNew);
							mBaiduMap.hideInfoWindow();
						}
					};
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -40, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				return false;
			}
		});*/
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setIsNeedAddress(true);
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		

		
		//setContentView(R.layout.activity_poisearch);
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey1);
		sugAdapter = new ArrayAdapter<String>(this,
				R.layout.activity_autostyle);
		keyWorldsView.setAdapter(sugAdapter);
		

		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				String city = ((EditText) findViewById(R.id.city1)).getText()
						.toString();
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});

	
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener{	

				@Override
		public void onReceiveLocation(BDLocation loc) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;//location.
			
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(loc.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(loc.getLatitude())
					.longitude(loc.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			
			/*MapStatusUpdate u=MapStatusUpdateFactory.zoomTo(15);
			//mBaiduMap.animateMapStatus(u);
		
			mBaiduMap.setMapStatus(u);*/
			if (isFirstLoc) {
				//location.BdLocation=loc;
				isFirstLoc = false;
				cityEditText.setText(loc.getCity());
				
				LatLng ll = new LatLng(loc.getLatitude(),
						loc.getLongitude());
				MapStatusUpdate
				 u = MapStatusUpdateFactory.newLatLng(ll);
				
				mBaiduMap.animateMapStatus(u);
				/*u=MapStatusUpdateFactory.zoomTo(15);
				mBaiduMap.setMapStatus(u);*/
				Log.e("map", mBaiduMap.getMapStatus()+"");
				Log.e("map", mBaiduMap.getMapType()+"");
				
			}
		}

		
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
		mSearch.destroy();
		mShareUrlSearch.destroy();
		mMapView = null;
		
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		String strInfo = String.format("纬度：%f 经度：%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {	//点击
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		mBaiduMap.setOnMarkerClickListener(this);
		//mBaiduMap。setOnMarkerClickListener(new on);
		String PoiAddress=poi;
	
		if(PoiAddress.contains("\\")) PoiAddress=PoiAddress.replace("\\", "");
		
		location.Poi=PoiAddress;
		location.Adress=result.getAddress();
		location.Poi_Adress=PoiAddress+":"+result.getAddress();
	
		if(isPoi) {
			
			Toast.makeText(this, location.Poi_Adress+url,Toast.LENGTH_LONG).show();
			keyWorldsView.setText(location.Poi_Adress);
		}
		else 
		{
			Toast.makeText(this, location.Adress+url,
				Toast.LENGTH_LONG).show();
		keyWorldsView.setText(location.Adress);
		}
		keyWorldsView.setTextColor(Color.DKGRAY);
		isPoi=false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 影响搜索按钮点击事件
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		//EditText editCity = (EditText) findViewById(R.id.city1);
		//EditText editSearchKey = (EditText) findViewById(R.id.searchkey1);
		String keyString=keyWorldsView.getText().toString();
		if(keyString.contains(":")) keyString = keyString.substring(0,keyString.indexOf(":"));
		Log.e("keyString", keyString);
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(cityEditText.getText().toString())
				.keyword(keyString)
				.pageNum(load_Index));
	}

/*	public void goToNextPage(View v) {
		load_Index++;
		searchButtonProcess(null);
	}*/

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void onGetPoiDetailResult(final PoiDetailResult result) {	//poi
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			String PoiAddress=result.getName();
			
			if(PoiAddress.contains("\\")) PoiAddress=PoiAddress.replace("\\", "");
			
			location.Poi=PoiAddress;
			location.Adress=result.getAddress();
			location.Poi_Adress=PoiAddress+":"+result.getAddress();
			keyWorldsView.setText(location.Poi_Adress);
			//location.latLng=result.getLocation();
			
			Button button = new Button(getApplicationContext());
			button.setBackgroundResource(R.drawable.popup);
			OnInfoWindowClickListener listener = null;			
				button.setText("设为活动地");
				button.setTextColor(Color.BLACK);
				listener = new OnInfoWindowClickListener() {
					public void onInfoWindowClick() {
						
							LatLng ll = result.getLocation();
							location.point=new Point(ll.latitude,ll.longitude);						
					Toast toast=Toast.makeText(getApplicationContext(), "活动地点设置成功！", Toast.LENGTH_SHORT);
						   toast.setGravity(Gravity.CENTER, 0, 0);
						   toast.show();
						//finish();
						/**/
						mBaiduMap.hideInfoWindow();
                        Intent aintent = new Intent();
                        aintent.putExtra("location",location);
                        setResult(12,aintent);

                        finish();
					}
				};
				LatLng ll = result.getLocation();
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -40, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				
			Toast.makeText(this, location.Poi_Adress, Toast.LENGTH_SHORT)
			.show();
		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult result) {
		// 分享短串结果
		url=result.getUrl();
		Toast.makeText(this, result.getUrl(), Toast.LENGTH_SHORT)
		.show();
	
		
	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {
		
		Toast.makeText(this, result.getUrl()+",poi", Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	
	public boolean onMarkerClick(final Marker marker) {
		 ActivityButton = new Button(getApplicationContext());
		ActivityButton.setBackgroundResource(R.drawable.popup);
		OnInfoWindowClickListener listener = null;			
			ActivityButton.setText("设为活动地");
			ActivityButton.setTextColor(Color.BLACK);
			listener = new OnInfoWindowClickListener() {
				public void onInfoWindowClick() {
					LatLng ll = marker.getPosition();
					location.point=new Point(ll.latitude,ll.longitude);	
					//Log.e("latlng", location.latLng.latitude+" "+location.latLng.longitude);
					//ActivityButton.setText("设置成功!");
					//ActivityButton.setBackgroundColor(Color.RED);
					Toast toast=Toast.makeText(getApplicationContext(), "活动地点设置成功！", Toast.LENGTH_SHORT);
					   toast.setGravity(Gravity.CENTER, 0, 0);
					   toast.show();
					mBaiduMap.hideInfoWindow();
                    Intent aintent = new Intent();
                    aintent.putExtra("location",location);
                    setResult(12,aintent);
                    finish();
					//finish();
				}
			};
			LatLng ll = marker.getPosition();
			mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(ActivityButton), ll, -40, listener);
			mBaiduMap.showInfoWindow(mInfoWindow);
		return false;
	}




}

/*class MyLocation implements Serializable{
	 // private static final long serialVersionUID = -7060210544600464481L; 
	Point point;//坐标，(latLng.longitude,latLng.latitude)
	String Poi;//在地理信息系统中,一个POI可以是一栋房子、一个商铺、一个邮筒、一个公交站等
	String Adress;//地址
	String Poi_Adress;//Poi+Adress
	//BDLocation BdLocation;	//保存了首次定位时的地理信息
}

class Point implements Serializable{
	public double x;
	public double y;
public Point(){
		
	}
	public Point(double a,double b){
		x=a;
		y=b;
	}		
}
*/