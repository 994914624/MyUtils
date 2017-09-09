package com.example.application;


import com.example.utils.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;

public class IdentifyCodeActivity extends Activity {
	
	private ContentResolver contentResolver=null;
	private EditText editText=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identitycode);
        editText=(EditText) findViewById(R.id.editText_identityCodeActivity);
        
        getIdentifyCode();
        
    }
    
    
    
    //自动获取验证码
	private void getIdentifyCode()
	{
		contentResolver=getContentResolver();
		Cursor cursor=contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
		CursorAdapter adapter=new CursorAdapter(getApplicationContext(),cursor,CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
		{
			@Override
			protected void onContentChanged()
			{
				Cursor cursor=contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
				cursor.moveToFirst();
				String address=cursor.getString(cursor.getColumnIndex("address"));
				String body=cursor.getString(cursor.getColumnIndex("body"));
				Log.i("-------", address);
				//如果来电号码是110,就给editText设置上验证码
				if(address.equals("110"))
				{
					body=body.substring(5,12);
					editText.setText(body);
				}
				super.onContentChanged();
			}
			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void bindView(View view, Context context, Cursor cursor)
			{
				// TODO Auto-generated method stub
				
			}
		};
		
	}


  
    
}
