/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewerinterface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;


import com.example.topnewgrid.R;
import com.example.topnewgrid.choosephotos.choosephotos.photo.Item;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewlibs.HackyViewPager;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewlibs.PhotoView;
import com.example.topnewgrid.choosephotos.choosephotos.util.PictureManageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewPagerActivity extends Activity {

	public static final String FILES = "files";
	public static final String CURRENT_INDEX = "currentIndex";
	
	private ArrayList<Item> files = new ArrayList<Item>();
	public int index;
	private Button btnCheck;
	private final int VIEW_PAGER_CODE = 1122;
	private int chooseNum = 0;
	private TextView tv;
	private ViewPager mViewPager;
	private Map<String, BitmapDrawable> cacheBitmap = new HashMap<String, BitmapDrawable>();
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		
		files = this.getIntent().getParcelableArrayListExtra(FILES);
		this.index = this.getIntent().getIntExtra(CURRENT_INDEX, 0);

		/**获取已经选择的图片**/
		for (int i = 0; i < files.size(); i++) {
			if(files.get(i).isSelect()){
				chooseNum++;
			}
		}
		tv = (TextView)findViewById(R.id.photo_album_chooseNum);
		tv.setText("选中"+chooseNum+"个");
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putParcelableArrayListExtra(FILES, files);
				setResult(VIEW_PAGER_CODE, data);
				ViewPagerActivity.this.finish();
			}
		});
		btnCheck = (Button)findViewById(R.id.btnCheck);
		btnCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = mViewPager.getCurrentItem();
				boolean isSelect = files.get(position).isSelect();
				files.get(position).setSelect(!isSelect);
				if(isSelect){
					chooseNum--;
					btnCheck.setBackgroundResource(R.drawable.image_unselected_icon);
				
				}else{
					chooseNum++;
					btnCheck.setBackgroundResource(R.drawable.image_selected_icon);
				}
				tv.setText("选中"+chooseNum+"个");
			}
		});
		boolean isSelect = files.get(mViewPager.getCurrentItem()).isSelect();
		if(isSelect){
			btnCheck.setBackgroundResource(R.drawable.image_selected_icon);
		}else{
			btnCheck.setBackgroundResource(R.drawable.image_unselected_icon);
		}
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(index);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				boolean isSelect = files.get(position).isSelect();
				if(isSelect){
					btnCheck.setBackgroundResource(R.drawable.image_selected_icon);
				}else{
					btnCheck.setBackgroundResource(R.drawable.image_unselected_icon);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		//完成
		Button btnFinish = (Button)findViewById(R.id.btnFinish);
		btnFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putParcelableArrayListExtra(FILES, files);
				setResult(VIEW_PAGER_CODE, data);
				ViewPagerActivity.this.finish();
			}
		});
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return files.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			BitmapDrawable bmDrawable;
			if(cacheBitmap.get(files.get(position).getPhotoPath())==null){
				//根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
				Bitmap bitmap = PictureManageUtil.getCompressBm(files.get(position).getPhotoPath());
				//获取旋转参数
				int rotate = PictureManageUtil.getCameraPhotoOrientation(files.get(position).getPhotoPath());
				//把压缩的图片进行旋转
				bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
				bmDrawable = new BitmapDrawable(ViewPagerActivity.this.getResources(), bitmap);
				cacheBitmap.put(files.get(position).getPhotoPath(), bmDrawable);
				photoView.setImageDrawable(bmDrawable);
			}else{
				photoView.setImageDrawable(cacheBitmap.get(files.get(position).getPhotoPath()));
			}
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}
}
