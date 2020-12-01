package com.koopers.techservice.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.koopers.techservice.App;
import com.koopers.techservice.data.local.dao.StatusDAO;
import com.koopers.techservice.data.local.database.AppDatabase;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.data.local.entity.Estado;
import com.koopers.techservice.data.network.NetworkBoundResource;
import com.koopers.techservice.data.network.Resource;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.StatusApiService;
import com.koopers.techservice.data.remote.model.StatusResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class EstadoRepository {
    private final StatusApiService statusApiService;
    private final StatusDAO statusDAO;

    public EstadoRepository(){
        //Local > Room
        statusDAO = AppDatabase.getAppDb(App.instance.getApplicationContext()).statusDAO();

        //Remote > Retrofit
        statusApiService =  RetrofitApi.getInstance().getRetrofit().create(StatusApiService.class);
    }

    public MutableLiveData<Resource<List<Estado>>> getStatus(){
        //Tipo que devuelve Room (BD local), Tipo que devuelve la Api con Retrofit
        return new NetworkBoundResource<List<Estado>, StatusResponse>(){

            @Override
            protected void saveCallResult(@NonNull StatusResponse item) {
                //Guardar en la B.D los datos actualizados
                statusDAO.saveDevices(item.getContenido());

                //Eliminar los datos que ya no existen en el server
                List<Integer> estado = new ArrayList<>();
                for (Estado status : item.getContenido()) {
                    estado.add(status.getId());
                }
                statusDAO.NukeTable(estado);
            }

            @NonNull
            @Override
            protected LiveData<List<Estado>> loadFromDb() {
                //Devuelve los datos que dispongamos, en la BD local
                return statusDAO.getAll();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Equipo> data) {
                return true;
            }

            @NonNull
            @Override
            protected Call<StatusResponse> createCall() {
                //Obtenemos los datos de la API Remotea
                return statusApiService.loadEstado();
            }
        }.getAsLiveData();
    }
}
