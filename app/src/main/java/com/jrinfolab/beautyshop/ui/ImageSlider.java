package com.jrinfolab.beautyshop.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.jrinfolab.beautyshop.Constant;
import com.jrinfolab.beautyshop.Preference;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageSlider extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_PICK = 101;
    private static final String TAG = "ImageSlider";

    private TextView mNoImage;
    private Button mActionBrowseImage, mActionCaptureImage;
    private ViewPager mViewPager;

    private MenuItem menuDelete, menuSave;

    private ImageAdapter imageAdapter;

    String defaultHeaderTitle = "Add Image";
    private List<String> imageUriList;

    private Context mContext;
    private Uri mCameraImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slider);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(defaultHeaderTitle);

        imageUriList = new ArrayList<String>();

        mNoImage = findViewById(R.id.no_image);
        mActionBrowseImage = findViewById(R.id.browse_image);
        mActionCaptureImage = findViewById(R.id.capture_image);
        mViewPager = findViewById(R.id.view_pager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle((position + 1 + "/" + imageUriList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mActionBrowseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICK);
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
                        mCameraImageUri = FileProvider.getUriForFile(mContext, "fileprovider", imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] storedImageList = Preference.getBranchImage(mContext);
        if (storedImageList != null && storedImageList.length > 0) {
            imageUriList = new ArrayList(Arrays.asList(storedImageList));
        }

        showSelectedImages(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.menu_save: {
                String[] imageList = imageUriList.toArray(new String[imageUriList.size()]);
                Preference.setBranchImage(mContext, imageList);
                Intent intent = new Intent();
                intent.putExtra(Constant.BRANCH_IMAGE_COUNT, imageList.length);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            break;

            case R.id.menu_delete: {
                int deletePos = mViewPager.getCurrentItem();
                imageUriList.remove(deletePos);
                if (imageUriList.size() > 0) {
                    int curPos = (imageUriList.size() == deletePos ? imageUriList.size() - 1 : deletePos);
                    showSelectedImages(curPos);
                } else {
                    updateViews();
                }
            }
            break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_slider, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuSave = menu.findItem(R.id.menu_save);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == REQUEST_IMAGE_PICK) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                }
            } else if (data.getData() != null) {
                imageUriList.add(data.getData().toString());
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (mCameraImageUri != null) imageUriList.add(mCameraImageUri.toString());
        }

        showSelectedImages(imageUriList.size() - 1);
    }

    private void showSelectedImages(int curImgPos) {
        if (imageUriList.size() > 0) {
            imageAdapter = new ImageAdapter();
            mViewPager.setAdapter(imageAdapter);
            mViewPager.setCurrentItem(curImgPos);

            getSupportActionBar().setTitle((mViewPager.getCurrentItem() + 1 + "/" + imageUriList.size()));
        }
        updateViews();
    }

    private void updateViews() {
        if (imageUriList.size() > 0) {
            mNoImage.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
        } else {
            mNoImage.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            getSupportActionBar().setTitle(defaultHeaderTitle);
        }
    }

    public class ImageAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER);

            Uri imageUri = Uri.parse(imageUriList.get(position));

            Log.d(TAG, imageUri.toString());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            return imageUriList.size();
        }
    }
}