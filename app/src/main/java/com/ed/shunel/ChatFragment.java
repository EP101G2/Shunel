package com.ed.shunel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.bean.ChatMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.ed.shunel.CommonTwo.chatWebSocketClient;
import static com.ed.shunel.CommonTwo.loadUserName;
import static com.ed.shunel.CommonTwo.showToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private static final String TAG = "TAG_ChatFragment";
    private Activity activity;
    private LocalBroadcastManager broadcastManager;
    private TextView tvMessage;
    private EditText etMessage;
    private ScrollView scrollView;
    private Button btSend;
    private RecyclerView rv;
    private String friend;

    private ChatMessage chatMessage = null;
    String message = "";
    private List<ChatMessage> chatMessageList = new ArrayList<>();


    public ChatFragment() {
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
        friend = "Shunel";
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(activity);
        registerChatReceiver();
        CommonTwo.connectServer(activity, loadUserName(activity));

        return inflater.inflate(R.layout.fragment_chat, container, false);
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
        rv = view.findViewById(R.id.rv);
        btSend = view.findViewById(R.id.btSend);
        etMessage = view.findViewById(R.id.etMessage);


    }

    private void initData() {
    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.setAdapter(new messageFragment(activity,chatMessageList)); //data要放什麼？

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etMessage.getText().toString();
                if (message.trim().isEmpty()) {
                    Toast.makeText(activity,"請輸入文字",Toast.LENGTH_LONG).show();
                    return;
                }
                String sender = loadUserName(activity);
                // 將欲傳送訊息轉成JSON後送出
                chatMessage = new ChatMessage("chat", sender, friend, message);
                String chatMessageJson = new Gson().toJson(chatMessage);
                chatWebSocketClient.send(chatMessageJson);
                Log.d("btSend:", "output: " + chatMessageJson);

                // 將欲傳送訊息顯示在TextView上
                chatMessageList.add(chatMessage);
//                tvMessage.append(sender + ": " + message + "\n");
                // 將輸入的訊息清空

                messageFragment adpter = (messageFragment) rv.getAdapter();
                if(adpter!=null) {
                    adpter.setListforMsg(chatMessageList);
                    adpter.notifyDataSetChanged();
                }
                etMessage.setText(null);
            }
        });

    }


    /**
     * 註冊廣播接收器攔截聊天資訊
     * 因為是在Fragment註冊，所以Fragment頁面未開時不會攔截廣播
     */
    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
    }

    // 接收到聊天訊息會在TextView呈現
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
            String sender = chatMessage.getSender();
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView
            chatMessageList.add(chatMessage);
            messageFragment adpter = (messageFragment) rv.getAdapter();
            if(adpter!=null) {
                adpter.setListforMsg(chatMessageList);
                adpter.notifyDataSetChanged();
            }
            Log.d("=============", message);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Fragment頁面切換時解除註冊，但不需要關閉WebSocket，
        // 否則回到前頁好友列表，會因為斷線而無法顯示好友
        broadcastManager.unregisterReceiver(chatReceiver);
    }




    private class messageFragment extends RecyclerView.Adapter<messageFragment.MyViewholder> {
        Context context;
        List<ChatMessage> message;

        public messageFragment(Context context ,List<ChatMessage> message ) {
            this.context = context;
            this.message = message;

        }

        void setListforMsg(List<ChatMessage> message){
            this.message = message;
        }

        @NonNull
        @Override
        public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.chat_list_test, parent, false);

            return new MyViewholder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

            final ChatMessage CM = message.get(position);
            if(CM.getReceiver().equals(friend)){

                holder.tv_their.setVisibility(View.VISIBLE);
                holder.iv_their.setVisibility(View.VISIBLE);


                //我方最新訊息
                holder.tvMy.setText(CM.getMessage());
                holder.iv_my.setVisibility(View.VISIBLE);
                holder.tv_their.setVisibility(View.GONE);
                holder.iv_their.setVisibility(View.GONE);
                Log.e("h123=",CM.getMessage());
            }
            else {

                holder.iv_my.setVisibility(View.VISIBLE);
                holder.tvMy.setVisibility(View.VISIBLE);

                //對方最新訊息
                holder.tvMy.setVisibility(View.GONE);
                holder.iv_my.setVisibility(View.GONE);
                holder.tv_their.setText(CM.getMessage());
                holder.iv_their.setImageResource(R.drawable.circle);
                Log.e("h223=",CM.getMessage());
            }



        }

        @Override
        public int getItemCount() {
            return message==null?0:message.size();
        }

        private class MyViewholder extends RecyclerView.ViewHolder {
            TextView tvMy,tv_their;
            ImageView iv_their,iv_my;
            public MyViewholder(View itemView) {
                super(itemView);

                tvMy = itemView.findViewById(R.id.tvMy);
                iv_my = itemView.findViewById(R.id.iv_my);
                tv_their = itemView.findViewById(R.id.tvTheir);
                iv_their = itemView.findViewById(R.id.iv_their);

            }
        }
    }
}
