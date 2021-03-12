package com.tarea.tarea5pmdm.Core;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.tarea.tarea5pmdm.DDBB.TareaLab;
import com.tarea.tarea5pmdm.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final RefrescarInterfaz refrescarInterfaz;
    public LayoutInflater myInflater;
    public List<Tarea> listaTareas;
    public Context context;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemLongClickListener;

    public CustomAdapter(Context context, ArrayList<Tarea> listaTareas, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener, RefrescarInterfaz refrescarInterfaz) {
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
        this.listaTareas = listaTareas;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.refrescarInterfaz = refrescarInterfaz;
    }

    //Crear el item del recycler
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = myInflater.inflate(R.layout.item_recycler, parent, false);
        return new ViewHolder(view, itemClickListener, itemLongClickListener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        holder.tituloTv.setText(tarea.getTitulo());
        if (tarea.getFechaLimite() != 0) {
            holder.fechaTv.setText(fechaLongString(tarea.getFechaLimite()));
            if (tarea.getFechaLimite() < Calendar.getInstance().getTimeInMillis() && tarea.getFechaLimite() != 0) {
                holder.tituloTv.setTextColor(Color.RED);
            }
        } else {
            holder.fechaTv.setVisibility(View.GONE);
            holder.tituloTv.setTextColor(Color.BLACK);
        }
        holder.importanteButtonOff.setVisibility(tarea.isFavorito() ? View.INVISIBLE : View.VISIBLE);
        holder.importanteButtonOn.setVisibility(tarea.isFavorito() ? View.VISIBLE : View.INVISIBLE);
        holder.checkCompleto.setChecked(tarea.isCompletado());
        //Seleccionar si es favorita o no
        holder.importanteButtonOff.setOnClickListener(v -> {
            holder.importanteButtonOff.setVisibility(View.INVISIBLE);
            holder.importanteButtonOn.setVisibility(View.VISIBLE);
            tarea.setFavorito(true);
            TareaLab.get(context).updateTarea(tarea);
            refrescarInterfaz.addFavoritas();
        });
        holder.importanteButtonOn.setOnClickListener(v -> {
            holder.importanteButtonOn.setVisibility(View.INVISIBLE);
            holder.importanteButtonOff.setVisibility(View.VISIBLE);
            tarea.setFavorito(false);
            TareaLab.get(context).updateTarea(tarea);
            //Actualizar la lista de Favoritos
            refrescarInterfaz.eliminarFavoritas();

        });
        //Boton eliminar
        holder.eliminarButton.setOnClickListener(v -> {
            //Aqui tiene que poner la movida de la notificación con deshacer

            int posicion = holder.getAdapterPosition();
            listaTareas.remove(posicion);
            notifyItemRemoved(posicion);
            Snackbar.make(holder.eliminarButton, "\"" + tarea.getTitulo() + "\" ha sido eliminada.", Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            switch (event) {
                                case (Snackbar.Callback.DISMISS_EVENT_ACTION):
                                    listaTareas.add(posicion, tarea);
                                    notifyItemInserted(posicion);
                                    break;
                                case (Snackbar.Callback.DISMISS_EVENT_SWIPE):
                                case (Snackbar.Callback.DISMISS_EVENT_TIMEOUT):
                                    TareaLab.get(context).deleteTarea(tarea);
                                    break;
                            }
                        }
                    })
                    .setAction("Deshacer", v1 -> {
                        //Nada
                    })
                    .show();
        });
        //CHECKED OR NOT
        holder.checkCompleto.setOnClickListener(v -> {
            boolean check = ((CheckBox) v).isChecked();
            tarea.setCompletado(check);
            TareaLab.get(context).updateTarea(tarea);
            refrescarInterfaz.eliminarFavoritas();
        });
    }


    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void actualizarListado(ArrayList<Tarea> listaTareas) {
        this.listaTareas.clear();
        this.listaTareas.addAll(listaTareas);
        notifyDataSetChanged();
    }

    public String fechaLongString(long fecha) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(fecha);
        String fechaHora = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", calendar.get(Calendar.MONTH)) + "/" + String.format("%02d", calendar.get(Calendar.YEAR)) + " " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));

        return fechaHora;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(int position);
    }
        /*
        ¡¡¡¡¡¡VIEWHOLDER!!!!!
         */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tituloTv, fechaTv;
        CardView cardViewItem;
        ImageButton importanteButtonOff, importanteButtonOn, eliminarButton;
        CheckBox checkCompleto;
        ItemClickListener itemClickListener;
        ItemLongClickListener itemLongClickListener;
        CustomAdapter adapter;

        public ViewHolder(@NonNull View itemView, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener, CustomAdapter adapter) {
            super(itemView);
            tituloTv = itemView.findViewById(R.id.titulo_textview);
            fechaTv = itemView.findViewById(R.id.fecha_textview);
            cardViewItem = itemView.findViewById(R.id.cardViewItem);
            importanteButtonOff = itemView.findViewById(R.id.boton_favorito_off);
            importanteButtonOn = itemView.findViewById(R.id.boton_favorito_on);
            eliminarButton = itemView.findViewById(R.id.boton_eliminar);
            checkCompleto = itemView.findViewById(R.id.checkbox_completado);
            this.adapter = adapter;
            this.itemClickListener = itemClickListener;
            this.itemLongClickListener = itemLongClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());

            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null)
                itemLongClickListener.onItemLongClick(getAdapterPosition());
            return true;
        }
    }
}