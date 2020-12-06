package com.jrinfolab.beautyshop.helper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static File createTempImageFile(Context context) {
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(imageFileName, ".jpeg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static String[] getStringUriList(List<Uri> uriList) {
        String[] stringUriList = new String[uriList.size()];
        for (int i = 0; i < uriList.size(); i++) {
            stringUriList[i] = (uriList.get(i)).toString();
        }
        return stringUriList;
    }


    public static String copyImage(Context context, Uri uri) {
        try {
            File file = FileUtil.createTempImageFile(context);
            if (file == null) return null;
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
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
