package com.koopers.techservice.ui.fragments.session;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.koopers.techservice.App;
import com.koopers.techservice.R;
import com.koopers.techservice.Stringer;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.UserApiService;
import com.koopers.techservice.data.remote.model.UserResponse;
import com.koopers.techservice.utils.sharedPref.DarkModePrefManager;
import com.koopers.techservice.utils.sharedPref.UserPrefManager;

import java.util.Objects;

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
public class Sign_in_Fragment extends Fragment {
    @BindView(R.id.Mail)
    TextInputEditText Mail;
    @BindView(R.id.Pass)
    TextInputEditText Pass;
    @BindView(R.id.login5)
    CircularProgressButton login5;
    @BindView(R.id.lytRegistrar)
    LinearLayout lytRegistrar;
    @BindView(R.id.bt_changeip)
    ImageButton btChangeip;

    public Sign_in_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        if (UserPrefManager.getmInstance(getContext()).isLoggedIn())
            NavHostFragment.findNavController(Sign_in_Fragment.this).navigate(R.id.dashboard_Activity);

        if (new DarkModePrefManager(Objects.requireNonNull(getContext())).isNightMode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        lytRegistrar.setOnClickListener(v ->
                NavHostFragment.findNavController(Sign_in_Fragment.this).navigate(R.id.sign_up_Fragment));

        login5.setBackground(getResources().getDrawable(R.drawable.btn_corner, null));
        login5.setOnClickListener(v -> userLogin());

        btChangeip.setOnClickListener(v -> CustomIPDialog());

        return view;
    }

    private void userLogin() {
        Stringer Eva = new Stringer(Objects.requireNonNull(getContext()));
        String mail = Eva.valMail(Mail);
        String pass = Eva.valPass(Pass);

        if (mail.isEmpty() || pass.isEmpty())
            return;

        login5.startAnimation();

        //Remote > Retrofit
        UserApiService userApiService =  RetrofitApi.getInstance().getRetrofit().create(UserApiService.class);
        Call<UserResponse> responseCall = userApiService.cargarUsuario(mail, pass);

        responseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        Toasty.success(Objects.requireNonNull(getContext()), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        //storing the user in shared preferences, got from responese
                        UserPrefManager.getmInstance(getContext()).userLogin(response.body().getContenido());
                        login5.revertAnimation();

                        //starting the profile fragment
                        NavHostFragment.findNavController(Sign_in_Fragment.this).navigate(R.id.login_to_dash);
                    } else {
                        login5.revertAnimation();
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                login5.revertAnimation();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CustomIPDialog() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_ip);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());
        CircularProgressButton updateIpAddress = dialog.findViewById(R.id.update);
        EditText CurrentlyIP = dialog.findViewById(R.id.NewIpAddress);
        CurrentlyIP.setText(
                new DarkModePrefManager(App.instance.getApplicationContext()).getIpAddress());

        final View.OnClickListener onClickListener = v -> {
            String ipAdress = CurrentlyIP.getText().toString().trim();

            if(!Patterns.IP_ADDRESS.matcher(ipAdress).matches()){
                Toasty.warning(getContext(), "IP No Valida" + ipAdress, Toasty.LENGTH_SHORT).show();
                return;
            }

            updateIpAddress.startAnimation();
            new DarkModePrefManager(App.instance.getApplicationContext()).setIpAddress(ipAdress);
            new RetrofitApi().NukeInstance();
            updateIpAddress.revertAnimation();
            dialog.dismiss();
            Toasty.success(getContext(), "IP Modificada", Toasty.LENGTH_SHORT).show();
        };

        dialog.findViewById(R.id.confirm).setOnClickListener(onClickListener);
        updateIpAddress.setOnClickListener(onClickListener);

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }
}

