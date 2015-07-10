package com.example.topnewgrid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.topnewgrid.R;
import com.example.topnewgrid.choosephotos.util.Bimp;
import com.example.topnewgrid.obj.Label_edit;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

/**
 * Created by 真爱de仙 on 2015/5/13.
 */
public class EditAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    EditAdapter editAdapter;
    public List<Label_edit> mItems;
    private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory().cacheOnDisc().build();
    protected ImageLoader imageLoader = ImageLoader.getInstance();


    private Context context;

    public EditAdapter(Context context, List<Label_edit> data) {
        editAdapter=this;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.context=context;

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Label_edit getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Label_edit item = getItem(position);
        convertView = mInflater.inflate(R.layout.label_edit, null);
        final EditText content=((EditText)convertView.findViewById(R.id.editText6));
        final ImageView tupian=((ImageView)convertView.findViewById(R.id.imageView81));
        convertView.findViewById(R.id.textView76).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("你选择的是"+position);
                mItems.remove(position);
                editAdapter.notifyDataSetChanged();
            }
        });
        if(item.getType()==0){
            content.setVisibility(View.VISIBLE);
            tupian.setVisibility(View.GONE);

            if(item.getWenzi()!=null){
                content.setText(item.getWenzi());
            }
            /*
            convertView.findViewById(R.id.editLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    content.requestFocus();
                    InputMethodManager imm6 = (InputMethodManager) content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm6.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
            });
*/
            content.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String contentText = content.getText().toString().trim();
                    getItem(position).setWenzi(contentText);
                }
            });

        }else{
            content.setVisibility(View.GONE);
            tupian.setVisibility(View.VISIBLE);
            if(item.getBitmapPath()!=null) {
                try {
                    Bitmap bm = Bimp.revitionImageSize(item.getBitmapPath());
                    tupian.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return convertView;
    }

}
