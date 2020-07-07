package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment {

    private static final String TAG = "TAG_NoticeFragment";
    private Activity activity;
    private RecyclerView rvNotice;
    private RecyclerView rvNews;
    private SearchView searchView;
    private NoticeAdapter noticeAdapter;
    private NewsAdapter newsAdapter;
    private List<NoticeList> btNoticeList;
    private List<NewsList> newsLists;


    public NoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = new Activity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


    private void findViews(View view) {

        rvNews = view.findViewById(R.id.rvNews);
        rvNews.setLayoutManager(new LinearLayoutManager(activity));

        rvNotice = view.findViewById(R.id.rvNotice);
        rvNotice.setLayoutManager(new LinearLayoutManager(activity));

        searchView =view.findViewById(R.id.searchView);
    }

    private void initData() {
    }

    private void setSystemServices() {
    }

    private void setLinstener() {

        noticeAdapter = new NoticeAdapter(activity,noticeAdapter);






    }


    private class NoticeAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class NewsAdapter {
    }
}
