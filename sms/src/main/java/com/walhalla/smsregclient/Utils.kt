package com.walhalla.smsregclient

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

/**
 * Created by combo on 29.03.2017.
 */
object Utils {
    /*  public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
    public static final String ACTION_MYINTENTSERVICE = "ru.timgor.alerttest.RESPONSE";
    public static final String TAG = "AlertTest";
    public static final String SUCCESS = "success";
    public static final String AUTOMATIC = "chbAutomatic";
*/
    //    public static Interceptor interceptor() {
    //        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    //        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    //        return logging;
    //    }
    @JvmStatic
    fun copyToClipboard(context: Context, msg: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", msg)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(
            context,
            context.getString(R.string.copy_to_buffer_handler, msg),
            Toast.LENGTH_SHORT
        ).show()
    }
}
