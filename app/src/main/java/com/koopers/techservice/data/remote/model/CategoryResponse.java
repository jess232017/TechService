package com.koopers.techservice.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Estado;

import java.util.List;

public class CategoryResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("contenido")
    private List<Categy> contenido;

    public CategoryResponse(Boolean error, String message, List<Categy> contenido) {
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

    public List<Categy> getContenido() {
        return contenido;
    }

    public void setContenido(List<Categy> contenido) {
        this.contenido = contenido;
    }
}
