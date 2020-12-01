package com.koopers.techservice.ui.fragments.dashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.koopers.techservice.R;
import com.koopers.techservice.data.EquipoRepository;
import com.koopers.techservice.data.local.entity.Usuario;
import com.koopers.techservice.data.remote.ApiConstants;
import com.koopers.techservice.ui.adapters.MyEquipo;
import com.koopers.techservice.utils.sharedPref.UserPrefManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Fragment extends Fragment {
    @BindView(R.id.profile)
    CircleImageView profile;
    @BindView(R.id.holderName)
    TextView holderName;
    @BindView(R.id.holderMode)
    TextView holderMode;
    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;
    @BindView(R.id.rvEquipos)
    RecyclerView rvEquipos;
    @BindView(R.id.btn_see)
    MaterialButton btnSee;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private EquipoRepository eqpRepost;
    private Usuario usuario;

    public Home_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        if (UserPrefManager.getmInstance(getContext()).isLoggedIn()) {
            usuario = UserPrefManager.getmInstance(getContext()).getUser();
            holderName.setText(usuario.getUsuario());
            holderMode.setText(usuario.getRole());

            Glide.with(this)
                    .load(ApiConstants.CARGAR_IMAGEN + usuario.getImage())
                    .into(profile);
        }

        profile.setOnClickListener(v -> ShowDialog());
        btnSee.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(R.id.fragment_to_status));

        //Recyclerview for devices
        rvHistory.setHasFixedSize(true);
        MyEquipo myAdapter = new MyEquipo(getActivity(), null);
        rvEquipos.setLayoutManager(new LinearLayoutManager(getContext()));
        eqpRepost = new EquipoRepository(swipeRefreshLayout);
        rvEquipos.setAdapter(myAdapter);
        eqpRepost.GetDevices();

        /*//Recyclerview for history
        rvEquipos.setHasFixedSize(true);
        myAdapter2 = new MyHistorial(getActivity(), equipoList);
        rvHistory.setLayoutManager((new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false)));
        rvHistory.setAdapter(myAdapter2);

        SnapHelper snapHelper = new GravitySnapHelper(Gravity.CENTER);
        snapHelper.attachToRecyclerView(rvHistory);*/

        eqpRepost.getDeviceDAO().getAll().observe(getViewLifecycleOwner(), myAdapter::setData);
        swipeRefreshLayout.setOnRefreshListener(() -> eqpRepost.GetDevices());

        return view;
    }

    private void ShowDialog() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_image);

        ImageView img_profile = dialog.findViewById(R.id.img_profile);
        Glide.with(this)
                .load(ApiConstants.CARGAR_IMAGEN + usuario.getImage())
                .into(img_profile);
        ((TextView) dialog.findViewById(R.id.txt_profile)).setText(usuario.getUsuario());
        ((TextView) dialog.findViewById(R.id.txt_mode)).setText(usuario.getRole());
        dialog.findViewById(R.id.bt_close).setOnClickListener(v1 -> dialog.dismiss());

        dialog.show();
    }

}
