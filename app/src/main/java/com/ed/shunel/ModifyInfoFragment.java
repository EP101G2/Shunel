package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class ModifyInfoFragment extends Fragment {
    private final static String TAG = "TAG_LoginFragment";
    private Activity activity;
    private TextView tvId, tvName, tvAddress, tv_Phone;
    private Button btn_Modify;
    private String id,name,address,phone;
    private CommonTask infoTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    tvId=view.findViewById(R.id.tvId);
    tvName=view.findViewById(R.id.tvName);
    tvAddress=view.findViewById(R.id.tvAddress);
    tv_Phone=view.findViewById(R.id.tv_Phone);
    btn_Modify=view.findViewById(R.id.btn_Modify);


        String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getLogin");
        jsonObject.addProperty("id", Common.getPreherences(activity).getString("id","id"));
        jsonObject.addProperty("password", Common.getPreherences(activity).getString("password","password"));
        infoTask = new CommonTask(url, jsonObject.toString());
        String jsonIn = "";
        try {
            jsonIn = infoTask.execute().get();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        JsonObject jsonObject2 = gson.fromJson(jsonIn, JsonObject.class);
        String result = jsonObject2.get("user").getAsString();
        User_Account user_account = gson.fromJson(result, User_Account.class);
        tvName.setText(user_account.getAccount_User_Name());
        tvAddress.setText(user_account.getAccount_Address());
        tv_Phone.setText(user_account.getAccount_Phone());
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", user_account);
        Log.i(TAG, result);



    Common.getPreherences(activity).edit().apply();


    btn_Modify.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.action_modifyInfoFragment_to_modifyNameFragment);
        }
    });
    }
}
