package com.koopers.techservice.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.koopers.techservice.data.local.LocalConstanst;
import com.koopers.techservice.data.local.dao.CategoryDAO;
import com.koopers.techservice.data.local.dao.ComponentDAO;
import com.koopers.techservice.data.local.dao.DeviceDAO;
import com.koopers.techservice.data.local.dao.StatusDAO;
import com.koopers.techservice.data.local.entity.Categy;
import com.koopers.techservice.data.local.entity.Elemento;
import com.koopers.techservice.data.local.entity.Equipo;
import com.koopers.techservice.data.local.entity.Estado;


@Database(entities = {Equipo.class, Estado.class, Categy.class, Elemento.class} ,  version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    //our app database object
    private static AppDatabase INSTANCE;
    public abstract DeviceDAO deviceDAO();
    public abstract StatusDAO statusDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract ComponentDAO componentDAO();

    public static AppDatabase getAppDb(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, LocalConstanst.NAME_DATABASE)
                    .build();
        }
        return INSTANCE;
    }

}
