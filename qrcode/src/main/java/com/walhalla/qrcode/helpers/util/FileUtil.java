package com.walhalla.qrcode.helpers.util;

import android.app.Activity;

import android.os.Environment;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import com.walhalla.ui.DLog;

public class FileUtil {

    //Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ?Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_DOCUMENTS
    private static final String directoryType = "";//Environment.DIRECTORY_PICTURES;
    public static final int STORAGE_PERMISSION_CODE = 443;
    public static String fileNameSuffix = ".png";

    public static File getEmptyFile(Activity activity, String fileName) {

//            File storageDirectory = new File(
//                    Environment.getExternalStoragePublicDirectory(directoryType)
//                    , activity.getString(R.string.app_name)
//            );

        //update
        File sdCard = Environment.getExternalStorageDirectory();
        DLog.d("@@@" + sdCard.toString());

        boolean isDirectoryCreated;

        if (!sdCard.exists()) {
            isDirectoryCreated = sdCard.mkdirs();
        } else {
            isDirectoryCreated = true;
        }
        File file;
        if (isDirectoryCreated) {
            try {
                file = new File(sdCard, fileName);
                if (!file.exists()) {
                    boolean r = file.createNewFile();
                }
            } catch (IOException e) {
                if (!TextUtils.isEmpty(e.getMessage())) {
                    DLog.handleException(e);
                }
                return null;
            }
        } else {
            return null;
        }
        return file;
    }

//    public static File getEmptyFile(Activity activity, String fileNamePrefix,
//                                    String fileNameBody, String fileNameSuffix) {
//
//
//
//        if (isExternalStorageWritable()) {
//            String fileName = fileNamePrefix + fileNameBody + fileNameSuffix;
//
////            File storageDirectory = new File(
////                    Environment.getExternalStoragePublicDirectory(directoryType)
////                    , activity.getString(R.string.app_name)
////            );
//
//            //update
//            File sdCard = Environment.getExternalStorageDirectory();
//            DLog.d("@@@" + sdCard.toString());
//
//            boolean isDirectoryCreated;
//
//            if (!sdCard.exists()) {
//                isDirectoryCreated = sdCard.mkdirs();
//            } else {
//                isDirectoryCreated = true;
//            }
//            File file;
//            if (isDirectoryCreated) {
//                try {
//                    file = new File(sdCard, fileName);
//                    if (!file.exists()) {
//                        boolean r = file.createNewFile();
//                    }
//                } catch (IOException e) {
//                    if (!TextUtils.isEmpty(e.getMessage())) {
//                        DLog.handleException(e);
//                    }
//                    return null;
//                }
//            } else {
//                return null;
//            }
//
//            return file;
//        } else {
//            return null;
//        }
//    }

//    public static void requestStoragePermission(Activity activity) {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            new AlertDialog.Builder(activity)
//                    .setTitle(R.string.permission_title_needed)
//                    .setMessage(R.string.this_permission_is_needed)
//                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
//                            ActivityCompat.requestPermissions(
//                                    activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
//                    .setNegativeButton(android.R.string.cancel,
//                            (dialog, which) -> dialog.dismiss()).create().show();
//
//        } else {
//            ActivityCompat.requestPermissions(activity, new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//            }, STORAGE_PERMISSION_CODE);
//        }
//    }

    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
