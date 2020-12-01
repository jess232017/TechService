package com.koopers.techservice.ui.fragments.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.koopers.techservice.R;
import com.koopers.techservice.data.EquipoRepository;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.ui.adapters.MyEquipo2;
import com.koopers.techservice.utils.QuerySearch;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Device_Fragment extends Fragment {
    @BindView(R.id.animationView)
    LottieAnimationView animationView;
    @BindView(R.id.lytHideable)
    ConstraintLayout lytHideable;
    @BindView(R.id.rvEquipos)
    RecyclerView rvEquipos;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fastscroll)
    FastScroller fastscroll;

    private List<Equipo> equipoList;
    private List<Equipo> equipoFilter;
    private EventBus bus = EventBus.getDefault();
    private EquipoRepository eqpRepost;
    private String[] happy_blob;
    private MyEquipo2 myAdapter;
    private String filterby;

    public Device_Fragment() {
        // Required empty public constructor
    }

    public static Device_Fragment newInstance(String filterby) {
        Device_Fragment f = new Device_Fragment();
        f.filterby = filterby;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        happy_blob = getResources().getStringArray(R.array.happy_blob);
        ButterKnife.bind(this, view);

        //Recyclerview
        rvEquipos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myAdapter = new MyEquipo2(getActivity(), null);
        eqpRepost = new EquipoRepository(swipeRefreshLayout);
        fastscroll = view.findViewById(R.id.fastscroll);
        rvEquipos.setAdapter(myAdapter);
        eqpRepost.GetDevices();

        fastscroll.setRecyclerView(rvEquipos);
        swipeRefreshLayout.setOnRefreshListener(() -> eqpRepost.GetDevices());
        eqpRepost.getDeviceDAO().getAll().observe(getViewLifecycleOwner(), equipos -> {
            equipoList = equipos;
            myAdapter.setData(equipoFilter, equipos);
        });

        eqpRepost.getDeviceDAO().getFilter(filterby).observe(getViewLifecycleOwner(), equipos -> {
            equipoFilter = equipos;
            myAdapter.setData(equipos, equipoList);
            if(equipos.isEmpty())
                emptyView();
            else{
                rvEquipos.setVisibility(View.VISIBLE);
                lytHideable.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void emptyView(){
        Random r = new Random();
        int anim2show = r.nextInt(7 - 1);
        anim2show = getResources().getIdentifier(happy_blob[anim2show], "raw",
                Objects.requireNonNull(getActivity()).getPackageName());
        rvEquipos.setVisibility(View.GONE);
        lytHideable.setVisibility(View.VISIBLE);
        animationView.setAnimation(anim2show);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ShownQuery(QuerySearch event) {
        swipeRefreshLayout.setEnabled(!event.getBoolean());
        myAdapter.getFilter().setBack(!event.getBoolean());
        myAdapter.getFilter().filter(event.getMessage());
    }
}
