package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.google.gson.JsonObject;


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
    private TextView tvPdName;
    private TextView tvPdPrice;
    private Spinner sp_Color;
    private Product product;
    private CommonTask addTask;


    public ProductDetailFragment() {
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

        iv_Prouduct = view.findViewById(R.id.iv_Prouct);
        iv_Like = view.findViewById(R.id.iv_Like);
        iv_Shoppcard = view.findViewById(R.id.iv_Shoppcard);
        tv_Buy = view.findViewById(R.id.tv_Buy);
        tv_Dital = view.findViewById(R.id.tv_Dital);
        tvPdName = view.findViewById(R.id.tvPdName);
        tvPdPrice = view.findViewById(R.id.tvPdPrice);
        sp_Color = view.findViewById(R.id.sp_Color);


        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("product") == null) {
            Common.showToast(activity, R.string.textNoFound);
            navController.popBackStack();
            return;
        }


        product = (Product) bundle.getSerializable("product");

        tvPdName.setText(product.getProduct_Name());
        tvPdPrice.setText(String.valueOf(product.getProduct_Price()));
        tv_Dital.setText(product.getProduct_Ditail());
//

    }

    private void initData() {


    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        iv_Shoppcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            Product product =
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Prouct_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "addShop");
                    jsonObject.addProperty("product_ID", product.getProduct_ID());

                    int count = 0;

                    try {
                        addTask = new CommonTask(url, jsonObject.toString());
                        String result = addTask.execute().get();
                        count = Integer.parseInt(result);
                        Toast.makeText(activity,result,Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (count == 0){
                        Toast.makeText(activity,R.string.Fail,Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


    }
}
