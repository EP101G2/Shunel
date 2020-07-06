package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment {
    private Activity activity;
    private ImageView iv_Prouduct;
    private ImageView iv_Like;
    private ImageView iv_Shoppcard;
    private TextView tv_Buy;
    private TextView tv_Dital;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private Spinner sp_Color;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
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

        iv_Prouduct=view.findViewById(R.id.iv_Prouct);
        iv_Like=view.findViewById(R.id.iv_Like);
        iv_Shoppcard=view.findViewById(R.id.iv_Shoppcard);
        tv_Buy=view.findViewById(R.id.tv_Buy);
        tv_Dital=view.findViewById(R.id.tv_Dital);
        tvProductName=view.findViewById(R.id.tvProductName);
        tvProductPrice=view.findViewById(R.id.tvProductPrice);
        sp_Color=view.findViewById(R.id.sp_Color);



    }

    private void initData() {
    }

    private void setSystemServices() {
    }

    private void setLinstener() {



        iv_Shoppcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }
}
