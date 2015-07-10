package draweradapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.topnewgrid.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import drawerlistitem.XiaoxiItem;

/**
 * Created by 真爱de仙 on 2015/1/17.
 */
public class XiaoxiAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<XiaoxiItem> mItems;

    public XiaoxiAdapter(Context context, List<XiaoxiItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = data;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public XiaoxiItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        XiaoxiItem item = getItem(position);
        TextView name = null;
        TextView dongtai=null;;
        TextView num=null;
        CircleImageView touxiang=null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.label_xiaoxi, null);
        }
        name = (TextView) convertView.findViewById(R.id.textView4);
        dongtai = (TextView) convertView.findViewById(R.id.textView5);
        num= (TextView) convertView.findViewById(R.id.textView3);
        touxiang = (CircleImageView) convertView.findViewById(R.id.profile_image);
        name.setText(item.getName());
        dongtai.setText(item.getDongtai());
        num.setText(item.getNum()+"");
        touxiang.setImageDrawable(item.getTouxiang());
        return convertView;
    }
}


