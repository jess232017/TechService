package com.koopers.techservice.data.remote.model;

import com.koopers.techservice.data.remote.ApiConstants;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApiService {
    @GET(ApiConstants.LEER_CATEGORIAS)
    Call<CategoryResponse> loadCategoria();
}
