package com.example.topnewgrid.obj;

import java.io.Serializable;
import java.util.List;

/**
 * Created by �氮de�� on 2015/1/23.
 */
public class Huodong implements Serializable {
    private Integer huodongId;
    private String authorId;
    private String authorName;
    private String touxiangUrl;
    private String guangBo;
    private  int level;
    private String fengmianUrl;
    private String fenlei;
    private String title;
    private String content;
    private String time;
    private String gps;
    private Integer peopleNum;
    private Integer joinMun;
    private String zhuangKuang;
    private String sex;
    private String executeTime;//��ʼʱ��
    private Integer collected;
    private Integer pinglunNum;
    private Integer ifneedtupian;   //0:no 1:yes
    private Integer ifneedjianli;
    private String chuangjianTime;
    private String excuteTime;
    private String locate;
    private String distance;//����
    private String flag;
    private String address;
    private String recompense;//֧����ʽ
    private String style;
    private String locationAdress="";
    private String locationPoi_Adress="";
    private String locationPoi="";
    private String locationx="";
    private String locationy="";
    private List<String>enrollUser;
    private String contactInformation;
    private String authorInformation;

    public Huodong(){
        huodongId=null;authorId=null;authorName=null;touxiangUrl=null;guangBo=null;fengmianUrl=null;fenlei=null;title=null;
        content=null;time=null;gps=null;peopleNum=null;joinMun=null;zhuangKuang=null;collected=null;pinglunNum=null;
    }

    public String getAuthorInformation() {
        return authorInformation;
    }

    public void setAuthorInformation(String authorInformation) {
        this.authorInformation = authorInformation;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public List<String> getEnrollUser() {
        return enrollUser;
    }

    public void setEnrollUser(List<String> enrollUser) {
        this.enrollUser = enrollUser;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getIfneedjianli() {
        return ifneedjianli;
    }

    public void setIfneedjianli(Integer ifneedjianli) {
        this.ifneedjianli = ifneedjianli;
    }

    public String getLocationAdress() {
        return locationAdress;
    }

    public void setLocationAdress(String locationAdress) {
        this.locationAdress = locationAdress;
    }

    public String getLocationPoi_Adress() {
        return locationPoi_Adress;
    }

    public void setLocationPoi_Adress(String locationPoi_Adress) {
        this.locationPoi_Adress = locationPoi_Adress;
    }

    public String getLocationPoi() {
        return locationPoi;
    }

    public void setLocationPoi(String locationPoi) {
        this.locationPoi = locationPoi;
    }

    public String getLocationx() {
        return locationx;
    }

    public void setLocationx(String locationx) {
        this.locationx = locationx;
    }

    public String getLocationy() {
        return locationy;
    }

    public void setLocationy(String locationy) {
        this.locationy = locationy;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRecompense() {
        return recompense;
    }

    public void setRecompense(String recompense) {
        this.recompense = recompense;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(String excuteTime) {
        this.excuteTime = excuteTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getChuangjianTime() {
        return chuangjianTime;
    }

    public void setChuangjianTime(String chuangjianTime) {
        this.chuangjianTime = chuangjianTime;
    }

    public Integer getIfneedtupian() {
        return ifneedtupian;
    }

    public void setIfneedtupian(Integer ifneedtupian) {
        this.ifneedtupian = ifneedtupian;
    }

    public Integer getHuodongId() {
        return huodongId;
    }

    public void setHuodongId(Integer huodongId) {
        this.huodongId = huodongId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTouxiangUrl() {
        return touxiangUrl;
    }

    public void setTouxiangUrl(String touxiangUrl) {
        this.touxiangUrl = touxiangUrl;
    }

    public String getGuangBo() {
        return guangBo;
    }

    public void setGuangBo(String guangBo) {
        this.guangBo = guangBo;
    }

    public String getFengmianUrl() {
        return fengmianUrl;
    }

    public void setFengmianUrl(String fengmianUrl) {
        this.fengmianUrl = fengmianUrl;
    }

    public String getFenlei() {
        return fenlei;
    }

    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getJoinMun() {
        return joinMun;
    }

    public void setJoinMun(Integer joinMun) {
        this.joinMun = joinMun;
    }

    public String getZhuangKuang() {
        return zhuangKuang;
    }

    public void setZhuangKuang(String zhuangKuang) {
        this.zhuangKuang = zhuangKuang;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Integer getPinglunNum() {
        return pinglunNum;
    }

    public void setPinglunNum(Integer pinglunNum) {
        this.pinglunNum = pinglunNum;
    }

    @Override
    public String toString() {
        return "Huodong{" +
                "huodongId=" + huodongId +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", touxiangUrl='" + touxiangUrl + '\'' +
                ", guangBo='" + guangBo + '\'' +
                ", fengmianUrl='" + fengmianUrl + '\'' +
                ", fenlei='" + fenlei + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", gps='" + gps + '\'' +
                ", peopleNum=" + peopleNum +
                ", joinMun=" + joinMun +
                ", zhuangKuang='" + zhuangKuang + '\'' +
                ", collected=" + collected +
                ", pinglunNum=" + pinglunNum +
                ", ifneedtupian=" + ifneedtupian +
                ", ifneedjianli=" + ifneedjianli +
                ", chuangjianTime='" + chuangjianTime + '\'' +
                ", excuteTime='" + excuteTime + '\'' +
                ", locate='" + locate + '\'' +
                ", distance='" + distance + '\'' +
                ", flag='" + flag + '\'' +
                ", address='" + address + '\'' +
                ", recompense='" + recompense + '\'' +
                ", style='" + style + '\'' +
                ", locationAdress='" + locationAdress + '\'' +
                ", locationPoi_Adress='" + locationPoi_Adress + '\'' +
                ", locationPoi='" + locationPoi + '\'' +
                ", locationx='" + locationx + '\'' +
                ", locationy='" + locationy + '\'' +
                '}';
    }
}
