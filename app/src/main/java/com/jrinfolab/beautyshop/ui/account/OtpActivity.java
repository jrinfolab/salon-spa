package com.jrinfolab.beautyshop.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.HomeActivity;
import com.jrinfolab.beautyshop.helper.Preference;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Util;

public class OtpActivity extends AppCompatActivity {

    private static final String TAG = "OtpActivity";

    private static final int TIMER_DURATION = 10000;
    private static final int OTP_LENGTH = 4;

    private MaterialTextView mTextPhone, mTextTimer;
    private EditText mEditOtp;
    private Button mBtnResend, mBtnVerify, mBtnEditPhone;
    private RelativeLayout mLayAutoCapture, mLayResendOtp;

    CountDownTimer mTimer = new CountDownTimer(TIMER_DURATION, 1000) {
        @Override
        public void onTick(long millis) {
            int remain = (int) millis / 1000;
            String text = "00:" + (remain < 10 ? ("0" + remain) : remain);
            mTextTimer.setText(text);
        }

        @Override
        public void onFinish() {
            mLayResendOtp.setVisibility(View.VISIBLE);
            mLayAutoCapture.setVisibility(View.GONE);
        }
    };

    private Intent mIntent;
    private Context mContext;
    private String mPhoneNumber;

    private boolean mIsMailOtp, mIsPhoneOtp;
    private String mPhoneOrMail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mContext = this;
        mIntent = getIntent();

        mTextPhone = findViewById(R.id.text_phone);
        mTextTimer = findViewById(R.id.timer_text);
        mBtnResend = findViewById(R.id.btn_resend_otp);
        mBtnVerify = findViewById(R.id.btn_verify_otp);
        mBtnEditPhone = findViewById(R.id.btn_edit_phone);
        mLayAutoCapture = findViewById(R.id.layout3);
        mLayResendOtp = findViewById(R.id.layout4);
        mEditOtp = findViewById(R.id.edit_otp);

        mIsMailOtp = mIntent.getIntExtra(Constant.DATA1, -1) == Constant.REQ_CODE_EMAIL_VERIFICATION;
        mIsPhoneOtp = mIntent.getIntExtra(Constant.DATA1, -1) == Constant.REQ_CODE_PHONE_VERIFICATION;
        mPhoneOrMail = mIntent.getStringExtra(Constant.DATA2);

        if (mIsMailOtp) {
            mLayAutoCapture.setVisibility(View.GONE);
            mLayResendOtp.setVisibility(View.VISIBLE);
        } else {
            mLayResendOtp.setVisibility(View.GONE);
            mLayAutoCapture.setVisibility(View.VISIBLE);
        }

        mTextPhone.setText(mPhoneOrMail);

        mBtnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mIsPhoneOtp) {
                    mLayResendOtp.setVisibility(View.GONE);
                    mLayAutoCapture.setVisibility(View.VISIBLE);
                    mEditOtp.setText("");
                    mBtnVerify.setEnabled(false);
                    mTimer.start();
                }

                // Trigger server call to resend otp
                Util.showToast(mContext, "Make server call");
            }
        });

        mBtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.hideKeyboard(mContext, view);
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA1, mIsMailOtp ? Constant.REQ_CODE_EMAIL_VERIFICATION : Constant.REQ_CODE_PHONE_VERIFICATION);
                intent.putExtra(Constant.DATA2, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mBtnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mEditOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String otp = editable.toString();
                boolean isValidOtp = otp.length() == OTP_LENGTH;
                mBtnVerify.setEnabled(isValidOtp);
                mEditOtp.setCursorVisible(!isValidOtp);
            }
        });

        mBtnVerify.setEnabled(false);

        if (mIsPhoneOtp) mTimer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        intent.putExtra(Constant.DATA1, mIsMailOtp ? Constant.REQ_CODE_EMAIL_VERIFICATION : Constant.REQ_CODE_PHONE_VERIFICATION);
        intent.putExtra(Constant.DATA2, false);
        setResult(RESULT_CANCELED, intent);
    }
}
