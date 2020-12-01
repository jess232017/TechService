package com.koopers.techservice.ui.fragments.dashboard;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koopers.techservice.R;
import com.koopers.techservice.utils.Scan_and_Search;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Check_Fragment extends Fragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fab_search)
    FloatingActionButton fabSearch;
    @BindView(R.id.bt_go_to_setting)
    AppCompatButton btGoToSetting;
    private Scan_and_Search andSearch;

    public Check_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        ButterKnife.bind(this, view);

        btGoToSetting.setOnClickListener(v -> openSettings());
        title.setText(getResources().getString(R.string.qrcode));
        CheckPermission();

        andSearch = new Scan_and_Search(getActivity());
        fabSearch.setOnClickListener(v -> andSearch.ShowDialog());

        return view;
    }

    //Verifica que se obtenga los permisos requeridos para usar el qr code
    private void CheckPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            NavHostFragment.findNavController(Check_Fragment.this).navigate(R.id.check_to_scanner);
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

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(getActivity().getString(R.string.dialog_permission_title));
        builder.setMessage(getActivity().getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getActivity().getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getActivity().getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }


    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", Objects.requireNonNull(getActivity()).getPackageName(), null);
        intent.setData(uri);
        getActivity().startActivityForResult(intent, 101);
    }

}
