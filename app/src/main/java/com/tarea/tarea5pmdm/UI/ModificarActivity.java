package com.tarea.tarea5pmdm.UI;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
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
import java.util.TimeZone;

public class ModificarActivity extends AppCompatActivity {
    String newTitulo;
    long newFecha;
    boolean newFavorito;
    double recibirLatitud, recibirLongitud;
    Tarea tarea;
    private TareaLab myTareaLab;
    private TextInputEditText ubicacion_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        TextInputEditText title_edittext = findViewById(R.id.title_edittext);
        TextInputEditText fecha_edittext = findViewById(R.id.fecha_edittext);
        SwitchMaterial swtichMaterial = findViewById(R.id.swtichMaterial);
        ubicacion_edittext = findViewById(R.id.ubicacion_edittext);
        MaterialButton boton_aceptar_add_material = findViewById(R.id.boton_aceptar_add_material);
        MaterialButton boton_cancelar_add_material = findViewById(R.id.boton_cancelar_add_material);

        myTareaLab = TareaLab.get(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        tarea = myTareaLab.getTarea(id);
        title_edittext.setText(tarea.getTitulo());
        newTitulo = tarea.getTitulo();
        title_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    newTitulo = s.toString();
                    boton_aceptar_add_material.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newTitulo = s.toString();
                boton_aceptar_add_material.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    boton_aceptar_add_material.setEnabled(false);
                }
            }
        });
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(tarea.getFechaLimite());
        String fechaHora = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        fecha_edittext.setText(fechaHora);
        newFecha = tarea.getFechaLimite();
        fecha_edittext.setOnClickListener(v -> {
            View dialog = View.inflate(ModificarActivity.this, R.layout.date_time_picker, null);
            AlertDialog alertDialog = new AlertDialog.Builder(ModificarActivity.this).create();
            dialog.findViewById(R.id.boton_date_time).setOnClickListener(v12 -> {
                DatePicker datePicker = dialog.findViewById(R.id.date_picker);
                TimePicker timePicker = dialog.findViewById(R.id.timePicker);

                Calendar calendario = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute());

                calendario.setTimeZone(TimeZone.getDefault());
                //Usar el calendario para poner el tiempo y la hora en el editText de fecha

                String fechaHora1 = calendario.get(Calendar.DAY_OF_MONTH) + "/" + calendario.get(Calendar.MONTH) + "/" + calendario.get(Calendar.YEAR) + " " + calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE);
                fecha_edittext.setText(fechaHora1);
                newFecha = calendario.getTimeInMillis();
                //Deberia notificar al adapter

                alertDialog.dismiss();
            });
            dialog.findViewById(R.id.botoncancelardatetime).setOnClickListener(v1 -> alertDialog.dismiss());
            alertDialog.setView(dialog);
            alertDialog.show();
        });
        //Tener favorita
        swtichMaterial.setChecked(tarea.isFavorito());
        swtichMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> newFavorito = isChecked);
        //Nombre de la localización
        String nombreCiudad = getNombreCiudad(tarea.getLatitud(), tarea.getLongitud());
        ubicacion_edittext.setText(nombreCiudad);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapsFragment fragmentMap = (MapsFragment) fragmentManager.findFragmentById(R.id.fragment_material_map_modificar);
        Bundle bundle = new Bundle();
        bundle.putDouble("latitud", tarea.getLatitud());
        bundle.putDouble("longitud", tarea.getLongitud());
        fragmentMap.setArguments(bundle);

        boton_aceptar_add_material.setOnClickListener(v -> {
            //Modificar la tarea
            boolean newCompletado, newSelected;
            newCompletado = tarea.isCompletado();
            newSelected = tarea.isSelected();
            tarea.setTitulo(newTitulo);
            tarea.setFechaLimite(newFecha);
            tarea.setFavorito(newFavorito);
            tarea.setCompletado(newCompletado);
            tarea.setSelected(newSelected);
            tarea.setLongitud(recibirLongitud);
            tarea.setLatitud(recibirLatitud);
            myTareaLab.updateTarea(tarea);
            finish();
        });

        boton_cancelar_add_material.setOnClickListener(v -> ModificarActivity.super.onBackPressed());

    }

    public void recibirDatosFragment(double lat, double lon) {
        recibirLatitud = lat;
        recibirLongitud = lon;
        String cityName = getNombreCiudad(recibirLatitud, recibirLongitud);
        ubicacion_edittext.setText(cityName);

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