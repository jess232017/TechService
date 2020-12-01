package com.koopers.techservice.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.koopers.techservice.data.local.LocalConstanst;
import com.koopers.techservice.data.local.entity.Equipo;

import java.util.List;

//Consultas local de la informacion (Room)
@Dao
public interface DeviceDAO {

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_DEVICE + " ORDER BY marca")
    LiveData<List<Equipo>> getAll();

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_DEVICE + " WHERE estado LIKE :filterby OR categoria LIKE :filterby ORDER BY marca")
    LiveData<List<Equipo>> getFilter(String filterby);

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_DEVICE + " WHERE id LIKE :id")
    List<Equipo> getOne(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveDevices(List<Equipo> device);

    @Query("DELETE FROM " + LocalConstanst.NAME_TABLE_DEVICE + " WHERE id NOT IN(:device)")
    void NukeTable(List<Integer> device);

}
