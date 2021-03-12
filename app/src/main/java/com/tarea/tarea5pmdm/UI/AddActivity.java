package com.tarea.tarea5pmdm.UI;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.tarea.tarea5pmdm.Core.ReminderBroadcast;
import com.tarea.tarea5pmdm.Core.Tarea;
import com.tarea.tarea5pmdm.DDBB.TareaLab;
import com.tarea.tarea5pmdm.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity {
    private SwitchCompat switch1;
    private TextInputEditText titulo_editText;
    private TextInputEditText fecha_vencimiento;
    private TextInputEditText ubicacion;
    private TextInputEditText recordatorioEditText;
    private Button boton_aceptar_add;
    private Button boton_cancelar_add;
    private MaterialAutoCompleteTextView autoCompleteTextViewRecordatorio;
    private String recordatorio = null;
    private String titulo;
    private int multiplicador;
    private long fechaLimite;
    private long recordatorioTime;
    private boolean favorito, completado, seleccionado;
    private double recibirLatitud, recibirLongitud;
    private TareaLab myTarealab;
    private ArrayList<String> listaRecordatorio;
    private ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_material_text);
        myTarealab = TareaLab.get(getApplicationContext());

        boton_aceptar_add = findViewById(R.id.boton_aceptar_add_material);
        boton_cancelar_add = findViewById(R.id.boton_cancelar_add_material);
        switch1 = findViewById(R.id.swtichMaterial);
        titulo_editText = findViewById(R.id.title_edittext);
        fecha_vencimiento = findViewById(R.id.fecha_edittext);
        ubicacion = findViewById(R.id.ubicacion_edittext);
        boton_aceptar_add.setEnabled(false);
        recordatorioEditText = findViewById(R.id.recordatorioEditText);
        autoCompleteTextViewRecordatorio = findViewById(R.id.autocompleteRecordatorio);

        listaRecordatorio = new ArrayList<>();
        listaRecordatorio.add(getResources().getString(R.string.min));
        listaRecordatorio.add(getResources().getString(R.string.horas));
        listaRecordatorio.add(getResources().getString(R.string.dias));
        stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.tv_entidades, listaRecordatorio);
        autoCompleteTextViewRecordatorio.setText(stringArrayAdapter.getItem(0).toString(), false);
        autoCompleteTextViewRecordatorio.setThreshold(1);
        autoCompleteTextViewRecordatorio.setAdapter(stringArrayAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Titulo de la tarea
        titulo_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    boton_aceptar_add.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    boton_aceptar_add.setEnabled(false);
                } else {
                    titulo = s.toString();
                    boton_aceptar_add.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    boton_aceptar_add.setEnabled(false);
                }
            }
        });

        //Fecha de vencimiento de la tarea
        fecha_vencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialog = View.inflate(AddActivity.this, R.layout.date_time_picker, null);
                AlertDialog alertDialog = new AlertDialog.Builder(AddActivity.this).create();

                dialog.findViewById(R.id.boton_date_time).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = dialog.findViewById(R.id.date_picker);
                        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
                        fechaLimite = System.currentTimeMillis();
                        Calendar calendario = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getHour(),
                                timePicker.getMinute());

                        calendario.setTimeZone(TimeZone.getDefault());
                        //Usar el calendario para poner el tiempo y la hora en el editText de fecha

                        String fechaHora = String.format("%02d", calendario.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", calendario.get(Calendar.MONTH)) + "/" + String.format("%02d", calendario.get(Calendar.YEAR)) + " " + String.format("%02d", calendario.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendario.get(Calendar.MINUTE));
                        fecha_vencimiento.setText(fechaHora);
                        fechaLimite = calendario.getTimeInMillis();
                        //Deberia notificar al adapter
                        alertDialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.botoncancelardatetime).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialog);
                alertDialog.show();
            }
        });

        //Switch de tarea favorita
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favorito = isChecked;
            }
        });

        //Recordatorio
        recordatorioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) recordatorio = null;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    recordatorio = s.toString();
                } else {
                    recordatorio = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) recordatorio = null;
            }
        });
        if (recordatorio != null) recordatorioTime = Long.parseLong(recordatorio);

        autoCompleteTextViewRecordatorio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = stringArrayAdapter.getItem(position);
                switch (selectedItem) {
                    case ("Min"):
                        multiplicador = 60 * 1000;
                        Toast.makeText(getApplicationContext(), stringArrayAdapter.getItem(0), Toast.LENGTH_LONG).show();
                        break;
                    case ("Horas"):
                        multiplicador = 3600 * 1000;
                        Toast.makeText(getApplicationContext(), stringArrayAdapter.getItem(1), Toast.LENGTH_LONG).show();
                        break;
                    case ("Dias"):
                        multiplicador = 24 * 3600 * 1000;
                        Toast.makeText(getApplicationContext(), stringArrayAdapter.getItem(2), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        //Botones
        boton_aceptar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tarea tarea = new Tarea(titulo, fechaLimite, favorito, false, false, recibirLongitud, recibirLatitud);
                recordatorioTime *= multiplicador;
                String channelID = tarea.getTitulo() + "_tarea";
                createNotificationChannel(channelID);
                crearNotificacionExpirar(tarea, channelID);
                if (recordatorio != null)
                    crearRecordatorio(tarea, channelID, recordatorioTime);
                myTarealab.addTarea(tarea);
                finish();
            }
        });
        boton_cancelar_add.setOnClickListener(v -> AddActivity.super.onBackPressed());

    }

    private void createNotificationChannel(String channelid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombreChannel = "notificationTareas" + channelid;
            String descriptionChannel = "Canal de notificaciones para las tareas";
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelid, nombreChannel, importancia);
            channel.setDescription(descriptionChannel);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void crearNotificacionExpirar(Tarea tarea, String channelID) {
        int requestcode = new Random().nextInt(100);
        String titulo = "Tarea expirada.";
        String texto = "La tarea " + tarea.getTitulo() + " ha vencido.";
        Intent intent = new Intent(AddActivity.this, ReminderBroadcast.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("texto", texto);
        intent.putExtra("channelid", channelID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, requestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long tareaTime = tarea.getFechaLimite();
        alarmManager.set(AlarmManager.RTC_WAKEUP, tareaTime, pendingIntent);
    }

    public void crearRecordatorio(Tarea tarea, String channelID, long timeRecordatorio) {
        int requestCode = new Random().nextInt(100);
        String titulo = "La tarea va a expirar.";
        String texto = "La tarea " + tarea.getTitulo() + " va a expirar.";
        Intent intent = new Intent(AddActivity.this, ReminderBroadcast.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("texto", texto);
        intent.putExtra("channelid", channelID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long tareaTime = tarea.getFechaLimite() - timeRecordatorio;
        alarmManager.set(AlarmManager.RTC_WAKEUP, tareaTime, pendingIntent);
    }

    public void recibirDatosFragment(double lat, double lon) {
        recibirLatitud = lat;
        recibirLongitud = lon;
        String cityName = getNombreCiudad(recibirLatitud, recibirLongitud);
        ubicacion.setText(cityName);
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