package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


public class MemberFragment extends Fragment {

    private CardView cvLike, cvChat, cvOrderlist, cvHistory, cvSetting;
    private Activity activity;
    private boolean login=false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
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

//        View view1 =View.inflate(R.layout.fragment_login,null);



//        View mview = view.inflater.inflate(R.layout.fragment_login,null);
//        if (!login) {
//            Intent intent = new Intent(activity,LoginFragment.class);
//            startActivity(intent);
//        }




        cvLike = view.findViewById(R.id.cvLike);
        cvChat = view.findViewById(R.id.cvChat);
        cvOrderlist = view.findViewById(R.id.cvOrderlist);
        cvHistory = view.findViewById(R.id.cvHistory);
        cvSetting = view.findViewById(R.id.cvSetting);

        cvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_settingFragment2);
            }
        });



    }
}
