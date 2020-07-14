package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ModifyInfoFragment extends Fragment {

    private Activity activity;
    private TextView tvId, tvName, tvAddress, tv_Phone;
    private Button btn_Modify;

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


    }
}
