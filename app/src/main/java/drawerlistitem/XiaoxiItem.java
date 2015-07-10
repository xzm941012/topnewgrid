package drawerlistitem;

import android.graphics.drawable.Drawable;

/**
 * Created by 真爱de仙 on 2015/1/17.
 */
public class XiaoxiItem {

    private Drawable touxiang;

    private String name;

    private String dongtai;

    private Integer num;

    public XiaoxiItem(Drawable touxiang, String name, String dongtai, Integer num) {
        this.touxiang = touxiang;
        this.name = name;
        this.dongtai = dongtai;
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Drawable getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(Drawable touxiang) {
        this.touxiang = touxiang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDongtai() {
        return dongtai;
    }

    public void setDongtai(String dongtai) {
        this.dongtai = dongtai;
    }
}
