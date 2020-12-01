package com.koopers.techservice.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koopers.techservice.App;
import com.koopers.techservice.data.local.dao.DeviceDAO;
import com.koopers.techservice.data.local.database.AppDatabase;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.DeviceApiService;
import com.koopers.techservice.data.remote.model.DeviceResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipoRepository {
    private final SwipeRefreshLayout swipeRefreshLayout;
    private final DeviceApiService equipoApiService;
    private final DeviceDAO deviceDAO;

    public EquipoRepository(SwipeRefreshLayout swipeRefreshLayout){
        this.swipeRefreshLayout = swipeRefreshLayout;
        //Local > Room
        deviceDAO = AppDatabase.getAppDb(App.instance.getApplicationContext()).deviceDAO();
        //Remote > Retrofit
        equipoApiService = RetrofitApi.getInstance().getRetrofit().create(DeviceApiService.class);
    }

    public void GetDevices(){
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            fetchfromServer();
        } else {
            //Toasty.warning(getContext(), "No hay conexion con el servidor", Toasty.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void fetchfromServer() {
        Call<DeviceResponse> responseCall = equipoApiService.loadEquipo();
        responseCall.enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(@NotNull Call<DeviceResponse> call, @NotNull Response<DeviceResponse> response) {
                if (response.body() != null) {
                    if (!response.body().getError()) {
                        saveTask(response.body().getContenido());
                        swipeRefreshLayout.setRefreshing(false);
                    }else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<DeviceResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void saveTask(List<Equipo> equipos){
        Thread thread = new Thread(() -> {
            //Guardar en la B.D los datos actualizados
            deviceDAO.saveDevices(equipos);

            //Eliminar los datos que ya no existen en el server
            List<Integer> integers = new ArrayList<>();
            for (Equipo equipo : equipos) {
                integers.add(equipo.getId());
            }
            deviceDAO.NukeTable(integers);
        });
        thread.start();
    }

    public DeviceDAO getDeviceDAO() {
        return deviceDAO;
    }

    protected Context getContext(){
        return App.instance.getApplicationContext();
    }
}
