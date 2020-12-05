package com.jrinfolab.beautyshop.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageViewer extends AppCompatActivity {

    private static final String TAG = "ImageSlider";

    private ViewPager mViewPager;

    private MenuItem menuDelete, menuSave;

    private ImageAdapter imageAdapter;

    private String[] mImagePathList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);
        Log.d(TAG, "onCreate");

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImagePathList = getIntent().getStringArrayExtra(Constant.IMAGE_PATH_LIST);

        mViewPager = findViewById(R.id.view_pager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle((position + 1 + "/" + mImagePathList.length));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mImagePathList != null && mImagePathList.length > 0) {
            imageAdapter = new ImageAdapter(new ArrayList(Arrays.asList(mImagePathList)));
            mViewPager.setAdapter(imageAdapter);
            mViewPager.setCurrentItem(0);
            getSupportActionBar().setTitle((mViewPager.getCurrentItem() + 1 + "/" + mImagePathList.length));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_image_slider, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuSave = menu.findItem(R.id.menu_save);
        menuDelete.setVisible(false);
        menuSave.setVisible(false);
        return true;
    }

    public class ImageAdapter extends PagerAdapter {

        List<String> imagePathList;

        public ImageAdapter(List<String> path) {
            imagePathList = path;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            File file = new File(imagePathList.get(position));

            Uri uri = Uri.fromFile(file);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
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
            return imagePathList.size();
        }
    }
}