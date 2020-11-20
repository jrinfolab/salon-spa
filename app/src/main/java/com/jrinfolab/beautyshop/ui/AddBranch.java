package com.jrinfolab.beautyshop.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jrinfolab.beautyshop.Constant;
import com.jrinfolab.beautyshop.R;

import java.lang.reflect.Type;
import java.util.List;

public class AddBranch extends AppCompatActivity {

    private static final String TAG = "Add Branch : ";
    private TextView mImageCount;
    private Button mActionSelectPhotos;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_branch);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Branch");

        mImageCount = findViewById(R.id.image_count);
        mActionSelectPhotos = findViewById(R.id.select_photos);

        mActionSelectPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageSlider.class);
                startActivityForResult(intent, Constant.REQ_CODE_IMAGE_LIST);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == Constant.REQ_CODE_IMAGE_LIST) {
            int imageCount = data.getIntExtra(Constant.BRANCH_IMAGE_COUNT, 0);
            if (imageCount > 0) {
                mImageCount.setText(imageCount + " Images Selected");
                mActionSelectPhotos.setText("Add / Delete Images");
            } else {
                mImageCount.setText("No Images Selected");
                mActionSelectPhotos.setText("Add Images");
            }
        }
    }
}