package com.example.myservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


//@SuppressWarnings("NewApi")
public class MyNotificationListenerService extends NotificationListenerService {

	/**
	 * 监视通知消息，判断是都包涵[微信红包]字样，如果有就打开这条消息
	 * 
	 * 要打开这各设置允许监听，startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
	 * 清单文件注册这个服务
	 */
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (null != notification) {
            Bundle extras = notification.extras;
            if (null != extras) {
                List<String> textList = new ArrayList<String>();
                String title = extras.getString("android.title");
                
                Log.i("-------------------", "----------000---------"+title);
                if (!TextUtils.isEmpty(title)) textList.add(title);

                String detailText = extras.getString("android.text");
                if (!TextUtils.isEmpty(detailText)) textList.add(detailText);
                Log.i("-------------------", "----------111---------"+detailText);
                if (textList.size() > 0) {
                    for (String text : textList) {
                        if (!TextUtils.isEmpty(text) && text.contains("[微信红包]")) {
                        	
                        	Log.i("-------------------", "---------------2222----"+text);
                            final PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
