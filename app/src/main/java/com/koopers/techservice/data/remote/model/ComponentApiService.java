package com.koopers.techservice.data.remote.model;

import com.koopers.techservice.data.remote.ApiConstants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ComponentApiService {
    @GET(ApiConstants.LEER_COMPONENTES)
    Call<ComponentResponse> loadComponente();
}
