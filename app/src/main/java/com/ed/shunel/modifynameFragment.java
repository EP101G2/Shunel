package com.ed.shunel;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ed.shunel.Task.Common;
import com.ed.shunel.Task.CommonTask;
import com.google.gson.JsonObject;


public class ModifynameFragment extends Fragment {
    Activity activity;
    private Button btTakePicture, btPickPicture, btCancel, btConfirm;
    private EditText etName, etAddress, etPhone;
    private ImageView ivProfilePhoto;
    private TextView tvId;
    private CommonTask UpdateInfoTask;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modifyname, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btTakePicture = view.findViewById(R.id.btTakePicture);
        btPickPicture = view.findViewById(R.id.btPickPicture);
        btCancel = view.findViewById(R.id.btCancel);
        btConfirm = view.findViewById(R.id.btConfirm);
        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        tvId = view.findViewById(R.id.tvId);


        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "User_Account_Servlet";//連server端先檢查網址
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");//變作ＪＳＯＮ自串
            String jsonOut = jsonObject.toString();
            UpdateInfoTask = new CommonTask(url, jsonOut);
            String jsonIn = "";

        }
    }


}
