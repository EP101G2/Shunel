package com.ed.shunel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.ed.shunel.Task.Common;
import com.ed.shunel.cache.MemoryCache;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //一開APP就連上聊天室
        CommonTwo.connectServer(this, loadUserName(this));
//        MainFragment fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
//        //步骤3:设置监听器，实现接口里面的方法
//        fragment.setCallBack(this);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        //建立bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment3);//推播的基底頁面設置
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        String isNotiFi = Common.getPreherences(this).getString("Notification", "N");
        Log.e("========isNotiFi", isNotiFi + "========");
        String pageFlag = Common.getPreherences(this).getString("pageFlag", "noFlag");


        //看帶過去的頁面要Bundle什麼，就要在這設置Bundle帶過去
        if (isNotiFi.equals("Y")) {
            Common.getPreherences(this).edit().putString("Notification", "N").apply();//設開關
            if (pageFlag.equals("0")) {
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
            }else if(pageFlag.equals("1")){
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
                Log.e("=====systemTitle=====",  systemTitle+ "=========");
                Log.e("saleTitle for bundle", bundle.getString("noticeTitle") + "systemTitle");
                Navigation.findNavController(this, R.id.fragment3)
                        .navigate(R.id.action_homeFragment_to_systemDetailFragment, bundle);
                Common.getPreherences(this).edit().remove("pageFlag").apply();
            }

        }
    }

    //    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        Fragment fragment = (Fragment) getChildFragmentManager().findFragmentByTag(childTag);
//        if (fragment != null) {
//            fragment.onActivityResult(requestCode, resultCode, intent);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        for (int index = 0; index < fragmentManager.getFragments().size(); index++) {
//            Fragment fragment = fragmentManager.getFragments().get(index); //找到第一层Fragment
//
//            if (fragment != null) {
//                handleResult(fragment, requestCode, resultCode, data);
//                return;
//            }
//
//            if (fragment == null) {
//                Log.w(TAG, "Activity result no fragment exists for index: 0x"
//                        + Integer.toHexString(requestCode));
//            } else {
//                handleResult(fragment, requestCode, resultCode, data);
//            }
//        }
//
//
//
//    }
//
//    /**
//     * 递归调用，对所有的子Fragment生效
//     *
//     * @param fragment
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    public void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
//        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
////        Log.e(TAG, "MyBaseFragmentActivity");
//        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
//        for (Fragment f : childFragment)
//            if (f != null) {
//                handleResult(f, requestCode, resultCode, data);
//            }
//    }
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

}
