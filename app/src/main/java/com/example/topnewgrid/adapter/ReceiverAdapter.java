package com.example.topnewgrid.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topnewgrid.R;
import com.example.topnewgrid.util.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import demo.MyLocation;
import demo.Point;
import demo.ReadLocation;
import drawerlistitem.PinglunItem;
import http.PostUrl;

/**
 * Created by 真爱de仙 on 2015/5/13.
 */
public class ReceiverAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<PinglunItem> mItems;
    private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory().cacheOnDisc().build();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public List<ImageView>imageViews;

    private Context context;

    public ReceiverAdapter(Context context, List<PinglunItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.context=context;
        imageViews=new ArrayList<ImageView>();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public PinglunItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PinglunItem item = getItem(position);
        final ViewHolder holder ;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.label_receiver, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String[]locate=item.getGps().split(";");
        if(!locate[0].equals("")){
            holder.locate.setText(locate[0]);
        }
        holder.num.setText(item.getCommentNum()+"");
        holder.content.setText(item.getContent());
        holder.bofang.setVisibility(View.GONE);
        holder.tupian.setVisibility(View.GONE);
        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLocation location=new MyLocation();
                location.point=new Point();
                location.point.x=Double.parseDouble(item.getGps().split(";")[1]);
                location.point.y=Double.parseDouble(item.getGps().split(";")[2]);
                Intent intent=new Intent();
                intent.setClass(context, ReadLocation.class);
                intent.putExtra("location", location);
                context.startActivity(intent);
            }
        });
        if(item.getMediaPath()!=null&&!("").equals(item.getMediaPath())){
            List<String>mediaPath=item.getMediaPath();
            System.out.println("有视频，视频数量为:"+mediaPath.size());
            holder.bofang.setVisibility(View.VISIBLE);
            holder.tupian.setVisibility(View.VISIBLE);
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" +mediaPath.get(0).split("\\.")[0]+".jpg" ,holder.tupian,defaultOptions);
            holder.tupian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*
                    String url = "http://121.41.74.72:8080/upload/"+item.getMediaPath().get(0).split("\\.")[0]+".mp4";
                    //String url = "http://121.41.74.72:8080/upload/"+"3.mp4";
                    Intent intent = new Intent();
                    intent.setClass(context, BBVideoPlayer.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                    */
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse("http://"+ PostUrl.Media+":8080/upload/"+item.getMediaPath().get(0).split("\\.")[0]+".mp4");
                    it.setDataAndType(uri, "video/mp4");
                    context.startActivity(it);

                }
            });
        }else if(item.getPicturePath()!=null&&!("").equals(item.getPicturePath())){
            List<String>pictureList=item.getPicturePath();
            holder.tupian.setVisibility(View.VISIBLE);
            System.out.println("有图片，图片数量为:"+pictureList.size());
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" +pictureList.get(0) ,holder.tupian);
            holder.tupian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
        holder.head.setVisibility(View.GONE);
        String time1 = TimeUtil.getInterval(item.getTime());
        String t1=item.getTime().split(" ")[0];
        String t2=t1.split("-")[0]+"年"+t1.split("-")[1]+"月"+t1.split("-")[2]+"日";
        if(time1.contains("刚刚")||time1.contains("秒前")||time1.contains("分钟前")||time1.contains("小时前")){
            item.setJiange(time1);
        }else if(time1.contains("1天")){
            item.setJiange("1天前");
        }else if(time1.contains("2天")){
            item.setJiange("2天前");
        }else{
            item.setJiange(t2);
        }

        if(position==0){
            holder.head.setVisibility(View.VISIBLE);
            holder.time.setText(item.getJiange());
        }else{
            if(!item.getJiange().equals(getItem(position-1).getJiange())){
                holder.head.setVisibility(View.VISIBLE);
                holder.time.setText(item.getJiange());
            }
        }

        return convertView;
    }
    public final class ViewHolder {
        ImageView tupian;
        TextView time;
        TextView locate;
        TextView content;
        TextView num;
        ImageView bofang;
        View head;
        public ViewHolder(View view){
            num=(TextView) view.findViewById(R.id.textView);
            head=view.findViewById(R.id.headlayout);
            bofang = (ImageView) view.findViewById(R.id.imageView262);
            tupian = (ImageView) view.findViewById(R.id.imageView260);
            content = (TextView) view.findViewById(R.id.textView279);
            time = (TextView) view.findViewById(R.id.textView294);
            locate = (TextView) view.findViewById(R.id.textView280);
        }
    }
}
