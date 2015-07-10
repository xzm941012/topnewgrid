package com.example.topnewgrid.obj;

/**
 * Created by 真爱de仙 on 2015/3/3.
 */
public class Friend {
    private int id;
    private String information;
    private String sex;
    private int distance;
    private String headSculpture;
    private String profession;
    private String album;
    private String userName;
    private String registerTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getHeadSculpture() {
        return headSculpture;
    }

    public void setHeadSculpture(String headSculpture) {
        this.headSculpture = headSculpture;
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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", information='" + information + '\'' +
                ", sex='" + sex + '\'' +
                ", distance=" + distance +
                ", headSculpture='" + headSculpture + '\'' +
                ", profession='" + profession + '\'' +
                ", album='" + album + '\'' +
                ", userName='" + userName + '\'' +
                ", registerTime='" + registerTime + '\'' +
                '}';
    }
}
