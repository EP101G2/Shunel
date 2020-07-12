package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.Notice;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class NoticeFragment<layoutInflater> extends Fragment {

    private static final String TAG = "TAG_NoticeFragment";
    private Activity activity;
    private RecyclerView rvNotice;
    private SearchView searchView;
    private NoticeAdapter noticeAdapter;
    private List<Notice> notice;
    private CommonTask noticeGetAllTask;
    private CardView cdSystem;

//    private LayoutInflater layoutInflater;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();
        // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_notice, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();
    }

    private void setLinstener() {

        cdSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    private void findViews(View view) {

        rvNotice = view.findViewById(R.id.rvNotice);
        rvNotice.setLayoutManager(new LinearLayoutManager(activity));
        cdSystem = view.findViewById(R.id.cdSystem);
        searchView = view.findViewById(R.id.searchView);
        rvNotice.setAdapter(new NoticeAdapter(activity, notice));
        rvNotice.setAdapter(noticeAdapter);
    }

    private void initData() {


        notice = getDate();


    }

    private List<Notice> getDate() {
        List<Notice> notices = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
                Type listType = new TypeToken<List<Notice>>() {
                }.getType();
                notices = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return notices;
    }

    private void setSystemServices() {
    }


    private class NoticeAdapter extends RecyclerView.Adapter <NoticeAdapter.Myviewholder>{
        //        layoutInflater =LayoutInflater.from(context);
        Context context;
        List<Notice> noticeList;

        public NoticeAdapter(Context context, List<Notice> noticeList) {
            this.context = context;
            this.noticeList = noticeList;
        }

        @NonNull
        @Override
        public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_view_notice, parent, false);
            return new Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
            final Notice notice = noticeList.get(position);
            String url = Common.URL_SERVER + "Notice_Servlet";
            int notice_ID = notice.getNotice_ID();
            holder.tvNoticeT.setText(notice.getNotice_Title());
            holder.tvNoticeD.setText(notice.getNotice_Content());
        }

        @Override
        public int getItemCount() {
            return notice == null ? 0 : notice.size();
        }

        private class Myviewholder extends RecyclerView.ViewHolder {
            ImageView ivProductMini;
            TextView tvNoticeT;
            TextView tvNoticeD;


            public Myviewholder(View view) {
                super(view);
                ivProductMini = view.findViewById(R.id.ivProductMini);
                tvNoticeT = view.findViewById(R.id.tvNoticeT);
                tvNoticeD = view.findViewById(R.id.tvNoticeD);

            }
        }


    }

}