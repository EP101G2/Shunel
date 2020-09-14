package com.ed.shunel;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;


import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTaskUser;
import com.ed.shunel.bean.ChatMessage;

import com.ed.shunel.bean.User_Account;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
import static com.ed.shunel.CommonTwo.loadUserName;


public class MemberFragment extends Fragment {

    private CardView cvLike, cvChat, cvOrderlist, cvHistory, cvSetting, cvMap;
    private Activity activity;
    private View view;
    private Button btn_Logout;
    private boolean login = false;
    private SharedPreferences sharedPreferences;
    private TextView tvId, tv_Name;
    private CommonTask memberTask,getAddress;
    private ImageTaskUser imageTask;
    private int imageSize;
    private ImageView ivUser;
    private CommonTask chatTask;
    private String id, name;
    private String user_Id;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int PER_ACCESS_LOCATION = 201;
    private static final int REQ_CHECK_SETTINGS = 101;


    private LocalBroadcastManager broadcastManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("===onCreate","");
        activity = getActivity();
//        final NavController navController = Navigation.findNavController(view);

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)         //單位是毫秒 , 每10秒取得位置一次
                .setSmallestDisplacement(1000); //user為移動1000公尺 才算是移動

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                //updateLastLocationInfo(lastLocation);
            }
        };

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
        askAccessLocationPermission();
        Log.i(TAG, sharedPreferences.getString("id", ""));
        cvMap = view.findViewById(R.id.cvMap);
        cvLike = view.findViewById(R.id.cvLike);
        cvChat = view.findViewById(R.id.cvChat);
        cvOrderlist = view.findViewById(R.id.cvOrderlist);
        cvHistory = view.findViewById(R.id.cvHistory);
        cvSetting = view.findViewById(R.id.cvSetting);
        btn_Logout = view.findViewById(R.id.btn_Logout);
        tvId = view.findViewById(R.id.tvId);
        tv_Name = view.findViewById(R.id.tv_Name);
        ivUser = view.findViewById(R.id.ivUser);


        tvId.setText(Common.getPreherences(activity).getString("id", "deVal"));
        tv_Name.setText(Common.getPreherences(activity).getString("name", "無名稱"));

        if (sharedPreferences.getString("id", "").equals("")) {

            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
            startActivity(intent);
            activity.finish();//把自己關掉
        }

        final String getAddressString = getAddress() ;
        //google地圖 導航到店家
        cvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (lastLocation == null) {
                    Toast.makeText(activity, R.string.textLocationNotFound, Toast.LENGTH_SHORT).show();
                    return;
                }

                Address address = geocode(getAddressString);

                String uriStr = String.format(Locale.US,
                        "https://www.google.com/maps/dir/?api=1" +
                                "&origin=%f,%f&destination=%f,%f&travelmode=driving",
                        lastLocation.getLatitude(), lastLocation.getLongitude(), address.getLatitude(),address.getLongitude());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriStr));
                intent.setClassName("com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });


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
                Log.e(TAG, "======================================" + Common.getPreherences(activity).getString("id", ""));
                Logout();

            }
        });
        cvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /********************************建立聊天室 Jack*****************************************/
                int chat_ID = 0;
                user_Id = Common.getPreherences(activity).getString("id", "");

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
                    Log.e(TAG, "============" + result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                Bundle bundle = new Bundle();
                bundle.putInt("chatroom", chat_ID);

                /********************************建立聊天室 Jack*****************************************/
                Navigation.findNavController(v).navigate(R.id.chatFragment, bundle);
            }
        });

        cvOrderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_memberFragment_to_orderListFragment2);
            }
        });

        String url = Common.URL_SERVER + "User_Account_Servlet";                           //connect servlet(eclipse)
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getLogin");
        jsonObject.addProperty("id", Common.getPreherences(activity).getString("id", ""));
        jsonObject.addProperty("password", Common.getPreherences(activity).getString("password", ""));
        jsonObject.addProperty("getToken", FirebaseInstanceId.getInstance().getToken());
        Log.e("ID_PAS", Common.getPreherences(activity).getString("id", ""));
        memberTask = new CommonTask(url, jsonObject.toString());
        String jsonIn = "";

        try {
            jsonIn = memberTask.execute().get();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.e("------------", jsonIn);
        Log.e("------------", sharedPreferences.getString("id", ""));
        Log.e("------------", sharedPreferences.getString("password", ""));


        User_Account user_account = gson.fromJson(jsonIn, User_Account.class);
//        tv_Name.setText(user_account.getAccount_User_Name());
//        tvId.setText(user_account.getAccount_ID());


        String Pic = Common.getPreherences(activity).getString("id", "");
        imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        imageTask = new ImageTaskUser(url, Pic, imageSize);
        try {
            Bitmap bitmap = imageTask.execute().get();
            if (bitmap == null) {
                ivUser.setImageResource(R.drawable.no_image);
            } else {
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

    private String getAddress() {
        String rp = "";
        JsonObject jsonObject = new JsonObject();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            jsonObject.addProperty("action", "getAddress");
            getAddress = new CommonTask(url, jsonObject.toString());
            try {
                rp = getAddress.execute().get();
                if (rp == null) {
                    Common.showToast(activity,"資料庫無位址");
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        return rp;
    }


    private void Logout() {
        Common.getPreherences(activity).edit().clear().apply();

//        MainActivity.preferences.edit().clear().apply();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
        startActivity(intent);
        CommonTwo.disconnectServer();
        activity.finish();

    }



    private void showLastLocation() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
                       // updateLastLocationInfo(lastLocation);
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // 請求user同意定位
        Log.e("========================onStart","");
        askAccessLocationPermission();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    // 請求user同意定位
    private void askAccessLocationPermission() {
        Log.e("===onStart  askAccessLocationPermission===","");
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result = ActivityCompat.checkSelfPermission(activity, permissions[0]);

        Log.e("result======",result+"");
        //|| result == PackageManager.PERMISSION_GRANTED
        if (result == PackageManager.PERMISSION_DENIED ) {
            Log.e("我愛超哥",result+"");
            requestPermissions(permissions, PER_ACCESS_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //檢查裝置是否開啟Location設定
        checkLocationSettings();
//        if (requestCode == PER_ACCESS_LOCATION
//                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
    //        textView.setText(R.string.textLocationAccessNotGrant);
//        }
    }


    // 檢查裝置是否開啟Location設定
    private void checkLocationSettings() {
        // 必須將LocationRequest設定加入檢查,檢查有沒有開啟
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(activity)
                        .checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // 取得並顯示最新位置
                    showLastLocation();
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    Log.e(TAG, e.getMessage());
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        // 跳出Location設定的對話視窗
                        resolvable.startResolutionForResult(activity, REQ_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private Address geocode(String locationName) {
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            return null;
        } else {
            return addressList.get(0);
        }
    }


}
