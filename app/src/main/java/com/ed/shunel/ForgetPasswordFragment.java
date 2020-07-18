package com.ed.shunel;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class ForgetPasswordFragment extends Fragment {
    private String TAG = "TAG_MainFragment";
    private Activity activity;
    private ConstraintLayout layoutVerify;
    private EditText etPhone, etVerificationCode;
    private Button btSend, btVerify, btResend;
    private TextView textView;
    private FirebaseAuth auth;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        auth = FirebaseAuth.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutVerify = view.findViewById(R.id.layoutVerify);
        etPhone = view.findViewById(R.id.etPhone);
        etVerificationCode = view.findViewById(R.id.etVerificationCode);
        btSend = view.findViewById(R.id.btSend);
        btVerify = view.findViewById(R.id.btVerify);
        btResend = view.findViewById(R.id.btResend);

        // 一開始先隱藏填寫驗證碼版面
        layoutVerify.setVisibility(View.GONE);

        // 必須使用實機發送(平板電腦也可以)
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 電話號碼格式要符合E.164，要加上country code，台灣為+886
                String phone = "+886" + etPhone.getText().toString().trim();
                Log.e("_____",phone);
                if (phone.isEmpty()) {
                    etPhone.setError(getString(R.string.textEmptyError));
                    return;
                }
                sendVerificationCode(phone);
            }
        });

        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = etVerificationCode.getText().toString().trim();
                if (verificationCode.isEmpty()) {
                    etVerificationCode.setError(getString(R.string.textEmptyError));
                    return;
                }
                verifyPhoneNumberWithCode(verificationId, verificationCode);
            }
        });

        btResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 電話號碼格式要符合E.164，要加上country code，台灣為+886
                String phone = "+886" + etPhone.getText().toString().trim();
                if (phone.isEmpty()) {
                    etPhone.setError(getString(R.string.textEmptyError));
                    return;
                }
                resendVerificationCode(phone, resendToken);
            }
        });
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // 電話號碼，驗證碼寄送的電話號碼
                60, // 驗證碼失效時間，設為60秒代表多次呼叫verifyPhoneNumber()，過了60秒才會發送第2次
                TimeUnit.SECONDS, // 設定時間單位為秒
                activity,
                verifyCallbacks, // 監聽電話驗證的狀態
                token); // 驗證碼發送後，verifyCallbacks.onCodeSent()會傳來token，方便user要求重傳驗證碼
    }


    private void verifyPhoneNumberWithCode(String verificationId, String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
        firebaseAuthWithPhoneNumber(credential);
    }

    private void firebaseAuthWithPhoneNumber(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Navigation.findNavController(etPhone)
                                    .navigate(R.id.action_forgetPasswordFragment_to_createNewPasswordFragment);

                        } else {
                            Exception exception = task.getException();
                            String message = exception == null ? "Sign in fail." : exception.getMessage();
                            textView.setText(message);
                            if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                etVerificationCode.setError(getString(R.string.textInvalidCode));
                            }
                        }
                    }
                });
    }
    private void sendVerificationCode(String phone) {
        layoutVerify.setVisibility(View.VISIBLE);
        // 設定簡訊語系為繁體中文
        auth.setLanguageCode("zh-Hant");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, // 電話號碼，驗證碼寄送的電話號碼
                60, // 驗證碼失效時間，設為60秒代表多次呼叫verifyPhoneNumber()，過了60秒才會發送第2次
                TimeUnit.SECONDS, // 設定時間單位為秒
                activity,
                verifyCallbacks); // 監聽電話驗證的狀態
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        /** This callback will be invoked in two situations:
         1 - Instant verification. In some cases the phone number can be instantly
         verified without needing to send or enter a verification code.
         2 - Auto-retrieval. On some devices Google Play services can automatically
         detect the incoming verification SMS and perform verification without
         user action. */
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted: " + credential);
        }

        /**
         * 發送驗證碼填入的電話號碼格式錯誤，或是使用模擬器發送都會產生發送錯誤，
         * 使用模擬器發送會產生下列執行錯誤訊息：
         * App validation failed. Is app running on a physical device?
         */
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e(TAG, "onVerificationFailed: " + e.getMessage());

        }

        /**
         * The SMS verification code has been sent to the provided phone number,
         * we now need to ask the user to enter the code and then construct a credential
         * by combining the code with a verification ID.
         */
        @Override
        public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d(TAG, "onCodeSent: " + id);
            verificationId = id;
            resendToken = token;
            // 顯示填寫驗證碼版面
            layoutVerify.setVisibility(View.VISIBLE);
        }
    };

//   @Override
//    public void onStart() {
//        super.onStart();
//        // 檢查user是否已經登入，是則FirebaseUser物件不為null
//        FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//            Navigation.findNavController(btSend)
//                    .navigate(R.id.action_forgetPasswordFragment_to_createNewPasswordFragment);
//        }
//    }
}
















