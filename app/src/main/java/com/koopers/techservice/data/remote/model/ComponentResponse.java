package com.koopers.techservice.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.entity.Elemento;

import java.util.List;

public class ComponentResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("contenido")
    private List<Elemento> contenido;

    public ComponentResponse(Boolean error, String message, List<Elemento> contenido) {
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

    public List<Elemento> getContenido() {
        return contenido;
    }

    public void setContenido(List<Elemento> contenido) {
        this.contenido = contenido;
    }
}
