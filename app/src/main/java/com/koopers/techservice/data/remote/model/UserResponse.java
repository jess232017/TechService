package com.koopers.techservice.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.entity.Usuario;


public class UserResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("contenido")
    private Usuario contenido;

    public UserResponse(Boolean error, String message, Usuario contenido) {
        this.error = error;
        this.message = message;
        this.contenido = contenido;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Usuario getContenido() {
        return contenido;
    }

    public void setContenido(Usuario contenido) {
        this.contenido = contenido;
    }
}
