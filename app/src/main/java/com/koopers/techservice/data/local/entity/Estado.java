package com.koopers.techservice.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.koopers.techservice.data.local.LocalConstanst;

import java.io.Serializable;

@Entity(tableName = LocalConstanst.NAME_TABLE_STATUS)
public class Estado implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "estado")
    private String estado;

    public Estado(int id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }
}
