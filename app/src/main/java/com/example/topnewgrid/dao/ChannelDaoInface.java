package com.example.topnewgrid.dao;

import java.util.List;
import java.util.Map;

import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.obj.User;

import android.content.ContentValues;

public interface ChannelDaoInface {
	public boolean addCache(ChannelItem item);

	public boolean deleteCache(String whereClause, String[] whereArgs);

	public boolean updateCache(ContentValues values, String whereClause,
                               String[] whereArgs);

	public Map<String, String> viewCache(String selection,
                                         String[] selectionArgs);

	public List<Map<String, String>> listCache(String selection,
                                               String[] selectionArgs);

	public void clearFeedTable();

    public Map<String,String> getLastUser();

    public boolean updateUser(User user);

    public void clearUserTable();
}
