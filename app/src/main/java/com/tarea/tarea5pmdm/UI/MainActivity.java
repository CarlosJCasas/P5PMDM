package com.tarea.tarea5pmdm.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private List<String> listaCaducadasNombre;
    private List<Tarea> listaCaducadasTareas;
    private boolean control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordenator_layout_main);

        myTareaLab = TareaLab.get(this);
        listaTareas = myTareaLab.getTareas();
        control = true;
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

        //Lista de caducadas
        if(control) {
            listaCaducadasNombre = new ArrayList<>();
            listaCaducadasTareas = new ArrayList<>();
            listaCaducadasNombre.clear();
            listaCaducadasTareas.clear();
            listaTareas = myTareaLab.getTareas();
            for (Tarea tarea : listaTareas) {
                long horaDia = Calendar.getInstance().getTimeInMillis();
                if (tarea.getFechaLimite() < horaDia && !tarea.isCompletado() && tarea.getFechaLimite() != 0) {
                    listaCaducadasNombre.add(tarea.getTitulo());
                    listaCaducadasTareas.add(tarea);
                }
            }

            List<Tarea> itemsSelected = new ArrayList<>();
            itemsSelected.clear();
            //Creamos un alertdialog
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setView(R.layout.dialogo_caducadas);
            TextView title = new TextView(this);
            title.setText(R.string.tareaVencida);
            title.setBackgroundColor(getResources().getColor(R.color.primaryBlue700));
            title.setTextSize(21);
            title.setPadding(16,16,16,16);
            title.setTextColor(getResources().getColor(R.color.white));
            title.setGravity(Gravity.CENTER);
            dialogBuilder.setCustomTitle(title);

            CharSequence[] cs = listaCaducadasNombre.toArray(new CharSequence[listaCaducadasNombre.size()]);
            dialogBuilder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if (isChecked) {
                        itemsSelected.add(listaCaducadasTareas.get(which));
                    } else {
                        itemsSelected.remove(listaCaducadasTareas.get(which));
                    }
                }
            });
            dialogBuilder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Hacer cosas de aceptar para eliminar las seleccionadas
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                    builder.setTitle("¿Estás seguro?");
                    builder.setMessage("Se eliminarán la/las tarea/as seleccionada/as");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Eliminar las tareas
                            for (Tarea t : itemsSelected) {
                                myTareaLab.deleteTarea(t);
                                notificarFragments();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    if (!itemsSelected.isEmpty()) builder.create().show();
                }
            });
            dialogBuilder.setNegativeButton("Cancelar", null);
            if (!listaCaducadasTareas.isEmpty()) {
                dialogBuilder.create().show();
            }
        }
//        //Notificar a los fragments
        control = false;
        notificarFragments();
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