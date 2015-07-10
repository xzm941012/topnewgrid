package com.example.topnewgrid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.example.topnewgrid.adapter.MediaGridViewAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.choosephotos.choosephotos.adapter.AddImageGridAdapter;
import com.example.topnewgrid.choosephotos.choosephotos.controller.SelectPicPopupWindow;
import com.example.topnewgrid.choosephotos.choosephotos.photo.Item;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewerinterface.ViewPagerDeleteActivity;
import com.example.topnewgrid.choosephotos.choosephotos.util.PictureManageUtil;
import com.example.topnewgrid.choosephotos.util.Bimp;
import com.example.topnewgrid.choosephotos.util.DialogUtil;
import com.example.topnewgrid.choosephotos.util.PictureNarrowUtils;
import com.example.topnewgrid.obj.User;
import com.example.topnewgrid.service.UploadReceiverService;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mediachooser.MediaChooser;
import mediachooser.activity.HomeFragmentActivity;

/**
 * Created by 真爱de仙 on 2015/1/21.
 */
public class Activity_fabu_receiver extends Activity implements Handler.Callback{

    Thread thread;
    public User user;
    private AppApplication app;
    GridView gridView;
    MediaGridViewAdapter adapter;
    public static Activity_fabu_receiver activity_fabu_receiver;
    BroadcastReceiver videoBroadcastReceiver;
    BroadcastReceiver imageBroadcastReceiver;
    public NotificationManager mNotificationManager;
    public int notifyId;
    public String nowTime;
    public NotificationCompat.Builder mBuilder;
    public BDLocation location;

    private ArrayList<Bitmap> microBmList = new ArrayList<Bitmap>();
    private ArrayList<Item> photoList = new ArrayList<Item>();
    private SelectPicPopupWindow menuWindow;
    public String contentup;
    private AddImageGridAdapter imgAdapter;
    private Bitmap addNewPic;
    // GridView预览删除页面过来
    private final int PIC_VIEW_CODE = 2016;

    EditText content;
    public List<String> videoArray=new ArrayList<>();
    public List<String> imageArray=new ArrayList<>();
    public String huodongID,huodongTitle;
    Handler handler;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fabu_receiver);
        location= AppApplication.location;
        activity_fabu_receiver=this;
        gridView = (GridView)findViewById(R.id.gridView2);
        addNewPic = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_pic);
        // addNewPic = PictureManageUtil.resizeBitmap(addNewPic, 180, 180);
        //microBmList.add(addNewPic);
        imgAdapter = new AddImageGridAdapter(this, microBmList);
        gridView.setAdapter(imgAdapter);

        huodongID=getIntent().getExtras().getString("huodongID");
        huodongTitle=getIntent().getExtras().getString("huodongTitle");
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        initUser();
        initLayout();
        initHandler();
        initParam();
        initListener();
        initBradcast();
        MediaChooser.setVideoSize(100);

        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);



    }
    private void initBradcast(){

        videoBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(Activity_fabu_receiver.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                videoArray.clear();
                videoArray.addAll(intent.getStringArrayListExtra("list"));
                for(String videoPath:videoArray){
                    File mediaFile = new File(videoPath);
                    Item item = new Item();
                    item.setPhotoPath(mediaFile.getAbsolutePath());
                    photoList.add(item);
                    // 根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
                    Bitmap bitmap = PictureManageUtil.getVideoThumbnail(mediaFile.getAbsolutePath(), 500, 500, MediaStore.Images.Thumbnails.MICRO_KIND);
                    // 获取旋转参数

                    microBmList.add(bitmap);
                }
                Message msg = handler.obtainMessage(7);
                msg.sendToTarget();
            }
        };



        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(Activity_fabu_receiver.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                imageArray.clear();
                imageArray.addAll(intent.getStringArrayListExtra("list"));
                for(String imagePath:imageArray){
                    System.out.println("图片地址:"+imagePath);
                    File mediaFile = new File(imagePath);
                    Item item = new Item();
                    item.setPhotoPath(mediaFile.getAbsolutePath());
                    photoList.add(item);
                    // 根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
                    Bitmap bitmap = PictureManageUtil.getCompressBm(mediaFile.getAbsolutePath());
                    // 获取旋转参数
                    int rotate = PictureManageUtil.getCameraPhotoOrientation(mediaFile.getAbsolutePath());
                    // 把压缩的图片进行旋转
                    bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
                    microBmList.add(bitmap);
                }
                Message msg = handler.obtainMessage(6);
                msg.sendToTarget();

            }
        };
    }


    private void initUser() {
        app = (AppApplication) getApplication();
        user = app.getUser();
    }
    private void setAdapter( List<String> filePathList) {
        adapter = new MediaGridViewAdapter(Activity_fabu_receiver.this, 0, filePathList);
        gridView.setAdapter(adapter);
    }
    private void initHandler(){
        handler=new Handler(this);
    }
    private void initParam(){

    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo: {
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        //打开图片选择
                        if(videoArray.size()!=0){
                            videoArray.clear();
                            photoList.clear();
                            microBmList.clear();
                        }
                        MediaChooser.setSelectedMediaCount(0);
                        MediaChooser.showOnlyImageTab();
                        MediaChooser.setSelectionLimit(1);
                        Intent intent = new Intent(Activity_fabu_receiver.this, HomeFragmentActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Activity_fabu_receiver.this, "打开失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case R.id.btn_pick_photo: {
                    // 打开视频选择
                    if(imageArray.size()!=0){
                        imageArray.clear();
                        photoList.clear();
                        microBmList.clear();
                    }
                    MediaChooser.setSelectedMediaCount(0);
                    MediaChooser.showOnlyVideoTab();
                    MediaChooser.setSelectionLimit(1);
                    Intent intent = new Intent(Activity_fabu_receiver.this, HomeFragmentActivity.class);
                    startActivity(intent);
                    break;
                }
                default:
                    break;
            }
        }
    };
    private void initLayout(){
        content=(EditText)findViewById(R.id.editText25);
    }
    private void initListener(){
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            public boolean onKey(final DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // Cancel task.
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_fabu_receiver.this);
                    builder.setMessage("确认停止上传")
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int id) {
                                    dialog1.dismiss();
                                    dialog.dismiss();
                                    if(thread.isAlive()){
                                        thread.interrupt();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int id) {
                                    dialog1.cancel();
                                }
                            });
                    builder.show();
                }
                return false;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == (photoList.size())) {
                    menuWindow = new SelectPicPopupWindow(Activity_fabu_receiver.this, itemsOnClick);
                    menuWindow.showAtLocation(Activity_fabu_receiver.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                } else {
                    Intent intent = new Intent(Activity_fabu_receiver.this, ViewPagerDeleteActivity.class);
                    intent.putParcelableArrayListExtra("files", photoList);
                    intent.putExtra(ViewPagerActivity.CURRENT_INDEX, position);
                    startActivityForResult(intent, PIC_VIEW_CODE);
                }
            }
        });
        findViewById(R.id.imageView148).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tupianselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoArray.size()!=0){
                    videoArray.clear();
                    photoList.clear();
                    microBmList.clear();
                }

                MediaChooser.setSelectedMediaCount(0);
                MediaChooser.showOnlyImageTab();
                MediaChooser.setSelectionLimit(1);
                Intent intent = new Intent(Activity_fabu_receiver.this, HomeFragmentActivity.class);
                startActivity(intent);

            }
        });
        findViewById(R.id.shipingselect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageArray.size()!=0){
                    imageArray.clear();
                    photoList.clear();
                    microBmList.clear();
                }

                MediaChooser.setSelectedMediaCount(0);
                MediaChooser.showOnlyVideoTab();
                MediaChooser.setSelectionLimit(1);
                Intent intent = new Intent(Activity_fabu_receiver.this, HomeFragmentActivity.class);
                startActivity(intent);

                //startActivityForResult(new Intent(Activity_fabu_receiver.this, MediaRecorderActivity.class),999);

            }
        });

        findViewById(R.id.contentlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.requestFocus();
                InputMethodManager imm = (InputMethodManager) content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        findViewById(R.id.textView153).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date dt=new Date();//如果不需要格式,可直接用dt,dt就是当前系统时间
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置显示格式
                nowTime= df.format(dt);//用DateFormat的format()方法在dt中获取并以yyyy/MM/dd HH:mm:ss格式显示
                if(!((EditText)findViewById(R.id.editText25)).getText().toString().trim().equals("")){
                    //dialog.show();
                    notifyId=app.titleBuild++;
                    contentup=content.getText().toString().trim();
                    startService(new Intent(Activity_fabu_receiver.this, UploadReceiverService.class));
                    finish();
                }else{
                    Toast.makeText(Activity_fabu_receiver.this,"请完善活动简介",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void loading() {
        for (int i=0;i<imageArray.size();i++) {
            try {
                String path=imageArray.get(i);
                Bitmap bm = Bimp.revitionImageSize(path);
                if(bm!=null){
                    System.out.println("bitmap not null");
                }
                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                PictureNarrowUtils.saveBitmap(bm, "" + newStr);
                Message message = new Message();
                //upLoadhand.sendEmptyMessage(0x8);
            } catch (IOException e) {
                DialogUtil.showDialog(Activity_fabu_receiver.this, e.toString(), true);
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==821821){
            videoArray.clear();
            videoArray.add(data.getStringExtra("videofile"));
            for(String videoPath:videoArray){
                File mediaFile = new File(videoPath);
                Item item = new Item();
                item.setPhotoPath(mediaFile.getAbsolutePath());
                photoList.add(item);
                // 根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
                Bitmap bitmap = PictureManageUtil.getVideoThumbnail(mediaFile.getAbsolutePath(), 500, 500, MediaStore.Images.Thumbnails.MICRO_KIND);
                // 获取旋转参数

                microBmList.add(bitmap);
            }
            Message msg = handler.obtainMessage(7);
            msg.sendToTarget();
        }
        switch (requestCode) {
            case PIC_VIEW_CODE: {
                ArrayList<Integer> deleteIndex = data.getIntegerArrayListExtra("deleteIndexs");
                if (deleteIndex.size() > 0) {
                    for (int i = deleteIndex.size() - 1; i >= 0; i--) {
                        microBmList.remove((int) deleteIndex.get(i));
                        photoList.remove((int) deleteIndex.get(i));
                        if(videoArray.size()!=0){
                            videoArray.remove((int) deleteIndex.get(i));
                        }else if(imageArray.size()!=0){
                            imageArray.remove((int) deleteIndex.get(i));
                        }
                    }
                }
                imgAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case 1:
                Toast.makeText(Activity_fabu_receiver.this,"发布成功，请下拉刷新页面",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Map<String,String>map=(HashMap)msg.obj;
                String imageNum=map.get("imageNum");
                dialog.setMessage("正在上传第"+imageNum+"张图片");
                break;
            case 3:
                Map<String,String>map1=(HashMap)msg.obj;
                String videoNum=map1.get("videoNum");
                mBuilder.setProgress(100, Integer.parseInt(videoNum), false);
                mNotificationManager.notify(notifyId, mBuilder.build());
                break;
            case 4:
                dialog.setMessage("正在发送广播");
                break;
            case 5:
                dialog.dismiss();
                break;
            case 6:
                imgAdapter.notifyDataSetChanged();
                MediaChooser.setSelectedMediaCount(imageArray.size());
                break;
            case 7:
                imgAdapter.notifyDataSetChanged();
                MediaChooser.setSelectedMediaCount(videoArray.size());
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        unregisterReceiver(imageBroadcastReceiver);
        unregisterReceiver(videoBroadcastReceiver);
    }

}
