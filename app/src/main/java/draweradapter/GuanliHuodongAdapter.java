package draweradapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.topnewgrid.R;

import java.util.List;

import drawerlistitem.GuanliHuodongItem;

/**
 * Created by 真爱de仙 on 2015/1/17.
 */
public class GuanliHuodongAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<GuanliHuodongItem> mItems;
    Context context;


    public GuanliHuodongAdapter(Context context, List<GuanliHuodongItem> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = data;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public GuanliHuodongItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GuanliHuodongItem item = getItem(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.label_guanli, null);
        }


        return convertView;
    }
}


