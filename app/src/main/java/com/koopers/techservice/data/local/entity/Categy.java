package com.koopers.techservice.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.koopers.techservice.data.local.LocalConstanst;

import java.io.Serializable;

@Entity(tableName = LocalConstanst.NAME_TABLE_CATEGORY)
public class Categy implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "categoria")
    private String categoria;

    public Categy(int id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }
}
