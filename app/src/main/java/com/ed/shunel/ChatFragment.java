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
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.ChatMessage;
import com.ed.shunel.bean.Shopping_Cart;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
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
    private CommonTask chatTask,messageTask;
    private String user_ID;
    private String user_Name;
    private int chat_ID;

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

        Bundle bundle = getArguments();

        chat_ID = bundle.getInt("chatroom");

        // Inflate the layout for this fragment
        friend = "Shunel";
        user_ID = Common.getPreherences(activity).getString("id", "");
        user_Name = Common.getPreherences(activity).getString("name", "");
//        Log.e("123312=","-----------------------"+user_Name+"------------"+user_ID);
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

    chatMessageList = getData();

    }

    private List<ChatMessage> getData() {

        List<ChatMessage> messages = null;

        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Chat_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "getAll");
            jsonObject.addProperty("chat_ID",chat_ID);
            messageTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = messageTask.execute().get();
                Type listType = new TypeToken<List<ChatMessage>>() {
                }.getType();
                messages = new Gson().fromJson(jsonIn, listType);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("--------------",messages+"");

        return  messages;
    }

    private void setSystemServices() {
    }

    private void setLinstener() {


        rv.setLayoutManager(new LinearLayoutManager(activity));
        rv.setAdapter(new messageFragment(activity, chatMessageList)); //data要放什麼？

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etMessage.getText().toString();
                if (message.trim().isEmpty()) {
                    Toast.makeText(activity, "請輸入文字", Toast.LENGTH_LONG).show();
                    return;
                }
                String sender = loadUserName(activity);
                // 將欲傳送訊息轉成JSON後送出
                chatMessage = new ChatMessage("chat",user_ID,friend,message,chat_ID);
                String chatMessageJson = new Gson().toJson(chatMessage);
                chatWebSocketClient.send(chatMessageJson);
                Log.d("btSend:", "output: " + chatMessageJson);

                // 將欲傳送訊息顯示在TextView上
                chatMessageList.add(chatMessage);
                // 將輸入的訊息清空
                sendChatDB(chatMessage);
                messageFragment adpter = (messageFragment) rv.getAdapter();
                if (adpter != null) {
                    adpter.setListforMsg(chatMessageList);
                    adpter.notifyDataSetChanged();
                }
                etMessage.setText(null);




            }
        });

    }

    private void sendChatDB(ChatMessage chatMessage) {

        String url = Common.URL_SERVER + "Chat_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "createChatID");
        jsonObject.addProperty("chatID", chatMessage.getChatRoom());
        jsonObject.addProperty("receiver",chatMessage.getReceiver());
        jsonObject.addProperty("sender",chatMessage.getSender());
        jsonObject.addProperty("msg",chatMessage.getMessage());
        jsonObject.addProperty("msgtype",chatMessage.getType());


        Log.e(TAG, jsonObject.toString());
        try {
            chatTask = new CommonTask(url, jsonObject.toString());
            String result = chatTask.execute().get();
//            chat_ID = Integer.parseInt(result);
            Log.e(TAG, "============"+result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

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
            if (adpter != null) {
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
        /*layout分別是自己發送的文字內容，自己發送的圖片，與別人發的文字和圖片*/
        private final int TYPE_MESSAGE_SENT = 0;
        private final int TYPE_MESSAGE_RECEIVED = 1;
        private final int TYPE_IMAGE_SENT = 2;
        private final int TYPE_IMAGE_RECEIVED = 3;
        private LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        Context context;
        List<ChatMessage> message;

        public messageFragment(Context context, List<ChatMessage> message) {
            this.context = context;
            this.message = message;

        }

        void setListforMsg(List<ChatMessage> message) {
            this.message = message;
        }

        @NonNull
        @Override
        public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;

            switch (viewType) {
                case TYPE_MESSAGE_SENT:
                    itemView = inflater.inflate(R.layout.their_message, parent, false);
                    return new SentMessageHolder(itemView);

                case TYPE_MESSAGE_RECEIVED:
                    itemView = inflater.inflate(R.layout.my_message, parent, false);
                    return new ReceivedMessageHolder(itemView);

                case TYPE_IMAGE_SENT:
                    itemView = inflater.inflate(R.layout.item_sent_image, parent, false);
                    return new SentImageHolder(itemView);

                case TYPE_IMAGE_RECEIVED:
                    itemView = inflater.inflate(R.layout.item_received_photo, parent, false);
                    return new ReceivedImageHolder(itemView);
                default:
                    throw new IllegalStateException("Unexpected value: " + viewType);
            }
        }

        @Override
        public int getItemViewType(int position) {
            ChatMessage CM = message.get(position);

            if (CM.getSender().equals("Shunel")) {
                if (CM.getType().equals("chat")) {
                    return TYPE_MESSAGE_SENT;
                } else {
                    return TYPE_IMAGE_SENT;
                }
            } else {
                if (CM.getType().equals("chat")) {
                    return TYPE_MESSAGE_RECEIVED;
                } else {
                    return TYPE_IMAGE_RECEIVED;
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

            final ChatMessage CM = message.get(position);


            if (CM.getSender().equals(Common.getPreherences(activity).getString("id",""))){
                ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
                receivedMessageHolder.messageTxt.setText(CM.getMessage());
            }else {
                SentMessageHolder sentMessageHolder = (SentMessageHolder) holder;
                sentMessageHolder.nameTxt.setText(CM.getSender());
                sentMessageHolder.messageTxt.setText(CM.getMessage());
            }


        }

        @Override
        public int getItemCount() {
            return message == null ? 0 : message.size();
        }

        private class MyViewholder extends RecyclerView.ViewHolder {
            public MyViewholder(View itemView) {
                super(itemView);
            }
        }

        private class SentMessageHolder extends MyViewholder {
//            TextView messageTxt;
//
//            public SentMessageHolder(@NonNull View itemView) {
//                super(itemView);
//                messageTxt = itemView.findViewById(R.id.message_mybody);
//            }

            TextView nameTxt, messageTxt;

            public SentMessageHolder(@NonNull View itemView) {
                super(itemView);

                nameTxt = itemView.findViewById(R.id.name);
                messageTxt = itemView.findViewById(R.id.message_mybody);
            }



        }

        private class ReceivedMessageHolder extends MyViewholder {
//            TextView nameTxt, messageTxt;
//
//            public ReceivedMessageHolder(@NonNull View itemView) {
//                super(itemView);
//
//                nameTxt = itemView.findViewById(R.id.name);
//                messageTxt = itemView.findViewById(R.id.message_mybody);
//            }


            TextView messageTxt;

            public ReceivedMessageHolder(@NonNull View itemView) {
                super(itemView);
                messageTxt = itemView.findViewById(R.id.message_mybody);
            }
        }














        private class SentImageHolder extends MyViewholder {
            ImageView imageView;
            public SentImageHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.imageView);
            }
        }

        private class ReceivedImageHolder extends MyViewholder {
            ImageView imageView;
            TextView nameTxt;

            public ReceivedImageHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                nameTxt = itemView.findViewById(R.id.nameTxt);
            }
        }
    }
}
