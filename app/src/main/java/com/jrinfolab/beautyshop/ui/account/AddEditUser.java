package com.jrinfolab.beautyshop.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.network.ServerUrl;
import com.jrinfolab.beautyshop.view.LoaderButton;

import org.json.JSONObject;

import java.util.concurrent.locks.ReadWriteLock;

public class AddEditUser extends AppCompatActivity {

    private TextInputEditText mEditName, mEditPhone, mEditEmail;
    private MaterialButton mBtnVerifyPhone, mBtnVerifyEmail;
    private Button mActionLogin;
    private TextInputLayout mIplName, mIplPhone, mIplEmail;
    private ProgressBar mProgressBar;
    private LoaderButton mLoaderButton;

    private Context mContext;
    private RequestQueue mRequestQueue;

    private boolean isPhoneVerified = true;
    private boolean isEmailVerified = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        mEditName = findViewById(R.id.text_name);
        mEditPhone = findViewById(R.id.text_phone);
        mEditEmail = findViewById(R.id.text_email);

        mIplName = findViewById(R.id.text_ipl_name);
        mIplPhone = findViewById(R.id.text_ipl_phone);
        mIplEmail = findViewById(R.id.text_ipl_email);

        mBtnVerifyPhone = findViewById(R.id.verify_phone);
        mBtnVerifyEmail = findViewById(R.id.verify_email);

        mProgressBar = findViewById(R.id.progress_circular);
        mLoaderButton = findViewById(R.id.loader_button);
        mActionLogin = findViewById(R.id.action_login);

        mLoaderButton.setText("Next", "Getting OTP Code", false);

        mLoaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(mContext, v);
                formValidation();
            }
        });


        mBtnVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtpActivity.class);
                intent.putExtra(Constant.DATA1, Constant.REQ_CODE_PHONE_VERIFICATION);
                intent.putExtra(Constant.DATA2, mEditPhone.getText().toString());
                startActivityForResult(intent, Constant.REQ_CODE_PHONE_VERIFICATION);
            }
        });

        mBtnVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtpActivity.class);
                intent.putExtra(Constant.DATA1, Constant.REQ_CODE_EMAIL_VERIFICATION);
                intent.putExtra(Constant.DATA2, mEditEmail.getText().toString());
                startActivityForResult(intent, Constant.REQ_CODE_EMAIL_VERIFICATION);
            }
        });


        mActionLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });

        mEditPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    formValidation();
                    handled = true;
                }
                return handled;
            }
        });

        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mIplName.setErrorEnabled(false);
            }
        });

        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                boolean isNumberValid = Util.isValidPhone(editable.toString());

                mBtnVerifyPhone.setVisibility(isNumberValid ? View.VISIBLE : View.GONE);

                mIplPhone.setErrorEnabled(false);
                mIplPhone.setHelperText("");
                isPhoneVerified = false;
            }
        });

        mEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                boolean isValid = Util.isValidEmail(editable.toString());

                mBtnVerifyEmail.setVisibility(isValid ? View.VISIBLE : View.GONE);

                mIplEmail.setErrorEnabled(false);
                mIplEmail.setHelperText("");
                isEmailVerified = false;
            }
        });

        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    private void formValidation() {

        String name = mEditName.getText().toString();
        String phone = mEditPhone.getText().toString();
        String email = mEditEmail.getText().toString();
        boolean isValidName = !TextUtils.isEmpty(name);
        boolean isValidPhone = Util.isValidPhone(phone);
        boolean isValidEmail = Util.isValidEmail(email);

        if (!isValidName) mIplName.setError("Enter valid name");

        if (isValidPhone) {
            if (!isPhoneVerified) mIplPhone.setError("Verify phone number");
        } else {
            mIplPhone.setError("Enter valid phone number");
        }

        // Email is optional
        if (email.length() > 0) {
            if (isValidEmail) {
                if (isEmailVerified) mIplEmail.setError("Verify email");
            } else {
                mIplEmail.setError("Enter valid email");
            }
        } else {
            isValidEmail = true;
            isEmailVerified = true;
        }

        if (isValidName && isValidPhone && isValidEmail && isPhoneVerified && isEmailVerified) {
            makeServerCall();
        }
    }

    private void makeServerCall() {
        Intent intent = new Intent(mContext, AddEditBrand.class);
        intent.putExtra(Constant.PHONE, mEditPhone.getText());
        startActivity(intent);
    }

    int reqType = Request.Method.GET;
    String url = ServerUrl.BASE_URL;
    private JsonObjectRequest otpRequest = new JsonObjectRequest(reqType, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                    mLoaderButton.showLoader(false);

                    Intent intent = new Intent(mContext, AddEditBrand.class);
                    intent.putExtra(Constant.PHONE, mEditPhone.getText());
                    startActivity(intent);

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constant.REQ_CODE_EMAIL_VERIFICATION) {
                isEmailVerified = true;

                mBtnVerifyEmail.setVisibility(View.GONE);
                mIplEmail.setHelperText("Email verified");
                boolean result = data.getBooleanExtra(Constant.DATA2, false);
                Util.showToast(mContext, "Email verification : " + result);
            }

            if (requestCode == Constant.REQ_CODE_PHONE_VERIFICATION) {
                isPhoneVerified = true;
                mIplPhone.setHelperText("Phone number verified");
                mBtnVerifyPhone.setVisibility(View.GONE);
                boolean result = data.getBooleanExtra(Constant.DATA2, false);
                Util.showToast(mContext, "Phone verification : " + result);
            }
        }

    }
}
