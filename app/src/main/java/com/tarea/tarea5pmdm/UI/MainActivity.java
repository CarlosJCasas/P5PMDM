package com.tarea.tarea5pmdm.UI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tarea.tarea5pmdm.Core.Tarea;
import com.tarea.tarea5pmdm.DDBB.TareaLab;
import com.tarea.tarea5pmdm.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ListaCompletaFragment listaCompletaFragment;
    private ListaFavoritaFragment listaFavoritaFragment;
    private ListaFinalizadasFragment listaFinalizadasFragment;
    private TareaLab myTareaLab;
    private List<Tarea> listaTareas;
    private List<Tarea> listaCaducadas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordenator_layout_main);

        myTareaLab = TareaLab.get(this);
        listaTareas = myTareaLab.getTareas();

        listaCompletaFragment = new ListaCompletaFragment();
        listaFavoritaFragment = new ListaFavoritaFragment();
        listaFinalizadasFragment = new ListaFinalizadasFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FragmentTransaction ftCompleta = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaCompletaFragment);
        ftCompleta.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        bottomNavigationView.setBackgroundColor(Color.TRANSPARENT);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ((item.getItemId())) {
                    case (R.id.menu_listacompleta):
                        FragmentTransaction ftCompleta = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaCompletaFragment);
                        ftCompleta.commit();
                        return true;
                    case (R.id.menu_listafavorito):
                        FragmentTransaction ftFavorito = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaFavoritaFragment);
                        ftFavorito.commit();
                        return true;
                    case (R.id.menu_listafinalizada):
                        FragmentTransaction ftFinalizadas = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaFinalizadasFragment);
                        ftFinalizadas.commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificarFragments();
        //Lista de caducadas
        listaCaducadas = new ArrayList<>();
        for (Tarea tarea : listaTareas) {
            long horaDia = Calendar.getInstance().getTimeInMillis();
            if (tarea.getFechaLimite() < horaDia) {
                listaCaducadas.add(tarea);
            }
        }
//        List<Tarea> itemsSelected = new ArrayList<>();
//        //Creamos un alertdialog
//        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
//        dialogBuilder.setView(R.layout.dialogo_caducadas);
//        dialogBuilder.setTitle("Tareas vencidas");
//        CharSequence[] cs = listaCaducadas.toArray(new CharSequence[listaCaducadas.size()]);
//        dialogBuilder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                if (isChecked) {
//                    itemsSelected.add(listaCaducadas.get(which));
//                } else {
//                    itemsSelected.remove(listaCaducadas.get(which));
//                }
//            }
//        });
//        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //Hacer cosas de aceptar para eliminar las seleccionadas
//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
//                builder.setTitle("¿Estás seguro?");
//                builder.setMessage("Se eliminarán la/las tarea/as seleccionada/as");
//                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //Eliminar las tareas
//                    }
//                });
//                builder.setNegativeButton("Cancelar", null);
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancelar", null);
//        dialogBuilder.create().show();


//        actualizarListados();
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragment);
//        if(currentFragment instanceof ListaCompletaFragment){
//            ((ListaCompletaFragment) currentFragment).myAdapter.notifyDataSetChanged();
//        }else if (currentFragment instanceof ListaFavoritaFragment){
//            ((ListaFavoritaFragment) currentFragment).myAdapter.notifyDataSetChanged();
//        }else if (currentFragment instanceof ListaFinalizadasFragment){
//            ((ListaFinalizadasFragment) currentFragment).myAdapter.notifyDataSetChanged();
//        }
        //Consultar cuando estan caducadas las tareas
    }

    //Cuando se pulsa el boton de add
    public void addTarea(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

    public void notificarFragments(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragment);
        if (currentFragment instanceof ListaCompletaFragment) {
            ((ListaCompletaFragment) currentFragment).actualizarListado();
        } else if (currentFragment instanceof ListaFavoritaFragment) {
            ((ListaFavoritaFragment) currentFragment).actualizarListado();
        } else if (currentFragment instanceof ListaFinalizadasFragment) {
            ((ListaFinalizadasFragment) currentFragment).actualizarListado();
        }
    }

}