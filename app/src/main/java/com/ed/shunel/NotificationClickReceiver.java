package com.ed.shunel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class NotificationClickReceiver extends BroadcastReceiver {//推播接廣播器的中介站

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("Preferenced",Context.MODE_PRIVATE);//注意偏好設定檔的key
        preferences.edit().putString("Notification","Y").apply();//把Notification的開關"Y"給打開
        Log.e("Click","廣播");
        Intent newIntent = new Intent(context,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//從class跳至MainActivity必須
        context.startActivity(newIntent);
    }
}
