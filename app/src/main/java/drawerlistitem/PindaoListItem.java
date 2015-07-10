package drawerlistitem;

/**
 * Created by 真爱de仙 on 2015/2/14.
 */
public class PindaoListItem {
    private Integer pindaoId;//频道id
    private String pindaoName;//频道名字
    private String pindaoJianjie;//频道简介
    private String pindaoNum; //频道人数

    public PindaoListItem(){}
    public PindaoListItem(Integer pindaoId, String pindaoName, String pindaoJianjie, String pindaoNum) {
        this.pindaoId = pindaoId;
        this.pindaoName = pindaoName;
        this.pindaoJianjie = pindaoJianjie;
        this.pindaoNum = pindaoNum;
    }

    public Integer getPindaoId() {
        return pindaoId;
    }

    public void setPindaoId(Integer pindaoId) {
        this.pindaoId = pindaoId;
    }

    public String getPindaoName() {
        return pindaoName;
    }

    public void setPindaoName(String pindaoName) {
        this.pindaoName = pindaoName;
    }

    public String getPindaoJianjie() {
        return pindaoJianjie;
    }

    public void setPindaoJianjie(String pindaoJianjie) {
        this.pindaoJianjie = pindaoJianjie;
    }

    public String getPindaoNum() {
        return pindaoNum;
    }

    public void setPindaoNum(String pindaoNum) {
        this.pindaoNum = pindaoNum;
    }
}
