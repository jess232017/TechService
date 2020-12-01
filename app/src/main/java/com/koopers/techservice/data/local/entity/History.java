package com.koopers.techservice.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.koopers.techservice.data.local.LocalConstanst;

@Entity(tableName = LocalConstanst.NAME_TABLE_HISTORY)
public class History {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "marca")
    private String marca;
    @ColumnInfo(name = "modelo")
    private String modelo;
    @ColumnInfo(name = "descripcion")
    private String descripcion;
    @ColumnInfo(name = "observacion")
    private String observacion;
    @ColumnInfo(name = "estado")
    private int estado;
    @ColumnInfo(name = "categoria")
    private int categoria;

    public History(int id, String marca, String modelo, String descripcion, String observacion, int estado, int categoria) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.observacion = observacion;
        this.estado = estado;
        this.categoria = categoria;
    }
}
