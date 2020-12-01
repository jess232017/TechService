package com.koopers.techservice.data.remote.model;

import com.koopers.techservice.data.remote.ApiConstants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DeviceApiService {
    @GET(ApiConstants.LEER_EQUIPOS)
    Call<DeviceResponse> loadEquipo();

    @POST(ApiConstants.LEER_EQUIPO)
    @FormUrlEncoded
    Call<DeviceResponse> OneEquipo(@Field("id") int id);

    @POST(ApiConstants.CREAR_EQUIPO)
    @FormUrlEncoded
    Call<DeviceResponse> createEquipo(@Field("marca") String marca,
                                      @Field("modelo") String modelo,
                                      @Field("descripcion") String descripcion,
                                      @Field("observacion") String observacion,
                                      @Field("categoria") int categoria,
                                      @Field("estado") int estado);

    @POST(ApiConstants.EDITAR_EQUIPO)
    @FormUrlEncoded
    Call<DeviceResponse> updateEquipo(@Field("id") int id,
                                      @Field("marca") String marca,
                                      @Field("modelo") String modelo,
                                      @Field("descripcion") String descripcion,
                                      @Field("observacion") String observacion,
                                      @Field("categoria") int categoria,
                                      @Field("estado") int estado);
}
