package com.example.myutils02;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 *
 * 获得手机唯一标识符
 *
 */
public class UUID_Util {


    public static String getMyUUID(Activity activity){

        final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;

        tmDevice = "" + tm.getDeviceId();//标识手机设备的串号

        tmSerial = "" + tm.getSimSerialNumber();//SIM卡的


        //16进制的字符串就是ANDROID_ID
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        //UUID通用唯一识别码 (Universally Unique Identifier)
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

        String uniqueId = deviceUuid.toString();


        return uniqueId;

    }
}
