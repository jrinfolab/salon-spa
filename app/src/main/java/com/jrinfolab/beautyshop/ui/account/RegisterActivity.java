package com.jrinfolab.beautyshop.ui.account;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.network.ServerUrl;
import com.jrinfolab.beautyshop.view.LoaderButton;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mEditPhone;
    private Button mActionLogin;
    private TextInputLayout tiplPhone;
    private LinearLayout mLayoutPhone;
    private ProgressBar mProgressBar;
    private LoaderButton mLoaderButton;

    private Context mContext;
    private RequestQueue mRequestQueue;
    private TextView testView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;

        mEditPhone = findViewById(R.id.text_phone);
        mLayoutPhone = findViewById(R.id.layout_phone);
        tiplPhone = findViewById(R.id.text_ipl_phone);
        mProgressBar = findViewById(R.id.progress_circular);
        mLoaderButton = findViewById(R.id.loader_button);
        mActionLogin = findViewById(R.id.action_login);

        mLoaderButton.setText("Receive OTP", "Getting OTP Code", false);

        mLoaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(mContext, v);
                makeServerCall();
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

        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    private void makeServerCall() {
        String phone = mEditPhone.getText().toString();
        if (phone.length() < 10) {
            tiplPhone.setError("Please enter 10 digit mobile number");
        } else {
            mLoaderButton.showLoader(true);
            mRequestQueue.add(otpRequest);
        }
    }

    int reqType = Request.Method.GET;
    String url = ServerUrl.BASE_URL;
    private JsonObjectRequest otpRequest = new JsonObjectRequest(reqType, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                    mLoaderButton.showLoader(false);

                    Intent intent = new Intent(mContext, OtpActivity.class);
                    intent.putExtra(Constant.PHONE, mEditPhone.getText());
                    startActivity(intent);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
}
