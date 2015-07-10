package com.example.topnewgrid;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.topnewgrid.app.AppApplication;
import com.example.topnewgrid.choosephotos.util.Bimp;
import com.example.topnewgrid.obj.Label_edit;
import com.example.topnewgrid.obj.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mediachooser.MediaChooser;
import mediachooser.activity.HomeFragmentActivity;

/**
 * Created by 真爱de仙 on 2015/6/2.
 */
public class Activity_edit extends Activity {
    Popupwindows_edit popupwindows_edit;
    List<Label_edit> edits=new ArrayList<>();
    EditAdapter adapter;
    ListView listView;
    List<String> imageArray=new ArrayList<>();
    User user;
    Gson gson;
    BroadcastReceiver imageBroadcastReceiver;
    public SharedPreferences edit_xiangqing;//参加的活动
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_edit);
        initUser();
        initLayout();
        gson = new Gson();
        edit_xiangqing=getSharedPreferences("edit",
                Activity.MODE_PRIVATE);
        String result=edit_xiangqing.getString(user.getUserId()+"","");
        if(!result.equals("")) {
            edits=gson.fromJson(result, new TypeToken<List<Label_edit>>() {}.getType());
        }
        adapter=new EditAdapter(Activity_edit.this,edits);
        listView.setAdapter(adapter);
        initBroadcast();
        MediaChooser.setSelectionLimit(1);
        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        initListenner();
    }
    private void initUser(){
        user=((AppApplication) getApplication()).getUser();
    }
    private void initListenner(){
        findViewById(R.id.textView73).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = edit_xiangqing.edit();
                editor.putString(user.getUserId()+"",gson.toJson(edits));
                editor.commit();
                Intent intent=new Intent();
                intent.putExtra("edit",gson.toJson(edits));
                setResult(999,intent);
                finish();
            }
        });
        findViewById(R.id.imageView80).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.relativeLayout29).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindows_edit = new Popupwindows_edit(Activity_edit.this, itemsOnClick);
                popupwindows_edit.showAtLocation(Activity_edit.this.findViewById(R.id.hearLayout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

            }
        });
    }
    private void initBroadcast(){
        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                imageArray.clear();
                imageArray.addAll(intent.getStringArrayListExtra("list"));
                Label_edit edit=new Label_edit();
                edit.setType(1);
                edit.setBitmapPath(imageArray.get(0));
                adapter.mItems.add(edit);
                adapter.notifyDataSetChanged();
            }
        };
    }


    private void initLayout(){
        listView=(ListView)findViewById(R.id.listView);
    }
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            popupwindows_edit.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo: {
                    Label_edit edit=new Label_edit();
                    edit.setType(0);
                    adapter.mItems.add(edit);
                    adapter.notifyDataSetChanged();
                    break;
                }
                case R.id.btn_pick_photo: {
                    MediaChooser.setSelectedMediaCount(0);
                    MediaChooser.showOnlyImageTab();
                    Intent intent = new Intent(Activity_edit.this, HomeFragmentActivity.class);
                    startActivity(intent);
                    break;
                }
                default:
                    break;
            }

        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(imageBroadcastReceiver);
    }
class EditAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        public List<Label_edit> mItems;
        private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();
        protected ImageLoader imageLoader = ImageLoader.getInstance();


        private Context context;

        public EditAdapter(Context context, List<Label_edit> data) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final Label_edit item = getItem(position);
            convertView = mInflater.inflate(R.layout.label_edit, null);
            final EditText content=((EditText)convertView.findViewById(R.id.editText6));
            final ImageView tupian=((ImageView)convertView.findViewById(R.id.imageView81));
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
                    item.setWenzi(contentText);
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
}
