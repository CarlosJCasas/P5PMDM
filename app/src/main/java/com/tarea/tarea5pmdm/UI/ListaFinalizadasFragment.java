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


public class ListaFinalizadasFragment extends Fragment implements CustomAdapter.ItemClickListener, CustomAdapter.ItemLongClickListener, RefrescarInterfaz {
    public static ListaFinalizadasFragment listaFinalizadasFragment;
    public List<Tarea> listaFinalizadas;
    public TareaLab myTareaLab;
    public View rootView;
    public CustomAdapter myAdapter;
    public RecyclerView recyclerViewFinalizadas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaFinalizadasFragment = this;
        rootView = inflater.inflate(R.layout.fragment_lista_finalizadas, container, false);

        myTareaLab = TareaLab.get(requireActivity().getApplicationContext());

        listaFinalizadas = new ArrayList<>();
        listaFinalizadas = myTareaLab.getTareasCompletadas();

        recyclerViewFinalizadas = rootView.findViewById(R.id.recycler_finalizada);
        recyclerViewFinalizadas.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

        myAdapter = new CustomAdapter(getContext(), (ArrayList<Tarea>) listaFinalizadas, this, this, this);
        myAdapter.setItemClickListener(this);
        myAdapter.setItemLongClickListener(this);
        recyclerViewFinalizadas.setAdapter(myAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listaFinalizadas.clear();
        listaFinalizadas.addAll(myTareaLab.getTareasCompletadas());
    }

    @Override
    public void onItemClick(int position) {
        listaFinalizadas = myTareaLab.getTareasCompletadas();
        String id = listaFinalizadas.get(position).getTareaId();
        Intent intent = new Intent(requireActivity(), MostrarActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    public void actualizarListado() {
        myAdapter.actualizarListado((ArrayList<Tarea>) myTareaLab.getTareasCompletadas());

    }


    @Override
    public void onItemLongClick(int position) {

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