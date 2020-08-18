package com.ed.shunel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.bean.User_Account;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

//FB
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ed.shunel.CommonTwo.loadUserName;
import static com.ed.shunel.CommonTwo.saveUserName;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Login_Fragment extends Fragment {
    private final static String TAG = "TAG_LoginFragment";
    private Activity activity;
    private EditText etTypeId, etTypePassword;
    private Button btLogin, btFacebook, btGoogle;
    private TextView tvRegisterNow, tvForgetPassword, tvMessage;
    private CommonTask loginTask;
    private String id, name, password, fbName, fbId, fbMail,iphone,address;
    private User_Account user_account;

    //google
    private GoogleSignInClient client;
    private static final int REQ_SIGN_IN = 101;
    final String INITIALIZED = "initialized";
    Boolean user_first;
    private View view;
    //FB
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        auth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        //fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(activity);
        //google
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 由google-services.json轉出
                .requestIdToken(getString(R.string.default_web_client_id))
                // 要求輸入email
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(activity, options);

//
//        AppEventsLogger.activateApp(getA);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//move the title
//
//        setting=getSharedPreferences("setting", 0);//開啟Preferences,名稱為setting,如果存在則開啟它,否則建立新的Preferences
//
//        user_first = setting.getBoolean("FIRST",true);//取得相應的值,如果沒有該值,說明還未寫入,用true作為預設值
//
//        if(user_first){
//            //第一次登入,正常載入
//            setContentView(R.layout.activity_sign_up);
//
//        }else{
//
////如果不是第一次登入,直接跳轉到下一個介面
//            Intent intent=new Intent(this, activity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        setting.edit().putBoolean("FIRST", false).commit();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity.setTitle(R.string.textLogin);

        return inflater.inflate(R.layout.fragment_login_, container, false);

    }


    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTypeId = view.findViewById(R.id.etTypePhonenumber);
        etTypePassword = view.findViewById(R.id.etTypePassword);
        btLogin = view.findViewById(R.id.btLogin);
        btFacebook = view.findViewById(R.id.btFacebook);
        btGoogle = view.findViewById(R.id.btGoogle);
        tvRegisterNow = view.findViewById(R.id.tvRegisterNow);
        tvForgetPassword = view.findViewById(R.id.tvForgetPassword);
        tvMessage = view.findViewById(R.id.tvMessage);

        btGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        btFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInFB();
            }
        });


        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_login_Fragment_to_forgetPasswordFragment);
            }
        });


        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_login_Fragment_to_register_Fragment);
            }
        });


        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = etTypeId.getText().toString();
                password = etTypePassword.getText().toString();
                String token = FirebaseInstanceId.getInstance().getToken();

                if (networkConnected()) {
                    String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
                    Gson gson = new Gson();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getLogin");
                    jsonObject.addProperty("id", id);
                    jsonObject.addProperty("password", password);
                    jsonObject.addProperty("getToken", token);
                    loginTask = new CommonTask(url, jsonObject.toString());
                    String jsonIn = "";
                    try {
                        jsonIn = loginTask.execute().get();

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    JsonObject jsonObject2 = gson.fromJson(jsonIn, JsonObject.class);

                    String result = jsonObject2.get("result").getAsString();


                    savePreferences();
                    Log.i(TAG, result);

                    if (etTypeId.length() == 0) {
                        etTypeId.setError("請輸入15位數內英文或數字 ");
                    } else if (etTypePassword.length() == 0) {
                        etTypePassword.setError("請輸入15位數內英文或數字密碼");
                    }
                    switch (result) {
                        case "fail":

                            Toast.makeText(activity, "失敗", Toast.LENGTH_SHORT).show();


                            break;
                        case "success":


                            String userJstr = jsonObject2.get("user").getAsString();
                            if (userJstr != null) {
                                User_Account user_account = gson.fromJson(userJstr, User_Account.class);
                                id = user_account.getAccount_ID();
                                name = user_account.getAccount_User_Name();
                                password = user_account.getAccount_Password();
                                iphone=  user_account.getAccount_Phone();
                                address= user_account.getAccount_Address();
                                savePreferences();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("User", user_account);

                                CommonTwo.saveUserName(activity,String.valueOf(id));
                                CommonTwo.connectServer(activity,loadUserName(activity));

                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
                                LayoutInflater inflater = LayoutInflater.from(activity);
                                final View view = inflater.inflate(R.layout.loginsuccess, null);
                                builder.setView(view);
//                                builder.create().show();

                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                intent.setClass(activity, MainActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
                                startActivity(intent);  //啟動跳頁動作
                                activity.finish();//把自己關掉

//                                CommonTwo.connectServer(activity, saveUserName());
                                Log.e(TAG,"id"+id+"\t"+loadUserName(activity));
                            }
                            break;
                    }


                } else {
                    showToast(activity, R.string.textNoNetwork);
                }
            }
        });
    }

//        preferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
//        loadPreferences();


    // 檢查是否有網路連線
    private boolean networkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // API 23支援getActiveNetwork()
                Network network = connectivityManager.getActiveNetwork();
                // API 21支援getNetworkCapabilities()
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    String msg = String.format(Locale.getDefault(),
                            "TRANSPORT_WIFI: %b%nTRANSPORT_CELLULAR: %b%nTRANSPORT_ETHERNET: %b%n",
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                    Log.d(TAG, msg);
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // API 29將NetworkInfo列為deprecated
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    private void savePreferences() {

        //置入name屬性的字串
        Common.getPreherences(activity).edit()
                .putString("id", id)
                .putString("password", password)
                .putString("name", name)
                .putString("phone",iphone)
                .putString("address",address)
                .apply();


        Log.i(TAG, "-------------------------------------------------------------");
        Log.i(TAG, Common.getPreherences(activity).getString("name", "deval"));
    }

    // 跳出FB登入畫面
    private void signInFB() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Logger.getLogger(object.toString());
                        try {
                            fbId = object.get("id").toString();
                            fbName = object.get("name").toString();
                            fbMail = object.get("email").toString();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle paramesters = new Bundle();
                paramesters.putString("fields", "id,name,link,email");
                request.setParameters(paramesters);
                request.executeAsync();



                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                signInFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "facebook:onError", error);
            }
        });
    }


    // 使用FB token完成Firebase驗證
    private void signInFirebase(final AccessToken token) {
        Log.d(TAG, "signInFirebase:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 登入成功轉至下頁；失敗則顯示錯誤訊息
                        if (task.isSuccessful()) {
                            if (task.isSuccessful()) {
                                Common.showToast(activity, "恭喜登入成功");
                                User_Account user = new User_Account();
                                gson = new Gson();
                                String token = FirebaseInstanceId.getInstance().getToken();
                                user.setAccount_ID(fbMail);
                                user.setAccount_User_Name(fbName);
                                user.setToken(token);
                                if (Common.networkConnected(activity)) {
                                    String url = Common.URL_SERVER + "User_Account_Servlet";
                                    JsonObject jsonObject = new JsonObject();   //把東西包在jsonObject裡
                                    jsonObject.addProperty("action", "google");
                                    jsonObject.addProperty("user", gson.toJson(user));
                                    loginTask = new CommonTask(url, jsonObject.toString());   //發送請球給ＳＥＶＥＲ

                                    String jsonIn = null;   //jsonin  SEVER丟回來的回應
                                    try {
                                        jsonIn = loginTask.execute().get();   //等待接收結果
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
//處理接回來的資料
                                    if(jsonIn.length()>1){
                                        JsonObject jsonObject1 = gson.fromJson(jsonIn, JsonObject.class);
                                        String googleLogin = jsonObject1.get("User").getAsString();
                                        User_Account data = gson.fromJson(googleLogin, User_Account.class);
                                        //                                字串              要解析成的類型    就變成ＵＳＥＲＡＣＣＯＵＮＴ的物件
                                        Common.getPreherences(activity).edit()
                                                .putString("id", data.getAccount_ID())
                                                .putString("phone", data.getAccount_Phone())
                                                .putString("password", data.getAccount_Password())
                                                .putString("address", data.getAccount_Address())
                                                .apply();
                                        Intent intent = new Intent();
                                        intent.setClass(activity, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Common.showToast(activity,"註冊失敗");
                                    }


                                }
                            } else {
                                Exception exception = task.getException();
                                String message = exception == null ? "Sign in fail." : exception.getMessage();
                                Common.showToast(activity, "再試一下");
                            }
                        }
                    }
                });
    }


    // 跳出Google登入畫面
    private void signInGoogle() {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, REQ_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SIGN_IN) {
            // 取得裝置上的Google登入帳號
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else {
                    Log.e(TAG, "GoogleSignInAccount is null");
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed");
            }
        } else {

            //跑fb
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // 使用Google帳號完成Firebase驗證
    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        // get the unique ID for the Google account
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 登入成功轉至下頁；失敗則顯示錯誤訊息
                        if (task.isSuccessful()) {
                            Common.showToast(activity, "恭喜登入成功");
                            User_Account user = new User_Account();
                            gson = new Gson();
                            String email = account.getEmail();
                            String name = account.getGivenName() + account.getFamilyName();
                            String token = FirebaseInstanceId.getInstance().getToken();
                            user.setAccount_ID(email);
                            user.setAccount_User_Name(name);
                            user.setToken(token);
                            if (Common.networkConnected(activity)) {
                                String url = Common.URL_SERVER + "User_Account_Servlet";
                                JsonObject jsonObject = new JsonObject();   //把東西包在jsonObject裡
                                jsonObject.addProperty("action", "google");
                                jsonObject.addProperty("user", gson.toJson(user));
                                loginTask = new CommonTask(url, jsonObject.toString());   //發送請球給ＳＥＶＥＲ

                                String jsonIn = null;   //jsonin  SEVER丟回來的回應
                                try {
                                    jsonIn = loginTask.execute().get();   //等待接收結果
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //處理接回來的資料
                                if(jsonIn.length()>1){
                                    JsonObject jsonObject1 = gson.fromJson(jsonIn, JsonObject.class);
                                    String googleLogin = jsonObject1.get("User").getAsString();
                                    User_Account data = gson.fromJson(googleLogin, User_Account.class);
                                    //                                字串              要解析成的類型    就變成ＵＳＥＲＡＣＣＯＵＮＴ的物件
                                    Common.getPreherences(activity).edit()
                                            .putString("id", data.getAccount_ID())
                                            .putString("phone", data.getAccount_Phone())
                                            .putString("password", data.getAccount_Password())
                                            .putString("address", data.getAccount_Address())
                                            .apply();
                                    Intent intent = new Intent();
                                    intent.setClass(activity, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Common.showToast(activity,"註冊失敗");
                                }


                            }
                        } else {
                            Exception exception = task.getException();
                            String message = exception == null ? "Sign in fail." : exception.getMessage();
                            Common.showToast(activity, "再試一下");
                        }

                    }
                });
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // 檢查user是否已經登入，是則FirebaseUser物件不為null
//        FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//            Navigation.findNavController(textView)
//                    .navigate(R.id.action_mainFragment_to_resultFragment);
//        }
//    }
}



