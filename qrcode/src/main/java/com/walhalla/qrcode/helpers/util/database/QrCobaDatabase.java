package com.walhalla.qrcode.helpers.util.database;

import android.content.Context;

import androidx.room.Database;
import com.walhalla.qrcode.R;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.helpers.model.CodeDao;

@Database(entities = {Code.class}, version = 1, exportSchema = false)
public abstract class QrCobaDatabase extends AppDatabase {

    private static volatile QrCobaDatabase sInstance;

    // Get a database instance
    public static synchronized QrCobaDatabase on() {
        return sInstance;
    }

    static synchronized void init(Context context) {

        if (sInstance == null) {
            synchronized (QrCobaDatabase.class) {
                sInstance = createDb(context, "scanner_code", QrCobaDatabase.class);
            }
        }
    }

    public abstract CodeDao codeDao();
}
