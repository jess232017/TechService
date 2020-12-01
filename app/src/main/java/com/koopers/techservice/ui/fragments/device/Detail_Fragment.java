package com.koopers.techservice.ui.fragments.device;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.balysv.materialripple.MaterialRippleLayout;
import com.koopers.techservice.R;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.activities.Detail_Activity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Detail_Fragment extends Fragment {
    @BindView(R.id.setDescription)
    TextView setDescription;
    @BindView(R.id.setObserve)
    TextView setObserve;
    @BindView(R.id.edit_spec)
    MaterialRippleLayout editSpec;
    @BindView(R.id.edit_detail)
    MaterialRippleLayout editDetail;

    private Equipo equipo;

    public Detail_Fragment() {
        // Required empty public constructor
    }


    public static Detail_Fragment newInstance(Equipo equipo) {
        Detail_Fragment f = new Detail_Fragment();
        f.equipo = equipo;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        setDescription.setText(equipo.getDescripcion());
        setObserve.setText(equipo.getObservacion());

        editSpec.setOnClickListener(v -> ((Detail_Activity) Objects.requireNonNull(getActivity())).ShowEdition());
        editDetail.setOnClickListener(v -> ((Detail_Activity) Objects.requireNonNull(getActivity())).ShowEdition());
        return view;
    }

}
