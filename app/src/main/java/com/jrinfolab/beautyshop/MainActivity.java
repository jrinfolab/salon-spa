package com.jrinfolab.beautyshop;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jrinfolab.beautyshop.ui.AddBranch;
import com.jrinfolab.beautyshop.ui.AddEmployee;
import com.jrinfolab.beautyshop.ui.account.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND = 100; // 1 Second
    private static final int HOLD_TIME = 1 * SECOND; // 3 Seconds
    private static final String TAG = "MainActivity";

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
        checkPermission();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "permission granted");
            } else {
                Log.d(TAG, "permission denied, closing app");
                finish();
            }
        }
    }

    public void checkPermission() {
        // TODO : add check for other permission also
        int result = mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            boolean canRequestAgain = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (canRequestAgain) {
                Log.d(TAG, "permission denied, request again");
                requestPermission();
            } else {
                Log.d(TAG, "permission denied, and opted 'Never ask again'");

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("Please enable permission in settings, to serve you better");
                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
                dialog.create().show();
            }
        } else {
            timer.start();
            Log.d(TAG, "permission granted");
        }
    }

    public void requestPermission() {
        try {
            String[] permissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA};
            requestPermissions(permissionList, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
