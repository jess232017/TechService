package com.koopers.techservice.ui.fragments.dashboard;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;
import com.koopers.techservice.R;
import com.koopers.techservice.utils.Scan_and_Search;
import com.koopers.techservice.utils.image.Tratar_Imagen;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Scanner_Fragment extends Fragment implements ZXingScannerView.ResultHandler {
    @BindView(R.id.zxscan)
    ZXingScannerView mScannerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_search)
    FloatingActionButton fabSearch;

    private int mCameraId = -1;
    private boolean mFlash = false;
    private boolean mAutoFocus = true;
    private Scan_and_Search andSearch;
    private Tratar_Imagen tratarImagen;
   // private String mCurrentPhotoPath = "";

    public Scanner_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this, view);

        andSearch = new Scan_and_Search(getActivity());
        mScannerView = view.findViewById(R.id.zxscan);
        mScannerView.setAutoFocus(mAutoFocus);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        tratarImagen = new Tratar_Imagen(getActivity(), this);
        fabSearch.setOnClickListener(v -> andSearch.ShowDialog());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.scanner_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Flash:
                mFlash = !mFlash;
                mScannerView.setFlash(mFlash);
                if (mFlash)
                    item.setIcon(R.drawable.ic_flash_off);
                else
                    item.setIcon(R.drawable.ic_flash_on);
                return true;

            case R.id.Focus:
                mAutoFocus = !mAutoFocus;
                mScannerView.setAutoFocus(mAutoFocus);
                if (mAutoFocus) {
                    item.setIcon(R.drawable.ic_center_focus_weak);
                    Toast.makeText(getContext(), "Autoenfoque activado", Toast.LENGTH_SHORT).show();
                } else {
                    item.setIcon(R.drawable.ic_center_focus_strong);
                    Toast.makeText(getContext(), "Autoenfoque desactivado", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.Camara:
                mScannerView.stopCamera();
                if (mCameraId == -1) {
                    item.setIcon(R.drawable.ic_camera_rear);
                    mCameraId = 1;
                } else {
                    item.setIcon(R.drawable.ic_camera_front);
                    mCameraId = -1;
                }
                mScannerView.startCamera(mCameraId);

                return true;

            case R.id.Load:
                tratarImagen.onImagePressed();
                return true;
            case R.id.History:
                Toast.makeText(getContext(), "En Camino", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera(mCameraId);          // Start camera on resume
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        andSearch.Search(rawResult.getText());
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == tratarImagen.getRequest()) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data != null ? data.getParcelableExtra("path") : null;

                // You can update this bitmap to your server
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);
                    andSearch.Search(tratarImagen.scanQRImage(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // loading profile image from local cache
                //mCurrentPhotoPath = uri != null ? Objects.requireNonNull(uri).toString() : null;
            }
        }
    }
}
