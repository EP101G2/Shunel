package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class noticeDetailFragment extends Fragment {
    private Activity activity;
    private TextView tvNoticeDetailT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNoticeDetailT = view.findViewById(R.id.tvNoticeDetailT);
        Bundle bundle = getArguments();
        switch (MainActivity.flag) {
            case 0:
//                if (bundle != null) {
//                    String BundleTForSale = bundle.getString("tvSaleT");
                String textSale = "促銷訊息";
                tvNoticeDetailT.append(textSale);
//                }
                break;

            case 1:
//                if (bundle != null) {
//                    String BundleTForQA = bundle.getString("tvQAT");
                String textQA = "提問通知";
                tvNoticeDetailT.append(textQA);
//                }
                break;

            case 2:
//                if (bundle != null) {
//                    String BundleTForSystem = bundle.getString("tvSystemT");
                String textSystem = "系統公告";
                tvNoticeDetailT.append(textSystem);
//                }
                break;
        }
    }
}
