package com.example.topnewgrid.obj;

/**
 * Created by 真爱de仙 on 2015/3/15.
 */
public class ReceiverMessage {
    private String userid="";
    private String username="";
    private String huodongid="";
    private String huodongname="";
    private String content="";
    private String type="";

    public ReceiverMessage() {
    }

    public ReceiverMessage(String userid, String username, String huodongid, String huodongname, String content, String type) {
        this.userid = userid;
        this.username = username;
        this.huodongid = huodongid;
        this.huodongname = huodongname;
        this.content = content;
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHuodongid() {
        return huodongid;
    }

    public void setHuodongid(String huodongid) {
        this.huodongid = huodongid;
    }

    public String getHuodongname() {
        return huodongname;
    }

    public void setHuodongname(String huodongname) {
        this.huodongname = huodongname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", huodongid='" + huodongid + '\'' +
                ", huodongname='" + huodongname + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
