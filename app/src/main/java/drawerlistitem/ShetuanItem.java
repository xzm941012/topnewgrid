package drawerlistitem;

import android.graphics.drawable.Drawable;

/**
 * Created by 真爱de仙 on 2015/1/17.
 */
public class ShetuanItem {

    private Drawable touxiang;

    private String name;

    private String dongtai;

    private String leixing;

    public ShetuanItem(Drawable touxiang, String name, String dongtai, String leixing) {
        this.touxiang = touxiang;
        this.name = name;
        this.dongtai = dongtai;
        this.leixing = leixing;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
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
