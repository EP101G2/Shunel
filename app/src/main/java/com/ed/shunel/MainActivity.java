package com.ed.shunel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.ed.shunel.cache.MemoryCache;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    public static int flag = 0;
    public static MemoryCache memoryCache = new MemoryCache();


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


}
