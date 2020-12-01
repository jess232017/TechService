package com.koopers.techservice.ui.sheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.koopers.techservice.App;
import com.koopers.techservice.R;
import com.koopers.techservice.Stringer;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.data.local.entity.Estado;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.DeviceApiService;
import com.koopers.techservice.data.remote.model.DeviceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Create_Fragment extends SuperBottomSheetFragment {
    private CircularProgressButton create;
    private EditText Brand;
    private EditText Model;
    private EditText Spec;
    private EditText Obsr;

    private Equipo equipo;
    private List<Categy> categy;
    private List<Estado> estado;
    private List<String> SpEstado;
    private List<String> SpCategy;
    private int E_pos;
    private int C_pos;
    private boolean edit = false;

    public static Create_Fragment newInstance(List<Estado> estado, List<Categy> categy, Equipo equipo) {
        Create_Fragment f = new Create_Fragment();
        f.equipo = equipo;
        f.estado = estado;
        f.categy = categy;

        f.SpEstado = new ArrayList<>();
        for (Estado aux : estado)
            f.SpEstado.add(aux.getEstado());

        f.SpCategy = new ArrayList<>();
        for (Categy aux : categy)
            f.SpCategy.add(aux.getCategoria());


        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.sheet_device, container, false);
        ImageButton confirm = view.findViewById(R.id.confirm);
        ImageButton close = view.findViewById(R.id.close);
        Spinner spnStat = view.findViewById(R.id.spnStat);
        Spinner spnCaty = view.findViewById(R.id.spnCaty);
        TextView Title = view.findViewById(R.id.Title);
        create = view.findViewById(R.id.create);
        Brand = view.findViewById(R.id.Brand);
        Model = view.findViewById(R.id.Model);
        Spec = view.findViewById(R.id.Spec);
        Obsr = view.findViewById(R.id.Obsr);

        close.setOnClickListener(v -> dismiss());
        create.setOnClickListener(v -> CreateDevice());
        confirm.setOnClickListener(v -> CreateDevice());

        spnStat.setAdapter(loadSData());

        spnStat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                E_pos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnCaty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                C_pos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnCaty.setAdapter(loadCData());

        if(equipo!=null){
            Brand.setText(equipo.getMarca());
            Model.setText(equipo.getModelo());
            Spec.setText(equipo.getDescripcion());
            Obsr.setText(equipo.getObservacion());

            spnStat.setSelection(SpEstado.indexOf(equipo.getEstado()));
            spnCaty.setSelection(SpCategy.indexOf(equipo.getCategoria()));

            edit = true;
            create.setText(R.string.titleDeviceEdit);
            Title.setText(R.string.titleDeviceEdit);
        }

        return view;
    }


    private void CreateDevice(){
        Stringer Eva = new Stringer(Objects.requireNonNull(getContext()));
        String Obser = Eva.valText(Obsr,10,500);
        String Espec = Eva.valText(Spec, 10,300);
        String Modlo = Eva.valText(Model,3 ,50);
        String Marca = Eva.valText(Brand,3,20);

        if (Marca.isEmpty() || Modlo.isEmpty() || Espec.isEmpty() || Obser.isEmpty())
            return;

        create.startAnimation();

        DeviceApiService deviceApiService =  RetrofitApi.getInstance().getRetrofit().create(DeviceApiService.class);
        Call<DeviceResponse> responseCall;

        if(edit)
            responseCall = deviceApiService.updateEquipo(equipo.getId(),Marca, Modlo, Espec, Obser, categy.get(C_pos).getId() , estado.get(E_pos).getId());
        else
            responseCall = deviceApiService.createEquipo(Marca, Modlo, Espec, Obser, categy.get(C_pos).getId(), estado.get(E_pos).getId());


        responseCall.enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceResponse> call, @NonNull Response<DeviceResponse> response) {
                if (response.body() != null) {
                    create.revertAnimation();
                    if (!response.body().getError()) {
                        Toasty.success(getAppContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toasty.info(getAppContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceResponse> call, @NonNull Throwable t) {
                create.revertAnimation();
                Toast.makeText(getAppContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayAdapter<String> loadSData(){
        return new ArrayAdapter<>(getAppContext(), R.layout.raw_spinner, SpEstado);
    }

    private ArrayAdapter<String> loadCData(){
        return new ArrayAdapter<>(getAppContext(), R.layout.raw_spinner, SpCategy);
    }

    private Context getAppContext(){
        return App.instance.getApplicationContext();
    }
}
