package com.example.topnewgrid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topnewgrid.adapter.DragAdapter;
import com.example.topnewgrid.adapter.OtherAdapter;
import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.bean.ChannelManage;
import com.example.topnewgrid.view.DragGrid;
import com.example.topnewgrid.view.OtherGridView;
import com.example.topnewgrid.view.OtherGridView2;

import java.util.ArrayList;

/**
 * 频道管理
 * @Author RA
 * @Blog http://blog.csdn.net/vipzjyno1
 */
public class ChannelActivity extends FragmentActivity implements OnItemClickListener {


	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
    private OtherGridView2 otherGridView2;
    private Button button,button2;
    private Button morePindao;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter,otherAdapter2;
	/** 其它栏目列表 */
	ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    ArrayList<ChannelItem> otherChannelList2 = new ArrayList<ChannelItem>();
	/** 用户栏目列表 */
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    ChannelManage channelManage;
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */	
	boolean isMove = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subscribe_activity);
        initLayout();
        initListenner();
		initView();
		initData();
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 1:
                Bundle b=data.getExtras();  //data为B中回传的Intent
                String str=b.getString("pindaoname");//str即为回传的值"Hello, this is B speaking"
                System.out.println("传过来的值为：" + str);
                ChannelItem channelItem=new ChannelItem(1,str,1,1);
                for(ChannelItem channelItem1:userChannelList){
                    if(channelItem1.getName().equals(str)){
                        Toast.makeText(ChannelActivity.this,"频道已存在",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }
                userAdapter.setVisible(false);
                userAdapter.addItem(channelItem);
                userAdapter.setVisible(true);
                userAdapter.notifyDataSetChanged();

        }
    }
    private void initLayout(){
        morePindao=(Button)findViewById(R.id.button5);

    }
	private void initListenner(){

        morePindao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(new Intent(ChannelActivity.this, Activity_pindao_list.class), 1);
            }
        });
    }
	/** 初始化数据*/
	private void initData() {
        AppApplication.getApp();
        channelManage=ChannelManage.getManage(AppApplication.getApp().getSQLHelper());
        String[] userList=getResources().getStringArray(R.array.userChannelList);
        String[] oherList=getResources().getStringArray(R.array.allChannelList);
        int m=1,n=1;
        userChannelList=new ArrayList<ChannelItem>();
        otherChannelList=new ArrayList<ChannelItem>();
        for(String arg:userList){
            userChannelList.add(new ChannelItem(m++,arg,n++,1));
        }
        n=1;
        for(String arg:oherList){
            otherChannelList.add(new ChannelItem(m++,arg,n++,0));
        }

	    userAdapter = new DragAdapter(this, userChannelList);
	    userGridView.setAdapter(userAdapter);
	    otherAdapter = new OtherAdapter(this, otherChannelList);
	    otherGridView.setAdapter(this.otherAdapter);

	    //设置GRIDVIEW的ITEM的点击监听
	    otherGridView.setOnItemClickListener(this);

	    userGridView.setOnItemClickListener(this);
        /*
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelManage.deleteAllChannel();
                channelManage.saveUserChannel(userChannelList);
                channelManage.saveOtherChannel(otherChannelList);
                channelManage.saveOtherChannel(otherChannelList2);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelManage.deleteAllChannel();
            }
        });
        */
	}
	
	/** 初始化布局*/
	private void initView() {

		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** GRIDVIEW对应的ITEM点击监听接口  */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position,long id) {
		//如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if(isMove){
			return;
		}
		if(parent.getId()== R.id.userGridView) {
            //position为 0，1 的不可以进行任何操作
            if (position != 0 ) {
                System.out.println("开始移动");
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                    for(ChannelItem channels:otherChannelList) {
                        if (channels.getName().equals(channel.getName())) {
                            userAdapter.setRemove(position);
                            userAdapter.remove();

                            return;
                        }
                    }


                    otherAdapter.setVisible(false);

                    //添加到最后一个
                    otherAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                userAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
            }
        }

		else if(parent.getId()== R.id.otherGridView) {
            final ImageView moveImageView = getView(view);
            if (moveImageView != null) {
                TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                final int[] startLocation = new int[2];
                newTextView.getLocationInWindow(startLocation);
                final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                for(ChannelItem channels:userChannelList){
                    if(channels.getName().equals(channel.getName())){
                        Toast.makeText(ChannelActivity.this,"频道已存在",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }
                userAdapter.setVisible(false);
                //添加到最后一个
                userAdapter.addItem(channel);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            int[] endLocation = new int[2];
                            //获取终点的坐标
                            userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                            MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                            otherAdapter.setRemove(position);
                        } catch (Exception localException) {
                        }
                    }
                }, 50L);
            }

        }else{
            return;
		}
	}
	/**
	 * 点击ITEM移动动画
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		//获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		//得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		//创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);//动画时间
		//动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				}else if(clickGridView instanceof OtherGridView){
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}
	
	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}
	
	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}
	
	/**
	 * 获取点击的Item的对应View，
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}
	
	/** 退出时候保存选择后数据库的设置  */
	private void saveChannel() {
		ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).deleteAllChannel();
		ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveUserChannel(userAdapter.getChannnelLst());
		ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).saveOtherChannel(otherAdapter.getChannnelLst());
	}
	
	@Override
	public void onBackPressed() {
		saveChannel();
		super.onBackPressed();
	}
}
