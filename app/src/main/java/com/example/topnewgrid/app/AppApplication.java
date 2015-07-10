package com.example.topnewgrid.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.baidu.cyberplayer.sdk.BCyberPlayerFactory;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.example.topnewgrid.R;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.db.SQLHelper;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.obj.OtherUser;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.yixia.camera.VCamera;
import com.yixia.camera.util.DeviceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.smssdk.SMSSDK;
import http.Manage;
import http.PostUrl;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;



public class AppApplication extends Application {
    public int titleBuild=0;
    public static int zhuangtai=0;  //0:没状态 1:发布动态 2:加好友
    public OtherUser otherUser;
    private static AppApplication sInstance;
	private static AppApplication mAppApplication;
    public static SharedPreferences mySharedPreferences;//好友
    public static SharedPreferences activitySharedPreferences;//参加的活动
    public static SharedPreferences allActivitySharedPreferences;
    public static SharedPreferences userSharedPreferences;//所有用户信息的缓存
    public static SharedPreferences message_unread_SharedPreferences;//所有用户信息的缓存
    public static SharedPreferences huodong_load_SharedPreferences;//活动缓存加载

    private ChannelManage channelManage;
	private SQLHelper sqlHelper;
    private User user;
    public  static BDLocation location;
    private LocationClient mLocClient;
    private String[] title={"热门","关注","学习","学习","游戏","运动"};
    private String token ;
    public static Context getAppContext() {
        return sInstance;
    }

    public OtherUser getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(OtherUser otherUser) {
        this.otherUser = otherUser;
    }

    @Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

        initCamaraSDK();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.color.brownss).displayer(new SimpleBitmapDisplayer()).showImageOnLoading(R.color.brownss).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        sInstance = this;
        BCyberPlayerFactory.init(this);

        //定位
        SMSSDK.initSDK(this, "61b14fdb3cb5", "07a592ca811795ec0a8e12b9b85bb601");
        SDKInitializer.initialize(this);

        mySharedPreferences=getSharedPreferences("friends",
                Activity.MODE_PRIVATE);
        message_unread_SharedPreferences=getSharedPreferences("unreadmessage",
                Activity.MODE_PRIVATE);
        activitySharedPreferences=getSharedPreferences("activity",
                Activity.MODE_PRIVATE);
        allActivitySharedPreferences=getSharedPreferences("allactivity",Activity.MODE_PRIVATE);
        userSharedPreferences=getSharedPreferences("users",Activity.MODE_PRIVATE);
        huodong_load_SharedPreferences=getSharedPreferences("huodongload",Activity.MODE_PRIVATE);

        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation locations) {
                if (location == null) {
                    location=locations;
                    mLocClient.stop();
                    return;
                }
            }

        });
        setLocationOption();
        mLocClient.start();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mAppApplication = this;
        if(user==null) {
            initUser();
        }
        RongIM.init(this);
        initConnect(user);

        RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider() {
            // App 返回指定的用户信息给 IMKit 界面组件。
            @Override
            public RongIMClient.UserInfo getUserInfo(String userId) {
                // 原则上 App 应该将用户信息和头像在移动设备上进行缓存，每次获取用户信息的时候，就不用再通过网络获取，提高加载速度，提升用户体验。我们后续将提供用户信息缓存功能，方便您开发。
                String user=userSharedPreferences.getString(userId+"","");
                OtherUser otherUser;
                RongIMClient.UserInfo user1=null;
                Gson gson=new Gson();
                if(!user.equals("")) {

                    otherUser=gson.fromJson(user,OtherUser.class);
                    user1 = new RongIMClient.UserInfo(userId, otherUser.getUserName(), "http://"+ PostUrl.Media+":8080/upload/"+otherUser.getId()+".jpg");
                }else{
                    try {
                        otherUser=Manage.FindUserById(userId);
                        user1 = new RongIMClient.UserInfo(userId, otherUser.getUserName(), "http://"+ PostUrl.Media+":8080/upload/"+otherUser.getId()+".jpg");
                        SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
                        editor.putString(userId+"",gson.toJson(otherUser));
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //return new RongIMClient.UserInfo(userId, "编号:"+userId, "http://"+ PostUrl.Url+":8080/upload/"+userId+".jpg"); // 由开发者提供此方法。
                return user1;
            }
        }, false);
        // 设置好友信息提供者。
        RongIM.setGetFriendsProvider(new RongIM.GetFriendsProvider() {
            @Override
            public List<RongIMClient.UserInfo> getFriends() {
                // 返回 App 的好友列表给 IMKit 界面组件，供会话列表页中选择好友时使用。
                List<RongIMClient.UserInfo> list = new ArrayList<RongIMClient.UserInfo>();
                List<OtherUser>friendsList=user.getFriendList();
                for(OtherUser friend:friendsList){
                    RongIMClient.UserInfo user1 = new RongIMClient.UserInfo(friend.getId()+"", friend.getUserName(), "http://"+ PostUrl.Media+":8080/upload/"+friend.getId()+".jpg");
                    list.add(user1);
                }
                return list;
            }
        });
        RongIM.setGetGroupInfoProvider(new RongIM.GetGroupInfoProvider() {
            // App 返回指定的群组信息给 IMKit 界面组件。
            @Override
            public RongIMClient.Group getGroupInfo(String groupId) {
                String activity=allActivitySharedPreferences.getString(groupId+"","");
                Gson gson=new Gson();
                RongIMClient.Group group=null;
                if(!activity.equals("")) {

                    Huodong huodong=gson.fromJson(activity,Huodong.class);
                    group = new RongIMClient.Group(groupId, huodong.getTitle(), "http://" + PostUrl.Media + ":8080/upload/"+"rc_group_default_portrait.png");
                }else{
                    try {
                        Huodong huodong= Manage.viewActivityByID(groupId);
                        group = new RongIMClient.Group(groupId, huodong.getTitle(), "http://" + PostUrl.Media + ":8080/upload/"+"rc_group_default_portrait.png");
                        SharedPreferences.Editor editor = AppApplication.allActivitySharedPreferences.edit();
                        editor.putString(groupId+"",gson.toJson(huodong));
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return group;
            }
        });
    }
    private void initCamaraSDK(){
        // 设置拍摄视频缓存路径
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        //Toast.makeText(getApplicationContext(), "前缀:" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), Toast.LENGTH_LONG).show();
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                VCamera.setVideoCachePath(dcim + "/learnNcode/");
                //VCamera.setVideoCachePath(dcim + "/Video/");
            } else {
                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/learnNcode/");
                //VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Video/");
            }
        } else {
            VCamera.setVideoCachePath(dcim + "/learnNcode/");
            //VCamera.setVideoCachePath(dcim + "/Video/");
        }
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);
    }
    public static void initConnect(final User users){
        // 连接融云服务器。
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(users!=null) {
                    JPushInterface.setAlias(getApp(), users.getUserId() + "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> strings) {
                            switch (i) {
                                case 0:
                                    System.out.println("设置成功");
                                    break;
                            }
                        }
                    });
                    try {
                        RongIM.connect(users.getToken(), new RongIMClient.ConnectCallback() {

                            @Override
                            public void onSuccess(String s) {
                                // 此处处理连接成功。
                                System.out.println("token:"+users.getToken());
                                Log.d("Connect:", "Login successfully.");
                                System.out.println("Login successfully.");
                            }

                            @Override
                            public void onError(ErrorCode errorCode) {
                                // 此处处理连接错误。
                                Log.d("Connect:", "Login failed.");
                                System.out.println("Login failed.");
                                System.out.println("errorCode:" + errorCode);
                                if (errorCode.toString().trim().equals("TOKEN_INCORRECT")) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String token = Manage.getToken(users.getUserId(), users.getName());
                                            token = token.substring(token.indexOf("token") + 8, token.indexOf("\"}}"));
                                            System.out.println("token:" + token);
                                            users.setToken(token);
                                            initConnect(users);
                                        }
                                    }).start();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    JPushInterface.setAlias(getApp(),"", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> strings) {
                            switch (i) {
                                case 0:
                                    System.out.println("设置成功");
                                    break;
                            }
                        }
                    });
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().disconnect(true);
                        //android.os.Process.killProcess(Process.myPid());
                    }
                }
            }
        }).start();

        /*
        List<RongIMClient.Group> groups = new ArrayList<RongIMClient.Group>();
        RongIMClient.Group group= new RongIMClient.Group("44", "编号44", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");
        RongIMClient.Group group1= new RongIMClient.Group("43", "编号43", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");
        RongIMClient.Group group2= new RongIMClient.Group("42", "编号42", "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png");
        groups.add(group);
        groups.add(group1);
        groups.add(group2);

         RongIM.getInstance().syncGroup(groups, new RongIM.OperationCallback() {

          @Override
             public void onSuccess() {
                Log.e("syncGroup", "=============syncGroup====onSuccess===========");
                            }

                            @Override
             public void onError(ErrorCode errorCode) {
                  Log.e("syncGroup", "=============syncGroup====onError===========" + errorCode);
             }
           });
*/

    }
    public User getUser() {
        return user;
    }
    private void initUser(){
        /*
        SharedPreferences.Editor editor = AppApplication.userSharedPreferences.edit();
        editor.putString("user","");
        editor.commit();
        */
        String user1=userSharedPreferences.getString("user","");
        //sqlHelper.close();
        if(user1.equals("")){
            System.out.println("没有user");

        }else {
            Gson gson=new Gson();
            user=gson.fromJson(user1,User.class);

            token=user.getToken();
            if (user.getHuodongTitle().size() == 0 || user.getHuodongTitle() == null) {
                user.setHuodongTitle(null);
            }
            String friends =mySharedPreferences.getString(user.getUserId()+"","");
            String publicActivity=activitySharedPreferences.getString(user.getUserId()+"","");
            if(!friends.equals("")){
                System.out.println("读取到了好友列表");

                List<OtherUser>friendList=gson.fromJson(friends,new TypeToken<List<OtherUser>>(){}.getType());
                System.out.println(friendList);
                user.setFriendList(friendList);
            }else{
                System.out.println("没有读取到好友列表");
                List<OtherUser>friendList=new ArrayList<>();
                user.setFriendList(friendList);
                System.out.println(friendList);
            }
            if(!publicActivity.equals("")){
                System.out.println("读取到了活动列表");

                List<String>publicActivityList=gson.fromJson(publicActivity,new TypeToken<List<String>>(){}.getType());
                user.setPublicActivity(publicActivityList);
            }else{
                List<String>publicActivityList=new ArrayList<>();
                user.setPublicActivity(publicActivityList);
            }
            /*
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap= Httppost.readImg(user.getUserId());
                    user.setTouxiang(bitmap);
                }
            }).start();
            */
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

    public void setUser(User user) {
        this.user = user;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

	/** 获取Application */
	public static AppApplication getApp() {
		return mAppApplication;
	}
	
	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mAppApplication);
		return sqlHelper;
	}
	
	/** 摧毁应用进程时候调用 */
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
	}
    public void clearUser(){
        user=null;
    }
	public void clearAppCache() {
	}
}
