package com.koopers.techservice.utils.image;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.koopers.techservice.R;
import com.koopers.techservice.data.remote.ApiConstants;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest;

import java.io.FileNotFoundException;
import java.util.List;


public class Tratar_Imagen {
    private static final String INTENT_IMAGE_PICKER_OPTION = "image_picker_option";
    private static final String INTENT_ASPECT_RATIO_X = "aspect_ratio_x";
    private static final String INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y";
    private static final String INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio";
    private static final String INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height";
    private static final String INTENT_BITMAP_MAX_WIDTH = "max_width";
    private static final String INTENT_BITMAP_MAX_HEIGHT = "max_height";

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_GALLERY_IMAGE = 1;

    private static final int REQUEST_IMAGE = 100;

    private Activity activity;
    private Fragment fragment;

    public Tratar_Imagen(Activity activity, Fragment fragment){
        this.activity = activity;
        this.fragment = fragment;
    }


    public int getRequest(){
        return REQUEST_IMAGE;
    }

    /*******************Codigos de la camara con scrop*************************/
    @SuppressLint("RestrictedApi")
    public void onImagePressed(){
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(activity)
                .setTitle("Escoger Imagen")
                .setMessage("¿De que fuente quieres obtener tu imagen?")
                .setCancelable(true)
                .setPositiveButton("Galeria", R.drawable.ic_photo, (dialogInterface, which) -> {
                    onProfileImageClick(REQUEST_GALLERY_IMAGE);
                    dialogInterface.dismiss();
                })
                .setNegativeButton("Camera", R.drawable.ic_camera, (dialogInterface, which) -> {
                    onProfileImageClick(REQUEST_IMAGE_CAPTURE);
                    dialogInterface.dismiss();
                })
                //.setAnimation(R.raw_elemento.image)
                .build();

        // Show Dialog
        mBottomSheetDialog.show();
    }

    /*
     * Con la ruta de la imagen obtenida subir el archivo
     * usando la library de gotev:uploadservice
     * */
    public boolean SendImageToServer(String path, String filename){
        try {
            new MultipartUploadRequest(activity, ApiConstants.SUBIR_IMAGEN).setMethod("POST")
                    .addParameter("filename", filename)
                    .addFileToUpload( path ,"image").startUpload();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void onProfileImageClick(int SELECT) {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if(SELECT == REQUEST_IMAGE_CAPTURE)
                                launchCameraIntent();
                            else
                                launchGalleryIntent();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(INTENT_IMAGE_PICKER_OPTION, REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(INTENT_BITMAP_MAX_HEIGHT, 1000);

        fragment.startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(INTENT_IMAGE_PICKER_OPTION, REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1);
        fragment.startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.dialog_permission_title));
        builder.setMessage(activity.getString(R.string.dialog_permission_message));
        builder.setPositiveButton(activity.getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(activity.getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    /*
     * Del bitmap de la imagen obtenida buscar un codigo qr
     * usando la library de zxing
     * */
    public String scanQRImage(Bitmap bMap) {
        String contents;

        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        }
        catch (Exception e) {
            contents = "Error decoding barcode";
            Log.e("QrTest", "Error decoding barcode", e);
        }
        return contents;
    }

}
