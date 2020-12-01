package com.koopers.techservice.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.koopers.techservice.data.local.LocalConstanst;

import java.io.Serializable;

@Entity(tableName = LocalConstanst.NAME_TABLE_COMPONENT)
public class Elemento implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "equipo")
    @SerializedName("equipo")
    private int equipo;

    @ColumnInfo(name = "titulo")
    @SerializedName("titulo")
    private String titulo;

    @ColumnInfo(name = "id_estado")
    @SerializedName("id_estado")
    private int id_estado;

    @ColumnInfo(name = "estado")
    @SerializedName("estado")
    private String estado;

    @ColumnInfo(name = "descripcion")
    @SerializedName("descripcion")
    private String descripcion;

    @ColumnInfo(name = "disponibilidad")
    @SerializedName("disponibilidad")
    private int disponibilidad;

    @ColumnInfo(name = "hora")
    @SerializedName("hora")
    private String hora;

    public Elemento(int id, int equipo, String titulo, int id_estado, String estado, String descripcion, int disponibilidad, String hora) {
        this.id = id;
        this.equipo = equipo;
        this.titulo = titulo;
        this.id_estado = id_estado;
        this.estado = estado;
        this.descripcion = descripcion;
        this.disponibilidad = disponibilidad;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipo() {
        return equipo;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
