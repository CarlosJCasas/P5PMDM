package com.tarea.tarea5pmdm.DDBB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tarea.tarea5pmdm.Core.Tarea;

@Database(entities = {Tarea.class}, version = 1, exportSchema = false)
public abstract class TareaDB extends RoomDatabase {
    public abstract TareaDao getTareaDao();
}
