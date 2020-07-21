package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.adapter.ProductAdapter_Sam;
import com.ed.shunel.adapter.SearchAdapter_Sam;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageButton ibBack;
    private SearchView searchView;
    private List<Product> product;
    private CommonTask productGetAllTask;
    Activity activity;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchview);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        product = getProduct();
        showBooks(product);
        recyclerView.setAdapter(new SearchAdapter_Sam(getContext(),product));



        ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).popBackStack();

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                    if (newText.isEmpty()) {   //沒有輸入東西 就是輸出所有的東西
                        showBooks(product);
                    } else {
                        List<Product> products = new ArrayList<>();
                        // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                        for (Product product : product) {
                            if (product.getProduct_Name().toUpperCase().contains(newText.toUpperCase())) {   //toUpperCase()全部轉成大寫 就可以達到不分大小寫
                                //contains這個是一個比對的方法
                                //再由傳入的值(newText) 改為全大寫 與全部的好友資訊做比對
                                products.add(product);  //把達到條件的 加入  products
                            }
                        }
                        showBooks(products);
                    }

                return true;
            }
        });





    }


    private List<Product> getProduct() {
        List<Product> products = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getSaleProduct");
            productGetAllTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = productGetAllTask.execute().get();
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

    private void showBooks(List<Product> product) {
        if (product == null || product.isEmpty()) {
            Common.showToast(activity, R.string.textnofound);
        }
        SearchAdapter_Sam productAdapter = ( SearchAdapter_Sam) recyclerView.getAdapter();
        if (productAdapter == null) {
            recyclerView.setAdapter(new ProductAdapter_Sam(activity, product));
        } else {
            productAdapter.setProducts(product);
            productAdapter.notifyDataSetChanged();

        }
    }



}
