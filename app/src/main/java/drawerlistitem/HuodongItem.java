package drawerlistitem;

import android.graphics.drawable.Drawable;

/**
 * Created by 真爱de仙 on 2015/1/16.
 */
public class HuodongItem {

    private Drawable touxiang;

    private Drawable tupian;

    private String name;

    private String time;

    private String juli;

    private String title;

    private String content;

    private String renshu;

    private String fenlei;

    public HuodongItem(Drawable touxiang, Drawable tupian, String name, String time, String juli, String title, String content, String renshu, String fenlei) {
        this.touxiang = touxiang;
        this.tupian = tupian;
        this.name = name;
        this.time = time;
        this.juli = juli;
        this.title = title;
        this.content = content;
        this.renshu = renshu;
        this.fenlei = fenlei;
    }

    public Drawable getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(Drawable touxiang) {
        this.touxiang = touxiang;
    }

    public Drawable getTupian() {
        return tupian;
    }

    public void setTupian(Drawable tupian) {
        this.tupian = tupian;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJuli() {
        return juli;
    }

    public void setJuli(String juli) {
        this.juli = juli;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRenshu() {
        return renshu;
    }

    public void setRenshu(String renshu) {
        this.renshu = renshu;
    }

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }
}
