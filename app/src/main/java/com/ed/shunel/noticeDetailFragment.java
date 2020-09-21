package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.Notice;
import com.ed.shunel.bean.Order_Detail;
import com.ed.shunel.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class noticeDetailFragment extends Fragment {
    private Activity activity;
    private TextView tvNoticeDetailT;
    private CommonTask noticeGetAllTask;
    private List<Notice> noticeDetail;

    private RecyclerView rvNoticeDetail;
    private NoticeDetailAdapter noticeDetailAdapter;


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
        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();
//        Bundle bundle = getArguments();

    }

    private void setLinstener() {
    }

    private void findViews(View view) {
        tvNoticeDetailT = view.findViewById(R.id.tvNoticeDetailT);
        rvNoticeDetail = view.findViewById(R.id.rvNoticeDetail);
        rvNoticeDetail.setLayoutManager(new LinearLayoutManager(activity));

        noticeDetail = getDate();
        rvNoticeDetail.setAdapter(new NoticeDetailAdapter(activity, noticeDetail));

    }

    private void initData() {


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
                String textQA = "貨態通知";
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

    private void setSystemServices() {

    }

    private List<Notice> getDate() {
        List<Notice> notices = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            JsonObject jsonObject = new JsonObject();
            switch (MainActivity.flag) {
                case 0:
                    jsonObject.addProperty("action", "getSaleAll");

                    break;

                case 1:
                    jsonObject.addProperty("action", "getGoodAll");
                    jsonObject.addProperty("account_ID", Common.getPreherences(activity).getString("id", ""));

                    break;

                case 2:
                    jsonObject.addProperty("action", "getSystemAll");

                    break;

            }
            String jsonOut = jsonObject.toString();
            noticeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = noticeGetAllTask.execute().get();
                Type listType = new TypeToken<List<Notice>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                參考Android web範例：jsonex
                notices = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return notices;
    }


    private class NoticeDetailAdapter extends RecyclerView.Adapter<NoticeDetailAdapter.MyViewHolder> {
        Context context;
        List<Notice> noticeDetail;
        private int imageSize;

        public NoticeDetailAdapter(Context context, List<Notice> noticeDetail) {
            this.context = context;
            this.noticeDetail = noticeDetail;
            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_view_notice, parent, false);
            return new MyViewHolder(view);
        }


        private Product getOrder_detail(int orderID) {
            Product product = new Product();
            if (Common.networkConnected(activity)) {
                String url = Common.URL_SERVER + "Notice_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrder_detail");
                jsonObject.addProperty("orderID", orderID);
                String jsonOut = jsonObject.toString();
                noticeGetAllTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = noticeGetAllTask.execute().get();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    product = gson.fromJson(jsonIn, Product.class);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
            return product;
        }


        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Notice notice = noticeDetail.get(position);
            Product product = getOrder_detail(notice.getCATEGORY_MESSAGE_ID());


            int notice_ID = notice.getNotice_ID();
            switch (MainActivity.flag) {
                case 0:
                    int productID = notice.getCATEGORY_MESSAGE_ID();
                    if (productID != 0) {
                        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
                        Bitmap bitmap = null;

                        String url = Common.URL_SERVER + "Prouct_Servlet";

                        try {
                            bitmap = new ImageTask(url, productID, imageSize, holder.ivProductMini).execute().get();
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        holder.ivProductMini.setImageBitmap(bitmap);
                    } else {
                        holder.ivProductMini.setImageResource(R.drawable.ic_action_tags);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            String saleTitle = notice.getNotice_Title().trim();
                            String saleDetail = notice.getNotice_Content().trim();
                            int product_ID = notice.getCATEGORY_MESSAGE_ID();
                            if (saleTitle.isEmpty() || saleDetail.isEmpty()) {
                                Common.showToast(activity, R.string.textnofound);
                            }
                            bundle.putString("noticeTitle", saleTitle);//title
                            bundle.putString("noticeDetail", saleDetail);
                            bundle.putInt("product_ID", product_ID);
                            Navigation.findNavController(view)
                                    .navigate(R.id.action_noticeDetailFragment_to_saleDetailFragment, bundle);

                        }
                    });
                    holder.tvNoticeT.setText(notice.getNotice_Title());
                    break;

                case 1:
                    int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
                    Bitmap bitmap = null;

                    String url = Common.URL_SERVER + "Prouct_Servlet";

                    try {
                        bitmap = new ImageTask(url, product.getProduct_ID(), imageSize, holder.ivProductMini).execute().get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }

                    holder.ivProductMini.setImageBitmap(bitmap);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavController navController = Navigation.findNavController(activity, R.id.fragment3);
                            navController.navigate(R.id.orderListFragment2);

                        }
                    });
                    holder.tvNoticeT.setText(notice.getNotice_Title() +"(訂單編號："+notice.getCATEGORY_MESSAGE_ID() + ")");
                    break;

                case 2:
                    holder.ivProductMini.setImageResource(R.drawable.ic_action_gear2);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            String systemTitle = notice.getNotice_Title().trim();
                            String systemDetail = notice.getNotice_Content().trim();
                            if (systemTitle.isEmpty() || systemDetail.isEmpty()) {
                                Common.showToast(activity, R.string.textnofound);
                            }
                            bundle.putString("noticeTitle", systemTitle);//title
                            bundle.putString("noticeDetail", systemDetail);
                            Navigation.findNavController(view)
                                    .navigate(R.id.action_noticeDetailFragment_to_systemDetailFragment, bundle);
                        }

                    });
                    holder.tvNoticeT.setText(notice.getNotice_Title());
                    break;
            }
//            String msg = String.valueOf(notice.getCATEGORY_MESSAGE_ID()).equals("") ? "" : product.getProduct_Name()+;

            String dateStr = notice.getNotice_time().toString();
            holder.tvNoticeD.setText(dateStr.substring(0, dateStr.length() - 5));


        }

        @Override
        public int getItemCount() {
            return noticeDetail == null ? 0 : noticeDetail.size();
        }


        private class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProductMini;
            TextView tvNoticeT;
            TextView tvNoticeD;


            public MyViewHolder(View view) {
                super(view);
                ivProductMini = view.findViewById(R.id.ivProductMini);
                tvNoticeT = view.findViewById(R.id.tvNoticeT);
                tvNoticeD = view.findViewById(R.id.tvNoticeD);
            }
        }

    }
}
