package com.koopers.techservice.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.LocalConstanst;

import java.io.Serializable;

@Entity(tableName = LocalConstanst.NAME_TABLE_DEVICE)
public class Equipo implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "marca")
    @SerializedName("marca")
    private String marca;

    @ColumnInfo(name = "modelo")
    @SerializedName("modelo")
    private String modelo;

    @ColumnInfo(name = "descripcion")
    @SerializedName("descripcion")
    private String descripcion;

    @ColumnInfo(name = "observacion")
    @SerializedName("observacion")
    private String observacion;

    @ColumnInfo(name = "id_estado")
    @SerializedName("id_estado")
    private int id_estado;

    @ColumnInfo(name = "estado")
    @SerializedName("estado")
    private String estado;

    @ColumnInfo(name = "id_categ")
    @SerializedName("id_categ")
    private int id_categ;

    @ColumnInfo(name = "categoria")
    @SerializedName("categoria")
    private String categoria;

    public Equipo(int id, String marca, String modelo, String descripcion, String observacion, int id_estado, String estado, int id_categ, String categoria) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.observacion = observacion;
        this.id_estado = id_estado;
        this.estado = estado;
        this.id_categ = id_categ;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_categ() {
        return id_categ;
    }

    public void setId_categ(int id_categ) {
        this.id_categ = id_categ;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
