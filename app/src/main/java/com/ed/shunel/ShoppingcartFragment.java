package com.ed.shunel;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.Shopping_Cart;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingcartFragment extends Fragment {

    private final static String TAG = "TAG_ShoppingcartFragment";
    private Activity activity;
    private RecyclerView rv_Shopping_Cart;
    private List<Shopping_Cart> shopping_cartList;
    private shopp_cart_adapter shopp_cart_adapter;
    private Button btn_next;
    private CheckBox checkbox_all;
    private boolean isSelect = false;//全选按钮的状态
    private List<Shopping_Cart> listdatas = new ArrayList<Shopping_Cart>();
//    private List<Shopping_Cart> cartList;

    private CommonTask shopGetall;


    //    test
    private List<Product> productList;

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
//        setLinstener();

    }

    private void setSystemServices() {
    }

    private void setLinstener() {
        //bug
        shopp_cart_adapter = new shopp_cart_adapter(activity, shopping_cartList);
        new ItemTouchHelper(item).attachToRecyclerView(rv_Shopping_Cart);
//        rv_Shopping_Cart.setAdapter(new shopp_cart_adprer(activity,shopping_cartList));
        rv_Shopping_Cart.setAdapter(shopp_cart_adapter);


        checkbox_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelect) {
                    isSelect = true;//全选
//                    adapter.All();
                    shopp_cart_adapter.All();
//                    checkbox_all.setText("取消全");
                } else {
                    isSelect = false;//取消全选
//                    adapter.neverall();
                    shopp_cart_adapter.All();
//                    checkbox_all.setText("全选");
                }

            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = "";
                listdatas.clear();
                Map<Integer, Boolean> map = shopp_cart_adapter.getMap();
                for (int i = 0; i < shopping_cartList.size(); i++) {
                    if (map.get(i)) {
                        listdatas.add(shopping_cartList.get(i));
                    }
                }
                for (int j = 0; j < listdatas.size(); j++) {
                    content += listdatas.get(j) + ",";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                if (content.length() == 0) {
                    builder.setMessage(R.string.Nodata);
                    builder.create().show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_shoppingcartFragment_to_buyerFragment);
                }


            }
        });


    }

    private void initData() {

        shopping_cartList = getDate();
        productList = getProduct();

    }

    private List<Product> getProduct() {
        List<Product> shoppingCarts = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllShop");
            String jsonOut = jsonObject.toString();
            shopGetall = new CommonTask(url, jsonOut);
            try {
                String jsonIn = shopGetall.execute().get();
                Type listType = new TypeToken<List<Shopping_Cart>>() {
                }.getType();
                shoppingCarts = new Gson().fromJson(jsonIn, listType);
//                Log.i();

            } catch (Exception e) {
                e.printStackTrace();
//                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return shoppingCarts;


    }

    private List<Shopping_Cart> getDate() {

        List<Shopping_Cart> shoppingCarts = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllShop");
//            jsonObject.addProperty("shoppingCarts",new Gson().toJson(shoppingCarts));
            String jsonOut = jsonObject.toString();
            shopGetall = new CommonTask(url, jsonOut);

            try {
                String jsonIn = shopGetall.execute().get();
                Type listType = new TypeToken<List<Shopping_Cart>>() {
                }.getType();
                shoppingCarts = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                e.printStackTrace();
//                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return shoppingCarts;

    }

    private void findViews(View view) {

        checkbox_all = view.findViewById(R.id.checkox_all);
        btn_next = view.findViewById(R.id.btn_next);
        rv_Shopping_Cart = view.findViewById(R.id.rv_Shopping_Cart);
        rv_Shopping_Cart.setLayoutManager(new LinearLayoutManager(activity));


    }

    private class shopp_cart_adapter extends RecyclerView.Adapter<shopp_cart_adapter.Myviewholder> {
        Context context;
        List<Shopping_Cart> shopping_cartList;
        private HashMap<Integer, Boolean> maps = new HashMap<Integer, Boolean>();//多选

        public shopp_cart_adapter(Context context, List<Shopping_Cart> shopping_cartList) {
            this.context = context;
            this.shopping_cartList = shopping_cartList;
//            initMap();
        }

        private void initMap() {

            for (int i = 0; i < shopping_cartList.size(); i++) {
                maps.put(i, false);   //每一次进入列表页面  都是未选中状态
            }


        }

        @NonNull
        @Override
        public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_shoppingcart_itemview, parent, false);

            return new Myviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myviewholder holder, final int position) {

            Shopping_Cart shoppingCart = shopping_cartList.get(position);
//            holder.tv_Name.setText(shoppingCart.getProduct_ID());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.productDetailFragment);
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    maps.put(position, isChecked);
                }
            });

            if (maps.get(position) == null) {
                maps.put(position, false);
            }
            //没有设置tag之前会有item重复选框出现，设置tag之后，此问题解决
            holder.checkBox.setChecked(maps.get(position));


        }


        //全选方法
        public void All() {
            Set<Map.Entry<Integer, Boolean>> entries = maps.entrySet();
            boolean shouldall = false;
            for (Map.Entry<Integer, Boolean> entry : entries) {
                Boolean value = entry.getValue();
                if (!value) {
                    shouldall = true;
                    break;
                }
            }
            for (Map.Entry<Integer, Boolean> entry : entries) {
                entry.setValue(shouldall);
            }
            notifyDataSetChanged();
        }

        //反选
        public void neverall() {
            Set<Map.Entry<Integer, Boolean>> entries = maps.entrySet();
            for (Map.Entry<Integer, Boolean> entry : entries) {
                entry.setValue(!entry.getValue());
            }
            notifyDataSetChanged();
        }

        //多选
        public void MultiSelection(int position) {
            //对当前状态取反
            if (maps.get(position)) {
                maps.put(position, false);
            } else {
                maps.put(position, true);
            }
            notifyItemChanged(position);
        }

        //获取最终的map存储数据
        public Map<Integer, Boolean> getMap() {
            return maps;
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

            Collections.swap(shopping_cartList, viewHolder.getAdapterPosition(), target
                    .getAdapterPosition());
            return true;
//            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            if (Common.networkConnected(activity)) {
                String url = Common.URL_SERVER + "Prouct_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "shopDelete");
//                jsonObject.addProperty("shopId", );
                int count = 0;
                try {
                    shopGetall= new CommonTask(url, jsonObject.toString());
                    String result = shopGetall.execute().get();
                    count = Integer.parseInt(result);
                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
                }
                if (count == 0) {
                    Common.showToast(activity, R.string.textDeleteFail);
                } else {
                    shopping_cartList.remove(viewHolder.getAbsoluteAdapterPosition());
                    shopp_cart_adapter.notifyDataSetChanged();
//                    SpotAdapter.this.notifyDataSetChanged();
                    // 外面spots也必須移除選取的spot
//                    SpotListFragment.this.spots.remove(spot);
                    Common.showToast(activity, R.string.textDeleteSuccess);
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
//            shopping_cartList.remove(viewHolder.getAdapterPosition());
//

        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder
                viewHolder) {
            // 拖拽的標記，這裏允許上下左右四個方向
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                    ItemTouchHelper.RIGHT;
            // 滑動的標記，這裏允許左右滑動
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        /*
               這個方法會在某個Item被拖動和移動的時候回調，這裏我們用來播放動畫，當viewHolder不為空時為選中狀態
               否則為釋放狀態
            */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
//            if (viewHolder != null) {
//                vh = viewHolder;
//                pickUpAnimation(viewHolder.itemView);
//            } else {
//                if (vh != null) {
//                    putDownAnimation(vh.itemView);
//                }
//            }
        }

        /*
       當onMove返回true時調用
    */
        @Override
        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int
                fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            // 移動完成後刷新列表
            shopp_cart_adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target
                    .getAdapterPosition());
        }


    };


}
