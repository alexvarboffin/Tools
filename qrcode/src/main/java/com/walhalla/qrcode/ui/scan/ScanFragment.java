package com.walhalla.qrcode.ui.scan;


import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.walhalla.qrcode.R;
import com.walhalla.qrcode.app.RunApp;
import com.walhalla.qrcode.databinding.FragmentScanBinding;
import com.walhalla.qrcode.helpers.constant.AppConstants;
import com.walhalla.qrcode.helpers.constant.IntentKey;
import com.walhalla.qrcode.helpers.constant.PreferenceKey;
import com.walhalla.qrcode.helpers.model.Code;
import com.walhalla.qrcode.helpers.util.FileUtil;
import com.walhalla.qrcode.helpers.util.ProgressDialogUtil;
import com.walhalla.qrcode.helpers.util.SharedPrefUtil;
import com.walhalla.qrcode.helpers.util.image.ImageInfo;
import com.walhalla.qrcode.helpers.util.image.ImagePicker;
import com.walhalla.qrcode.ui.pickedfromgallery.PickedFromGalleryActivity;
import com.walhalla.qrcode.ui.activity.ScanResultActivity;
import com.walhalla.ui.DLog;

import org.jetbrains.annotations.NotNull;


public class ScanFragment extends androidx.fragment.app.Fragment {

    private Context mContext;
    private Activity mActivity;

    private BeepManager mBeepManager;


    private boolean mIsFlashOn;

    private final String key_qr = "qr";
    private FragmentScanBinding binding;

    public ScanFragment() {
    }

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        } else {
            mActivity = getActivity();
        }
        int res0 = mIsFlashOn ? R.drawable.ic_flash_off : R.drawable.ic_flash_on;
        Drawable flashIcon = ContextCompat.getDrawable(mContext, res0);
        binding.actionFlash.setCompoundDrawablesWithIntrinsicBounds(null, flashIcon, null, null);

        Drawable drawable = ContextCompat.getDrawable(
                mContext,
                //        mIsFlashOn ? R.drawable.ic_flash_off : R.drawable.ic_flash_on
                R.drawable.ic_gallery
        );
        binding.textViewScanGallery.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);


        doPreRequisites();
        binding.actionFlash.setOnClickListener(v -> {
            if (mContext == null) return;
            int res00 = mIsFlashOn ? R.drawable.ic_flash_off : R.drawable.ic_flash_on;
            Drawable flashIcon0 = ContextCompat.getDrawable(mContext, res00);
            binding.actionFlash.setCompoundDrawablesWithIntrinsicBounds(null, flashIcon0, null, null);
            if (mIsFlashOn) {
                binding.barcodeView.setTorchOff();
            } else {
                binding.barcodeView.setTorchOn();
            }
            mIsFlashOn = !mIsFlashOn;
        });
        binding.textViewScanGallery.setOnClickListener(v -> {
            Application m = getActivity().getApplication();
            if(m instanceof RunApp){
                ((RunApp) m).setBannerLock();
            }
            ImagePicker.pickImage(this);
        });


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            doScan(binding.barcodeView);

        } else {
            //show permission popup
            requestStoragePermission(getActivity());
        }
    }

    public static void requestStoragePermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(activity)
                    .setTitle(R.string.permission_title_needed)
                    .setMessage(R.string.this_permission_is_needed)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                            ActivityCompat.requestPermissions(
                                    activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    FileUtil.STORAGE_PERMISSION_CODE))
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> dialog.dismiss()).create().show();

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, FileUtil.STORAGE_PERMISSION_CODE);
        }
    }

    private void doScan(@Nullable DecoratedBarcodeView view) {
        if (view == null) {
            return;
        }
        view.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult obj) {
                view.pause();
                mBeepManager.playBeepSoundAndVibrate();

                if (obj != null
                        && !TextUtils.isEmpty(obj.getText())
                        && !TextUtils.isEmpty(obj.getBarcodeFormat().name())) {

                    Code code;

                    if (obj.getBitmap() != null) {
                        int typeIndex = obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                ? Code.QR_CODE : Code.BAR_CODE;
                        String type = getResources().getStringArray(R.array.code_types)[typeIndex];
                        String fileNameBody = String.format(Locale.ENGLISH, getString(R.string.file_name_body),
                                type.substring(0, type.indexOf(" Code")),
                                String.valueOf(System.currentTimeMillis()));


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            String fileName0 = AppConstants.PREFIX_IMAGE + fileNameBody + FileUtil.fileNameSuffix;
                            ContentResolver resolver = mActivity.getContentResolver();
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName0);
                            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

//            File storageDirectory = new File(
//                    Environment.getExternalStoragePublicDirectory(directoryType)
//                    , activity.getString(R.string.app_name)
//            );
                            if (imageUri != null) {
                                DLog.d("@@@" + imageUri);
                                //File file = new File(imageUri.getPath());

                                try {
                                    OutputStream out = resolver.openOutputStream(imageUri);
                                    obj.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                                    out.flush();
                                    out.close();
                                    code = new Code(
                                            obj.getText(), obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                            ? Code.QR_CODE : Code.BAR_CODE,
                                            imageUri.toString(), obj.getResult().getTimestamp());
                                    try {
                                        MediaScannerConnection.scanFile(getContext(), new String[]{imageUri.getPath()}, null,
                                                (path, uri) -> DLog.d("@ Image is saved in gallery and gallery is refreshed.")
                                        );
                                    } catch (Exception e) {
                                        DLog.handleException(e);
                                    }

                                } catch (Exception e) {
                                    if (!TextUtils.isEmpty(e.getMessage())) {
                                        DLog.handleException(e);
                                    }

                                    code = new Code(
                                            obj.getText(), obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                            ? Code.QR_CODE : Code.BAR_CODE, obj.getResult().getTimestamp());

                                }

                            } else {
                                code = new Code(obj.getText(),
                                        obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                                ? Code.QR_CODE : Code.BAR_CODE, obj.getResult().getTimestamp());
                            }
                        } else {
                            String fileName0 = AppConstants.PREFIX_IMAGE + fileNameBody + FileUtil.fileNameSuffix;
                            File codeImageFile = FileUtil.getEmptyFile(getActivity(), fileName0);


                            if (codeImageFile != null) {
                                DLog.d("@@@" + codeImageFile);
                                try (FileOutputStream out = new FileOutputStream(codeImageFile)) {
                                    obj.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);

                                    Uri uri;
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                                        uri = FileProvider.getUriForFile(mContext,
                                                getString(R.string.file_provider_authority), codeImageFile);
                                    } else {
                                        uri = Uri.fromFile(codeImageFile);
                                    }

                                    code = new Code(obj.getText(),
                                            obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                                    ? Code.QR_CODE : Code.BAR_CODE,
                                            uri.toString(), obj.getResult().getTimestamp());

                                } catch (IOException e) {
                                    if (!TextUtils.isEmpty(e.getMessage())) {
                                        DLog.handleException(e);
                                    }

                                    code = new Code(obj.getText(),
                                            obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                                    ? Code.QR_CODE : Code.BAR_CODE, obj.getResult().getTimestamp());
                                }
                            } else {
                                code = new Code(obj.getText(),
                                        obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                                ? Code.QR_CODE : Code.BAR_CODE, obj.getResult().getTimestamp());
                            }
                        }

                    } else {
                        code = new Code(obj.getText(),
                                obj.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                        ? Code.QR_CODE : Code.BAR_CODE, obj.getResult().getTimestamp());
                    }

                    Intent intent = new Intent(mContext, ScanResultActivity.class);
                    intent.putExtra(IntentKey.MODEL, code);
                    startActivity(intent);
                } else {
                    view.resume();
                    doScan(view);
                    Toast.makeText(mContext, getString(R.string.error_occured_while_scanning),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    private void doPreRequisites() {
        mBeepManager = new BeepManager(mActivity);
        mBeepManager.setVibrateEnabled(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.VIBRATE));
        mBeepManager.setBeepEnabled(SharedPrefUtil.readBooleanDefaultTrue(PreferenceKey.PLAY_SOUND));
        if (binding.barcodeView != null) {
            binding.barcodeView.setStatusText(AppConstants.EMPTY_STRING);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        doPreRequisites();
        binding.barcodeView.resume();
        doScan(binding.barcodeView);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.barcodeView.pause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.REQUEST_CODE_PICK_IMAGE && mContext != null) {
            Application m = getActivity().getApplication();
            if(m instanceof RunApp){
                ((RunApp) m).onActivityResult();
            }

            ProgressDialogUtil.on().showProgressDialog(mContext);
            Bitmap bitmap = ImagePicker.getPickedImageFromResult(mContext, resultCode, data);
            Result result = processBitmapToGetResult(bitmap);

            if (result != null) {
                ImageInfo imageInfo = ImagePicker.getPickedImageInfo(mContext, resultCode, data);


                if (imageInfo != null && imageInfo.getImageUri() != null) {


                    //String imagePath = getPathFromUri(imageInfo.getImageUri());
                    //DLog.d("@@@@@@@@@@@" + imageInfo + " " + imageInfo.getImageUri());


                    if (imageInfo.getImageUri() != null) {
                        int typeIndex = result.getBarcodeFormat().name().toLowerCase().startsWith(key_qr)
                                ? Code.QR_CODE : Code.BAR_CODE;

                        Code code = new Code(result.getText(), typeIndex,
                                imageInfo.getImageUri().toString(), result.getTimestamp());

                        Intent intent = new Intent(mContext, PickedFromGalleryActivity.class);
                        intent.putExtra(IntentKey.MODEL, code);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, R.string.error_did_not_find_any_content, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.error_did_not_find_any_content, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Result processBitmapToGetResult(Bitmap bitmap) {
        if (bitmap != null) {
            int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            LuminanceSource source =
                    new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            try {
                Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<>();
                decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

                Result result = reader.decode(binaryBitmap, decodeHints);
                String codeResult = result.getText();

                if (!TextUtils.isEmpty(codeResult)) {
                    ProgressDialogUtil.on().hideProgressDialog();
                    return result;
                } else {
                    ProgressDialogUtil.on().hideProgressDialog();
                    Toast.makeText(mContext, getString(R.string.error_did_not_find_any_content),
                            Toast.LENGTH_SHORT).show();

                    return null;
                }

            } catch (Exception e) {
                ProgressDialogUtil.on().hideProgressDialog();
                Toast.makeText(mContext, getString(R.string.error_did_not_find_any_content),
                        Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(e.getMessage())) {
                    DLog.d(e.getMessage());
                }
                return null;
            }
        } else {
            ProgressDialogUtil.on().hideProgressDialog();
            Toast.makeText(mContext, getString(R.string.error_could_not_load_the_image), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

//    private String getPathFromUri(Uri uri) {
//        if (mContext == null) {
//            return null;
//        }
//
//        String[] data = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(mContext, uri, data, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        if (cursor == null) {
//            return null;
//        }
//
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }

}
