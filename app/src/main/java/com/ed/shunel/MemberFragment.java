package com.ed.shunel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTaskUser;
import com.ed.shunel.bean.ChatMessage;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;


public class MemberFragment extends Fragment {

    private CardView cvLike, cvChat, cvOrderlist, cvHistory, cvSetting;
    private Activity activity;
    private View view;
    private Button btn_Logout;
    private boolean login = false;
    private SharedPreferences sharedPreferences;
    private TextView tvId, tv_Name;
    private CommonTask memberTask;
    private ImageTaskUser imageTask;
    private int imageSize;
    private ImageView ivUser;
    private CommonTask chatTask;
    private String id, name;
    private String user_Id;

    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
//        final NavController navController = Navigation.findNavController(view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//

        sharedPreferences = Common.getPreherences(activity);
        //置入name屬性的字串


        Log.i(TAG, "-------------------------MemberFragment------------------------------------");
        Log.i(TAG, sharedPreferences.getString("id", ""));

        cvLike = view.findViewById(R.id.cvLike);
        cvChat = view.findViewById(R.id.cvChat);
        cvOrderlist = view.findViewById(R.id.cvOrderlist);
        cvHistory = view.findViewById(R.id.cvHistory);
        cvSetting = view.findViewById(R.id.cvSetting);
        btn_Logout = view.findViewById(R.id.btn_Logout);
        tvId = view.findViewById(R.id.tvId);
        tv_Name = view.findViewById(R.id.tv_Name);
        ivUser=view.findViewById(R.id.ivUser);


tvId.setText(Common.getPreherences(activity).getString("id","deVal"));
tv_Name.setText(Common.getPreherences(activity).getString("name","deVal"));

        if (sharedPreferences.getString("id", "").equals("")) {

            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
            startActivity(intent);
            activity.finish();//把自己關掉

        }

        cvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.likeDetail_SamFragment);
            }
        });


        cvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_settingFragment2);
            }
        });


        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
                LayoutInflater inflater = LayoutInflater.from(activity);
                final View view = inflater.inflate(R.layout.logoutsuccess, null);
                builder.setView(view);
                builder.create().show();

                Logout();

                broadcastManager.unregisterReceiver(chatReceiver);

            }
        });
        cvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /********************************建立聊天室 Jack*****************************************/
                int chat_ID = 0;
                user_Id = Common.getPreherences(activity).getString("id","");

                String url = Common.URL_SERVER + "Chat_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", "createRoom");
                jsonObject.addProperty("admin_Id", "1");
                jsonObject.addProperty("user_ID", user_Id);

                Log.e(TAG, jsonObject.toString());
                try {
                    chatTask = new CommonTask(url, jsonObject.toString());
                    String result = chatTask.execute().get();
                    chat_ID = Integer.parseInt(result);
                    Log.e(TAG, "============"+result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                Bundle bundle = new Bundle();
                bundle.putInt("chatroom",chat_ID);

                /********************************建立聊天室 Jack*****************************************/
                Navigation.findNavController(v).navigate(R.id.chatFragment,bundle);
            }
        });
        cvOrderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_orderListFragment2);//
            }
        });

        String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getLogin");
        jsonObject.addProperty("id", Common.getPreherences(activity).getString("id", ""));
        jsonObject.addProperty("password", Common.getPreherences(activity).getString("password", ""));
        Log.e("ID_PAS", Common.getPreherences(activity).getString("id", ""));
        memberTask = new CommonTask(url, jsonObject.toString());
        String jsonIn = "";

        try {
            jsonIn = memberTask.execute().get();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.e("------------",jsonIn);
        Log.e("------------",sharedPreferences.getString("id", ""));
        Log.e("------------",sharedPreferences.getString("password", ""));




        

        User_Account user_account = gson.fromJson(jsonIn, User_Account.class);
//        tv_Name.setText(user_account.getAccount_User_Name());
//        tvId.setText(user_account.getAccount_ID());



        String Pic=Common.getPreherences(activity).getString("id","");
        imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        imageTask= new ImageTaskUser(url,Pic,imageSize);
        try {
            Bitmap bitmap=imageTask.execute().get();
            if (bitmap == null){
                ivUser.setImageResource(R.drawable.no_image);
            }else {
                ivUser.setImageBitmap(bitmap);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "123");
        Common.getPreherences(activity).edit().apply();





    }




    private void Logout() {
        Common.getPreherences(activity).edit().clear().apply();

//        MainActivity.preferences.edit().clear().apply();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
        startActivity(intent);
//        if (MainActivity.preferences.edit())


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
            Log.d(TAG, message);
        }
    };
}
