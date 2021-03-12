package com.tarea.tarea5pmdm.Core;

import android.content.Context;
import android.view.View;

import com.tarea.tarea5pmdm.DDBB.TareaLab;

import java.util.List;

public class DeshacerListener implements View.OnClickListener {
    private Tarea tarea;
    private int position;
    private Context context;
    private List<Tarea> tareaList;

    public DeshacerListener(Tarea tarea, int position, Context context, List<Tarea> tareaList) {
        this.tarea = tarea;
        this.position = position;
        this.context = context;
        this.tareaList = tareaList;
    }

    @Override
    public void onClick(View v) {
        TareaLab.get(context).addTarea(tarea);
        tareaList.add(position, tarea);
    }
}
