package com.ed.shunel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.bean.Shopping_Cart;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerFragment extends Fragment {

    private final static String TAG = "BuyerFragment";
    private Activity activity;
    private List<Product> productList;


    /*UI元件*/
    private RecyclerView rv_Product;
    private RadioButton radioButton_Default_Address;
    private RadioButton radioButton_New_Address;
    private TextView tv_Buyer_Name;
    private TextView tv_Buyer_Phone;
    private TextView tv_Buyer_Address;
    private EditText et_Name;
    private EditText et_Phone;
    private EditText et_Address;
    private Button btn_Buyer_Confirm;
    private Button btn_Pagenext;
    private LinearLayout line_Name;
    private LinearLayout line_Phone;
    private LinearLayout line_Address;

    private List<Shopping_Cart> listdatas;
    private Shopping_Cart_List shopping_cart_list;
    public BuyerFragment() {
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
        return inflater.inflate(R.layout.fragment_buyer, container, false);
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

        rv_Product = view.findViewById(R.id.rv_Product);

        tv_Buyer_Name = view.findViewById(R.id.tv_Buyer_Name);
        tv_Buyer_Phone = view.findViewById(R.id.tv_Phone);
        tv_Buyer_Address = view.findViewById(R.id.tv_Address);

        btn_Buyer_Confirm = view.findViewById(R.id.btn_Buyer_Confirm);
        btn_Pagenext = view.findViewById(R.id.btn_Pagenext);

        line_Address=view.findViewById(R.id.line_Address);

        rv_Product.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("shopcard") == null) {
            Common.showToast(activity, R.string.textNoFound);
            navController.popBackStack();
            return;
        }
        shopping_cart_list = (Shopping_Cart_List) bundle.getSerializable("shopcard");

//        for (int i = 0 ; i<=shopping_cart_list.)
//        tv_Buyer_Name.setText();





    }

    private void initData() {
    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        rv_Product.setAdapter(new productAdapter(activity, shopping_cart_list.getCart()));


        btn_Pagenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.deliveryFragment);
                Intent intent = new Intent(getContext(),PayActivity.class);
                startActivity(intent);


            }
        });



//        radioButton_New_Address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked){
//                    line_Name.setVisibility(View.VISIBLE);
//                    line_Phone.setVisibility(View.VISIBLE);
//                    line_Address.setVisibility(View.VISIBLE);
//                    btn_Buyer_Confirm.setVisibility(View.VISIBLE);
//                }else {
//                    line_Name.setVisibility(View.INVISIBLE);
//                    line_Address.setVisibility(View.INVISIBLE);
//                    line_Phone.setVisibility(View.INVISIBLE);
//                    btn_Buyer_Confirm.setVisibility(View.INVISIBLE);
//                }
//
////                et_Phone.setVisibility(View.VISIBLE);
//            }
//
//        });






    }

    private class productAdapter extends RecyclerView.Adapter<productAdapter.Myviewholder> {
        Context context;
        List<Shopping_Cart> productList;

        public productAdapter(Context context, List<Shopping_Cart> productList) {
            this.context = context;
            this.productList = productList;

        }

        @NonNull
        @Override
        public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_shoppingcart_itemview, parent, false);
            return new Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
            final  Shopping_Cart list = productList.get(position);

            holder.tv_Name.setText(list.getProduct_Name());
//            holder.tv_Count.setText(list.getAmount());
            holder.tv_Price.setText(String.valueOf(list.getPrice()*list.getAmount()));
//            holder.checkBox.setVisibility(View.GONE);
//            holder.tv_Less.setVisibility(View.GONE);
//            holder.tv_Add.setVisibility(View.GONE);
//            holder.tv_Count.setText("數量");

            ;


        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        private class Myviewholder extends RecyclerView.ViewHolder {
            ImageView iv_Prouct;
            TextView tv_Name;
            TextView tv_specification;
            TextView tv_Price;
            TextView tv_Add;
            TextView tv_Count;
            TextView tv_Less;
            CheckBox checkBox;


            public Myviewholder(View view) {
                super(view);
                iv_Prouct = view.findViewById(R.id.iv_Prouct);
                tv_Name = view.findViewById(R.id.tv_Name);
                tv_specification = view.findViewById(R.id.tv_specification);
                tv_Price = view.findViewById(R.id.tv_Price);
                tv_Add = view.findViewById(R.id.btAdd);
                tv_Count = view.findViewById(R.id.tv_Count);
                tv_Less = view.findViewById(R.id.btLess);
                checkBox = view.findViewById(R.id.checkBox);

            }
        }
    }
}
