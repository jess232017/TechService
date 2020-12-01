package com.koopers.techservice.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.entity.Estado;

import java.util.List;

public class StatusResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("contenido")
    private List<Estado> contenido;

    public StatusResponse(Boolean error, String message, List<Estado> contenido) {
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

    public List<Estado> getContenido() {
        return contenido;
    }

    public void setContenido(List<Estado> contenido) {
        this.contenido = contenido;
    }
}
