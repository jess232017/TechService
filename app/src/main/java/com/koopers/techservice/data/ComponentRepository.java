package com.koopers.techservice.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koopers.techservice.App;
import com.koopers.techservice.data.local.dao.ComponentDAO;
import com.koopers.techservice.data.local.database.AppDatabase;
import com.koopers.techservice.data.local.entity.Elemento;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.ComponentApiService;
import com.koopers.techservice.data.remote.model.ComponentResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComponentRepository {
    private final ComponentApiService componentApiService;
    private final SwipeRefreshLayout swipeRefreshLayout;
    private final ComponentDAO componentDAO;

    public ComponentRepository(SwipeRefreshLayout swipeRefreshLayout){
        this.swipeRefreshLayout = swipeRefreshLayout;
        //Local > Room
        componentDAO = AppDatabase.getAppDb(App.instance.getApplicationContext()).componentDAO();
        //Remote > Retrofit
        componentApiService = RetrofitApi.getInstance().getRetrofit().create(ComponentApiService.class);
    }

    public void GetComponents(){
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
        Call<ComponentResponse> responseCall = componentApiService.loadComponente();
        responseCall.enqueue(new Callback<ComponentResponse>() {
            @Override
            public void onResponse(@NotNull Call<ComponentResponse> call, @NotNull Response<ComponentResponse> response) {
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
            public void onFailure(@NotNull Call<ComponentResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void saveTask(List<Elemento> componentes){
        Thread thread = new Thread(() -> {
            //Guardar en la B.D los datos actualizados
            componentDAO.saveDevices(componentes);

            //Eliminar los datos que ya no existen en el server
            List<Integer> integers = new ArrayList<>();
            for (Elemento componente : componentes) {
                integers.add(componente.getId());
            }
            componentDAO.NukeTable(integers);
        });
        thread.start();
    }

    protected Context getContext(){
        return App.instance.getApplicationContext();
    }

    public ComponentDAO getComponentDAO() {
        return componentDAO;
    }
}
