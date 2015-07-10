package com.example.topnewgrid.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.topnewgrid.bean.ChannelItem;
import com.example.topnewgrid.db.SQLHelper;
import com.example.topnewgrid.obj.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelDao implements ChannelDaoInface {
	private SQLHelper helper = null;

	public ChannelDao(Context context) {
		helper = new SQLHelper(context);
	}

	@Override
	public boolean addCache(ChannelItem item) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		long id = -1;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("name", item.getName());
			values.put("id", item.getId());
			values.put("orderId", item.getOrderId());
			values.put("selected", item.getSelected());
			id = database.insert(SQLHelper.TABLE_CHANNEL, null, values);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}
    //更新数据库里最后登录的用户信息
    public boolean updateUser(User user){
        boolean flag = false;
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("email", user.getEmail());
            values.put("password", user.getPassword());
            values.put("username", user.getName());
            values.put("userid", user.getUserId());
            values.put("token",user.getToken());
            if(user.getTouxiang()!=null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                user.getTouxiang().compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("image", os.toByteArray());
            }
            List<String>activitytag=user.getHuodongTitle();
            if(activitytag!=null&&!activitytag.equals("")) {
                StringBuffer activityString = new StringBuffer();
                for (String a : activitytag) {
                    activityString.append(a + ";");
                }

                values.put("activitytag", activityString.toString());
            }
            id = database.insert(SQLHelper.USER_TABLE, null, values);
            System.out.println("插入完成");
            flag = (id != -1 ? true : false);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

	@Override
	public boolean deleteCache(String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLHelper.TABLE_CHANNEL, whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.update(SQLHelper.TABLE_CHANNEL, values, whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public Map<String, String> viewCache(String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = null;
		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(true, SQLHelper.TABLE_CHANNEL, null, selection,
					selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return map;
	}
    //获得最后登陆的用户的信息
    public Map getLastUser(){
        SQLiteDatabase database = null;
        Map map=null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(false, SQLHelper.USER_TABLE, null,null ,null, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            if (cursor.moveToNext()) {
                map = new HashMap<String, String>();
                for (int i = 0; i < cols_len; i++) {

                    String cols_name = cursor.getColumnName(i);
                    System.out.print("cols_name:"+cols_name);
                    if(cols_name.equals("image")){
                        byte[] in=cursor.getBlob(cursor.getColumnIndex("image"));
                        if(in!=null) {
                            map.put("image", BitmapFactory.decodeByteArray(in, 0, in.length));
                        }
                        continue;
                    }
                    String cols_values = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    map.put(cols_name, cols_values);
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return map;
    }

	@Override
	public List<Map<String, String>> listCache(String selection,String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(false, SQLHelper.TABLE_CHANNEL, null, selection,selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cols_len; i++) {

					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
				list.add(map);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public void clearFeedTable() {
		String sql = "DELETE FROM " + SQLHelper.TABLE_CHANNEL + ";";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
		revertSeq();
	}
    public void clearUserTable(){
        String sql = "DELETE FROM " + SQLHelper.USER_TABLE + ";";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
        revertSeq();
    }
	private void revertSeq() {
		String sql = "update sqlite_sequence set seq=0 where name='"
				+ SQLHelper.USER_TABLE + "'";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
	}

}
