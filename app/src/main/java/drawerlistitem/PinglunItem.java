package drawerlistitem;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 真爱de仙 on 2015/1/22.
 */
public class PinglunItem implements Serializable {
    Integer pinglunId;
    Integer userId;
    Integer huodongId;
    String jiange;
    String name;
    String content;
    String time;
    String num;
    Drawable touxiang;
    String likes;
    List<String>mediaPath;
    List<String >picturePath;
    String gps;
    String commentNum;


    public PinglunItem() {

    }

    public PinglunItem(Integer pinglunId, Integer userId, Integer huodongId, String name, String content, String time, String num) {
        this.pinglunId = pinglunId;
        this.userId = userId;
        this.huodongId = huodongId;
        this.name = name;
        this.content = content;
        this.time = time;
        this.num = num;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getJiange() {
        return jiange;
    }

    public void setJiange(String jiange) {
        this.jiange = jiange;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public List<String> getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(List<String> mediaPath) {
        this.mediaPath = mediaPath;
    }

    public List<String> getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(List<String> picturePath) {
        this.picturePath = picturePath;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Integer getPinglunId() {
        return pinglunId;
    }

    public void setPinglunId(Integer pinglunId) {
        this.pinglunId = pinglunId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHuodongId() {
        return huodongId;
    }

    public void setHuodongId(Integer huodongId) {
        this.huodongId = huodongId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Drawable getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(Drawable touxiang) {
        this.touxiang = touxiang;
    }
}
