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
 * Created by �氮de�� on 2015/5/31.
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

        //ȡ����ť
        mMenuView.findViewById(R.id.textView51).setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //���ٵ�����
                dismiss();
            }
        });
        //���ð�ť����

        //����SelectPicPopupWindow��View
        this.setContentView(mMenuView);
        //����SelectPicPopupWindow��������Ŀ�
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //����SelectPicPopupWindow��������ĸ�
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //����SelectPicPopupWindow��������ɵ��
        this.setFocusable(true);
        //����SelectPicPopupWindow�������嶯��Ч��
        this.setAnimationStyle(R.style.AnimBottom);
        //ʵ����һ��ColorDrawable��ɫΪ͸��
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //����SelectPicPopupWindow��������ı���
        this.setBackgroundDrawable(dw);
        //mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
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
