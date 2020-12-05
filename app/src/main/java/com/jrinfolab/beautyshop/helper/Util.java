package com.jrinfolab.beautyshop.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Util {

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

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static String[] getStringUriList(List<Uri> uriList) {
        String[] stringUriList = new String[uriList.size()];
        for (int i = 0; i < uriList.size(); i++) {
            stringUriList[i] = (uriList.get(i)).toString();
        }
        return stringUriList;
    }

    public static void copyFile(File from, File to) {
        try {
            FileInputStream inStream = new FileInputStream(from);
            FileOutputStream outStream = new FileOutputStream(to);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String[] data) {
        if (data != null && data.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i]).append(",");
            }
            return sb.toString();
        }
        return null;
    }

    public static String[] getStringArray(String data) {
        if (data != null) {
            return data.split(",");
        }
        return null;
    }


}
