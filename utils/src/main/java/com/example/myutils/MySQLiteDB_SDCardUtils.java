package com.example.myutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class MySQLiteDB_SDCardUtils {
	
	/*********注意：用完之后关闭数据库的链接***********
	 * 这里是对于SD卡内的数据库操作
	 * 
	 * environmentPoint_Dir 这个参数是指SD卡下的哪个文件夹
	 * 如：Environment.DIRECTORY_DOWNLOADS
	 * 
	 * db_fileName指数据库的文件名
	 * 
	 * creatTableYUju为建表语句
	 * 如："create table if not exists tb_words (_id integer primary key autoincrement,english)"
	 * */

	private SQLiteDatabase dbConn = null;

	public MySQLiteDB_SDCardUtils(String environmentPoint_Dir,String db_fileName,String creatTableYUju) {

		File file = new File(Environment.getExternalStoragePublicDirectory(environmentPoint_Dir).getAbsolutePath(),db_fileName);
		String dbpath=file.getAbsoluteFile().toString();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 第一个参数是数据库文件的完全路径名，第二个参数是一个可缺省的Cursor工厂，第三个参数是数据库的打开的方式读写
		// cursor它是一个数据库的查询结果，cursor可以被遍历，就能拿到所有的查询结果，可以理解cursor就是一个二维的表结构
		dbConn = SQLiteDatabase.openDatabase(dbpath, null,
				SQLiteDatabase.OPEN_READWRITE);
		// execSQL可以执行任意的sql语句，包括ddl，dml
		dbConn.execSQL(creatTableYUju);

	}

	/**
	 * 
	 *  这是查询的方法，返回值为cursor，selectionArgs它是带替换占位符的
	 * 
	 * */
	
	public Cursor selectCursor(String sql, String[] selectionArgs) {

		// 在数据查询的时候，我们经常使用替换占位符
		// String str=select * from tb_words where id=? and sex=? and name=?;
		// xxxx(str,new String [] {"1","1","zhangsan"});
		return dbConn.rawQuery(sql, selectionArgs);
	}

	/**
	 * 
	 * 这是查询方法，直接返回list；
	 * 
	 * */
	
	public List<Map<String, Object>> selectList(String sql,
			String[] selectionArgs) {
		return cursorToList(selectCursor(sql, selectionArgs));
	}

	// 把一个cursor转化为list<map>的方法，一个二维结构的等价替换。
	public List<Map<String, Object>> cursorToList(Cursor cursor) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 需要知道这一个查询结果都有什么列名
		String[] arrColumnName = cursor.getColumnNames();

		// cursor.moveToNext()是有一个指向行的游标
		// 在cursor最开始产生的时候，游标指向第一个的前一个
		// 当存在下一个的时候，这个方法会指向下一个，并且返回true
		// 当不存在下一个的时候，就直接返回false；

		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < arrColumnName.length; i++) {
				Object cols_value = cursor.getString(i);
				map.put(arrColumnName[i], cols_value);
			}
			list.add(map);

		}

		// 需要注意的是：cursor使用完毕后，要关闭
		if (cursor != null) {
			cursor.close();
		}

		return list;
	}

	/**
	 * 
	 * 执行任意语句，主要是为了insert、delete、update
	 * 
	 * */
	
	public boolean execData(String sql, Object[] bindArgs) {
		try {
			dbConn.execSQL(sql, bindArgs);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	// 关闭数据库的链接
	public void destroy() {
		if (dbConn != null) {
			dbConn.close();
		}
	}

}
