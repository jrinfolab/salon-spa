package com.jrinfolab.beautyshop.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.jrinfolab.beautyshop.HomeActivity;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.network.ServerUrl;
import com.jrinfolab.beautyshop.view.LoaderButton;

import org.json.JSONObject;

public class AddEditBrand extends AppCompatActivity {

    private TextInputEditText mEditName;
    private TextInputLayout mIplName;
    private LoaderButton mLoaderButton;

    private Context mContext;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_brand);

        mContext = this;

        mEditName = findViewById(R.id.text_name);
        mIplName = findViewById(R.id.text_ipl_name);
        mLoaderButton = findViewById(R.id.loader_button);

        mLoaderButton.setText("Register", "Please wait ...", false);

        mLoaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(mContext, v);
                formValidation();
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


        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    private void formValidation() {

        String name = mEditName.getText().toString();
        boolean isValidName = !TextUtils.isEmpty(name);

        if (!isValidName) mIplName.setError("Enter valid name");

        if (isValidName) {
            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
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
                    startActivity(intent);

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
}
