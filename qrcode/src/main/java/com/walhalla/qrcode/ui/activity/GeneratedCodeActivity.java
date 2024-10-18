package com.walhalla.qrcode.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import com.walhalla.qrcode.IntentUtils;
import com.walhalla.qrcode.R;
import com.walhalla.qrcode.databinding.ActivityGeneratedCodeBinding;
import com.walhalla.qrcode.helpers.constant.AppConstants;
import com.walhalla.qrcode.helpers.constant.IntentKey;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.helpers.util.FileUtil;
import com.walhalla.qrcode.helpers.util.PermissionUtil;
import com.walhalla.qrcode.helpers.util.ProgressDialogUtil;
import com.walhalla.qrcode.ui.SettingsActivity;
import com.walhalla.ui.DLog;

import org.jetbrains.annotations.NotNull;

public class GeneratedCodeActivity extends AppCompatActivity {


    //public static final String MIME_TYPE_JPG = "image/jpg";
    public static final String MIME_TYPE_PNG = "image/png";


    private ActivityGeneratedCodeBinding binding;
    private Menu mToolbarMenu;


    private Code mCurrentCode;
    private Bitmap mCurrentGeneratedCodeBitmap;

    private Uri mCurrentCodeFile0;
    private File mCurrentPrintedFile;

    private CompositeDisposable mCompositeDisposable;
    private String fileName;

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        mCompositeDisposable = compositeDisposable;
    }

    public File getCurrentPDF() {
        return mCurrentPrintedFile;
    }

    public void setCurrentPDF(File currentPrintedFile) {
        mCurrentPrintedFile = currentPrintedFile;
    }

    public Uri getCurrentCodeFile() {
        return mCurrentCodeFile0;
    }

    public void setCurrentCodeFile(Uri currentCodeFile) {
        mCurrentCodeFile0 = currentCodeFile;
    }


    public Menu getToolbarMenu() {
        return mToolbarMenu;
    }

    public void setToolbarMenu(Menu toolbarMenu) {
        mToolbarMenu = toolbarMenu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneratedCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setCompositeDisposable(new CompositeDisposable());

        getWindow().setBackgroundDrawable(null);
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        loadQRCode();
        setListeners();

        AdRequest mm = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("28964E2506C9A8C6400A9E8FF42D3486")
                .build();
        binding.b3.loadAd(mm);
    }

    private void setListeners() {
        binding.imageViewSave.setOnClickListener(v->{
            imageViewSave();
        });
        binding.imageViewShare.setOnClickListener(v->{
            if (PermissionUtil.on().requestSharePermission(this)) {
                if (getCurrentCodeFile() == null) {
                    storeCodeImage(false);
                } else {
                    String content = mCurrentCode.getContent();
                    IntentUtils.shareCodeFromUri(this, getCurrentCodeFile(), content);
                }
            }
        });
        binding.imageViewPrint.setOnClickListener(v->{
            imageViewPrint();
        });
    }

    private void imageViewSave() {
        if (PermissionUtil.on().requestSavePermission(this)) {
            if (getCurrentCodeFile() == null) {
                storeCodeImage(true);
            } else {
                makeSavedImage(this, getCurrentCodeFile(), R.string.generated_qr_code_already_exists);
                //makeSnack(this, R.string.generated_qr_code_already_exists);
            }
        }
    }

    private void imageViewPrint() {
        if (PermissionUtil.on().requestPrintPermission(this)) {
            if (getCurrentPDF() == null) {
                storeCodeDocument();
            } else {
//                        if (getCurrentCodeFile() != null) {
//                            makeSavedPdf(this, getCurrentPDF());
//                        }
//                        else {
                makeSnack(this, R.string.generated_qr_code_already_exists);
//                        }
            }
        }
    }

    private void loadQRCode() {
        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null && bundle.containsKey(IntentKey.MODEL)) {
                mCurrentCode = (bundle.getParcelable(IntentKey.MODEL));
            }

            String type = getResources().getStringArray(R.array.code_types)[mCurrentCode.getType()];

            String fileNameBody = String.format(Locale.ENGLISH, getString(R.string.file_name_body),
                    type.substring(0, type.indexOf(" Code")),
                    String.valueOf(System.currentTimeMillis()));
            fileName = AppConstants.PREFIX_IMAGE + fileNameBody + FileUtil.fileNameSuffix;

        }

        if (mCurrentCode != null) {
            ProgressDialogUtil.on().showProgressDialog(this);

            binding.textViewContent.setText(String.format(Locale.ENGLISH, getString(R.string.scanResultContent), mCurrentCode.getContent()));

            binding.textViewType.setText(String.format(Locale.ENGLISH, getString(R.string.code_type),
                    getResources().getStringArray(R.array.code_types)[mCurrentCode.getType()]));

            BarcodeFormat barcodeFormat;
            switch (mCurrentCode.getType()) {
                case Code.BAR_CODE:
                    barcodeFormat = BarcodeFormat.CODE_128;
                    break;

                case Code.QR_CODE:
                    barcodeFormat = BarcodeFormat.QR_CODE;
                    break;

                default:
                    barcodeFormat = null;
                    break;
            }

            if (barcodeFormat != null) {
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(mCurrentCode.getContent(), barcodeFormat, 1000, 1000);
                    binding.imageViewGeneratedCode.setImageBitmap(bitmap);
                    mCurrentGeneratedCodeBitmap = bitmap;
                } catch (Exception e) {
                    if (!TextUtils.isEmpty(e.getMessage())) {
                        Log.e(getClass().getSimpleName(), e.getMessage());
                    }
                }
            }

            ProgressDialogUtil.on().hideProgressDialog();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setToolbarMenu(menu);
        return true;
    }

    private void storeCodeImage(boolean justSave) {
        ProgressDialogUtil.on().showProgressDialog(this);

        getCompositeDisposable().add(
                Completable.create(emitter -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                ContentResolver resolver = this.getContentResolver();
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                                values.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_PNG);
                                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                if (imageUri != null && mCurrentGeneratedCodeBitmap != null) {
                                    try {
                                        OutputStream out = resolver.openOutputStream(imageUri);
                                        mCurrentGeneratedCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                        out.flush();
                                        out.close();

                                        //File codeImageFile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName0);
                                        setCurrentCodeFile(imageUri);

                                        if (!emitter.isDisposed()) {
                                            emitter.onComplete();
                                        }
                                    } catch (IOException e) {
                                        if (!emitter.isDisposed()) {
                                            emitter.onError(e);
                                        }
                                    }
                                } else {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(new NullPointerException());
                                    }
                                }
                            } else {

                                File codeImageFile = FileUtil.getEmptyFile(this, fileName);

                                if (codeImageFile != null && mCurrentGeneratedCodeBitmap != null) {
                                    try (FileOutputStream out = new FileOutputStream(codeImageFile)) {
                                        mCurrentGeneratedCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                                        Uri uri;
                                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                            uri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), codeImageFile);
                                        } else {
                                            uri = Uri.fromFile(codeImageFile);
                                        }
                                        setCurrentCodeFile(uri);

                                        if (!emitter.isDisposed()) {
                                            emitter.onComplete();
                                        }
                                    } catch (IOException e) {
                                        if (!emitter.isDisposed()) {
                                            emitter.onError(e);
                                        }
                                    }
                                } else {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(new NullPointerException());
                                    }
                                }
                            }

                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                ProgressDialogUtil.on().hideProgressDialog();
                                if (justSave) {
                                    //makeSnack(GeneratedCodeActivity.this, R.string.saved_the_code_successfully);
                                    makeSavedImage(GeneratedCodeActivity.this, getCurrentCodeFile(), R.string.saved_the_code_successfully);

                                } else {
                                    String content = mCurrentCode.getContent();
                                    IntentUtils.shareCodeFromUri(GeneratedCodeActivity.this, getCurrentCodeFile(), content);
                                }
                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                if (!TextUtils.isEmpty(e.getMessage())) {
                                    Log.e(getClass().getSimpleName(), e.getMessage());
                                }

                                ProgressDialogUtil.on().hideProgressDialog();
                                if (justSave) {
                                    makeSnack(GeneratedCodeActivity.this, R.string.failed_to_save_the_code);
                                } else {
                                    makeSnack(GeneratedCodeActivity.this, R.string.failed_to_share_the_code);
                                }
                            }
                        }));
    }

//    private Uri getLocalBitmapUri(File file) {
//        Uri bmpUri = null;
//        bmpUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
//        return bmpUri;
//    }

    //29 23 22
    public OutputStream streamLoader(File file) throws FileNotFoundException {

        OutputStream fos;
        Uri fileUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) //Android 10 level 29 (Android 10).
        {
            //fileUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
            //content://media/external/images/media/147

            long currentTime = System.currentTimeMillis();
            ContentResolver resolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, currentTime + ".pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");

            contentValues.put(MediaStore.Video.Media.TITLE, "" + currentTime);
            contentValues.put(MediaStore.Video.Media.DATE_ADDED, currentTime / 1000);
            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, currentTime);

            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

//            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);
//            fileUri = resolver.insert(MediaStore.Files.EXTERNAL_CONTENT_URI, contentValues);

            fos = this.getContentResolver().openOutputStream(fileUri);

        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                fileUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
            } else {
                fileUri = Uri.fromFile(file);
            }
            fos = new FileOutputStream(file);
        }
        DLog.d("===========" + Build.VERSION.SDK_INT + " -> " + fileUri);
        return fos;
    }

    private void storeCodeDocument() {
        ProgressDialogUtil.on().showProgressDialog(this);

        getCompositeDisposable().add(
                Completable.create(emitter -> {
                            String type = getResources().getStringArray(R.array.code_types)[mCurrentCode.getType()];


                            //File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName0);
                            //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
                            //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "1.pdf");
                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "1.pdf");

                            if (file != null && mCurrentGeneratedCodeBitmap != null && mCurrentCode != null) {
                                try {
                                    DLog.d("@@@@" + file.getAbsolutePath());
                                    Document document = new Document();
                                    OutputStream fos;

//                                Uri fileUri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
//                                final ParcelFileDescriptor pfd = resolver.openFileDescriptor(fileUri, "w", null);
//                                fos = new FileOutputStream(pfd.getFileDescriptor());

                                    fos = streamLoader(file);


                                    PdfWriter.getInstance(document, fos);

                                    document.open();
                                    document.setPageSize(PageSize.A4);
                                    document.addCreationDate();
                                    document.addAuthor(getString(R.string.app_name));
                                    document.addCreator(getString(R.string.app_name));

                                    BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
                                    float headingFontSize = 20.0f;
                                    float valueFontSize = 26.0f;

                                    BaseFont baseFont = BaseFont.createFont("res/font/opensans_regular.ttf", "UTF-8", BaseFont.EMBEDDED);

                                    LineSeparator lineSeparator = new LineSeparator();
                                    lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

                                    // Adding Title....
                                    Font mOrderDetailsTitleFont = new Font(baseFont, 36.0f, Font.NORMAL, BaseColor.BLACK);
                                    Chunk mOrderDetailsTitleChunk = new Chunk("Code Details", mOrderDetailsTitleFont);
                                    Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
                                    mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
                                    document.add(mOrderDetailsTitleParagraph);

                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));
                                    document.add(Chunk.NEWLINE);
                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));
                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));
                                    document.add(Chunk.NEWLINE);
                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));

                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    mCurrentGeneratedCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    Image codeImage = Image.getInstance(stream.toByteArray());
                                    codeImage.setAlignment(Image.ALIGN_CENTER);
                                    codeImage.scalePercent(40);
                                    Paragraph imageParagraph = new Paragraph();
                                    imageParagraph.add(codeImage);
                                    document.add(imageParagraph);

                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));
                                    document.add(Chunk.NEWLINE);
                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));

                                    // Adding Chunks for Title and value
                                    Font mOrderIdFont = new Font(baseFont, headingFontSize, Font.NORMAL, colorAccent);
                                    Chunk mOrderIdChunk = new Chunk("Content:", mOrderIdFont);
                                    Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
                                    document.add(mOrderIdParagraph);

                                    Font mOrderIdValueFont = new Font(baseFont, valueFontSize, Font.NORMAL, BaseColor.BLACK);
                                    Chunk mOrderIdValueChunk = new Chunk(mCurrentCode.getContent(), mOrderIdValueFont);
                                    Paragraph mOrderIdValueParagraph = new Paragraph(mOrderIdValueChunk);
                                    document.add(mOrderIdValueParagraph);

                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));
                                    document.add(Chunk.NEWLINE);
                                    document.add(new Paragraph(AppConstants.EMPTY_STRING));

                                    // Fields of Order Details...
                                    Font mOrderDateFont = new Font(baseFont, headingFontSize, Font.NORMAL, colorAccent);
                                    Chunk mOrderDateChunk = new Chunk("Type:", mOrderDateFont);
                                    Paragraph mOrderDateParagraph = new Paragraph(mOrderDateChunk);
                                    document.add(mOrderDateParagraph);

                                    Font mOrderDateValueFont = new Font(baseFont, valueFontSize, Font.NORMAL, BaseColor.BLACK);
                                    Chunk mOrderDateValueChunk = new Chunk(type, mOrderDateValueFont);
                                    Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
                                    document.add(mOrderDateValueParagraph);

                                    document.close();

                                    setCurrentPDF(file);
                                    if (!emitter.isDisposed()) {
                                        emitter.onComplete();
                                    }
                                } catch (IOException | DocumentException ie) {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(ie);
                                    }
                                } catch (ActivityNotFoundException ae) {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(ae);
                                    }
                                }
                            } else {
                                if (!emitter.isDisposed()) {
                                    emitter.onError(new NullPointerException());
                                }
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                ProgressDialogUtil.on().hideProgressDialog();
                                makeSnack(GeneratedCodeActivity.this, R.string.saved_the_code_successfully);
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                                    DLog.d(e.getMessage());
                                }

                                ProgressDialogUtil.on().hideProgressDialog();
                                makeSnack(GeneratedCodeActivity.this, R.string.failed_to_save_the_code);
                            }
                        })
        );
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isValid = true;

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isValid = false;
            }
        }
        switch (requestCode) {
            case PermissionUtil.REQUEST_CODE_TO_SAVE:
                if (isValid) {
                    if (getCurrentCodeFile() == null) {
                        storeCodeImage(true);
                    } else {
                        makeSnack(this, R.string.generated_qr_code_already_exists);
                    }
                }
                break;

            case PermissionUtil.REQUEST_CODE_TO_PRINT:
                if (isValid) {
                    if (getCurrentPDF() == null) {
                        storeCodeDocument();
                    } else {
                        makeSnack(this, R.string.generated_qr_code_already_exists);
                    }
                }
                break;

            case PermissionUtil.REQUEST_CODE_TO_SHARE:
                if (isValid) {
                    if (getCurrentCodeFile() == null) {
                        storeCodeImage(false);
                        if (getCurrentCodeFile() != null) {
                            String content = mCurrentCode.getContent();
                            IntentUtils.shareCodeFromUri(GeneratedCodeActivity.this, getCurrentCodeFile(), content);
                        } else {
                            makeSnack(this, R.string.failed_to_share_the_code);
                        }
                    } else {
                        String content = mCurrentCode.getContent();
                        IntentUtils.shareCodeFromUri(GeneratedCodeActivity.this, getCurrentCodeFile(), content);
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onPause() {
        if (binding.b3 != null) {
            binding.b3.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.b3 != null) {
            binding.b3.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (binding.b3 != null) {
            binding.b3.destroy();
        }
        super.onDestroy();
        getCompositeDisposable().dispose();
    }

    private void makeSnack(Context mContext, int res0) {
        //Toast.makeText(this, res0, Toast.LENGTH_LONG).show();
        //View view = findViewById(R.id.cLayout);
        View view = findViewById(android.R.id.content);
        if (view == null) {
            Toast.makeText(this, res0, Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, res0, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void makeSavedImage(Activity activity, Uri imageUri, int res0) {
//        int delayMillis = 900;
//        handler.postDelayed(() -> makeSaved(activity, imageUri), delayMillis);
        makeSaved(activity, imageUri, res0);
    }

    private void makeSaved(Activity activity, @NonNull Uri imageUri, int res0) {
        View view = activity.findViewById(android.R.id.content);
        DLog.d("@@@@@" + imageUri);
        //R.string.file_saved
        String mm = activity.getString(res0) + " " + imageUri.getPath();

        Snackbar.make(view,
                        mm,
                        //Snackbar.LENGTH_LONG
                        3000
                )
                .setAction(R.string.action_open, view2 -> {
                    if (Build.VERSION.SDK_INT >= 24) {
                        try {
                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                            m.invoke(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    target.setDataAndType(imageUri, MIME_TYPE_PNG);
                    Intent chooser = Intent.createChooser(target, activity.getString(R.string.share_file));
                    List<ResolveInfo> infos = activity.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : infos) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    if (Build.VERSION.SDK_INT == 21) {
                        try {
                            activity.startActivity(target);
                        } catch (ActivityNotFoundException e) {
                            try {
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUri.toString()));
                                activity.startActivity(myIntent);
                            } catch (ActivityNotFoundException e0) {
                                Toast.makeText(activity, R.string.jpg_viewer_not_installed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (infos.size() > 0) {
                            try {
                                activity.startActivity(chooser);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(activity, R.string.jpg_viewer_not_installed, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, R.string.jpg_viewer_not_installed, Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .show();
    }
}