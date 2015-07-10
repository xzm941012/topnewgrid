/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.topnewgrid.imageviewloader.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.topnewgrid.imageviewloader.ImageDownloader;

import java.util.ArrayList;

public class DemoImageAdapter extends BaseAdapter {
	private final ImageDownloader imageDownloader;
    private ArrayList<String> URLS ;
	public DemoImageAdapter(ArrayList URLS,Context context) {
		super();
		this.URLS=URLS;
		this.imageDownloader = new ImageDownloader(context);
	}
    public int getCount() {
        return URLS.size();
    }
    
    public String getItem(int position) {
        return URLS.get(position);
    }

    public long getItemId(int position) {
        return URLS.get(position).hashCode();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = new ImageView(parent.getContext());
//            LayoutParams layoutParams = new LayoutParams(400, 50);
//            view.setLayoutParams(layoutParams);
            view.setPadding(6, 6, 6, 6);
            view.setMinimumHeight(156);
        }      
        imageDownloader.download(URLS.get(position), (ImageView) view);
        return view;
    }

}
