package com.koopers.techservice.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.koopers.techservice.data.local.LocalConstanst;
import com.koopers.techservice.data.local.entity.Elemento;

import java.util.List;

//Consultas local de la informacion (Room)
@Dao
public interface ComponentDAO {

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_COMPONENT + " ORDER BY titulo")
    LiveData<List<Elemento>> getAll();

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_COMPONENT + " WHERE equipo LIKE :id ORDER BY titulo")
    LiveData<List<Elemento>> getByDevice(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveDevices(List<Elemento> device);

    @Query("DELETE FROM " + LocalConstanst.NAME_TABLE_COMPONENT + " WHERE id NOT IN(:component)")
    void NukeTable(List<Integer> component);

}
