package com.ed.shunel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.ed.shunel.cache.MemoryCache;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    public static int flag = 0;
    public static MemoryCache memoryCache = new MemoryCache();
    private final static String TAG = "MainActivity";
    private int requestCode;
    private int resultCode;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MainFragment fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
//        //步骤3:设置监听器，实现接口里面的方法
//        fragment.setCallBack(this);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        //建立bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment3);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
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
