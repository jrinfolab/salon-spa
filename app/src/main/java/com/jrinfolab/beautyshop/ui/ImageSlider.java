package com.jrinfolab.beautyshop.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jrinfolab.beautyshop.helper.Constant;
import com.jrinfolab.beautyshop.helper.FileUtil;
import com.jrinfolab.beautyshop.helper.Preference;
import com.jrinfolab.beautyshop.R;
import com.jrinfolab.beautyshop.helper.Util;

import java.io.File;
import java.io.FileOutputStream;
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

    private String defaultHeaderTitle = "Add Image";

    private List<String> mImagePathList;
    private String mCameraImagePath;

    private Context mContext;

    ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slider);
        Log.d(TAG, "onCreate");

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(defaultHeaderTitle);

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait ....");

        mImagePathList = new ArrayList<String>();

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
                getSupportActionBar().setTitle((position + 1 + "/" + mImagePathList.size()));
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
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File imageFile = Util.createTempImageFile(mContext);
                    if (imageFile != null) {
                        mCameraImagePath = imageFile.getPath();
                        Uri uri = FileProvider.getUriForFile(mContext, "fileprovider", imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        String[] storedImageList = Preference.getBranchImage(mContext);
        if (storedImageList != null && storedImageList.length > 0) {
            mImagePathList = new ArrayList(Arrays.asList(storedImageList));
        }
        showSelectedImages(0);
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

            case R.id.menu_save: {

                Intent intent = new Intent();

                if (mImagePathList.size() > 0) {
                    String[] imageList = mImagePathList.toArray(new String[mImagePathList.size()]);
                    Preference.setBranchImage(mContext, imageList);
                    intent.putExtra(Constant.BRANCH_IMAGE_COUNT, imageList.length);
                } else {
                    Preference.setBranchImage(mContext, null);
                    intent.putExtra(Constant.BRANCH_IMAGE_COUNT, 0);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            break;

            case R.id.menu_delete:
                deleteImage();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_image_slider, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuSave = menu.findItem(R.id.menu_save);
        updateMenuView();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == REQUEST_IMAGE_PICK) {

            List<Uri> uriList = new ArrayList<>();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    uriList.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                uriList.add(data.getData());
            }

            if (uriList.size() > 0) copyImages(uriList);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            mImagePathList.add(mCameraImagePath);
            showSelectedImages(mImagePathList.size() - 1);
        }
    }

    private void deleteImage() {

        mDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final int deletePos = mViewPager.getCurrentItem();
                File file = new File(mImagePathList.get(deletePos));
                file.delete();
                mImagePathList.remove(deletePos);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mImagePathList.size() > 0) {
                            int curPos = (mImagePathList.size() == deletePos ? mImagePathList.size() - 1 : deletePos);
                            showSelectedImages(curPos);
                        } else {
                            updateViews();
                        }
                        mDialog.dismiss();
                    }
                });
            }
        });
        thread.start();
    }

    private void copyImages(final List<Uri> uriList) {

        mDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    for (Uri uri : uriList) {
                        File file = FileUtil.createTempImageFile(mContext);
                        if (file == null) return;
                        mImagePathList.add(file.getPath());
                        FileOutputStream fos = new FileOutputStream(file);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSelectedImages(mImagePathList.size() - 1);
                        mDialog.dismiss();
                    }
                });
            }
        });

        thread.start();
    }

    private void showSelectedImages(int curImgPos) {
        if (mImagePathList.size() > 0) {
            imageAdapter = new ImageAdapter(mImagePathList);
            mViewPager.setAdapter(imageAdapter);
            mViewPager.setCurrentItem(curImgPos);
            getSupportActionBar().setTitle((mViewPager.getCurrentItem() + 1 + "/" + mImagePathList.size()));
        }
        updateViews();
    }

    private void updateViews() {
        if (mImagePathList.size() > 0) {
            mNoImage.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
        } else {
            mNoImage.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            getSupportActionBar().setTitle(defaultHeaderTitle);
        }

        updateMenuView();
    }

    private void updateMenuView() {
        if (menuDelete != null && menuSave != null) {
            menuDelete.setVisible(mImagePathList.size() > 0);
        }
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