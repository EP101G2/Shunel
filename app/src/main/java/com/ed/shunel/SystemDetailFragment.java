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

import com.ed.shunel.bean.Notice;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class SystemDetailFragment extends Fragment {
    private Activity activity;
    private Notice notice;
    private TextView tvSystemDetailT,tvSystemDetailD;

    public SystemDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_system_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.flag = 0;
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();


        findViews(view);

    }

    private void findViews(View view) {
        tvSystemDetailT = view.findViewById(R.id.tvSystenDetailT);
        tvSystemDetailD = view.findViewById(R.id.tvSystemDetailD);
        tvSystemDetailT.setText(notice.getNotice_Title());
        tvSystemDetailD.setText(notice.getNotice_Content());

    }

    private void initData() {
        notice= (Notice) (getArguments() != null ? getArguments().getSerializable("notice") : null);
    }
}
