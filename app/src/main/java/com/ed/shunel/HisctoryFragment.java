package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.adapter.HictoryAdapter;
import com.ed.shunel.adapter.ProductAdapter_Sam;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class HisctoryFragment extends Fragment {
    Activity activity;
    RecyclerView recyclerView;
    CommonTask getHisctoryTask;
    private List<Product> hisctory;

    public HisctoryFragment() {
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


        return inflater.inflate(R.layout.fragment_hisctory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.flag = 12;
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new HictoryAdapter(getContext(),hisctory));
        hisctory = getHisctory();
        showhisctory(hisctory);
    }





    private List<Product> getHisctory() {
        List<Product> products = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getHisctoryProduct");
            jsonObject.addProperty("id", Common.getPreherences(activity).getString("id", "defValue"));
            getHisctoryTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = getHisctoryTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                products = new Gson().fromJson(jsonIn, listType);

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

    private void showhisctory(List<Product> product) {
        if (product == null || product.isEmpty()) {
            Common.showToast(activity, "沒有歷史紀錄");
        }
        HictoryAdapter productAdapter = (HictoryAdapter) recyclerView.getAdapter();
        if (productAdapter == null) {
            recyclerView.setAdapter(new HictoryAdapter(activity, product));
        } else {
            productAdapter.setProducts(product);
            productAdapter.notifyDataSetChanged();

        }
    }
}
