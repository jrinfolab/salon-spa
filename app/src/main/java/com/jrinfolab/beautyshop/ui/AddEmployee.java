package com.jrinfolab.beautyshop.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.db.DbHelper;
import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.helper.FileUtil;
import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.pojo.Branch;
import com.jrinfolab.beautyshop.pojo.Employee;
import com.jrinfolab.beautyshop.view.LoaderButton;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.xmlpull.v1.XmlPullParser.TYPES;

public class AddEmployee extends AppCompatActivity {

    private ImageView mUserImage, mActionCaptureImage, mActionBrowseImage;

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_PICK = 101;

    private Uri mImageUri;
    private String mImagePath;

    private TextInputLayout mIplName, mIplPhone, mIplYoe, mIplAddress;
    private TextInputEditText mEditName, mEditPhone, mEditYoe, mEditAddress;
    private ScrollView mRootView;
    private AppCompatAutoCompleteTextView mBranchList;

    private LoaderButton mLoaderButton;

    private Context mContext;

    private boolean mIsUpdate;
    private Employee mUpdateEmployee;
    String[] mBranchIds;
    String[] mBranchNames;
    int mBranchIndex;

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee);

        mContext = this;

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait ....");

        mIsUpdate = getIntent().getBooleanExtra(Constant.IS_UPDATE, false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mIsUpdate ? "Update Employee" : "Add Employee");

        mIplName = findViewById(R.id.ipl_name);
        mIplPhone = findViewById(R.id.ipl_phone);
        mIplYoe = findViewById(R.id.ipl_yoe);
        mIplAddress = findViewById(R.id.ipl_address);

        mEditName = findViewById(R.id.edit_name);
        mEditPhone = findViewById(R.id.edit_phone);
        mEditYoe = findViewById(R.id.edit_yoe);
        mEditAddress = findViewById(R.id.edit_address);

        mRootView = findViewById(R.id.root_layout);
        mBranchList = findViewById(R.id.branch);

        mLoaderButton = findViewById(R.id.loader_button);

        mUserImage = findViewById(R.id.user_image);
        mActionCaptureImage = findViewById(R.id.capture_image);
        mActionBrowseImage = findViewById(R.id.browse_image);

        mEditName.addTextChangedListener(mTextWatcher);
        mEditPhone.addTextChangedListener(mTextWatcher);
        mEditYoe.addTextChangedListener(mTextWatcher);
        mEditAddress.addTextChangedListener(mTextWatcher);

        mLoaderButton.setText(mIsUpdate ? "Update Employee" : "Add Employee", "Please wait ....", false);

        mLoaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formValidation();
            }
        });

        mActionCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {

                    // Create the File where the photo should go
                    File imageFile = Util.createTempImageFile(mContext);

                    // Continue only if the File was successfully created
                    if (imageFile != null) {
                        mImageUri = FileProvider.getUriForFile(mContext, "fileprovider", imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        mActionBrowseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            }
        });

        mBranchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBranchIndex = position;
            }
        });

        if (mIsUpdate) setOldValues();

        setGroupInfo();
    }

    private void setOldValues() {
        mUpdateEmployee = DbHelper.getEmployee(mContext, getIntent().getStringExtra(Constant.DATA1));
        mEditName.setText(mUpdateEmployee.getName());
        mEditAddress.setText(mUpdateEmployee.getAddress());
        mEditPhone.setText(mUpdateEmployee.getPhone());
        mEditYoe.setText(mUpdateEmployee.getYoe()+"");
        mImagePath = mUpdateEmployee.getImageUrl();
        updatePhotoUi(mImagePath);
    }

    private void setGroupInfo() {
        List<Branch> branches = DbHelper.getBranchList(mContext);
        if (branches != null && branches.size() > 0) {
            mBranchIds = new String[branches.size()];
            mBranchNames = new String[branches.size()];
            for (int i = 0; i < branches.size(); i++) {
                Branch branch = branches.get(i);
                mBranchIds[i] = branch.getId();
                mBranchNames[i] = branch.getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.dropdown_menu_popup_item, mBranchNames);
            mBranchList.setAdapter(adapter);

            if (mIsUpdate) {
                mBranchIndex = Arrays.asList(mBranchIds).indexOf(mUpdateEmployee.getBranchId());
                mBranchList.setText(mBranchList.getAdapter().getItem(mBranchIndex).toString(), false);
            } else {
                mBranchList.setText(mBranchList.getAdapter().getItem(0).toString(), false);
            }
        }
    }

    private void formValidation() {

        mLoaderButton.showLoader(true);

        String name = mEditName.getText().toString();
        String phone = mEditPhone.getText().toString();
        String yoe = mEditYoe.getText().toString();
        String address = mEditAddress.getText().toString();

        boolean validated = true;

        if (name.length() < 5) {
            mIplName.setError("Enter valid name");
            validated = false;
        }

        if (phone.length() < 10) {
            mIplPhone.setError("Enter valid phone number");
            validated = false;
        }

        if (yoe.length() <= 0 || Integer.parseInt(yoe) > 50 || Integer.parseInt(yoe) <= 0) {
            mIplYoe.setError("Seems wrong experience");
            validated = false;
        }

        if (address.length() < 15) {
            mIplAddress.setError("Enter valid address");
            validated = false;
        }

        if (mImagePath == null) {
            showSnackBar("Please Add Employee Image");
            validated = false;
        }

        if (!validated) {
            mLoaderButton.showLoader(false);
            return;
        }

        Employee employee = new Employee();
        employee.setId(mIsUpdate ? mUpdateEmployee.getId() : String.valueOf(System.currentTimeMillis()));
        employee.setName(name);
        employee.setPhone(phone);
        employee.setYoe(Integer.parseInt(yoe));
        employee.setAddress(address);
        employee.setImageUrl(mImagePath);
        employee.setBranchId(mBranchIds[mBranchIndex]);
        employee.setBranchName(mBranchNames[mBranchIndex]);

        // TODO : Do db operation in background thread
        if (mIsUpdate) {
            DbHelper.updateEmployee(mContext, employee);
        } else {
            DbHelper.addEmployee(mContext, employee);
        }
        mLoaderButton.showLoader(false);
        finish();
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


    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mEditName.getText().hashCode() == editable.hashCode()) {
                mIplName.setErrorEnabled(false);
            } else if (mEditPhone.getText().hashCode() == editable.hashCode()) {
                mIplPhone.setErrorEnabled(false);
            } else if (mEditYoe.getText().hashCode() == editable.hashCode()) {
                mIplYoe.setErrorEnabled(false);
            } else if (mEditAddress.getText().hashCode() == editable.hashCode()) {
                mIplAddress.setErrorEnabled(false);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            copyImageFile();
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            copyImageFile();
        }
    }

    private void copyImageFile() {
        // Copy images
        mDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mImagePath = FileUtil.copyImage(mContext, mImageUri);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updatePhotoUi(mImagePath);
                        mDialog.dismiss();
                    }
                });
            }
        });

        thread.start();
    }

    private void updatePhotoUi(String imagePath) {
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                mUserImage.setImageBitmap(myBitmap);
            }
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG);
             /*   .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });*/
        snackbar.show();
    }
}