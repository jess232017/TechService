package com.koopers.techservice.ui.fragments.device;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koopers.techservice.R;
import com.koopers.techservice.data.ComponentRepository;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.adapters.MyComponent;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Component_Fragment extends Fragment {
    @BindView(R.id.rvComponente)
    RecyclerView rvComponente;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ComponentRepository cptRepost;
    private Equipo equipo;

    public Component_Fragment() {
        // Required empty public constructor
    }

    public static Component_Fragment newInstance(Equipo equipo) {
        Component_Fragment f = new Component_Fragment();
        f.equipo = equipo;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_component, container, false);
        ButterKnife.bind(this, view);

        MyComponent myAdapter = new MyComponent(null);
        rvComponente.setLayoutManager(new LinearLayoutManager(getContext()));
        cptRepost = new ComponentRepository(swipeRefreshLayout);
        rvComponente.setAdapter(myAdapter);
        cptRepost.GetComponents();

        cptRepost.getComponentDAO().getByDevice(equipo.getId()).observe(getViewLifecycleOwner(), myAdapter::setData);
        swipeRefreshLayout.setOnRefreshListener(() -> cptRepost.GetComponents());

        return view;
    }

}
