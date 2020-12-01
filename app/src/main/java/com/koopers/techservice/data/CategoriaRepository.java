package com.koopers.techservice.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.koopers.techservice.App;
import com.koopers.techservice.data.local.dao.CategoryDAO;
import com.koopers.techservice.data.local.database.AppDatabase;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.data.network.NetworkBoundResource;
import com.koopers.techservice.data.network.Resource;
import com.koopers.techservice.data.remote.RetrofitApi;
import com.koopers.techservice.data.remote.model.CategoryApiService;
import com.koopers.techservice.data.remote.model.CategoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CategoriaRepository {
    private final CategoryApiService categoryApiService;
    private final CategoryDAO categoryDAO;

    public CategoriaRepository(){
        //Local > Room
        categoryDAO = AppDatabase.getAppDb(App.instance.getApplicationContext()).categoryDAO();

        //Remote > Retrofit
        categoryApiService =  RetrofitApi.getInstance().getRetrofit().create(CategoryApiService.class);
    }

    public MutableLiveData<Resource<List<Categy>>> getCategory(){
        //Tipo que devuelve Room (BD local), Tipo que devuelve la Api con Retrofit
        return new NetworkBoundResource<List<Categy>, CategoryResponse>(){

            @Override
            protected void saveCallResult(@NonNull CategoryResponse item) {
                //Guardar en la B.D los datos actualizados
                categoryDAO.saveDevices(item.getContenido());

                //Eliminar los datos que ya no existen en el server
                List<Integer> categoria = new ArrayList<>();
                for (Categy categy : item.getContenido()) {
                    categoria.add(categy.getId());
                }
                categoryDAO.NukeTable(categoria);
            }

            @NonNull
            @Override
            protected LiveData<List<Categy>> loadFromDb() {
                //Devuelve los datos que dispongamos, en la BD local
                return categoryDAO.getAll();
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Equipo> data) {
                return true;
            }

            @NonNull
            @Override
            protected Call<CategoryResponse> createCall() {
                //Obtenemos los datos de la API Remotea
                return categoryApiService.loadCategoria();
            }
        }.getAsLiveData();
    }
}
