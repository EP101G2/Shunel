package com.ed.shunel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.ed.shunel.Task.Common;
import com.ed.shunel.bean.ChatMessage;
import com.ed.shunel.cache.MemoryCache;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

import static com.ed.shunel.CommonTwo.loadUserName;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    public static int flag;
    public static MemoryCache memoryCache = new MemoryCache();
    private final static String TAG = "MainActivity";
    private int requestCode;
    private int resultCode;
    private Intent data;
    private SharedPreferences preferences;
    //廣播接收
    private LocalBroadcastManager broadcastManager;
    //通知訊息
    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_ID = "Channel01";
    private NotificationManager notificationManager;
    private String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id= Common.getPreherences(this).getString("id","");
        /* jack-------------------------------------------------------------------------------------------*/
        //一開APP就連上聊天室
        if(id==null){
            Log.e(TAG,"1id"+id);
            return;
        }else {
            Log.e(TAG,"2id"+id);
            CommonTwo.connectServer(this, id);
        }


        //通知
        notificationManager = (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);



        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
        registerChatReceiver();

        /* jack-------------------------------------------------------------------------------------------*/

        //建立bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment3);//推播的基底頁面設置
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        String isNotiFi = Common.getPreherences(this).getString("Notification", "N");
        Log.e("========isNotiFi", isNotiFi + "========");
        Log.e("========isNotiFi", isNotiFi + "========");
        String pageFlag = Common.getPreherences(this).getString("pageFlag", "noFlag");


        //看帶過去的頁面要Bundle什麼，就要在這設置Bundle帶過去
        if (isNotiFi.equals("Y")) {
            Common.getPreherences(this).edit().putString("Notification", "N").apply();//設開關
            if (pageFlag.equals("0")) {
                Log.e("1010101010101010","0101010101010");
                String saleTitle = Common.getPreherences(this).getString("noticeTitle", "saleTitle");
                String saleDetail = Common.getPreherences(this).getString("noticeDetail", "saleDetail");
                Bundle bundle = new Bundle();
                bundle.putString("noticeTitle", saleTitle);
                bundle.putString("noticeDetail", saleDetail);
                Log.e("=====saleTitle=====", saleTitle + "=========");
                Log.e("saleTitle=====", bundle.getString("noticeTitle") + "saleTitle");
                Navigation.findNavController(this, R.id.fragment3)
                        .navigate(R.id.action_homeFragment_to_saleDetailFragment, bundle);
                Common.getPreherences(this).edit().remove("pageFlag").apply();//移除偏好設定中的flag
            } else if (pageFlag.equals("1")) {
                String orderTitle = Common.getPreherences(this).getString("noticeTitle", "orderTitle");
                String orderDetail = Common.getPreherences(this).getString("noticeDetail", "orderDetail");
                Bundle bundle = new Bundle();
                bundle.putString("noticeTitle", orderTitle);
                bundle.putString("noticeDetail", orderDetail);
                Log.e("=====saleTitle=====", orderTitle + "=========");
                Log.e("saleTitle=====", bundle.getString("noticeTitle") + "saleTitle");
                Navigation.findNavController(this, R.id.fragment3)
                        .navigate(R.id.action_homeFragment_to_orderListFragment2, bundle);
                Common.getPreherences(this).edit().remove("pageFlag").apply();
            } else if (pageFlag.equals("2")) {
                String systemTitle = Common.getPreherences(this).getString("noticeTitle", "systemTitle");
                String systemDetail = Common.getPreherences(this).getString("noticeDetail", "systemDetail");
                Bundle bundle = new Bundle();
                bundle.putString("noticeTitle", systemTitle);
                bundle.putString("noticeDetail", systemDetail);
                Log.e("=====systemTitle=====", systemTitle + "=========");
                Log.e("saleTitle for bundle", bundle.getString("noticeTitle") + "systemTitle");
                Navigation.findNavController(this, R.id.fragment3)
                        .navigate(R.id.action_homeFragment_to_systemDetailFragment, bundle);
                Common.getPreherences(this).edit().remove("pageFlag").apply();
            }else if(pageFlag.equals(3)){
                String likeTitle = Common.getPreherences(this).getString("noticeTitle", "likeTitle");
                String likeDetail = Common.getPreherences(this).getString("noticeDetail", "likeDetail");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int index = 0; index < fragmentManager.getFragments().size(); index++) {
            Fragment fragment = fragmentManager.getFragments().get(index); //找到第一层Fragment

            if (fragment != null && fragment instanceof BuyerFragment) {
                handleResult(fragment, requestCode, resultCode, data);
                return;
            }

            if (fragment == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            } else {
                handleResult(fragment, requestCode, resultCode, data);
            }
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
//        Log.e(TAG, "MyBaseFragmentActivity");
        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
        if (childFragment != null)
            for (Fragment f : childFragment)
                if (f != null) {
                    handleResult(f, requestCode, resultCode, data);
                }
        if (childFragment == null)
            Log.e(TAG, "MyBaseFragmentActivity1111");
    }


/*JACK 生命週期 接收消息------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onPause() {
        super.onPause();


        }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"測試資料"+id+"==============");
        if (id==null){
            broadcastManager.unregisterReceiver(chatReceiver);
        }else {
            Log.e(TAG, "31");
                CommonTwo.connectServer(this, loadUserName(this));
                // 初始化LocalBroadcastManager並註冊BroadcastReceiver
                broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
                registerChatReceiver();

        }



        Log.e(TAG, "3");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"測試資料"+id+"==============");
        if (id==null){
            broadcastManager.unregisterReceiver(chatReceiver);
        }else {
            Log.e(TAG, "31");
            CommonTwo.connectServer(this, loadUserName(this));
            // 初始化LocalBroadcastManager並註冊BroadcastReceiver
            broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
            registerChatReceiver();

        }


    }



    /*Jack 廣播接收*/
    /**
     * 註冊廣播接收器攔截聊天資訊
     * 因為是在Fragment註冊，所以Fragment頁面未開時不會攔截廣播
     */
    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
    }

    // 接收到聊天訊息會在TextView呈現
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);

            chatMessage.setFlag(1);
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 重要性越高，提示(打擾)user方式就越明確，設為IMPORTANCE_HIGH會懸浮通知並發出聲音
                NotificationChannel notificationChannel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "MyNotificationChannel",
                        NotificationManager.IMPORTANCE_HIGH);
                // 如果裝置有支援，開啟指示燈
                notificationChannel.enableLights(true);
                // 設定指示燈顏色
                notificationChannel.setLightColor(Color.RED);
                // 開啟震動
                notificationChannel.enableVibration(true);
                // 設定震動頻率
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationManager.createNotificationChannel(notificationChannel);
            }


            Notification notification = new NotificationCompat.Builder(MainActivity.this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_email)
                    .setContentTitle(chatMessage.getSender())
                    .setContentText(chatMessage.getMessage())
                    .setAutoCancel(true)
//                .setContentIntent(pendingIntent) // 若無開啟頁面可不寫
                    .build();
            notificationManager.notify(NOTIFICATION_ID, notification);


            Log.d("=============", message);
        }
    };
    /*JACK------------------------------------------------------------------------------------------------------------------*/
}
