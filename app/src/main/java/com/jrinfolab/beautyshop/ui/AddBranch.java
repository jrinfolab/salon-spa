package com.jrinfolab.beautyshop.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.helper.Preference;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.network.MyVolley;
import com.jrinfolab.beautyshop.network.ServerUrl;
import com.jrinfolab.beautyshop.pojo.Branch;
import com.jrinfolab.beautyshop.view.LoaderButton;

import org.json.JSONObject;

public class AddBranch extends AppCompatActivity {

    private static final String TAG = "Add Branch : ";

    private static int mImageCount = 0;

    private TextView mTvImageCount;
    private TextInputLayout mIplName, mIplAddress;
    private TextInputEditText mEditName, mEditAddress;
    private LoaderButton mLoaderButton;
    private Button mActionSelectPhotos;

    private Context mContext;

    private boolean isUpdate = false;
    private String updateBranchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_branch);

        mContext = this;

        isUpdate = getIntent().getBooleanExtra(Constant.IS_UPDATE, false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(isUpdate ? "Update Branch":"Add Branch");

        mIplName = findViewById(R.id.ipl_name);
        mIplAddress = findViewById(R.id.ipl_address);
        mEditName = findViewById(R.id.edit_name);
        mEditAddress = findViewById(R.id.edit_address);
        mLoaderButton = findViewById(R.id.loader_button);

        mTvImageCount = findViewById(R.id.image_count);
        mActionSelectPhotos = findViewById(R.id.select_photos);

        mActionSelectPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageSlider.class);
                startActivityForResult(intent, Constant.REQ_CODE_IMAGE_LIST);
            }
        });

        mLoaderButton.setText(isUpdate ? "Update Branch" : "Add Branch", "Please wait ....", false);

        mLoaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mEditAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mIplAddress.setErrorEnabled(false);
            }
        });

        if (isUpdate)  setOldValues();
    }

    private void setOldValues() {
        updateBranchId = getIntent().getStringExtra(Constant.DATA1);
        Branch branch = DbHelper.getBranch(mContext, updateBranchId);
        mEditName.setText(branch.getName());
        mEditAddress.setText(branch.getAddress());

        mTvImageCount.setText(branch.getPhotoList().length + " Images Selected");
        mActionSelectPhotos.setText("Edit Images");
    }

    private void formValidation() {

        mLoaderButton.showLoader(true);

        String name = mEditName.getText().toString();
        String address = mEditAddress.getText().toString();

        boolean validated = true;

        if (name.length() < 5) {
            mIplName.setError("Enter valid name");
            validated = false;
        }

        if (address.length() < 15) {
            mIplAddress.setError("Enter valid address");
            validated = false;
        }

        if (mImageCount <= 0) {
            Toast.makeText(mContext, "Please add images", Toast.LENGTH_LONG).show();
            validated = false;
        }

        if (!validated) {
            mLoaderButton.showLoader(false);
            return;
        }

        Branch branch = new Branch();
        branch.setId(isUpdate ? updateBranchId : String.valueOf(System.currentTimeMillis()));
        branch.setName(name);
        branch.setAddress(address);
        branch.setLat(1.0);
        branch.setLng(1.0);
        branch.setPhotoList(Preference.getBranchImage(mContext));

        // TODO : Do db operation in background thread
        if (isUpdate) {
            DbHelper.updateBranch(mContext, branch);
        } else {
            DbHelper.addBranch(mContext, branch);
        }
        mLoaderButton.showLoader(false);
        finish();
    }

    private void makeServerCall() {

        int reqType = Request.Method.POST;
        String url = ServerUrl.BASE_URL;
        JSONObject reqBody = null;

        MyVolley.getInstance(mContext).addToRequestQueue(new JsonObjectRequest(reqType, url, reqBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == Constant.REQ_CODE_IMAGE_LIST) {
            mImageCount = data.getIntExtra(Constant.BRANCH_IMAGE_COUNT, 0);
            if (mImageCount > 0) {
                mTvImageCount.setText(mImageCount + " Images Selected");
                mActionSelectPhotos.setText("Edit Images");
            } else {
                mTvImageCount.setText("No Images Selected");
                mActionSelectPhotos.setText("Add Images");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preference.setBranchImage(mContext, null);
    }
}