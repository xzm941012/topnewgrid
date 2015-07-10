/**
 * 
 */
package com.example.topnewgrid.choosephotos.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

/**
 * Description: <br/>
 * 锟斤拷站: <a href="http://www.crazyit.org">锟斤拷锟絁ava锟斤拷锟斤拷</a> <br/>
 * Copyright (C), 2001-2012, Yeeku.H.Lee <br/>
 * This program is protected by copyright laws. <br/>
 * Program Name: <br/>
 * Date:
 * 
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class DialogUtil {
	public static void showDialog(final Context ctx, String msg, boolean closeSelf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
		if (closeSelf) {
			builder.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		} else {
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}
	public static void showDialog(Context ctx, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setView(view).setCancelable(false).setPositiveButton("确锟斤拷", null);
		builder.create().show();
	}
}
