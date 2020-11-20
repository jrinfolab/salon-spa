package com.jrinfolab.beautyshop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jrinfolab.beautyshop.ui.AddBranch;
import com.jrinfolab.beautyshop.ui.AddEmployee;
import com.jrinfolab.beautyshop.ui.account.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND = 100; // 1 Second
    private static final int HOLD_TIME = 1 * SECOND; // 3 Seconds

    private Context mContext;

    boolean isLoggedIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;

        isLoggedIn = Preference.isLoggedId(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }

    CountDownTimer timer = new CountDownTimer(HOLD_TIME, HOLD_TIME / SECOND) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (isLoggedIn) {
                Intent intent = new Intent(mContext, AddBranch.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, AddBranch.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
