package com.example.topnewgrid.imageviewloader.test;/*
package com.example.topnewgrid.imageviewloader.test;


import android.app.ListActivity;
import android.os.Bundle;


import com.example.topnewgrid.R;
import com.example.topnewgrid.obj.Huodong;
import com.example.topnewgrid.util.DialogUtil;


import java.util.ArrayList;
import java.util.List;


public class ImageViewLoaderActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			List<Huodong> result= Manage.viewByCategory("运动");
			DialogUtil.showDialog(ImageViewLoaderActivity.this, result, true);
			ArrayList list=Manage.pathList;
			Manage.pathList=null;
			DialogUtil.showDialog(ImageViewLoaderActivity.this, list.toString(),true);
	        setListAdapter(new DemoImageAdapter(list,ImageViewLoaderActivity.this));
		} catch (Exception e) {
			e.printStackTrace();
		}     
    }
}
*/