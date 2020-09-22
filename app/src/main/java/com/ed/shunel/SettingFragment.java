package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class SettingFragment extends Fragment {

    Activity activity;
    private LinearLayout btMInformation;
    private Button btSignOut;
    private Switch swPush;
    private int noticeStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Common.getPreherences()


        User_Account user_account = new User_Account();

        final int[] status = {user_account.getAccount_Notice_Status()};

        btMInformation = view.findViewById(R.id.btMInformation);
        btSignOut = view.findViewById(R.id.btSignOut);

        swPush = view.findViewById(R.id.swPush);



//        if (Common.networkConnected(activity)) {
//            String url1 = Common.URL_SERVER + "Prouct_Servlet";
//            Gson gson = new Gson();
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "findById");
//            if (product != null) {
//                jsonObject.addProperty("PRODUCT_Id", product.getProduct_ID());
//            } else {
//                jsonObject.addProperty("PRODUCT_Id", product_id);
//
//            }
//            String jsonOutSystem = jsonObject.toString();
//            addTask = new CommonTask(url1, jsonOutSystem);
//            try {
//                String jsonIn = addTask.execute().get();
//                productSale = gson.fromJson(jsonIn, Product.class);
//                Log.e(TAG, "-----------------------------------" + jsonIn);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//
//        }


        swPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    noticeStatus = 1;
                } else {
                    noticeStatus = 0;
                }


            }
        });

        btMInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_settingFragment_to_modifyFragment);

            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, MainActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
                startActivity(intent);  //啟動跳頁動作\
                Common.getPreherences(activity).edit().clear().apply();
//                activity.finish();//把自己關掉
            }
        });
    }


}
