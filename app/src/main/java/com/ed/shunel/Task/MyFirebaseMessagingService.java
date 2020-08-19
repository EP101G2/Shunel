package com.ed.shunel.Task;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;



import com.ed.shunel.MainActivity;
import com.ed.shunel.NotificationClickReceiver;
import com.ed.shunel.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("msg", "消息", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        //點擊通知後跳轉頁面
        Intent intent = new Intent(this, NotificationClickReceiver.class);//過中轉站到廣播器
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Preferenced", MODE_PRIVATE);//用廣播去攔截點擊事件
        preferences.edit().putString("noticeTitle", remoteMessage.getData().get("title")).apply(); //從偏好設定
        preferences.edit().putString("noticeDetail", remoteMessage.getData().get("msg")).apply();
        preferences.edit().putString("pageFlag", remoteMessage.getData().get("flag")).apply();
        final Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(); // 建立BigTextStyle
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title")); // 當BigTextStyle顯示時，用BigTextStyle的setBigContentTitle覆蓋setContentTitle的設定
        bigTextStyle.bigText(remoteMessage.getData().get("msg")); // 設定BigTextStyle的文字內容


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this, "msg")
                .setContentTitle(remoteMessage.getData().get("title"))//Data收到
                .setContentText(remoteMessage.getData().get("msg"))//Data收到
                .setWhen(System.currentTimeMillis())
                .setNumber(5)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setStyle(bigTextStyle)
                .build();
        wakeUpAndUnlock(this);

        int Notification_ID = 0;
        notificationManager.notify(Notification_ID, notification);
    }

    public void wakeUpAndUnlock(Context context) {
        //獲取電源管理者
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //開啟螢幕(10分鐘)
        wl.acquire(10 * 60 * 1000L /*10 minutes*/);
        //釋放資源
        wl.release();
    }

    @Override
    public void onNewToken(String token) {
        Log.e("---", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }
}
