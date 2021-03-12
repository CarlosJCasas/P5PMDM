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


public class ListaFavoritaFragment extends Fragment implements CustomAdapter.ItemClickListener, CustomAdapter.ItemLongClickListener, RefrescarInterfaz {
    public static ListaFavoritaFragment listaFavoritaFragment;
    public List<Tarea> listaFavorita;
    public TareaLab myTareaLab;
    public View rootView;
    public CustomAdapter myAdapter;
    public RecyclerView recyclerViewFavoritas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaFavoritaFragment = this;
        rootView = inflater.inflate(R.layout.fragment_lista_favorita, container, false);

        myTareaLab = TareaLab.get(requireActivity().getApplicationContext());

        listaFavorita = new ArrayList<>();
        listaFavorita = myTareaLab.getTareasFavoritas();

        recyclerViewFavoritas = rootView.findViewById(R.id.recycler_favorita);
        recyclerViewFavoritas.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));

        myAdapter = new CustomAdapter(getContext(), (ArrayList<Tarea>) listaFavorita, this, this, this);
        myAdapter.setItemClickListener(this);
        myAdapter.setItemLongClickListener(this);
        recyclerViewFavoritas.setAdapter(myAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listaFavorita.clear();
        listaFavorita.addAll(myTareaLab.getTareasFavoritas());
    }

    @Override
    public void onItemClick(int position) {

        listaFavorita = myTareaLab.getTareasFavoritas();
        String id = listaFavorita.get(position).getTareaId();
        Intent intent = new Intent(requireActivity(),MostrarActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void actualizarListado() {

        myAdapter.actualizarListado((ArrayList<Tarea>) myTareaLab.getTareasFavoritas());

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