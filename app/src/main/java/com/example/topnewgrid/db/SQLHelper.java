package com.example.topnewgrid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "database6.db";// 数据库名称
	public static final int VERSION = 1;
	
	public static final String TABLE_CHANNEL = "channel";//数据表
    public static final String USER_TABLE="lastuser";

	public static final String ID = "id";//
	public static final String NAME = "name";
	public static final String ORDERID = "orderId";
	public static final String SELECTED = "selected";

    //储存在lastuser表中的用户名和密码
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String USERNAME="username";
    public static final String USERID="userid";
    public static final String ACTIVYTYTAG="activitytag";
    public static final String TOKEN="token";
    public static final String IMAGE="image";

	private Context context;
	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作
		String sql = "create table if not exists "+TABLE_CHANNEL +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ID + " INTEGER , " +
				NAME + " TEXT , " +
				ORDERID + " INTEGER , " +
				SELECTED + " SELECTED)";
        String sql2 = "create table if not exists "+USER_TABLE +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EMAIL + " char(20) , " +
                USERID + " char(20) , " +
                USERNAME + " char(20) , " +
                ACTIVYTYTAG + " char(20) , " +
                TOKEN + " char(20) ," +
                IMAGE + " BLOB ," +
                PASSWORD + " char(20)  )" ;
		db.execSQL(sql);
        System.out.println("sql执行完毕");
        db.execSQL(sql2);
        System.out.println("sql2执行完毕");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
		onCreate(db);
	}

}
