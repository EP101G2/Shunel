package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */

//public class NoticeFragment<layoutInflater> extends Fragment {
//
//    private static final String TAG = "TAG_NoticeFragment";
//    private Activity activity;
//    private RecyclerView rvNotice;
//    private RecyclerView rvNews;
//    private SearchView searchView;
//    private NoticeAdapter noticeAdapter;
//    private NewsAdapter newsAdapter;
//    private List<Notice> notice;
//    private List<News> news;
//    private CommonTask noticeGetAllTask;
//
////    private LayoutInflater layoutInflater;
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = new Activity();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_notice, container, false);
//
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        findViews(view);
//        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
//        initData();
//        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
//        setSystemServices();
//        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
//        setLinstener();
//    }
//
//    private void setLinstener() {
//        rvNotice.setAdapter(new NoticeAdapter(activity, noticeAdapter);
//        rvNotice.setAdapter(noticeAdapter);
//    }
//
//
//    private void findViews(View view) {
//
//        rvNews = view.findViewById(R.id.rvNews);
//        rvNews.setLayoutManager(new LinearLayoutManager(activity));
//
//        rvNotice = view.findViewById(R.id.rvNotice);
//        rvNotice.setLayoutManager(new LinearLayoutManager(activity));
//
//        searchView = view.findViewById(R.id.searchView);
//    }
//
//    private void initData() {
//
//
//        notice = getDate();
//
//
//    }
//
//    private List<Notice> getDate() {
//        List<Notice> notices = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "Notice_Servlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            noticeGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = noticeGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Notice>>() {
//                }.getType();
//                notices = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        return notices;
//    }
//
//
//    private void setSystemServices() {
//    }
//
//
//private class NoticeAdapter extends RecyclerView.Adapter {
//    //        layoutInflater =LayoutInflater.from(context);
//    Context context;
//    List<Product> noticeList;
//
//    public NoticeAdapter(Context context, List<Product> noticeList) {
//        this.context = context;
//        this.noticeList = noticeList;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    private class Myviewholder extends RecyclerView.ViewHolder {
//        TextView tvNoticeT;
//        TextView tvNoticeD;
//
//
//        public Myviewholder(View view) {
//            super(view);
//            tvNoticeT = view.findViewById(R.id.tvNoticeT);
//            tvNoticeD = view.findViewById(R.id.tvNoticeD);
//
//        }
//    }
//
//
//}
//
//    private class NewsAdapter {
//    }
//}

public class NoticeFragment extends Fragment {

    private static final String TAG = "TAG_NoticeFragment";
    private Activity activity;
    private RecyclerView rvNotice;
    private RecyclerView rvNews;
    private SearchView searchView;
    private NoticeAdapter noticeAdapter;
    private NewsAdapter newsAdapter;
//    private List<NoticeList> btNoticeList;
//    private List<NewsList> newsLists;


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

//        noticeAdapter = new NoticeAdapter(activity,noticeAdapter);






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
