package com.example.topnewgrid.choosephotos.choosephotos.photo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.topnewgrid.R;
import com.example.topnewgrid.choosephotos.choosephotos.ActivityManager;
import com.example.topnewgrid.choosephotos.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;

import java.io.File;
import java.util.ArrayList;

public class PhotoActivity extends Activity implements OnClickListener{
	private GridView gv;
	private Album album;
	private PicAdappter adapter;
	private TextView tv;
	private int chooseNum = 0;
	private Button finishBtn;//完成按钮
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_album_gridview);
		tv = (TextView)findViewById(R.id.photo_album_chooseNum);
		finishBtn = (Button)findViewById(R.id.finishBtn);
		album = (Album)getIntent().getExtras().get("album");
		/**获取已经选择的图片**/
		for (int i = 0; i < album.getBitList().size(); i++) {
			if(album.getBitList().get(i).isSelect()){
				chooseNum++;
			}
		}
		gv =(GridView)findViewById(R.id.photo_gridview);
		adapter = new PicAdappter(this,album);
		gv.setAdapter(adapter);
		tv.setText("选中"+chooseNum+"个");
		finishBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<Item> fileNamesList = new ArrayList<Item>();
				for (int i = 0; i < album.getBitList().size(); i++) {
					if(album.getBitList().get(i).isSelect()){
						fileNamesList.add(album.getBitList().get(i));					
					}
				}
				Intent it = new Intent();
				it.putParcelableArrayListExtra("fileNames", fileNamesList);
				ActivityManager.getActivity("PhotoAlbumActivity").setResult(RESULT_OK, it);
				ActivityManager.getActivity("PhotoAlbumActivity").finish();
				ActivityManager.removeActivity("PhotoAlbumActivity");
				PhotoActivity.this.finish();
			}
		});
	}
	
	public void back(View v){
		this.finish();
	}

	@Override
	public void onClick(View view) {
		if(view.getTag()!=null){
			if(view.getTag().toString().startsWith("select")){
				String vPosition=view.getTag().toString().substring(7);
				if( album.getBitList().get(Integer.parseInt(vPosition)).isSelect()){
					album.getBitList().get(Integer.parseInt(vPosition)).setSelect(false);
					chooseNum--;
				}else{
					album.getBitList().get(Integer.parseInt(vPosition)).setSelect(true);
					chooseNum++;
				}
				tv.setText("选中"+chooseNum+"个");
				adapter.notifyDataSetChanged();
			}
			else{
				String vPosition=view.getTag().toString().substring(6);
				Intent intent = new Intent(PhotoActivity.this, ViewPagerActivity.class);
				final String paths = album.getBitList().get(Integer.parseInt(vPosition)).getPhotoPath();
				new AlertDialog.Builder(PhotoActivity.this).setMessage(paths)
				.setPositiveButton("删除？", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						File f = new File(paths);
						if(!f.exists()){
							Toast.makeText(PhotoActivity.this, "该图片不存在", Toast.LENGTH_SHORT).show();
						}else{
							f.delete();
						}
					}
				})
				.setNegativeButton("取消", null)
				.show();
				
				//List->>ArrayList
				ArrayList<Item> fileNames= new ArrayList<Item>();
				for(int i = 0;i<album.getBitList().size();i++){
					fileNames.add(album.getBitList().get(i));
				}
				intent.putExtra(ViewPagerActivity.FILES, fileNames);
				intent.putExtra(ViewPagerActivity.CURRENT_INDEX, Integer.parseInt(vPosition));
//				startActivity(intent);
			}
		}
	}
}
