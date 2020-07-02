package com.ed.shunel;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingcartFragment extends Fragment {

    private static final String TAG = "TAG_ShoppingcartFragment";
    private Activity activity;
    private RecyclerView rv_Shopping_Cart;
    private List<Product> shopping_cartList;
    private shopp_cart_adprer shopp_cart_adprer;

    public ShoppingcartFragment() {
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
        return inflater.inflate(R.layout.fragment_shoppingcart, container, false);
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

    private void setSystemServices() {
    }

    private void setLinstener() {

        shopp_cart_adprer = new shopp_cart_adprer(activity, shopping_cartList);
        new ItemTouchHelper(item).attachToRecyclerView(rv_Shopping_Cart);
//        rv_Shopping_Cart.setAdapter(new shopp_cart_adprer(activity,shopping_cartList));
        rv_Shopping_Cart.setAdapter(shopp_cart_adprer);


    }

    private void initData() {

        shopping_cartList = getDate();

    }

    private List<Product> getDate() {

        List<Product> shoppingCarts = new ArrayList<>();
        shoppingCarts.add(new Product(1, "測試", "黑色", 203, "內容", 1, 1));
        shoppingCarts.add(new Product(1, "測試", "黑色", 203, "內容", 1, 1));
        shoppingCarts.add(new Product(1, "測試", "黑色", 203, "內容", 1, 1));
        shoppingCarts.add(new Product(1, "測試", "黑色", 203, "內容", 1, 1));
        shoppingCarts.add(new Product(1, "測試", "黑色", 203, "內容", 1, 1));


        return shoppingCarts;
    }

    private void findViews(View view) {

        rv_Shopping_Cart = view.findViewById(R.id.rv_Shopping_Cart);
        rv_Shopping_Cart.setLayoutManager(new LinearLayoutManager(activity));


    }

    private class shopp_cart_adprer extends RecyclerView.Adapter<shopp_cart_adprer.Myviewholder> {
        Context context;
        List<Product> shopping_cartList;

        public shopp_cart_adprer(Context context, List<Product> shopping_cartList) {
            this.context = context;
            this.shopping_cartList = shopping_cartList;

        }

        @NonNull
        @Override
        public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_shoppingcart_itemview, parent, false);

            return new Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

            Product product = shopping_cartList.get(position);
            holder.tv_Name.setText(product.getProduct_Name());


        }

        @Override
        public int getItemCount() {
            return shopping_cartList.size();
        }

        private class Myviewholder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            ImageView iv_Prouct;
            TextView tv_Name;
            TextView tv_specification;
            TextView tv_Price;
            TextView tv_Add;
            TextView tv_Count;
            TextView tv_Less;


            public Myviewholder(View view) {
                super(view);
                checkBox = view.findViewById(R.id.checkBox);
                iv_Prouct = view.findViewById(R.id.iv_Prouct);
                tv_Name = view.findViewById(R.id.tv_Name);
                tv_specification = view.findViewById(R.id.tv_specification);
                tv_Price = view.findViewById(R.id.tv_Price);
                tv_Add = view.findViewById(R.id.tv_Add);
                tv_Count = view.findViewById(R.id.tv_Count);
                tv_Less = view.findViewById(R.id.tv_Less);

            }
        }
    }

    private void pickUpAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 1f, 10f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }


    ItemTouchHelper.SimpleCallback item = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            shopping_cartList.remove(viewHolder.getAdapterPosition());
            shopp_cart_adprer.notifyDataSetChanged();

        }
    };


}
