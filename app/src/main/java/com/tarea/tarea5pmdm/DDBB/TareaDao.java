package com.tarea.tarea5pmdm.DDBB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tarea.tarea5pmdm.Core.Tarea;

import java.util.List;

@Dao
public interface TareaDao {

    @Query("SELECT * FROM tarea")
    List<Tarea> getTareas();

    @Query("SELECT * FROM tarea WHERE tareaId LIKE :uuid")
    Tarea getTarea(String uuid);

    @Query("DELETE FROM tarea")
    void deleteAll();

    @Query("SELECT * FROM tarea WHERE favorito = 1 AND completado = 0")
    List<Tarea> getTareasFavoritas();

    @Query("SELECT * FROM tarea WHERE completado = 1")
    List<Tarea> getTareasCompletadas();

    @Query("SELECT * FROM tarea WHERE completado = 0")
    List<Tarea> getTareasIncompletas();

    @Insert
    void addTarea(Tarea tarea);

    @Delete
    void deleteTarea(Tarea tarea);

    @Update
    void updateTarea(Tarea tarea);
}