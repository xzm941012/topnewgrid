package com.example.topnewgrid.choosephotos.choosephotos.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.topnewgrid.R;


public class PhotoGridItem extends RelativeLayout implements Checkable {
	private Context mContext;
	private boolean mCheck;
	private ImageView mImageView;
	private ImageView mSelect;
	
	public PhotoGridItem(Context context) {
		this(context, null, 0);
	}
	
	public PhotoGridItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);  
    }

	public PhotoGridItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.list_album_gridview_item, this);
		mImageView = (ImageView)findViewById(R.id.photo_img_view);
		mSelect = (ImageView)findViewById(R.id.photo_select);
	}

	@Override
	public void setChecked(boolean checked) {
		mCheck = checked;
		System.out.println(checked);
		mSelect.setImageDrawable(checked?getResources().getDrawable(R.drawable.image_selected_icon):getResources().getDrawable(R.drawable.image_unselected_icon));
		//mSelect.setVisibility(checked?View.VISIBLE:View.GONE);
	}   
	
	@Override
	public boolean isChecked() {
		return mCheck;
	}

	@Override
	public void toggle() {  
		setChecked(!mCheck);
	}
	
	public void setImgResID(int id){
		if(mImageView != null){
			mImageView.setBackgroundResource(id);
		}
	}
	
	public void SetBitmap(Bitmap bit){
		if(mImageView != null){
			mImageView.setImageBitmap(bit);
		}
	}
	
	public void SetDefaultBitmap(){
		if(mImageView != null){
			mImageView.setImageResource(R.drawable.image_default_progress);
		}
	}

	public ImageView getmImageView() {
		return mImageView;
	}

	public void setmImageView(ImageView mImageView) {
		this.mImageView = mImageView;
	}

	public ImageView getmSelect() {
		return mSelect;
	}

	public void setmSelect(ImageView mSelect) {
		this.mSelect = mSelect;
	}
	
	
}
