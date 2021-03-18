package com.jrinfolab.beautyshop.ui.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.network.MyVolley;
import com.jrinfolab.beautyshop.network.ServerUrl;
import com.jrinfolab.beautyshop.ui.budget.CategoryList;
import com.jrinfolab.beautyshop.view.LoaderButton;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mEditPhone, mEditPassword;
    private Button mActionRegister;
    private TextInputLayout tiplPhone, tiplPassword;
    private LinearLayout mLayoutPhone;
    private ProgressBar mProgressBar;
    private LoaderButton mLoaderButtonLogin, mLoaderButtonOtp;

    private Context mContext;
    private TextView testView;

    private String mEnteredPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        mEditPhone = findViewById(R.id.text_phone);
        mEditPassword = findViewById(R.id.text_password);
        mLayoutPhone = findViewById(R.id.layout_phone);
        tiplPhone = findViewById(R.id.text_ipl_phone);
        tiplPassword = findViewById(R.id.ipl_password);
        mProgressBar = findViewById(R.id.progress_circular);
        mLoaderButtonLogin = findViewById(R.id.loader_button_login);
        mLoaderButtonOtp = findViewById(R.id.loader_button_otp);
        mActionRegister = findViewById(R.id.action_register);

        mLoaderButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;

                String phoneEmail = mEditPhone.getText().toString();
                String password = mEditPassword.getText().toString();

                boolean isValidEmail = Util.isValidEmail(phoneEmail);
                boolean isValidPhone = Util.isValidPhone(phoneEmail);

                if (!isValidEmail && !isValidPhone) {
                    tiplPhone.setError("Enter valid phone number or email id");
                    validation = false;
                }

                if (!Util.isValidPassword(password)) {
                    validation = false;
                    tiplPassword.setError("Enter valid password");
                }

                if (validation) makeServerCall();
            }
        });

        mLoaderButtonOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;

                String phoneEmail = mEditPhone.getText().toString();

                boolean isValidEmail = Util.isValidEmail(phoneEmail);
                boolean isValidPhone = Util.isValidPhone(phoneEmail);

                if (!isValidEmail && !isValidPhone) {
                    tiplPhone.setError("Enter valid phone number or email id");
                    validation = false;
                }
                if (validation) makeServerCall();

            }
        });

        mLoaderButtonLogin.setText("Login", "Please wait...", false);
        mLoaderButtonOtp.setText("Receive OTP", "Please wait...", false);

        mActionRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CategoryList.class));
            }
        });

        mEditPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    makeServerCall();
                    handled = true;
                }
                return handled;
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
                tiplPhone.setErrorEnabled(false);
            }
        });

        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tiplPassword.setErrorEnabled(false);
            }
        });
    }

    private void makeServerCall() {
        mEnteredPhoneNumber = mEditPhone.getText().toString();
        if (mEnteredPhoneNumber.length() < 10) {
            tiplPhone.setError("Please enter 10 digit mobile number");
        } else {
            mLoaderButtonLogin.showLoader(true);
            MyVolley.getInstance(mContext).addToRequestQueue(otpRequest);
        }
    }

    private JsonObjectRequest otpRequest = new JsonObjectRequest(
            Request.Method.GET,
            ServerUrl.BASE_URL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    mLoaderButtonLogin.showLoader(false);
                    Intent intent = new Intent(mContext, OtpActivity.class);
                    intent.putExtra(Constant.PHONE, mEnteredPhoneNumber);
                    startActivity(intent);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
}
