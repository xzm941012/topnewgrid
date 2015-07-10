package com.example.topnewgrid.obj;

import android.app.Application;

/**
 * Created by 真爱de仙 on 2015/1/20.
 */
public class ApplicationUser extends Application {
    User user;
    String[] title={"热门","关注","学习","学习","游戏","运动"};

    @Override
    public void onCreate(){
        super.onCreate();
        setUser(null);
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }
}
