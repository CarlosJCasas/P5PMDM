package com.tarea.tarea5pmdm.Core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "tarea")
public class Tarea implements Serializable {
    @PrimaryKey
    @NonNull
    private String tareaId;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "fecha")
    private long fechaLimite;

    @ColumnInfo(name = "favorito")
    private boolean favorito;

    @ColumnInfo(name = "completado")
    private boolean completado;

    @ColumnInfo(name = "selected")
    private boolean selected;

    //Necesita un parametro location, longitud y latitud
    @ColumnInfo(name = "longitud")
    private double longitud;

    @ColumnInfo(name = "latitud")
    private double latitud;


    public Tarea(String titulo, long fechaLimite, boolean favorito, boolean completado, boolean selected, double longitud, double latitud) {
        this.tareaId = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.fechaLimite = fechaLimite;
        this.favorito = favorito;
        this.completado = completado;
        this.selected = selected;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    @NonNull
    public String getTareaId() {
        return tareaId;
    }

    public void setTareaId(@NonNull String tareaId) {
        this.tareaId = tareaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(long fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
}
