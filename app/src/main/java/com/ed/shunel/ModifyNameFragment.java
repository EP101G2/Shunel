package com.ed.shunel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.ed.shunel.Task.ImageTask;
import com.ed.shunel.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;


public class ModifyNameFragment extends Fragment {

    Activity activity;
    private final static String TAG = "TAG_SpotUpdateFragment";
    private Button btTakePicture, btPickPicture, btCancel, btConfirm;
    private EditText etName, etAddress, etPhone;
    private ImageView ivProfilePhoto;
    private TextView tvId;
    private CommonTask UpdateInfoTask;
    private User_Account user_account;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri;
    private String name, address, phone;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btTakePicture = view.findViewById(R.id.btTakePicture);
        btPickPicture = view.findViewById(R.id.btPickPicture);
        btCancel = view.findViewById(R.id.btCancel);
        btConfirm = view.findViewById(R.id.btConfirm);
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etPhone = view.findViewById(R.id.etPhone);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        tvId = view.findViewById(R.id.tvId);
        final NavController navController = Navigation.findNavController(view);

        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("User") == null) {
            Common.showToast(activity, R.string.textNoUserFound);
            navController.popBackStack();
            return;
        }
        user_account = (User_Account) bundle.getSerializable("User");

        etName.setText(user_account.getAccount_User_Name());
        etAddress.setText(user_account.getAccount_Address());
        etPhone.setText(user_account.getAccount_Phone());

        btTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(activity, R.string.textNoCameraApp);
                }
            }
        });

        btPickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_PICTURE);
            }
        });


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = etName.getText().toString();
                address = etAddress.getText().toString();
                phone = etPhone.getText().toString();
                user_account.setAccount_User_Name(name);
                user_account.setAccount_Address(address);
                user_account.setAccount_Phone(phone);


                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "User_Account_Servlet";//連server端先檢查網址
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "Update");//變作ＪＳＯＮ自串
                    jsonObject.addProperty("user", new Gson().toJson(user_account));
                    // 有圖才上傳
                    if (image != null) {
                        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
                    }
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.parseInt(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(activity, R.string.textUpdateFail);
                    } else {
                        Common.showToast(activity, R.string.textUpdateSuccess);
                    }
                } else {
                    Common.showToast(activity, R.string.textNoNetwork);
                }
                /* 回前一個Fragment */
                navController.popBackStack();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 回前一個Fragment */
                navController.popBackStack();
            }
        });

    }

}