package com.ed.shunel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    public static SharedPreferences preferences;
    private final static String PREFERENCES_NAME = "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE);
    }
}
