package com.koopers.techservice.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.koopers.techservice.data.local.LocalConstanst;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Estado;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_CATEGORY + " ORDER BY categoria")
    LiveData<List<Categy>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveDevices(List<Categy> categorias);

    @Query("DELETE FROM " + LocalConstanst.NAME_TABLE_CATEGORY + " WHERE id NOT IN(:categoria)")
    void NukeTable(List<Integer> categoria);
}
