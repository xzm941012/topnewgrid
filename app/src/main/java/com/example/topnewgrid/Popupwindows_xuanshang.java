package com.example.topnewgrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by �氮de�� on 2015/5/31.
 */
public class Popupwindows_xuanshang extends PopupWindow {
    private View mMenuView;
    public Popupwindows_xuanshang(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popwindows_xuanshang, null);

        //ȡ����ť
        mMenuView.findViewById(R.id.layout1).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //���ٵ�����
                dismiss();
            }
        });
        //���ð�ť����
        mMenuView.findViewById(R.id.imageView).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.imageView3).setOnClickListener(itemsOnClick);
        mMenuView.findViewById(R.id.imageView2).setOnClickListener(itemsOnClick);

        //����SelectPicPopupWindow��View
        this.setContentView(mMenuView);
        //����SelectPicPopupWindow��������Ŀ�
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //����SelectPicPopupWindow��������ĸ�
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //����SelectPicPopupWindow��������ɵ��
        this.setFocusable(true);
        //����SelectPicPopupWindow�������嶯��Ч��
        this.setAnimationStyle(R.style.AnimBottom);
        //ʵ����һ��ColorDrawable��ɫΪ͸��
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //����SelectPicPopupWindow��������ı���
        this.setBackgroundDrawable(dw);
        //mMenuView����OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
        /*
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        */

    }
}