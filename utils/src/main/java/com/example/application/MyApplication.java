package com.example.application;

import java.io.File;

import com.example.myutils.MyAssets_dbUtils;
import com.example.myutils.MySDCardStorage_ReadWriteUtils;

import android.app.Application;
import android.os.Environment;

public class MyApplication extends Application {

	//写入到SD卡里数据库的名字命名为
	public static final String DB_NAME = "my.db";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		/**
		 * 把数据库，写入到SD卡的DIRECTORY_DOWNLOADS
		 * 
		 * 
		 *  别忘了在清单文件中，的application节点下声明：
		 *  android:name="com.example.application.MyApplication"
		 * 
		 * */

		String pathName = MySDCardStorage_ReadWriteUtils
				.getSDCardPublicDir(Environment.DIRECTORY_DOWNLOADS)
				+ File.separator + DB_NAME;
		File file = new File(pathName);
		if (!file.exists()) {
			//android_manual.db为Assets目录下的数据库的名字叫什么
			MyAssets_dbUtils.saveDataFromAssetsToSDCard(this, "android_manual.db",
					pathName);
		}

	}
}
