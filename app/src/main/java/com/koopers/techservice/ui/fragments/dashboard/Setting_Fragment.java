package com.koopers.techservice.ui.fragments.dashboard;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.koopers.techservice.R;
import com.koopers.techservice.Stringer;
import com.koopers.techservice.data.local.entity.Usuario;
import com.koopers.techservice.data.remote.ApiConstants;
import com.koopers.techservice.data.remote.model.UserApiService;
import com.koopers.techservice.data.remote.model.UserResponse;
import com.koopers.techservice.utils.image.Tratar_Imagen;
import com.koopers.techservice.utils.sharedPref.DarkModePrefManager;
import com.koopers.techservice.utils.sharedPref.UserPrefManager;

import java.util.Objects;
import java.util.UUID;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Setting_Fragment extends Fragment {

    private String mCurrentPhotoPath = "";
    private Tratar_Imagen tratarImagen;
    private Usuario usuario;

    @BindView(R.id.edit_passwd)
    MaterialRippleLayout editPasswd;
    @BindView(R.id.edit_detail)
    MaterialRippleLayout editDetail;
    @BindView(R.id.profileCircleImageView)
    CircleImageView profileCircleImageView;
    @BindView(R.id.usernameTextView)
    TextView usernameTextView;
    @BindView(R.id.darkModeSwitch)
    Switch darkModeSwitch;
    @BindView(R.id.edit_profile)
    MaterialRippleLayout editProfile;
    @BindView(R.id.SignOut)
    MaterialRippleLayout SignOut;

    private String mage = "";


    public Setting_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        tratarImagen = new Tratar_Imagen(getActivity(), this);
        ButterKnife.bind(this, view);


        if (new DarkModePrefManager(Objects.requireNonNull(getContext())).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        //Enabling dark mode
        darkModeSwitch.setChecked(new DarkModePrefManager(getContext()).isNightMode());
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(Objects.requireNonNull(getContext()));
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Objects.requireNonNull(getActivity()).recreate();
        });

        SignOut.setOnClickListener(v -> {
            UserPrefManager.getmInstance(getContext()).logOut();
            NavHostFragment.findNavController(Setting_Fragment.this).navigate(R.id.setting_to_login);
        });

        editProfile.setOnClickListener(v -> CustomDialogProf());
        editDetail.setOnClickListener(v -> CustomDialogProf());
        editPasswd.setOnClickListener(v -> CustomDialogPass());

        if (UserPrefManager.getmInstance(getContext()).isLoggedIn()) {
            usuario = UserPrefManager.getmInstance(getContext()).getUser();
            usernameTextView.setText(usuario.getUsuario());
            Glide.with(this)
                    .load(ApiConstants.CARGAR_IMAGEN + usuario.getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileCircleImageView);
        }
        return view;
    }

    private ImageButton image;
    private void CustomDialogProf() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_profile);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;

        CircularProgressButton update = dialog.findViewById(R.id.update);
        EditText name = dialog.findViewById(R.id.Name);
        EditText mail = dialog.findViewById(R.id.Mail);
        image = dialog.findViewById(R.id.image);
        name.setText(usuario.getUsuario());
        mail.setText(usuario.getMail());

        Glide.with(this)
                .load(ApiConstants.CARGAR_IMAGEN + usuario.getImage())
                .into(image);

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());
        image.setOnClickListener(v -> tratarImagen.onImagePressed());

        View.OnClickListener clickListener = v -> Check_Prof(name, mail, update);
        dialog.findViewById(R.id.confirm).setOnClickListener(clickListener);
        update.setOnClickListener(clickListener);

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void Check_Prof(EditText Name, EditText Mail, CircularProgressButton update){
        Stringer Eva = new Stringer(Objects.requireNonNull(getContext()));
        String NewMail = Eva.valMail(Mail);
        String NewName = Eva.valName(Name);

        if(NewName.isEmpty() || NewMail.isEmpty())
            return;

        if((NewName.equals(usuario.getUsuario())) && (NewMail.equals(usuario.getMail())) && mCurrentPhotoPath.isEmpty()){
            Toasty.warning(Objects.requireNonNull(getContext()), R.string.nochange, Toasty.LENGTH_SHORT).show();
            return;
        }

        update.startAnimation();
        userUpdate(NewName, NewMail, "", false, update);
    }

    private void CustomDialogPass() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_passwrd);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;


        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());
        CircularProgressButton update = dialog.findViewById(R.id.update);
        EditText CurrentlyPass = dialog.findViewById(R.id.CurrentlyPass);
        EditText NewlyPass = dialog.findViewById(R.id.NewlyPass);

        final View.OnClickListener onClickListener = v -> Check_Pass(CurrentlyPass, NewlyPass, update);
        dialog.findViewById(R.id.confirm).setOnClickListener(onClickListener);
        update.setOnClickListener(onClickListener);

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void Check_Pass(EditText CurrentlyPass, EditText NewlyPass, CircularProgressButton update){
        Stringer Eva = new Stringer(Objects.requireNonNull(getContext()));
        String NewPass = Eva.valName(NewlyPass);
        String CurrentPass = Eva.valName(CurrentlyPass);

        if (CurrentPass.equals(usuario.getPassword())) {
            if (CurrentPass.equals(NewPass)) {
                Toasty.warning(Objects.requireNonNull(getContext()), R.string.no_valid, Toast.LENGTH_SHORT, true).show();
                NewlyPass.requestFocus();
                return;
            }
            update.startAnimation();
            userUpdate("", "", NewPass, true, update);
        } else {
            Toasty.error(Objects.requireNonNull(getContext()), R.string.error_pass, Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void userUpdate(String name, String mail, String pass, Boolean PassChanged, CircularProgressButton Cpb) {
        if (PassChanged) {
            usuario.setPassword(pass);
        }else{
            usuario.setUsuario(name);
            usuario.setMail(mail);

            if (mCurrentPhotoPath.isEmpty()) {
                mage = usuario.getImage();
                Toasty.warning(Objects.requireNonNull(getContext()), "No seleciono ninguna imagen",
                        Toasty.LENGTH_SHORT).show();
            } else {
                String extension = mCurrentPhotoPath.substring(mCurrentPhotoPath.lastIndexOf("."));
                mage = mail.substring(0, mail.indexOf("@") + 1) + UUID.randomUUID().toString();
                if (tratarImagen.SendImageToServer(mCurrentPhotoPath, mage)) {
                    Toasty.error(Objects.requireNonNull(getContext()), "No se pudo guardar la imagen", Toasty.LENGTH_SHORT).show();
                } else {
                    mage = mage + extension;
                }
            }
        }

        //Remote > Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.getURLBASE())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApiService userApiService = retrofit.create(UserApiService.class);
        Call<UserResponse> responseCall = userApiService.editarUsuario(usuario.getUsuario(),
                usuario.getMail(), usuario.getPassword(), usuario.getRole(), mage ,usuario.getId());

        responseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null) {
                    Cpb.revertAnimation();

                    if (!response.body().getError()) {
                        Toasty.success(Objects.requireNonNull(getContext()), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(getContext()).setMessage(R.string.needly)
                                .setCancelable(false)
                                .setPositiveButton(R.string.aceptar, (dialog, id) -> {
                                    UserPrefManager.getmInstance(getContext()).logOut();
                                    NavHostFragment.findNavController(Setting_Fragment.this).navigate(R.id.setting_to_login);
                                }).create().show();
                    }else{
                        Toasty.warning(Objects.requireNonNull(getContext()), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Cpb.revertAnimation();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == tratarImagen.getRequest()) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data != null ? data.getParcelableExtra("path") : null;
                mCurrentPhotoPath = uri != null ? Objects.requireNonNull(uri).getEncodedPath() : null;
                Glide.with(this)
                        .load(mCurrentPhotoPath)
                        .into(image);
            }
        }
    }
}
