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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;


import com.example.topnewgrid.R;
import com.example.topnewgrid.choosephotos.choosephotos.ApplicationContent;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewlibs.HackyViewPager;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewlibs.PhotoView;

import java.util.List;

public class ViewPagerBitmapActivity extends Activity {

	public static final String FILES = "files";
	public static final String CURRENT_INDEX = "currentIndex";
	
	public List<Bitmap> receiveBmList;
	public int index;
	
	public RelativeLayout header;
	public PhotoView photoView;
	GestureDetector tapGestureDetector;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_view);
        ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
//		setContentView(mViewPager);
		
        header = (RelativeLayout)findViewById(R.id.header);
        photoView = new PhotoView(this);
        receiveBmList = (List<Bitmap>) ApplicationContent.get("mImageBm");
		this.index = this.getIntent().getIntExtra(CURRENT_INDEX, 0);

		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(index);
		tapGestureDetector = new GestureDetector(this, new TapGestureListener());

	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return receiveBmList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			photoView = new PhotoView(container.getContext());
			photoView.setImageDrawable(new BitmapDrawable(receiveBmList.get(position)));

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			photoView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					header.setVisibility(header.getVisibility()==View.GONE? View.VISIBLE:View.GONE);
//				}
//			});
//			photoView.setOnTouchListener(new OnTouchListener() {
//		        public boolean onTouch(View v, MotionEvent event) {
//		            if(event.getAction() == MotionEvent.ACTION_DOWN){
//		            	tapGestureDetector.onTouchEvent(event);
//		            }
//		            return true;
//		        }
//			});
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
	
	class TapGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
        	header.setVisibility(header.getVisibility()== View.GONE? View.VISIBLE: View.GONE);
        	return true;
        }
    }

	public void back(View v){
		ViewPagerBitmapActivity.this.finish();
	}
	
	public void showOrHidden(View v){
		int a = header.getVisibility();
		int b = View.VISIBLE;
		int c = View.GONE;
//		header.setVisibility(header.getVisibility()==View.GONE? View.VISIBLE:View.GONE);
	}
	

}
