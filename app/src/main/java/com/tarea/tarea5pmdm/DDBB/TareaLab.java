package com.tarea.tarea5pmdm.DDBB;

import android.content.Context;

import androidx.room.Room;

import com.tarea.tarea5pmdm.Core.Tarea;

import java.util.List;

public class TareaLab implements TareaDao {

    private static TareaLab myTareaLab;
    private TareaDao myTareaDao;

    private TareaLab(Context context) {
        Context appContext = context.getApplicationContext();
        TareaDB database = Room.databaseBuilder(appContext, TareaDB.class, "tarea").allowMainThreadQueries().build();
        myTareaDao = database.getTareaDao();
    }

    public static TareaLab get(Context context) {
        if (myTareaLab == null) {
            myTareaLab = new TareaLab(context);
        }
        return myTareaLab;
    }

    @Override
    public List<Tarea> getTareas() {
        return myTareaDao.getTareas();
    }

    @Override
    public Tarea getTarea(String uuid) {
        return myTareaDao.getTarea(uuid);
    }

    @Override
    public void deleteAll() {
        myTareaDao.deleteAll();
    }

    @Override
    public List<Tarea> getTareasFavoritas() {
        return myTareaDao.getTareasFavoritas();
    }

    @Override
    public List<Tarea> getTareasCompletadas() {
        return myTareaDao.getTareasCompletadas();
    }

    @Override
    public List<Tarea> getTareasIncompletas() {
        return myTareaDao.getTareasIncompletas();
    }

    @Override
    public void addTarea(Tarea tarea) {
        myTareaDao.addTarea(tarea);
    }

    @Override
    public void deleteTarea(Tarea tarea) {
        myTareaDao.deleteTarea(tarea);
    }

    @Override
    public void updateTarea(Tarea tarea) {
        myTareaDao.updateTarea(tarea);
    }
}

