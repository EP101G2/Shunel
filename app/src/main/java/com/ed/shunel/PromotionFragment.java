package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.adapter.ProductAdapter_Sam;
import com.ed.shunel.adapter.PromotionAdapter_Sam;
import com.ed.shunel.bean.Promotion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionFragment extends Fragment {

    private Activity activity;
    private RecyclerView recyclerView;
    private CommonTask productGetAllTask;
    private List<Promotion> promotionProduct;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final static String TAG = "TAG_SpotInsertFragment";

    public PromotionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_all_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.flag = 9 ;
        recyclerView = view.findViewById(R.id.recyclerView);



        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(new PromotionAdapter_Sam(getContext(), promotionProduct));
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        promotionProduct = getProduct();
        showBooks(promotionProduct);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                swipeRefreshLayout.setRefreshing(true);
                showBooks(getProduct());
                //直到讀取完 結束
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private List<Promotion> getProduct() {
        List<Promotion> products = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Promotion_Servlet";
            //String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPromotionAll");
            //jsonObject.addProperty("action", "getTOP5Product");
            productGetAllTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = productGetAllTask.execute().get();
                Type listType = new TypeToken<List<Promotion>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                products = gson.fromJson(jsonIn, listType);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("--------------",products+"");
        return products;
    }

    private void showBooks(List<Promotion> promotionProduct) {
        if (promotionProduct == null || promotionProduct.isEmpty()) {
            Common.showToast(activity, R.string.textnofound);
        }
        PromotionAdapter_Sam productAdapter = (PromotionAdapter_Sam) recyclerView.getAdapter();
        if (productAdapter == null) {
            recyclerView.setAdapter(new PromotionAdapter_Sam(activity, promotionProduct));
        } else {
            productAdapter.setpromotionProducts(promotionProduct);
            productAdapter.notifyDataSetChanged();

        }
    }
}
