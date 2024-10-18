package com.walhalla.qrcode;


import static com.walhalla.abcsharedlib.Share.comPinterestEXTRA_DESCRIPTION;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.walhalla.abcsharedlib.Share;
import com.walhalla.ui.UConst;

import java.util.List;

public class IntentUtils {

    public static void shareCodeFromUri(Context context, Uri uri, String description) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (description == null || description.trim().isEmpty()) {
            description = "";
        }
        String packageName0 = context.getPackageName();
        intent.putExtra(Intent.EXTRA_TEXT, description);
        if (BuildConfig.DEBUG) {
            //intent.putExtra(Intent.EXTRA_EMAIL, aa);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Share.email});
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(com.walhalla.ui.R.string.app_name));
        intent.setType("image/*");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        //More Options
        intent.putExtra("url", UConst.GOOGLE_PLAY_CONSTANT + packageName0);
        intent.putExtra(comPinterestEXTRA_DESCRIPTION, description);

        //E/DatabaseUtils: Writing exception to parcel
        //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider uri content:
        //Android 11
        Intent chooser = Intent.createChooser(intent, context.getString(R.string.share_code_using));
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resInfoList.isEmpty()) {
            // Попробовать другой MIME тип
            intent.setType("image/png");
            chooser = Intent.createChooser(intent, context.getString(R.string.share_code_using));
            resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        }

        if (!resInfoList.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            chooser.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            context.startActivity(chooser);
        } else {
            Toast.makeText(context, R.string.no_app_available_to_share, Toast.LENGTH_SHORT).show();
        }
    }
}
