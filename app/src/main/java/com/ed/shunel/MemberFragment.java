package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static android.content.ContentValues.TAG;


public class MemberFragment extends Fragment {

    private CardView cvLike, cvChat, cvOrderlist, cvHistory, cvSetting;
    private Activity activity;
    private View view;
    private Button btn_Logout;
    private boolean login = false;
    private SharedPreferences sharedPreferences;
    private TextView tvId, tv_Name;
    private CommonTask memberTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
//        final NavController navController = Navigation.findNavController(view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//

        sharedPreferences = Common.getPreherences(activity);
        //置入name屬性的字串


        Log.i(TAG, "-------------------------MemberFragment------------------------------------");
        Log.i(TAG, sharedPreferences.getString("id", ""));

        cvLike = view.findViewById(R.id.cvLike);
        cvChat = view.findViewById(R.id.cvChat);
        cvOrderlist = view.findViewById(R.id.cvOrderlist);
        cvHistory = view.findViewById(R.id.cvHistory);
        cvSetting = view.findViewById(R.id.cvSetting);
        btn_Logout = view.findViewById(R.id.btn_Logout);
        tvId = view.findViewById(R.id.tvId);
        tv_Name = view.findViewById(R.id.tv_Name);




        if (sharedPreferences.getString("id", "").equals("")) {
//            Intent intent=new Intent();
//            intent.setClass(getActivity(),LoginFragment.class);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
            startActivity(intent);
            activity.finish();//把自己關掉

        }

        cvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.likeDetail_SamFragment);
            }
        });


        cvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_settingFragment2);
            }
        });


        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
                LayoutInflater inflater = LayoutInflater.from(activity);
                final View view = inflater.inflate(R.layout.logoutsuccess, null);
                builder.setView(view);
                builder.create().show();
                Logout();
            }
        });
        cvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.chatFragment);
            }
        });
        cvOrderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_orderListFragment2);
            }
        });

        String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getLogin");
        jsonObject.addProperty("id", Common.getPreherences(activity).getString("id", ""));
        jsonObject.addProperty("password", Common.getPreherences(activity).getString("password", ""));
        Log.e("ID_PAS", Common.getPreherences(activity).getString("id", ""));
        memberTask = new CommonTask(url, jsonObject.toString());
        String jsonIn = "";
        try {
            jsonIn = memberTask.execute().get();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        JsonObject jsonObject2 = gson.fromJson(jsonIn, JsonObject.class);
        String result = jsonObject2.get("user").getAsString();
        User_Account user_account = gson.fromJson(result, User_Account.class);
        tv_Name.setText(user_account.getAccount_User_Name());
        tvId.setText(user_account.getAccount_ID());

        Log.e("TAG", "123");
    }

    private void Logout() {
        Common.getPreherences(activity).edit().clear().apply();
//        MainActivity.preferences.edit().clear().apply();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
        startActivity(intent);
//        if (MainActivity.preferences.edit())


    }
}
