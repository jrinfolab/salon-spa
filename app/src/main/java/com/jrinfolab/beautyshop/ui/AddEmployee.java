package com.jrinfolab.beautyshop.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.Util;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class AddEmployee extends AppCompatActivity {

    private TextInputLayout tilDob;
    private TextInputEditText editDob;
    private TextView mTextView, mImageInfo;
    private Button mActionSelectPhotos;
    private ViewPager mViewPager;
    private ImageView mImageView;

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_PICK = 101;

    private List<Uri> imageUriList;
    private int imageCurrentPosition = 0;
    private String currentPhotoPath;
    private Uri photoUri;
    private File imageFile;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Employee");

        mImageView = findViewById(R.id.user_image);
        tilDob = findViewById(R.id.ipl_dob);
        editDob = findViewById(R.id.edit_dob);

        tilDob.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(mContext, android.R.style.Theme_Holo_Dialog,
                        datePickListener, year, month, day);
                picker.getDatePicker().setSpinnersShown(true);
                picker.getDatePicker().setCalendarViewShown(false);

                picker.show();

            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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

    private DatePickerDialog.OnDateSetListener datePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            String dayString = String.format("%02d", day);
            String monthString = String.format("%02d", month + 1);
            editDob.setText(dayString + "/" + monthString + "/" + year);

            validateDate(calendar);
        }
    };

    private void selectImage() {

        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();
                if (position == 0) {
                    captureImage();
                } else if (position == 1) {
                    pickImage();
                }
            }
        });
        builder.show();
    }

    private void validateDate(Calendar date) {
        Calendar today = Calendar.getInstance();
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int todayMonth = today.get(Calendar.MONTH);
        int todayYear = today.get(Calendar.YEAR);

        String message = "Cool";
        if (today.getTimeInMillis() < date.getTimeInMillis()) {
            message = "Seems he did not born yet !!!";
        } else if (todayYear - date.get(Calendar.YEAR) < 19) {
            message = "Seems he is too young to work !!!";
        } else if (todayYear - date.get(Calendar.YEAR) > 60) {
            message = "Seems he is too old !!!";
        }

        tilDob.setError(message);
    }

    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            imageFile = Util.createTempImageFile(mContext);

            // Continue only if the File was successfully created
            if (imageFile != null) {
                Uri photoUri = FileProvider.getUriForFile(mContext, "fileprovider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void pickImage() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            updatePhotoUi();
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            imageFile = new File(getRealPathFromUri(data.getData()));
            updatePhotoUi();
        }
    }

    private void updatePhotoUi() {
        if (imageFile != null) {
            if (imageFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                mImageView.setImageBitmap(myBitmap);
            }
        }
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}