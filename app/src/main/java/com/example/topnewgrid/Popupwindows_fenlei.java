package com.example.topnewgrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by 真爱de仙 on 2015/5/31.
 */
public class Popupwindows_fenlei extends PopupWindow {
    private View mMenuView;
    public Popupwindows_fenlei(Activity context,View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwindows_fenlei, null);
        mMenuView.findViewById(R.id.textView43).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView44).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView45).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView46).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView47).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView48).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView66).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView67).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView68).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.textView71).setOnClickListener(itemsOnClick);

        //取消按钮
        mMenuView.findViewById(R.id.textView51).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.poplinearLayout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
