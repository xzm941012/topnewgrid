package com.example.topnewgrid.obj;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 真爱de仙 on 2015/1/20.
 */
public class User {
    private String userId;
    private String email;//邮箱
    private String password;//密码
    private String name;//用户名
    private String information;
    private String distance;
    private String headSculpture;
    private int joinNum;
    private int publicNum;

    private List<String>huodongTitle;//活动标签
    private List<String>xuqiuTitle;//需求标签
    private List<String>album;
    private String sex;//性别
    private Bitmap touxiang;//头像
    private String registerTime; //注册时间
    private String profession;//职业
    private String lastLoginTime;//最后登陆时间
    private String locate;//现居地
    private String hometown;//老家
    private String level;//等级
    private String jianjie;//简介
    private String collectionId;//收藏
    private String teamID;//群组Id
    private String friendID;//好友Id
    private String trueName; //真实姓名
    private String GPS; //GPS地址
    private String token;//token
    private List<String>publicActivity;//所参加的活动
    private List<String>fabuActivity;
    private List<String>conlectionActivity;
    private List<OtherUser>friendList;
    private List<Huodong>huodongList;



    public User(){
        userId=null;
        email=null;password=null;
        name=null;
        huodongTitle=null;
        xuqiuTitle=null;
        sex=null;
        touxiang=null;
        registerTime=null;
        profession=null;
        lastLoginTime=null;
        locate=null;
        hometown=null;
        level=null;
        jianjie=null;
        collectionId=null;
        teamID=null;
        friendID=null;
        trueName=null;
        GPS=null;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
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

    public int getPublicNum() {
        return publicNum;
    }

    public void setPublicNum(int publicNum) {
        this.publicNum = publicNum;
    }

    public List<String> getAlbum() {
        return album;
    }

    public void setAlbum(List<String> album) {
        this.album = album;
    }

    public List<Huodong> getHuodongList() {
        return huodongList;
    }

    public void setHuodongList(List<Huodong> huodongList) {
        this.huodongList = huodongList;
    }

    public List<OtherUser> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<OtherUser> friendList) {
        this.friendList = friendList;
    }

    public List<String> getPublicActivity() {
        return publicActivity;
    }
    public Boolean stringExistList(String activity){
        return publicActivity.contains(activity);
    }
    public void addPublicActivity(String activity){
        if(publicActivity==null){
            publicActivity=new ArrayList<String>();
        }
        publicActivity.add(activity);
    }
    public void setPublicActivity(List<String> publicActivity) {
        this.publicActivity = publicActivity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHuodongTitle() {
        return huodongTitle;
    }

    public void setHuodongTitle(List<String> huodongTitle) {
        this.huodongTitle = huodongTitle;
    }

    public List<String> getXuqiuTitle() {
        return xuqiuTitle;
    }

    public void setXuqiuTitle(List<String> xuqiuTitle) {
        this.xuqiuTitle = xuqiuTitle;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Bitmap getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(Bitmap touxiang) {
        this.touxiang = touxiang;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getJianjie() {
        return jianjie;
    }

    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getGPS() {
        return GPS;
    }

    public void setGPS(String GPS) {
        this.GPS = GPS;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", information='" + information + '\'' +
                ", distance='" + distance + '\'' +
                ", headSculpture='" + headSculpture + '\'' +
                ", joinNum=" + joinNum +
                ", publicNum=" + publicNum +
                ", huodongTitle=" + huodongTitle +
                ", xuqiuTitle=" + xuqiuTitle +
                ", album=" + album +
                ", sex='" + sex + '\'' +
                ", touxiang=" + touxiang +
                ", registerTime='" + registerTime + '\'' +
                ", profession='" + profession + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", locate='" + locate + '\'' +
                ", hometown='" + hometown + '\'' +
                ", level='" + level + '\'' +
                ", jianjie='" + jianjie + '\'' +
                ", collectionId='" + collectionId + '\'' +
                ", teamID='" + teamID + '\'' +
                ", friendID='" + friendID + '\'' +
                ", trueName='" + trueName + '\'' +
                ", GPS='" + GPS + '\'' +
                ", token='" + token + '\'' +
                ", publicActivity=" + publicActivity +
                ", fabuActivity=" + fabuActivity +
                ", conlectionActivity=" + conlectionActivity +
                ", friendList=" + friendList +
                ", huodongList=" + huodongList +
                '}';
    }
}
