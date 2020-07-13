package com.ed.shunel.Task;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import static android.content.Context.MODE_PRIVATE;

public class CommonTwo {

    private final static String TAG = "CommonTwo";
    public static final String SERVER_URI =
            "ws://10.0.2.2:8080/Shunel_Web/TwoChatServer/";
    public static ChatWebSocketClient chatWebSocketClient;




    // 建立WebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (chatWebSocketClient == null) {
//            chatWebSocketClient = new ChatWebSocketClient(uri, context);
//            chatWebSocketClient.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
    }

    public static void saveUserName(Context context, String userName) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        preferences.edit().putString("userName", userName).apply();
    }

    public static String loadUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        Log.d(TAG, "userName = " + userName);
        return userName;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }

}
