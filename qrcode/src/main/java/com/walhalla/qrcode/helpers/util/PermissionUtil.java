package com.walhalla.qrcode.helpers.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.Fragment;

import com.walhalla.qrcode.app.RunApp;

public class PermissionUtil {
    public static final int REQUEST_CODE_PERMISSION_DEFAULT = 1;
    private static PermissionUtil sInstance;

    private PermissionUtil() {

    }

    public static PermissionUtil on() {
        if (sInstance == null) {
            sInstance = new PermissionUtil();
        }

        return sInstance;
    }

    public synchronized boolean requestPermission(Activity activity, String... permissions) {
        return requestPermission(null, activity,
                REQUEST_CODE_PERMISSION_DEFAULT, Arrays.asList(permissions));
    }

    public synchronized boolean requestPermission(Fragment fragment, String... permissions) {
        return requestPermission(fragment, null, REQUEST_CODE_PERMISSION_DEFAULT, Arrays.asList(permissions));
    }

    public static final int REQUEST_CODE_TO_PRINT = 3;
    public static final int REQUEST_CODE_TO_SAVE = 2;
    public static final int REQUEST_CODE_TO_SHARE = 1;


    public synchronized boolean requestPrintPermission(Activity activity) {
        String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {//>30
            return true;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return requestPermission(null, activity, REQUEST_CODE_TO_PRINT, Collections.singletonList(write_perm));
    }

    public synchronized boolean requestSharePermission(Activity activity) {
        String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {//>30
            return true;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return requestPermission(null, activity, REQUEST_CODE_TO_SHARE, Collections.singletonList(write_perm));
    }

    public synchronized boolean requestSavePermission(Activity activity) {
        String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {//>30
            return true;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return requestPermission(null, activity, REQUEST_CODE_TO_SAVE, Collections.singletonList(write_perm));
    }

    public synchronized boolean requestPermission(Fragment fragment, int requestCode, String... permissions) {
        return requestPermission(fragment, null, requestCode, Arrays.asList(permissions));
    }

    private boolean requestPermission(Fragment fragment, Activity activity,
                                      int requestCode, List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        List<String> permissionsNotTaken = new ArrayList<>();

        for (int i = 0; i < permissions.size(); i++) {
            if (!isAllowed(permissions.get(i))) {
                permissionsNotTaken.add(permissions.get(i));
            }
        }

        if (permissionsNotTaken.isEmpty()) {
            return true;
        }
        String[] mmm = permissionsNotTaken.toArray(new String[permissionsNotTaken.size()]);
        System.out.println("@@@@" + permissionsNotTaken);
        if (fragment == null) {
            activity.requestPermissions(mmm, requestCode);
        } else {
            fragment.requestPermissions(mmm, requestCode);
        }

        return false;
    }

    boolean isAllowed(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return RunApp.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
