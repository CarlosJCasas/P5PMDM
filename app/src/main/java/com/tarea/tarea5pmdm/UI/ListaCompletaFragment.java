package com.tarea.tarea5pmdm.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarea.tarea5pmdm.Core.CustomAdapter;
import com.tarea.tarea5pmdm.Core.RefrescarInterfaz;
import com.tarea.tarea5pmdm.Core.Tarea;
import com.tarea.tarea5pmdm.DDBB.TareaLab;
import com.tarea.tarea5pmdm.R;

import java.util.ArrayList;
import java.util.List;


public class ListaCompletaFragment extends Fragment implements CustomAdapter.ItemClickListener, CustomAdapter.ItemLongClickListener, RefrescarInterfaz {
    public static ListaCompletaFragment listaCompletaFragment;
    public List<Tarea> listaCompleta = new ArrayList<>();
    public TareaLab myTareaLab;
    public View rootView;
    public CustomAdapter myAdapter;
    public RecyclerView recyclerViewCompleta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaCompletaFragment = this;
        rootView = inflater.inflate(R.layout.fragment_lista_completa, container, false);

        myTareaLab = TareaLab.get(requireActivity().getApplicationContext());
        listaCompleta = myTareaLab.getTareasIncompletas();

        recyclerViewCompleta = rootView.findViewById(R.id.recycler_completa);
        recyclerViewCompleta.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

        myAdapter = new CustomAdapter(getContext(), (ArrayList<Tarea>) listaCompleta, this, this, this);
        myAdapter.setItemClickListener(this);
        myAdapter.setItemLongClickListener(this);
        recyclerViewCompleta.setAdapter(myAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listaCompleta.clear();
        listaCompleta.addAll(myTareaLab.getTareasIncompletas());
    }

    @Override
    public void onItemClick(int position) {
        //Lanzar la actividad mostrar con los datos de donde hace click pasar la id de la tare
        listaCompleta = myTareaLab.getTareasIncompletas();
        String id = listaCompleta.get(position).getTareaId();
        Intent intent = new Intent(requireActivity(), MostrarActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void actualizarListado() {

        myAdapter.actualizarListado((ArrayList<Tarea>) myTareaLab.getTareasIncompletas());
    }

    @Override
    public void onItemLongClick(int position) {
        //Lanzar actividad de modificar
        listaCompleta = myTareaLab.getTareasIncompletas();
        String id = listaCompleta.get(position).getTareaId();
        Intent intent = new Intent(requireActivity(), ModificarActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void eliminarFavoritas() {
        actualizarListado();
    }

    @Override
    public void addFavoritas() {
        actualizarListado();
    }
}