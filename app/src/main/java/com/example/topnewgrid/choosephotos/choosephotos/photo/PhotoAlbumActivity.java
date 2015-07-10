package com.example.topnewgrid.choosephotos.choosephotos.photo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;


import com.example.topnewgrid.R;
import com.example.topnewgrid.choosephotos.choosephotos.ActivityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoAlbumActivity extends Activity {
	private GridView albumGV;
	private List<Album> albumList;

	// 设置获取图片的字段
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // 显示的名
			MediaStore.Images.Media.LATITUDE, // 维度
			MediaStore.Images.Media.LONGITUDE, // 经度
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name 目录名字
			MediaStore.Images.Media.DATA, //路径
			MediaStore.Images.Media.DATE_TAKEN  //
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_album);
		
		//ActivityManager是自己写的用来管理Activity的类，主要用来跨页面finish
		ActivityManager.addActivity(this, "PhotoAlbumActivity");
		
		albumGV = (GridView) findViewById(R.id.album_gridview);
		albumList = getPhotoAlbum();
		albumGV.setAdapter(new AlbumAdapter(albumList, this));
		albumGV.setOnItemClickListener(albumClickListener);
	}

	/**
	 * 相册点击事件
	 */
	OnItemClickListener albumClickListener =  new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Intent intent = new Intent(PhotoAlbumActivity.this,
					PhotoActivity.class);
			intent.putExtra("album", albumList.get(position));
			startActivity(intent);
		}
	};
	/**
	 * 方法描述：按相册获取图片信息
	 */
	private List<Album> getPhotoAlbum() {
		List<Album> albumList = new ArrayList<Album>();
		Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				null, null, MediaStore.Images.Media.DATE_TAKEN + " desc");
		Map<String, Album> countMap = new HashMap<String, Album>();
		Album pa = null;
		while (cursor.moveToNext()) {
			String id = cursor.getString(3);
			String dir_id = cursor.getString(4);
			String dir = cursor.getString(5);
			String path = cursor.getString(6);
			String dateTaken = cursor.getString(7);
			if (!countMap.containsKey(dir_id)) {
				pa = new Album();
				pa.setName(dir);
				pa.setBitmap(Integer.parseInt(id));
				pa.setCount("1");
				pa.getBitList().add(new Item(Integer.valueOf(id), path, dateTaken));
				countMap.put(dir_id, pa);
			} else {
				pa = countMap.get(dir_id);
				pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
				pa.getBitList().add(new Item(Integer.valueOf(id), path, dateTaken));
			}
		}
		cursor.close();
		Iterable<String> it = countMap.keySet();
		for (String key : it) {
			albumList.add(countMap.get(key));
		}
		return albumList;
	}
	
	public void cancel(View v){
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}
}
