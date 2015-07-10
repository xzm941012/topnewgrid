package com.example.topnewgrid.view;
  
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
/** 
 * �ڽ���仯ʱ���������ݷ����仯ʱ��Ҫ�ж��Ƿ���ʾ�ұ�cleanͼ�� 
 */
public class CleanableAutoCompleteTextView extends AutoCompleteTextView {
    private Drawable mRightDrawable; 
    private boolean isHasFocus; 

    public CleanableAutoCompleteTextView(Context context) {
        super(context); 
        init(); 
    } 
    public CleanableAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs); 
        init(); 
    } 
  
    public CleanableAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle); 
        init(); 
    } 
      
    private void init(){ 
        //getCompoundDrawables: 
        //Returns drawables for the left, top, right, and bottom borders. 
        Drawable [] drawables=this.getCompoundDrawables(); 
        //ȡ��rightλ�õ�Drawable 
        //�������ڲ����ļ������õ�android:drawableRight 
        mRightDrawable=drawables[2];   
        //���ý���仯�ļ��� 
        this.setOnFocusChangeListener(new FocusChangeListenerImpl()); 
        //����EditText���ֱ仯�ļ��� 
        this.addTextChangedListener(new TextWatcherImpl()); 
        //��ʼ��ʱ���ұ�cleanͼ�겻�ɼ� 
        setClearDrawableVisible(false); 
    } 
      
      
    /** 
     * ����ָ̧���λ����clean��ͼ������� 
     * ���ǽ�����Ϊ����������� 
     * getWidth():�õ��ؼ��Ŀ�� 
     * event.getX():̧��ʱ������(������������ڿؼ�������Ե�) 
     * getTotalPaddingRight():clean��ͼ�����Ե���ؼ��ұ�Ե�ľ��� 
     * getPaddingRight():clean��ͼ���ұ�Ե���ؼ��ұ�Ե�ľ��� 
     * ����: 
     * getWidth() - getTotalPaddingRight()��ʾ: 
     * �ؼ���ߵ�clean��ͼ�����Ե������ 
     * getWidth() - getPaddingRight()��ʾ: 
     * �ؼ���ߵ�clean��ͼ���ұ�Ե������ 
     * ����������֮�������պ���clean��ͼ������� 
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) { 
        switch (event.getAction()) { 
        case MotionEvent.ACTION_UP: 
              
            boolean isClean =(event.getX() > (getWidth() - getTotalPaddingRight()))&& 
                             (event.getX() < (getWidth() - getPaddingRight())); 
            if (isClean) { 
                setText(""); 
            } 
            break; 
  
        default: 
            break; 
        } 
        return super.onTouchEvent(event); 
    } 
      
    private class FocusChangeListenerImpl implements OnFocusChangeListener{ 
        @Override
        public void onFocusChange(View v, boolean hasFocus) { 
             isHasFocus=hasFocus; 
             if (isHasFocus) { 
                 boolean isVisible=getText().toString().length()>=1; 
                 setClearDrawableVisible(isVisible); 
            } else { 
                 setClearDrawableVisible(false); 
            } 
        } 
          
    } 
      
    //������������ж��Ƿ���ʾ�ұ�clean��ͼ�� 
    private class TextWatcherImpl implements TextWatcher{ 
        @Override
        public void afterTextChanged(Editable s) { 
             boolean isVisible=getText().toString().length()>=1; 
             setClearDrawableVisible(isVisible); 
        } 
  
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) { 
              
        } 
  
        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) { 
              
        } 
          
    }    
      
    //���ػ�����ʾ�ұ�clean��ͼ�� 
    protected void setClearDrawableVisible(boolean isVisible) { 
        Drawable rightDrawable; 
        if (isVisible) { 
            rightDrawable = mRightDrawable; 
        } else { 
            rightDrawable = null; 
        } 
        //ʹ�ô������øÿؼ�left, top, right, and bottom����ͼ�� 
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],  
                             rightDrawable,getCompoundDrawables()[3]); 
    }  
  
  
}