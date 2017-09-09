package com.example.myutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class MyAssets_dbUtils {
	
	

	/**
	 * 
	 * 把Assets文件夹下的，数据库文件循环写入（避免OOM）到ＳＤ卡内，pathName是指定文件夹
	 * 
	 * （在MyApplication的 onCreate 的方法里调用这个方法来写入）
	 * 别忘了在清单文件中，的application节点下声明：自己写的MyApplication。
	   android:name="com.example.application.MyApplication"
	 * */
	public static void saveDataFromAssetsToSDCard(Context context,
			String fileName, String pathName) {
		FileOutputStream os = null;
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			File file = new File(pathName);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 这个boolean为true是续写的意思，如果它为false，是覆盖的意思
			os = new FileOutputStream(file, true);
			int c = 0;
			byte[] buffer = new byte[1024];
			while ((c = in.read(buffer)) != -1) {
				os.write(buffer, 0, c);
				os.flush();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	

}
