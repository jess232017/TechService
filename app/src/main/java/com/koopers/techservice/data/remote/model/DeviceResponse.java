package com.koopers.techservice.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.entity.Equipo;

import java.util.List;

public class DeviceResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("contenido")
    private List<Equipo> contenido;

    public DeviceResponse(Boolean error, String message, List<Equipo> contenido) {
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

    public List<Equipo> getContenido() {
        return contenido;
    }

    public void setContenido(List<Equipo> contenido) {
        this.contenido = contenido;
    }
}
