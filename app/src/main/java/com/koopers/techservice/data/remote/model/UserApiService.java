package com.koopers.techservice.data.remote.model;

import com.koopers.techservice.data.remote.ApiConstants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserApiService {
    @POST(ApiConstants.CREAR_USUARIO)
    @FormUrlEncoded
    Call<UserResponse> createUsuario(@Field("usuario") String usuario,
                                     @Field("image") String image,
                                     @Field("mail") String mail,
                                     @Field("pass") String pass,
                                     @Field("role") String role);

    @POST(ApiConstants.LEER_USUARIO)
    @FormUrlEncoded
    Call<UserResponse> cargarUsuario(@Field("mail") String mail,
                                     @Field("pass") String pass);

    @POST(ApiConstants.EDIT_USUARIO)
    @FormUrlEncoded
    Call<UserResponse> editarUsuario(@Field("usuario") String usuario,
                                     @Field("mail") String mail,
                                     @Field("pass") String pass,
                                     @Field("role") String role,
                                     @Field("mage") String mage,
                                     @Field("id") int id);
}
