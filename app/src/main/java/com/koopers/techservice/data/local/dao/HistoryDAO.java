package com.koopers.techservice.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.koopers.techservice.data.local.LocalConstanst;
import com.koopers.techservice.data.local.entity.Estado;

import java.util.List;

@Dao
public interface HistoryDAO {

    @Query("SELECT * FROM " + LocalConstanst.NAME_TABLE_HISTORY)
    LiveData<List<Estado>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveDevices(List<Estado> device);
}
