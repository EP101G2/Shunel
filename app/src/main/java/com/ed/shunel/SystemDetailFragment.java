package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.bean.Notice;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SystemDetailFragment extends Fragment {
    private Activity activity;
    private Notice notice;
    private TextView tvSystemDetailT, tvSystemDetailD;
    private String systemTitle,systemDetail;

    public SystemDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Common.getPreherences(activity).edit().remove("noticeTitle").apply();
        Common.getPreherences(activity).edit().remove("noticeDetail").apply();
        return inflater.inflate(R.layout.fragment_system_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.flag = 2;

        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        findViews(view);
    }

    private void findViews(View view) {
        tvSystemDetailT = view.findViewById(R.id.tvSystenDetailT);
        tvSystemDetailD = view.findViewById(R.id.tvSystemDetailD);
        tvSystemDetailT.setText(systemTitle);
        tvSystemDetailD.setText(systemDetail);

    }

    private void initData() {
//        notice = (Notice) (getArguments() != null ? getArguments().getSerializable("noticeSystem") : null);
        Bundle bundle = getArguments();
        if (bundle != null) {
            systemTitle = bundle.getString("noticeTitle");
            systemDetail = bundle.getString("noticeDetail");
        }
    }
}
