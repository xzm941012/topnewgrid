package com.example.topnewgrid.obj;

/**
 * Created by �氮de�� on 2015/3/5.
 */
public class OtherUser {
    private int id;
    private String information;
    private String sex;
    private String uerId;
    private double distance;
    private String locate;
    private String headSculpture;
    private int joinNum;
    private String profession;//ְҵ
    private String album;//���
    private String userName;
    private int publicNum;//�����Ļ����
    private String registerTime;
    private int level;
    private String GPS;

    public String getUerId() {
        return uerId;
    }

    public void setUerId(String uerId) {
        this.uerId = uerId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPublicNum() {
        return publicNum;
    }

    public void setPublicNum(int publicNum) {
        this.publicNum = publicNum;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "OtherUser{" +
                "id=" + id +
                ", information='" + information + '\'' +
                ", sex='" + sex + '\'' +
                ", distance=" + distance +
                ", locate='" + locate + '\'' +
                ", headSculpture='" + headSculpture + '\'' +
                ", joinNum=" + joinNum +
                ", profession='" + profession + '\'' +
                ", album='" + album + '\'' +
                ", userName='" + userName + '\'' +
                ", publicNum=" + publicNum +
                ", registerTime='" + registerTime + '\'' +
                ", level=" + level +
                ", GPS='" + GPS + '\'' +
                '}';
    }
}
