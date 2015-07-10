package com.example.topnewgrid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.topnewgrid.adapter.EditSeeAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.choosephotos.choosephotos.adapter.AddImageGridAdapter;
import com.example.topnewgrid.choosephotos.choosephotos.controller.SelectPicPopupWindow;
import com.example.topnewgrid.choosephotos.choosephotos.photo.Item;
import com.example.topnewgrid.choosephotos.choosephotos.photo.PhotoAlbumActivity;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewerinterface.ViewPagerDeleteActivity;
import com.example.topnewgrid.choosephotos.choosephotos.util.PictureManageUtil;
import com.example.topnewgrid.choosephotos.util.Bimp;
import com.example.topnewgrid.choosephotos.util.DialogUtil;
import com.example.topnewgrid.choosephotos.util.PictureNarrowUtils;
import com.example.topnewgrid.choosephotos.util.UploadFile;
import com.example.topnewgrid.obj.Label_edit;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.service.UploadService;
import com.example.topnewgrid.util.AndroidClass;
import com.example.topnewgrid.util.GraphicsBitmapUtils;
import com.example.topnewgrid.view.ListViewForScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import demo.LocationMain;
import demo.MyLocation;
import http.PostUrl;
import mediachooser.MediaChooser;
import mediachooser.activity.HomeFragmentActivity;


public class Activity_fabu_huodong_step3 extends Activity implements Handler.Callback ,OnClickListener{

    public String titleup;
    public String fenleiTextup;
    public String jineup;
    public String textView169;

    private Popupwindows_fenlei popupwindows_fenlei;
    Popupwindows_xuanshang popupwindows_xuanshang;
    public List<String> videoArray=new ArrayList<>();
    public List<String> imageArray=new ArrayList<>();
    List<String> imageArray2=new ArrayList<>();
    List<Label_edit> edits=new ArrayList<>();
    public EditSeeAdapter adapter;
    ListViewForScrollView editListview;
    public static Activity_fabu_huodong_step3 activity_fabu_huodong_step3;

	//String uploadUrl = "http://"+ PostUrl.Url+":8080/Server/android/uploadImage.jsp";
	/* 用来标识请求照相功能的activity */
    Thread thread;
    String title1,content1;
    String fenmianPath;
	private final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	// GridView预览删除页面过来
	private final int PIC_VIEW_CODE = 2016;
    private final int BAIDUMAP=12;
	/* 拍照的照片存储位置 */
	private final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.photo.choosephotos");
	private File mCurrentPhotoFile;// 照相机拍照得到的图片
	// 用来显示预览图
	private ArrayList<Bitmap> microBmList = new ArrayList<Bitmap>();
	// 所选图的信息(主要是路径)
	private ArrayList<Item> photoList = new ArrayList<Item>();
	private AddImageGridAdapter imgAdapter;
	private Bitmap addNewPic;
	private GridView gridView;// 显示所有上传图片
	private SelectPicPopupWindow menuWindow;
	private Button b1;
	private EditText t1;
	private EditText t2;
	private EditText t3;
	final Handler upLoadhand = new Handler(this);
    private RelativeLayout renshuLayout,baomingLayout,moreOption;
    private EditText renshuNum;
    private Calendar startCalendar,endCalendar;
    private Integer year,month,day;
    private TextView startText,timeText;
    private TextView fabuButton;//发布活动的按钮
    private Spinner fenlei;
    private  View moreView;//隐藏的view
    private Boolean ifOpen=false;
    private User user;
    private AppApplication app;
    public String district;      //城市
    public Double longitude,latitude;   //经度,纬度
    private String []huodongtitle;
    private String[]fenleis;
    private MyLocation location=new MyLocation();
    public String locationAdress="";
    public String locationPoi_Adress="";
    public String locationPoi="";
    public String locationx="";
    public String locationy="";
    public String editText4;

    private EditText biaotiTxt,jianjieTxt,jineTxt,renshuTxt,lianxiStyle;
    private TextView fenleiTxt,locate,timeTxt,shiduanTxt,xiangxiLocate,clearTxt;
    private CheckBox needtupian;
    private int isOpen=0;
    private String xiangxiLocate2;

    LocationClient mLocClient;//定位
    private ProgressDialog dialog;


    // 用来实现 UI 线程的更新。
    Handler mHandler,handler2,handler3,handler4;


    private EditText biaoti,neirong,renshu;
    private TextView endTime;
    private CheckBox ifTupian;
    BroadcastReceiver videoBroadcastReceiver;
    BroadcastReceiver imageBroadcastReceiver;

    String[] arrayString = { "选择图片", "选择视频" };
    String title = "选择封面";

    // 上传的地址
    String uploadUrl ="http://"+ PostUrl.Media+":8080/Server/android/uploadImage.jsp?";
    String filename;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private byte[] photodata=null;
    NotificationManager mNotificationManager;
    public int notifyId;
    NotificationCompat.Builder mBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fabu_huodong_step2);
        activity_fabu_huodong_step3=this;
        title1=getIntent().getStringExtra("title");
        content1=getIntent().getStringExtra("content");
        fenmianPath=Environment.getExternalStorageDirectory() + "/formats/"+"fenmian"+System.currentTimeMillis()+".jpg";
        mLocClient = new LocationClient(Activity_fabu_huodong_step3.this.getApplicationContext());

        mLocClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) return;
                longitude=location.getLongitude();
                latitude=location.getLatitude();
                district=location.getAddrStr();
                xiangxiLocate2=location.getAddrStr();
                if(longitude!=null&&latitude!=null&&district!=null){
                    mLocClient.stop();
                }
            }

        });
        setLocationOption();
        mLocClient.start();
        if (!(PHOTO_DIR.exists() && PHOTO_DIR.isDirectory())) {
            PHOTO_DIR.mkdirs();
        }
        // 添加图片
        gridView = (GridView) findViewById(R.id.allPic);

        // 加号图片
        addNewPic = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_pic);
        // addNewPic = PictureManageUtil.resizeBitmap(addNewPic, 180, 180);
        microBmList.add(addNewPic);
        imgAdapter = new AddImageGridAdapter(this, microBmList);
        gridView.setAdapter(imgAdapter);
        // 事件监听，点击GridView里的图片时，在ImageView里显示出来
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == (photoList.size())) {
                    menuWindow = new SelectPicPopupWindow(Activity_fabu_huodong_step3.this, itemsOnClick);
                    menuWindow.showAtLocation(Activity_fabu_huodong_step3.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                } else {
                    Intent intent = new Intent(Activity_fabu_huodong_step3.this, ViewPagerDeleteActivity.class);
                    intent.putParcelableArrayListExtra("files", photoList);
                    intent.putExtra(ViewPagerActivity.CURRENT_INDEX, position);
                    startActivityForResult(intent, PIC_VIEW_CODE);
                }
            }
        });
        initHandler();
        initUser();
        initLayout();
        initListener();
        MediaChooser.setSelectionLimit(1);
        MediaChooser.setVideoSize(100);
        videoBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(Activity_fabu_huodong_step3.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                videoArray=(intent.getStringArrayListExtra("list"));
                if(videoArray.size()!=0){
                    System.out.println("imageArray3:"+imageArray.size());
                    imageArray.clear();
                    imageArray2.clear();
                    System.out.println("imageArray4:"+imageArray.size());
                    File mediaFile = new File(videoArray.get(0));
                    Bitmap bitmap = PictureManageUtil.getVideoThumbnail(mediaFile.getAbsolutePath(), 500, 500, Thumbnails.MICRO_KIND);
                    ((ImageView)findViewById(R.id.background_img)).setImageBitmap(bitmap);
                    findViewById(R.id.textView225).setVisibility(View.GONE);
                }
            }
        };
        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(Activity_fabu_huodong_step3.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                imageArray=(intent.getStringArrayListExtra("list"));
                if(imageArray.size()!=0) {
                    videoArray.clear();
                    imageArray2.add(imageArray.get(0));
                    System.out.println("imageArray1:"+imageArray.size());
                    startPhotoZoom(Uri.fromFile(new File(imageArray.get(0))), 150);
                }
            }
        };
        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
	}
    private void initHandler(){
        handler2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.setMessage("正在上传第"+(msg.what+1)+"张图片");
            }
        };
        handler3=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        ((ImageView)findViewById(R.id.background_img)).setImageBitmap((Bitmap) msg.obj);
                        findViewById(R.id.textView225).setVisibility(View.GONE);
                        //((TextView)findViewById(R.id.textView151)).setText("");
                        break;
                    case 3:
                        Map<String,String>map1=(HashMap)msg.obj;
                        String videoNum=map1.get("videoNum");
                        mBuilder.setProgress(100, Integer.parseInt(videoNum), false);
                        mNotificationManager.notify(notifyId, mBuilder.build());
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                    default:
                        break;

                }
            }
        };
    }

    // 创建一个以当前时间为名称的文件
    public File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    // 对话框
    DialogInterface.OnClickListener onDialogClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    //startCamearPicCut(dialog);// 开启照相
                    MediaChooser.setSelectedMediaCount(0);
                    MediaChooser.showOnlyImageTab();
                    Intent intent = new Intent(Activity_fabu_huodong_step3.this, HomeFragmentActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    //startImageCaptrue(dialog);// 开启图库
                    MediaChooser.setSelectedMediaCount(0);
                    MediaChooser.showOnlyVideoTab();
                    Intent intent2 = new Intent(Activity_fabu_huodong_step3.this, HomeFragmentActivity.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }

        private void startCamearPicCut(DialogInterface dialog) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            // 调用系统的拍照功能
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra("camerasensortype", 2);// 调用前置摄像头
            intent.putExtra("autofocus", true);// 自动对焦
            intent.putExtra("fullScreen", false);// 全屏
            intent.putExtra("showActionIcons", false);
            // 指定调用相机拍照后照片的储存路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
        }

        private void startImageCaptrue(DialogInterface dialog) {
            // TODO Auto-generated method stub
            dialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }
    };




    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }



    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        if(picdata==null){
            return;
        }
        System.out.println("imageArray4:"+imageArray.size());
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            System.out.println("imageArray5:"+imageArray.size());
            //Bitmap photo = bundle.getParcelable("data");
            Bitmap photo=null;
            photo = Bimp.getimage(tempFile.getAbsolutePath());
            String newStr = tempFile.getAbsolutePath().substring(tempFile.getAbsolutePath().lastIndexOf("/") + 1, tempFile.getAbsolutePath().lastIndexOf("."));
            PictureNarrowUtils.saveBitmap2(photo, "" + newStr);
            photodata = GraphicsBitmapUtils.Bitmap2Bytes(photo);
            System.out.println("imageArray6:"+imageArray.size());
            Boolean isUploadSuccess;
            handler3.obtainMessage(1, photo).sendToTarget();
        }
    }

    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);				//打开gps
        option.setCoorType("bd09ll");		//设置坐标类型
        option.setIsNeedAddress(true);//设置地址信息，默认无地址信息
        option.setAddrType("all");

        option.setScanSpan(1000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
        mLocClient.setLocOption(option);
    }
    private void initUser(){
        app = (AppApplication) getApplication();
        user=app.getUser();
        List<String>huodonglist2=new ArrayList<String>();
        if(user.getHuodongTitle()==null||user.getHuodongTitle().equals("")){
            String[] a1= getResources().getStringArray(R.array.huodongTitlenotnull);
            for(String a:a1){
                if(!a.equals("关注")&&!a.equals("推荐")){
                    huodonglist2.add(a);
                }
            }
        }else{
            List<String>huodonglist=user.getHuodongTitle();
            for(String a:huodonglist){
                if(!a.equals("关注")&&!a.equals("推荐")){
                    huodonglist2.add(a);
                }
            }
        }
        fenleis=new String[huodonglist2.size()];
        for(int i=0;i<fenleis.length;i++){
            fenleis[i]=(huodonglist2.get(i));
        }
    }
    private void upLoad() {
        imageArray=imageArray2;
        final String title=biaotiTxt.getText().toString().trim();
        titleup=title;
        final String fenleiText=fenleiTxt.getText().toString();
        fenleiTextup=fenleiText;
        //final String neirongText=jianjieTxt.getText().toString().trim();
        System.out.println("imageArray2"+imageArray.size()+"  "+videoArray.size());
        if (title.equals("")) {
            upLoadhand.sendEmptyMessage(0x3);
            return;
        }

        else if(imageArray.size()==0&&videoArray.size()==0){
            System.out.println("imageArray2"+imageArray.size()+"  "+videoArray.size());
            upLoadhand.sendEmptyMessage(0x10);
            return;
        }

        else if (adapter.mItems.size()==0) {
            upLoadhand.sendEmptyMessage(0x4);
            return ;
        }else if (fenleiText.equals("")) {
            upLoadhand.sendEmptyMessage(0x8);
            return ;
        }
        if(((TextView)findViewById(R.id.textView169)).getText().toString().trim().equals("金额")) {
            if (((EditText) findViewById(R.id.editText4)).getText().toString().trim().equals("")) {
                upLoadhand.sendEmptyMessage(0x11);
                return;
            }
        }
        notifyId=app.titleBuild++;
        editText4=((EditText) findViewById(R.id.editText4)).getText().toString().trim();
        textView169=((TextView)findViewById(R.id.textView169)).getText().toString().trim();
        jineup=((TextView)findViewById(R.id.textView169)).getText().toString().trim();
		startService(new Intent(Activity_fabu_huodong_step3.this, UploadService.class));
        finish();
	}
    private void initLayout(){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("提交数据中...");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);

        mHandler = new Handler(this);

        ((ScrollView) findViewById(R.id.scrollview)).smoothScrollTo(0, 0);

        editListview=(ListViewForScrollView)findViewById(R.id.listView2);
        adapter=new EditSeeAdapter(Activity_fabu_huodong_step3.this,edits);
        editListview.setAdapter(adapter);

        xiangxiLocate=(TextView)findViewById(R.id.textView156);
        biaotiTxt=(EditText)findViewById(R.id.editText39);
        fenleiTxt=(TextView)findViewById(R.id.textView149);
        //jianjieTxt=(EditText)findViewById(R.id.editText40);
        lianxiStyle=(EditText)findViewById(R.id.editText21);
        // startText=(TextView)findViewById(R.id.startText);
        startCalendar= Calendar.getInstance(Locale.CHINA);
        Date mydate=new Date(); //获取当前日期Date对象
        startCalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        year=startCalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month=startCalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day=startCalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        timeText=(TextView)findViewById(R.id.textView167);
        fabuButton=(TextView)findViewById(R.id.textView143);

    }
    private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener()
    {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear,int dayOfMonth) {
            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year=myyear;
            month=monthOfYear;
            day=dayOfMonth;
            //更新日期
            updateDate();

        }
        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate()
        {
            //在TextView上显示日期
            timeText.setText(year+"-"+(month+1)+"-"+day);
        }
    };
    private void initListener(){
        findViewById(R.id.editLayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(imageBroadcastReceiver);
                unregisterReceiver(videoBroadcastReceiver);
                startActivityForResult(new Intent(Activity_fabu_huodong_step3.this,Activity_edit.class),166);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // Cancel task.
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(thread!=null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_fabu_huodong_step3.this);
                        builder.setMessage("确认停止上传")
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        if(thread.isAlive()){
                                            thread.interrupt();
                                        }
                                        finish();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                        builder.show();
                    }
                    finish();
                }
                return false;
            }
        });
        findViewById(R.id.xuanshanglayout).setOnClickListener(this);
        findViewById(R.id.imageView217).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.headlayout).setOnClickListener(this);
        findViewById(R.id.fenleilayout).setOnClickListener(this);
        findViewById(R.id.layout11).setOnClickListener(this);
        findViewById(R.id.relativeLayout47).setOnClickListener(this);
        findViewById(R.id.relativeLayout46).setOnClickListener(this);
        findViewById(R.id.relativeLayout69).setOnClickListener(this);

        fabuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                //dialog.show();
                UploadFile uploadFile = new UploadFile(uploadUrl);
                upLoad();

            }
        });
    }
	@Override
	public boolean handleMessage(Message msg) {
		if (msg.what == 0x1) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "图片上传成功！！", true);
           // dialog.dismiss();
        }
		else if (msg.what == 0x2) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "图片上传失败，请检查网络设置", true);
            //dialog.dismiss();
        }
		else if (msg.what == 0x3) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "请完善活动标题", true);
            //dialog.dismiss();
        }
		else if (msg.what == 0x4) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "请完善详情", true);
           // dialog.dismiss();
        }
		else if (msg.what == 0x5) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "活动信息上传成功", true);
           // dialog.dismiss();
        }
		else if (msg.what == 0x6) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "活动信息上传失败，请检查网络", true);
          //  dialog.dismiss();
        }
		else if (msg.what == 0x7) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "请写上活动类型，方便其它用户查找", true);
         //   dialog.dismiss();
        }
		else if (msg.what == 0x7) {
            DialogUtil.showDialog(Activity_fabu_huodong_step3.this, "图片已压缩好", true);
          //  dialog.dismiss();
        }
        else if (msg.what == 0x8) {
            Toast.makeText(Activity_fabu_huodong_step3.this,"请完善分类",Toast.LENGTH_SHORT).show();
            //DialogUtil.showDialog(MainActivity.this, "请选择活动标签", true);
        }else if(msg.what==0x9){
           /// dialog.dismiss();
        }else if(msg.what==0x10){
            Toast.makeText(Activity_fabu_huodong_step3.this,"请选择封面",Toast.LENGTH_SHORT).show();
        //    dialog.dismiss();
        }else if(msg.what==0x11){
            Toast.makeText(Activity_fabu_huodong_step3.this,"请输入悬赏金额",Toast.LENGTH_SHORT).show();
          //  dialog.dismiss();
        }


		return false;
	}

	private String getPictureName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo: {

				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					// 判断是否有SD卡
					doTakePhoto();// 用户点击了从照相机获取
				} else {
					Toast.makeText(Activity_fabu_huodong_step3.this, "没有SD卡", Toast.LENGTH_LONG).show();
				}

				break;
			}
			case R.id.btn_pick_photo: {
				// 打开选择图片界面

				doPickPhotoFromGallery();

				break;
			}
			default:
				break;
			}
		}
	};
    private OnClickListener itemsOnClick3 = new OnClickListener() {
        public void onClick(View v) {
            popupwindows_fenlei.dismiss();
            switch (v.getId()) {
                case R.id.textView43: {
                    fenleiTxt.setText("IT");
                    break;
                }
                case R.id.textView44: {
                    fenleiTxt.setText("美食");
                    break;

                }
                case R.id.textView45: {
                    fenleiTxt.setText("手工");
                    break;

                }
                case R.id.textView46: {
                    fenleiTxt.setText("运动");
                    break;

                }
                case R.id.textView47: {
                    fenleiTxt.setText("艺术");
                    break;
                }
                case R.id.textView48: {
                    fenleiTxt.setText("舞蹈");
                    break;
                }
                case R.id.textView66: {
                    fenleiTxt.setText("音乐");
                    break;
                }
                case R.id.textView67: {
                    fenleiTxt.setText("生活");
                    break;
                }
                case R.id.textView68: {
                    fenleiTxt.setText("学习");
                    break;
                }
                case R.id.textView71: {
                    fenleiTxt.setText("其他");
                    break;
                }
                default:
                    break;
            }
        }
    };
    private OnClickListener itemsOnClick2 = new OnClickListener() {
        public void onClick(View v) {
            popupwindows_xuanshang.dismiss();
            switch (v.getId()) {
                case R.id.imageView: {
                    ((TextView)findViewById(R.id.textView169)).setText("无");
                    findViewById(R.id.xuanshanglayout).setVisibility(View.GONE);
                    break;
                }
                case R.id.imageView3: {
                    ((TextView)findViewById(R.id.textView169)).setText("金额");
                    findViewById(R.id.xuanshanglayout).setVisibility(View.VISIBLE);
                    break;
                }
                case R.id.imageView2: {
                    ((TextView)findViewById(R.id.textView169)).setText("面议");
                    findViewById(R.id.xuanshanglayout).setVisibility(View.GONE);
                    break;
                }

                default:
                    break;
            }

        }
    };

	/**
	 * 拍照获取图片
	 *
	 */
	protected void doTakePhoto() {
		try {
			// 创建照片的存储目录
			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "找不到相机", Toast.LENGTH_LONG).show();
		}
	}



	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			final ProgressDialog dialog;
			dialog = new ProgressDialog(this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置为圆形
			dialog.setMessage("数据加载中...");
			dialog.setIndeterminate(false);//
			// dialog.setCancelable(true); //按回退键取消
			dialog.show();
			Window window = dialog.getWindow();
			View view = window.getDecorView();
			// Tools.setViewFontSize(view,21);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					// 初始化提示框
					dialog.dismiss();
				}

			}, 1000);
			// final Intent intent = new
			// Intent(MainActivity.this,GetAllImgFolderActivity.class);
			final Intent intent = new Intent(Activity_fabu_huodong_step3.this, PhotoAlbumActivity.class);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "图库中找不到照片", Toast.LENGTH_LONG).show();
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				imgAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 处理其他页面返回数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==999){
            Gson gson=new Gson();
            String result=data.getStringExtra("edit");
            adapter.mItems=gson.fromJson(result, new TypeToken<List<Label_edit>>() {}.getType());
            adapter.notifyDataSetChanged();
            //setListViewHeightBasedOnChildren(editListview);
            return;
        }
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    startPhotoZoom(data.getData(), 150);
                }
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        if(resultCode==12){
            location = (MyLocation)data.getSerializableExtra("location");
            locationPoi=location.Poi;
            locationAdress=location.Adress;
            locationPoi_Adress=location.Poi_Adress;
            locationx=location.point.x+"";
            locationy=location.point.y+"";
            xiangxiLocate.setText(locationAdress);
            /*
            System.out.println("接收到");
            Intent intent=new Intent();
            intent.setClass(MainActivity.this, ReadLocation.class);
            intent.putExtra("location", location);
            startActivity(intent);
            */
            return;
        }
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {
			// 调用Gallery返回的
			ArrayList<Item> tempFiles = new ArrayList<Item>();
			if (data == null)
				return;
			tempFiles = data.getParcelableArrayListExtra("fileNames");
			Log.e("test", "被选中的照片" + tempFiles.toString());

			if (tempFiles == null) {
				return;
			}
			microBmList.remove(addNewPic);
			Bitmap bitmap;
			for (int i = 0; i < tempFiles.size(); i++) {
				bitmap = Thumbnails.getThumbnail(this.getContentResolver(), tempFiles.get(i).getPhotoID(), Thumbnails.MICRO_KIND, null);
				int rotate = PictureManageUtil.getCameraPhotoOrientation(tempFiles.get(i).getPhotoPath());
				bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
				microBmList.add(bitmap);
				photoList.add(tempFiles.get(i));
			}
			microBmList.add(addNewPic);
			imgAdapter.notifyDataSetChanged();
			break;
		}
		case CAMERA_WITH_DATA: {
			Long delayMillis = 0L;
			if (mCurrentPhotoFile == null) {
				delayMillis = 500L;
			}
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
					// 去掉GridView里的加号
					microBmList.remove(addNewPic);
					Item item = new Item();
					item.setPhotoPath(mCurrentPhotoFile.getAbsolutePath());
					photoList.add(item);
					// 根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
					Bitmap bitmap = PictureManageUtil.getCompressBm(mCurrentPhotoFile.getAbsolutePath());
					// 获取旋转参数
					int rotate = PictureManageUtil.getCameraPhotoOrientation(mCurrentPhotoFile.getAbsolutePath());
					// 把压缩的图片进行旋转
					bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
					microBmList.add(bitmap);
					microBmList.add(addNewPic);
					Message msg = handler.obtainMessage(1);
					msg.sendToTarget();
				}
			}, delayMillis);
			break;
		}
		case PIC_VIEW_CODE: {
			ArrayList<Integer> deleteIndex = data.getIntegerArrayListExtra("deleteIndexs");
			if (deleteIndex.size() > 0) {
				for (int i = deleteIndex.size() - 1; i >= 0; i--) {
					microBmList.remove((int) deleteIndex.get(i));
					photoList.remove((int) deleteIndex.get(i));
				}
			}
			imgAdapter.notifyDataSetChanged();
			break;
		}

        }
	}

	public void loading() {
        /*
        for (int i=0;i<photoList.size();i++) {
            try {
                String path=photoList.get(i).getPhotoPath();
                Bitmap bm = Bimp.revitionImageSize(path);
                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                PictureNarrowUtils.saveBitmap(bm, "" + newStr);

                //upLoadhand.sendEmptyMessage(0x8);
            } catch (IOException e) {
                DialogUtil.showDialog(Activity_fabu_huodong_step3.this,e.toString(), true);
            }
        }
        */
        for (int i=0;i<adapter.mItems.size();i++) {
            try {
                if(adapter.mItems.get(i).getType()==1) {
                    String path = adapter.mItems.get(i).getBitmapPath();
                    Bitmap bm = Bimp.revitionImageSize(path);
                    String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                    PictureNarrowUtils.saveBitmap(bm, "" + newStr);
                }

                //upLoadhand.sendEmptyMessage(0x8);
            } catch (IOException e) {
                DialogUtil.showDialog(Activity_fabu_huodong_step3.this,e.toString(), true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xuanshanglayout:
                EditText jine=(EditText)findViewById(R.id.editText4);
                jine.requestFocus();
                InputMethodManager imm6 = (InputMethodManager) jine.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm6.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.relativeLayout69:
                /*
                jianjieTxt.requestFocus();
                InputMethodManager imm5 = (InputMethodManager) jianjieTxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                */
                break;
            case R.id.relativeLayout47:
                /*
                final String[]a={"无","面议","金额"};
                AlertDialog.Builder builder3 = new AlertDialog.Builder(Activity_fabu_huodong_step3.this);

                builder3.setItems(a, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        ((TextView)findViewById(R.id.textView169)).setText(a[arg1]);
                        if(arg1==0){
                            findViewById(R.id.xuanshanglayout).setVisibility(View.GONE);
                        }else if(arg1==1){
                            findViewById(R.id.xuanshanglayout).setVisibility(View.GONE);
                        }else if(arg1==2){
                            findViewById(R.id.xuanshanglayout).setVisibility(View.VISIBLE);
                        }
                    }
                });
                builder3.show();
                */
                popupwindows_xuanshang = new Popupwindows_xuanshang(Activity_fabu_huodong_step3.this, itemsOnClick2);
                popupwindows_xuanshang.showAtLocation(Activity_fabu_huodong_step3.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

                break;
            case R.id.headlayout:
                AlertDialog.Builder dialog = AndroidClass.getListDialogBuilder(
                        Activity_fabu_huodong_step3.this, arrayString, title,
                        onDialogClick);
                dialog.show();
                break;


            case R.id.fenleilayout:
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_fabu_huodong_step3.this);

                builder.setItems(fenleis, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        fenleiTxt.setText(fenleis[arg1]);
                    }
                });
                builder.show();
                */
                popupwindows_fenlei = new Popupwindows_fenlei(Activity_fabu_huodong_step3.this, itemsOnClick3);
                popupwindows_fenlei.showAtLocation(Activity_fabu_huodong_step3.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

                break;

            case R.id.layout11:
                startActivityForResult(new Intent(Activity_fabu_huodong_step3.this, LocationMain.class),12);
                break;
            case R.id.relativeLayout46:
                DatePickerDialog dpd=new DatePickerDialog(Activity_fabu_huodong_step3.this,Datelistener,year,month,day);
                dpd.show();//显示DatePickerDialog组件
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("destroy unregisterReceiver");
        if(thread!=null){

        }
        unregisterReceiver(imageBroadcastReceiver);
        unregisterReceiver(videoBroadcastReceiver);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
