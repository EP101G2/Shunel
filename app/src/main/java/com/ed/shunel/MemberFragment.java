package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class MemberFragment extends Fragment {

    private CardView cvLike, cvChat, cvOrderlist, cvHistory, cvSetting;
    private Activity activity;
    private View view;
    private Button btn_Logout;
    private boolean login = false;


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


        cvLike = view.findViewById(R.id.cvLike);
        cvChat = view.findViewById(R.id.cvChat);
        cvOrderlist = view.findViewById(R.id.cvOrderlist);
        cvHistory = view.findViewById(R.id.cvHistory);
        cvSetting = view.findViewById(R.id.cvSetting);
        btn_Logout=view.findViewById(R.id.btn_Logout);

        Log.e("TAG", "123");
        if (MainActivity.preferences.getString("id", "").equals("")) {
//            Intent intent=new Intent();
//            intent.setClass(getActivity(),LoginFragment.class);
//            startActivity(intent);
            Intent intent= new Intent();
            intent.setClass(activity,LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
            startActivity(intent);
        }

        cvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_settingFragment2);
            }
        });

        
        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
        

    }

    private void Logout() {



        MainActivity.preferences.edit().clear().apply();

//        if (MainActivity.preferences.edit())


    }
}
