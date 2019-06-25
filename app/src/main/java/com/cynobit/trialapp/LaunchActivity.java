package com.cynobit.trialapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(this, MainActivity.class), OnActivityResultContract.MAIN_ACTIVITY);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode == OnActivityResultContract.MAIN_ACTIVITY) finish();
        super.onActivityReenter(resultCode, data);
    }
}
