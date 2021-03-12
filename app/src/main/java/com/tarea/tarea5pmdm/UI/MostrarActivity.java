package com.tarea.tarea5pmdm.UI;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.tarea.tarea5pmdm.Core.Tarea;
import com.tarea.tarea5pmdm.DDBB.TareaLab;
import com.tarea.tarea5pmdm.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MostrarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_activity);

        TextInputEditText title_edittext = findViewById(R.id.title_edittext);
        TextInputEditText fecha_edittext = findViewById(R.id.fecha_edittext);
        SwitchMaterial swtichMaterial = findViewById(R.id.swtichMaterial);
        TextInputEditText ubicacion_edittext = findViewById(R.id.ubicacion_edittext);
        MaterialButton boton_aceptar_add_material = findViewById(R.id.boton_aceptar_add_material);
        TareaLab myTareaLab = TareaLab.get(this);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        //Titulo
        Tarea tarea = myTareaLab.getTarea(id);
        title_edittext.setText(tarea.getTitulo());
        //Fechay hora
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(tarea.getFechaLimite());
        String fechaHora = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        fecha_edittext.setText(fechaHora);
        //Favorito
        swtichMaterial.setChecked(tarea.isFavorito());
        //Localizacion
        String nombreCiudad = getNombreCiudad(tarea.getLatitud(), tarea.getLongitud());
        ubicacion_edittext.setText(nombreCiudad);
        //MAPA?
        FragmentManager fragmentManager = getSupportFragmentManager();
        MapsFragment fragmentMap = (MapsFragment) fragmentManager.findFragmentById(R.id.fragment_material_map_mostrar);
        Bundle bundle = new Bundle();
        bundle.putDouble("latitud", tarea.getLatitud());
        bundle.putDouble("longitud", tarea.getLongitud());
        fragmentMap.setArguments(bundle);
        boton_aceptar_add_material.setOnClickListener(v -> finish());
    }

    public String getNombreCiudad(double lat, double lon) {
        String nombreCiudad = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> listaAddress;
        try {
            listaAddress = geocoder.getFromLocation(lat, lon, 1);
            if (listaAddress.size() > 0) {
                for (Address adr : listaAddress) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        nombreCiudad = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nombreCiudad;
    }
}