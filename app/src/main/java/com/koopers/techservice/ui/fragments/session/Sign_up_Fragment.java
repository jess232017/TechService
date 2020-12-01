package com.koopers.techservice.ui.fragments.session;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.koopers.techservice.R;
import com.koopers.techservice.Stringer;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.UserApiService;
import com.koopers.techservice.data.remote.model.UserResponse;
import com.koopers.techservice.utils.image.Tratar_Imagen;

import java.util.Objects;
import java.util.UUID;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sign_up_Fragment extends Fragment {
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.Name)
    TextInputEditText Name;
    @BindView(R.id.Mail)
    TextInputEditText Mail;
    @BindView(R.id.Pass)
    TextInputEditText Pass;
    @BindView(R.id.register5)
    CircularProgressButton register5;
    @BindView(R.id.lytIngresar)
    LinearLayout lytIngresar;
    @BindView(R.id.addImage)
    ImageButton addImage;

    private String mCurrentPhotoPath = "";
    private Tratar_Imagen tratarImagen;


    public Sign_up_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        tratarImagen = new Tratar_Imagen(getActivity(), this);
        ButterKnife.bind(this, view);

        register5 = view.findViewById(R.id.register5);
        register5.setBackground(getResources().getDrawable(R.drawable.btn_corner, null));
        addImage.setOnClickListener(v -> tratarImagen.onImagePressed());

        lytIngresar.setOnClickListener(v -> NavHostFragment.findNavController(Sign_up_Fragment.this).navigate(R.id.sign_in_Fragment));
        register5.setOnClickListener(v -> User_SigIn());
        return view;

    }


    private void User_SigIn() {
        Stringer Eva = new Stringer(Objects.requireNonNull(getContext()));
        String name = Eva.valName(Name);
        String mail = Eva.valMail(Mail);
        String pass = Eva.valPass(Pass);

        if (name.isEmpty() || mail.isEmpty() || pass.isEmpty())
            return;

        register5.startAnimation();

        String mage;
        if (mCurrentPhotoPath.isEmpty()) {
            mage = "nothing";
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

        //Remote > Retrofit
        UserApiService userApiService =  RetrofitApi.getInstance().getRetrofit().create(UserApiService.class);
        Call<UserResponse> responseCall = userApiService.createUsuario(name, mage, mail, pass, "Cliente");

        responseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        register5.revertAnimation();
                        Toasty.success(Objects.requireNonNull(getContext()), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        NavHostFragment.findNavController(Sign_up_Fragment.this).navigate(R.id.sign_in_Fragment);
                    } else {
                        register5.revertAnimation();
                        Toasty.info(Objects.requireNonNull(getContext()), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                register5.revertAnimation();
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
                message.setText(R.string.register_complete);
                addImage.setImageResource(R.drawable.ic_check_white_24dp);
            }
        }
    }
}
