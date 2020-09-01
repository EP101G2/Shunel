package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Notice;
import com.ed.shunel.bean.Product_List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private Product productImageMini;
    private Notice saleLast, goodLast, systemLast;
    private CommonTask noticeGetAllTask;
    private CardView cdSystem, cdSale, cdQA;
    private TextView tvSaleT, tvSaleD, tvQAT, tvQAD, tvSystemT, tvSystemD;
    private String i = "";
    private int id;
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
        MainActivity.flag = 0;
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();

        findViews(view);


        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();
    }

    private void setLinstener() {
//        final Bundle bundle = new Bundle();

//        final String BundleTForSale = tvSaleT.getText().toString();
//        final String BundleTForQA = tvQAT.getText().toString();
//        final String BundleTForSystem = tvSystemT.getText().toString();


            cdSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.flag = 0;
//                Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_noticeDetailFragment,bundle);
                    Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_noticeDetailFragment);
//                bundle.putString("tvSaleT",BundleTForSale);
                }
            });

            cdQA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.flag = 1;
                    Log.e("flag","flag"+MainActivity.flag);
//                Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_noticeDetailFragment,bundle);
                    Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_noticeDetailFragment);
//                bundle.putString("tvQAT",BundleTForQA);
                }
            });

            cdSystem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.flag = 2;
//                Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_noticeDetailFragment,bundle);
                    Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_noticeDetailFragment);
//                bundle.putString("tvSystemT",BundleTForSystem);
                }
            });
        }




    private void findViews(View view) {
//        if (notice == null && notice.isEmpty()) {
//            Log.e("saleLast", "無資料" + saleLast);
//            Common.showToast(activity, R.string.textnofound);
//        } else {

        Log.e("goodLast", "有街道" + goodLast);
        rvNotice = view.findViewById(R.id.rvNotice);

        cdSystem = view.findViewById(R.id.cdSystem);
        cdQA = view.findViewById(R.id.cdQA);
        cdSale = view.findViewById(R.id.cdSale);
        searchView = view.findViewById(R.id.searchView);
        tvSaleT = view.findViewById(R.id.tvSaleT);
        tvSaleD = view.findViewById(R.id.tvSaleD);
        tvQAT = view.findViewById(R.id.tvQAT);
        tvQAD = view.findViewById(R.id.tvQAD);
        tvSystemT = view.findViewById(R.id.tvSystemT);
        tvSystemD = view.findViewById(R.id.tvSystemDetailD);
        tvSaleD.setText(saleLast.getNotice_Title());
        tvQAD.setText(goodLast.getNotice_Title());
        tvSystemD.setText(systemLast.getNotice_Title());
//        rvNotice.setLayoutManager(new LinearLayoutManager(activity));
        rvNotice.setLayoutManager(new MyLinearLayoutManager(activity,false));
        rvNotice.setAdapter(new NoticeAdapter(activity, notice));
//        }
    }

    private void initData() {
//        MainActivity.flag = 0;
//        for (MainActivity.flag = 0; MainActivity.flag <= 3; MainActivity.flag++) {

        notice = getDate();
        saleLast = getLastSaleN();
        goodLast = getLastGoodN();
        systemLast = getLastSystemN();


//            for (int i = 0 ; i<=notice.size() ; i++){
//                 int Notice_ID = ;
//                 String Notice_Content;
//                 String Notice_Title;
//                 Timestamp Notice_time;
//                 int NOTICE_CATEGORY_ID;
//                 int CATEGORY_MESSAGE_ID;
//                Notice_ID = notice_ID;
//                Notice_Content = notice_Content;
//                Notice_Title = notice_Title;
//                Notice_time = notice_time;
//                this.NOTICE_CATEGORY_ID = NOTICE_CATEGORY_ID;
//                this.CATEGORY_MESSAGE_ID = CATEGORY_MESSAGE_ID;
//            }
//        }
    }

    private Notice getLastSystemN() {
        systemLast = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getLastSystemN");
            String jsonOutSystem = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOutSystem);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
                systemLast = gson.fromJson(jsonIn, Notice.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return systemLast;
    }

    private Notice getLastGoodN() {
        Notice qaLast = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getLastGoodN");
            jsonObject.addProperty("account_ID", Common.getPreherences(activity).getString("id", ""));
            String jsonOutGoods = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOutGoods);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
                qaLast = gson.fromJson(jsonIn, Notice.class);
                Log.d(TAG, qaLast.getNotice_Content() + "-------------------");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return qaLast;
    }

    private Notice getLastSaleN() {
        Notice saleLast = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
//            Gson gson = new Gson();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getLastSaleN");
//            jsonObject.addProperty("action", "getLastQAN");
//                    jsonObject.addProperty("action", "getLastSystemN");
            String jsonOutSale = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOutSale);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
//                JsonArray jsonArray = gson.fromJson(jsonIn, JsonArray.class);
//                Log.d(TAG,"234234234234234234234");
                saleLast = gson.fromJson(jsonIn, Notice.class);
//                JsonObject jsonObject2 = gson.fromJson(jsonIn, JsonObject.class);
//                Log.d(TAG,jsonObject2+"-------------------");

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
//        Log.i(TAG,notceLast.getNotice_Content());
        return saleLast;
    }

    private List<Notice> getDate() {
        List<Notice> notices = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getNoticeAll");
            jsonObject.addProperty("account_ID", Common.getPreherences(activity).getString("id", ""));
            String jsonOut = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
                Type listType = new TypeToken<List<Notice>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                //MMM 是英文月份縮寫，7月是Jul
                //a = AM/PM
                notices = gson.fromJson(jsonIn, listType);
                Log.e(TAG, String.valueOf(notices.get(0).getPRODUCT_STATUS()));
                Log.e(TAG, String.valueOf(notices.get(0).getPrice()));

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {

            Common.showToast(activity, R.string.textNoNetwork);
        }
        return notices;
    }





    public class MyLinearLayoutManager extends LinearLayoutManager {
        private  final String TAG  = MyLinearLayoutManager.class.getSimpleName();

        private boolean isScrollEnabled = true;

        public MyLinearLayoutManager(Context context, boolean isScrollEnabled) {
            super(context);
            this.isScrollEnabled = isScrollEnabled;
        }

        public MyLinearLayoutManager(Context context,int orientation,boolean reverseLayout){
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollVertically() {
            //設定是否禁止滑動
            return isScrollEnabled && super.canScrollVertically();
        }
    }


    private class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Myviewholder> {
        //        layoutInflater =LayoutInflater.from(context);
        Context context;
        List<Notice> noticeList;
        List<Product> productList;
        private ImageTask productimageTaskForN;
        private int imageSize;

        public NoticeAdapter(Context context, List<Notice> noticeList) {
            this.context = context;
            this.noticeList = noticeList;
            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
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


            int notice_ID = notice.getNotice_ID();
            holder.tvNoticeT.setText(notice.getNotice_Content());
            String dateStr = notice.getNotice_time().toString();
//            holder.tvNoticeD.setText(notice.getNotice_time().toString());
            holder.tvNoticeD.setText(dateStr.substring(0,dateStr.length()-5));

            int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
            Bitmap bitmap = null;
//        int price = promotionProduct.getPromotion_Price();

            String url = Common.URL_SERVER + "Prouct_Servlet";
            id = notice.getCATEGORY_MESSAGE_ID();
            try {
                bitmap = new ImageTask(url, id, imageSize,holder.ivProductMini).execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    int product_ID = notice.getCATEGORY_MESSAGE_ID();
                    bundle.putInt("product_ID", product_ID);
                    bundle.putInt("number", 1);
                    bundle.putSerializable("notice", notice);
                    Navigation.findNavController(v).navigate(R.id.action_noticeFragment_to_productDetailFragment, bundle);


                }
            });

        }



        @Override
        public int getItemCount() {
            return noticeList == null ? 0 : noticeList.size();
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