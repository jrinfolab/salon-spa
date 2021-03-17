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

import com.jrinfolab.beautyshop.helper.Preference;
import com.jrinfolab.beautyshop.ui.AddBranch;
import com.jrinfolab.beautyshop.ui.account.LoginActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND = 100; // 1 Second
    private static final int HOLD_TIME = 1 * SECOND; // 3 Seconds
    private static final String TAG = "MainActivity";

    private Context mContext;

    boolean isLoggedIn = false;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private String[] mPermisssionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        isLoggedIn = Preference.isLoggedId(mContext);

        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    CountDownTimer timer = new CountDownTimer(HOLD_TIME, HOLD_TIME / SECOND) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (isLoggedIn) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, LoginActivity.class);
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

        if (requestCode == PERMISSIONS_REQUEST_CODE) {

            boolean isDenied = false;
            boolean isBlocked = false;

            for (int i = 0; i < permissions.length; i++) {

                String permissionItem = permissions[i];

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    isDenied = true;

                    boolean canRequestAgain = shouldShowRequestPermissionRationale(permissionItem);

                    if (!canRequestAgain) {
                        isBlocked = true;
                    }
                }
            }

            if (!isDenied && !isBlocked) {
                timer.start();
            } else if (isDenied && isBlocked) {
                showBlockedDialog();
            } else if (isDenied && !isBlocked) {
                showDeniedDialog();
            }
        }
    }

    public void checkPermission() {

        boolean isDenied = false;

        ArrayList<String> deniedPermissionList = new ArrayList<>();

        for (String permissionItem : mPermisssionList) {
            int result = mContext.checkSelfPermission(permissionItem);
            if (result == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, permissionItem + " : Granted");
            } else {
                deniedPermissionList.add(permissionItem);
                isDenied = true;
                Log.d(TAG, permissionItem + " : Denied");
            }
        }

        if (isDenied) {
            requestPermissions(deniedPermissionList.toArray(new String[deniedPermissionList.size()]), PERMISSIONS_REQUEST_CODE);
        } else {
            timer.start();
        }
    }

    private void showDeniedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(getString(R.string.deny_permission_content));
        dialog.setCancelable(false);// Cannot close the dialog, as it is mandatory
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermission();
            }
        });
        dialog.create().show();
    }

    private void showBlockedDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage(getString(R.string.blocked_permission_content));
        dialog.setCancelable(false);// Cannot close the dialog, as it is mandatory
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                finish();
            }
        });
        dialog.create().show();
    }
}
