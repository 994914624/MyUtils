package com.example.application;

import com.example.myutils.MySQLiteOpenHelperUtils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

	private MySQLiteOpenHelperUtils mySQLiteOpenHelper = null;

	// 这个是ContentProvider被创建的回调。
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mySQLiteOpenHelper = new MySQLiteOpenHelperUtils(getContext());
		mySQLiteOpenHelper
				.execData(
						"create table if not exists tb_my(_id integer primary key autoincrement,name)",
						new String[] {});

		return false;
	}

	// 当有contentresolver获取这个contentprovider的数据的时候就会调用这个方法
	// 也就是说，contentprovider要在这里访问数据库，把对数据库的查询的结果返回给contentresolver
	// 第一个参数，uri，它来描述contentresolver想要获取什么样类型的数据
	// 第二个参数，是获取数据的时候都获取哪些数据
	// 第三个参数，是获取数据采用的过滤条件，也就是where语句
	// 第四个参数，是第三个参数的替换占位符。替换？
	// 第五个参数是，排序条件。
	// 返回值，返回查询的结果

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		// 对数据库进行查询
		// 你可以使用原生的sql，不使用这个方法里的参数。
		// select * from tb_user;
		// Cursor cursor=mySQLiteOpenHelper.query("tb_my", projection,
		// selection, selectionArgs, sortOrder);
		// 使用这个拿不到cursors
		// mySQLiteOpenHelper.execData("select * from tb_my;");
		Cursor cursor=mySQLiteOpenHelper
				.selectCursor("select * from tb_my;", new String[] {});
		return cursor;
	}

	// 返回数据的mimetype，来描述数据是什么。
	// 要传入数据的类型描述

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	// 插入数据用的，在contentresolver调用insert方法时，就会执行这个方法
	// 第一个参数是数据类型描述，第二个参数用来封装insert语句中插入的字段（名-值）
	// contentValues是封装键值对的，在这里用来描述insert语句的键值对表示。
	// 返回值，就是插入时的数据类型。
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		// uri是数据类型，我们在这里可以进行判断。那么此例子暂时不研究
		mySQLiteOpenHelper.insert("tb_my", "name", values);
		return uri;
	}

	// 删除数据，在contentresolver调用delete方法时，就会执行这个方法
	// 第一个参数是描述数据类型，
	// 第二个参数是删除条件，第三个参数是删除条件的替换占位符
	// 一般而言，都是返回删除的这一条记录的主键（唯一性标识）
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int n = mySQLiteOpenHelper.delete("tb_my", selection, selectionArgs);
		return n;
	}

	// 更新数据，在contentresolver调用update方法时，就会执行这个方法
	// 第一个参数是描述数据类型
	// 第二个参数是键值对，用来描述更新数据的时候的字段和新值。
	// 第三个参数更新的条件，第四个参数是更新条件的替换占位符。
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int n = mySQLiteOpenHelper.update("tb_my", values, selection,
				selectionArgs);
		return n;
	}

}
