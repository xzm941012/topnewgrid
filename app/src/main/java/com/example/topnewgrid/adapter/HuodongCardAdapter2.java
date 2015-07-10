package com.example.topnewgrid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.topnewgrid.R;
import com.example.topnewgrid.obj.Huodong;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import http.PostUrl;

/**
 * Created by 真爱de仙 on 2015/5/12.
 */
public class HuodongCardAdapter2 extends BaseAdapter {
    private DisplayImageOptions defaultOptions ;
    private DisplayImageOptions defaultOptions2 ;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public List<Huodong> URLS ;
    private LayoutInflater mInflater;
    private Context context;
    public HuodongCardAdapter2(List<Huodong> URLS, Context context) {
        super();
        this.URLS=URLS;
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.drawable.avatar_default).build();
        defaultOptions2 = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().showImageOnFail(R.color.brownss).displayer(new SimpleBitmapDisplayer()).showImageOnLoading(R.color.brownss).build();
    }
    public int getCount() {
        return URLS.size();
    }

    public Huodong getItem(int position) {
        return URLS.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder ;
        final Huodong item=getItem(position);

        if(position==0){
            view = mInflater.inflate(R.layout.label_card3, null);
            holder = new ViewHolder(view);

            //view.setTag(holder2);
            ImageView tupian;
            ImageView bofang;
            ImageView touxiang;
            ImageView background;
            TextView time;
            TextView name,colectNum,pinglunNum,neirong,biaoti,distance,recompense;
            TextView t1,t2,t3,t4;
            bofang=(ImageView)view.findViewById(R.id.imageView238);
            tupian = (ImageView) view.findViewById(R.id.imageView153);
            name = (TextView) view.findViewById(R.id.textView162);
            time = (TextView) view.findViewById(R.id.textView164);
            biaoti = (TextView) view.findViewById(R.id.textView161);
            biaoti.getPaint().setFakeBoldText(true);
            distance = (TextView) view.findViewById(R.id.textView163);
            touxiang = (CircleImageView) view.findViewById(R.id.imageView);
            background=(ImageView)view.findViewById(R.id.imageView154);
            recompense=(TextView)view.findViewById(R.id.textView165);
            bofang.setVisibility(View.GONE);
            /*
            view = mInflater.inflate(R.layout.label_card, null);
            holder = new ViewHolder(view);
            */

            biaoti.setText(item.getTitle());
            name.setText(item.getAuthorName());
            String s[] = item.getFengmianUrl().split(";");
            if(s[0].contains("mp4")||s[0].contains("wmv")||s[0].contains("avi")||s[0].contains("3gp")){
                bofang.setVisibility(View.VISIBLE);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0].split("\\.")[0]+".jpg",tupian,defaultOptions2);
            }else {
                //imageDownloader.download("http://" + PostUrl.Url + ":8080/upload/" + s[0], holder.tupian);
                imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0], tupian, defaultOptions2);
            }
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getAuthorId() + ".jpg" ,touxiang,defaultOptions);
            /*
            if(item.getChuangjianTime().split(" ").length==2){
                    holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()).split(" ")[0]);
                }else {
                    holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()));
                }
                */
            time.setText(item.getContent());
            if (item.getDistance() != null) {
                if (!item.getDistance().equals("0")) {
                    String lengs = item.getDistance();
                    System.out.println("长度为:" + lengs);
                    double distances = Double.parseDouble(lengs);
                    int distanceInt = (int) distances;
                    if (distanceInt < 1000) {
                        distance.setText(distanceInt + "m");
                    } else {
                        distanceInt = distanceInt / 100;
                        distance.setText(distanceInt / 10 + "km");
                    }
                } else {
                    distance.setText("<100m");
                }
            }
        /*
        if (item.getSex().equals("男")) {
            holder.background.setImageDrawable(context.getResources().getDrawable(R.drawable.gender_male));
        } else if (item.getSex().equals("女")) {
            holder.background.setImageDrawable(context.getResources().getDrawable(R.drawable.gender_female));
        }
        */
            /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getAuthorId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = holder.touxiang;
                            handler7.sendMessage(ms);
                        }else{
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = holder.touxiang;
                            handler8.sendMessage(ms);
                        }
                    }
                }).start();
                */
            if (!item.getStyle().equals("活动")) {
                if(!item.getRecompense().equals("")&&!item.getRecompense().contains("免费")&&!item.getRecompense().trim().contains("无")) {
                    recompense.setVisibility(View.VISIBLE);
                    recompense.setText("$" + item.getRecompense());
                    System.out.println("item.getRecompense()："+item.getRecompense());
                }
            }
            return view;

        }else {
            if (view == null) {
                view = mInflater.inflate(R.layout.label_card, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
                if(holder==null){
                    view = mInflater.inflate(R.layout.label_card, null);
                    holder = new ViewHolder(view);
                    view.setTag(holder);
                }
            }
        }
        holder.bofang.setVisibility(View.GONE);
            /*
            view = mInflater.inflate(R.layout.label_card, null);
            holder = new ViewHolder(view);
            */

        holder.biaoti.setText(item.getTitle());
        holder.name.setText(item.getAuthorName());
        String s[] = item.getFengmianUrl().split(";");
        if(s[0].contains("mp4")||s[0].contains("wmv")||s[0].contains("avi")||s[0].contains("3gp")){
            holder.bofang.setVisibility(View.VISIBLE);
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0].split("\\.")[0]+".jpg",holder.tupian,defaultOptions2);
        }else {
            //imageDownloader.download("http://" + PostUrl.Url + ":8080/upload/" + s[0], holder.tupian);
            imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + s[0], holder.tupian, defaultOptions2);
        }
        imageLoader.displayImage("http://" + PostUrl.Media + ":8080/upload/" + item.getAuthorId() + ".jpg" ,holder.touxiang,defaultOptions);
            /*
            if(item.getChuangjianTime().split(" ").length==2){
                    holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()).split(" ")[0]);
                }else {
                    holder.time.setText(TimeUtil.getInterval(item.getChuangjianTime()));
                }
                */
        holder.time.setText(item.getContent());
        if (item.getDistance() != null) {
            if (!item.getDistance().equals("0")) {
                String lengs = item.getDistance();
                System.out.println("长度为:" + lengs);
                double distances = Double.parseDouble(lengs);
                int distanceInt = (int) distances;
                if (distanceInt < 1000) {
                    holder.distance.setText(distanceInt + "m");
                } else {
                    distanceInt = distanceInt / 100;
                    holder.distance.setText(distanceInt / 10 + "km");
                }
            } else {
                holder.distance.setText("<100m");
            }
        }
        /*
        if (item.getSex().equals("男")) {
            holder.background.setImageDrawable(context.getResources().getDrawable(R.drawable.gender_male));
        } else if (item.getSex().equals("女")) {
            holder.background.setImageDrawable(context.getResources().getDrawable(R.drawable.gender_female));
        }
        */
            /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getAuthorId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = holder.touxiang;
                            handler7.sendMessage(ms);
                        }else{
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = holder.touxiang;
                            handler8.sendMessage(ms);
                        }
                    }
                }).start();
                */
        if (!item.getStyle().equals("活动")) {
            if(!item.getRecompense().equals("")&&!item.getRecompense().contains("免费")&&!item.getRecompense().trim().contains("无")) {
                holder.recompense.setVisibility(View.VISIBLE);
                holder.recompense.setText("￥" + item.getRecompense());
                System.out.println("item.getRecompense()："+item.getRecompense());
            }
        }
                /*
                view = mInflater.inflate(R.layout.label_huodong2, null);

                background = (ImageView) view.findViewById(R.id.imageView108);
                t1 = (TextView) view.findViewById(R.id.textView32);
                t2 = (TextView) view.findViewById(R.id.textView33);
                t3 = (TextView) view.findViewById(R.id.textView34);
                t4 = (TextView) view.findViewById(R.id.textView35);
                tupian = (ImageView) view.findViewById(R.id.imageView18);
                name = (TextView) view.findViewById(R.id.name);
                time = (TextView) view.findViewById(R.id.time2);
                //neirong=(TextView)view.findViewById(R.id.neirong);
                biaoti = (TextView) view.findViewById(R.id.textView31);
                biaoti.getPaint().setFakeBoldText(true);
                distance = (TextView) view.findViewById(R.id.textView6);
                touxiang = (ImageView) view.findViewById(R.id.imageView);

                name.setText(item.getAuthorName());
                //neirong.setText(item.getContent());
                biaoti.setText(item.getTitle());
                System.out.println("发布时间为" + item.getChuangjianTime());
                System.out.println("相隔时间为:" + TimeUtil.getInterval(item.getChuangjianTime()));
                time.setText(TimeUtil.getInterval(item.getChuangjianTime()));
                if (item.getDistance() != null) {
                    if (!item.getDistance().equals("0")) {
                        String lengs = item.getDistance();
                        System.out.println("长度为:" + lengs);
                        double distances = Double.parseDouble(lengs);
                        int distanceInt = (int) distances;
                        if (distanceInt < 1000) {
                            distance.setText(distanceInt + "m");
                        } else {
                            distanceInt = distanceInt / 100;
                            distance.setText(distanceInt / 10 + "km");
                        }
                    } else {
                        distance.setText("<100m");
                    }
                }
                if (item.getSex().equals("男")) {
                    ((ImageView) view.findViewById(R.id.imageView121)).setImageDrawable(getResources().getDrawable(R.drawable.gender_male));
                } else if (item.getSex().equals("女")) {
                    ((ImageView) view.findViewById(R.id.imageView121)).setImageDrawable(getResources().getDrawable(R.drawable.gender_female));
                }
                if (item.getStyle().equals("活动")) {
                    background.setImageDrawable(getResources().getDrawable(R.color.activitycolor));
                    t1.setText(item.getFlag());
                    t2.setText("城市:" + item.getLocate());
                    t3.setText(item.getJoinMun() + "人报名");
                    t4.setText("简介:" + item.getContent());
                } else {
                    background.setImageDrawable(getResources().getDrawable(R.color.helpcolor));
                    t1.setText(item.getFlag());
                    t2.setText("城市:" + item.getLocate());
                    t3.setText("悬赏:" + item.getRecompense());
                    t4.setText("简介:" + item.getContent());
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Httppost.urlIsTrue(item.getAuthorId() + "")) {
                            Message ms = new Message();
                            ms.what = Integer.parseInt(item.getAuthorId() + "");
                            ms.obj = touxiang;
                            handler7.sendMessage(ms);
                        }
                    }
                }).start();

                if (item.getFengmianUrl() != null && !item.getFengmianUrl().equals("")) {
                    tupian.setVisibility(View.VISIBLE);
                    System.out.println("item.getFengmianUrl():" + item.getFengmianUrl());
                    String s[] = item.getFengmianUrl().split(";");

                    imageDownloader.download("http://" + PostUrl.Url + ":8080/upload/" + s[0], tupian);

                    if (item.getFengmianUrl() == null || item.getFengmianUrl().equals("")) {
                        //tupian.setVisibility(View.GONE);

                    }
                }
                */

        return view;
    }
    public final class ViewHolder {
        ImageView tupian;
        ImageView bofang;
        ImageView touxiang;
        ImageView background;
        TextView time;
        TextView name,colectNum,pinglunNum,neirong,biaoti,distance,recompense;
        TextView t1,t2,t3,t4;
        public ViewHolder(View view){
            bofang=(ImageView)view.findViewById(R.id.imageView238);
            tupian = (ImageView) view.findViewById(R.id.imageView153);
            name = (TextView) view.findViewById(R.id.textView162);
            time = (TextView) view.findViewById(R.id.textView164);
            biaoti = (TextView) view.findViewById(R.id.textView161);
            biaoti.getPaint().setFakeBoldText(true);
            distance = (TextView) view.findViewById(R.id.textView163);
            touxiang = (CircleImageView) view.findViewById(R.id.imageView);
            background=(ImageView)view.findViewById(R.id.imageView154);
            recompense=(TextView)view.findViewById(R.id.textView165);
        }
    }

}
