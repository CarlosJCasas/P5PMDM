package com.tarea.tarea5pmdm.Core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tarea.tarea5pmdm.R;
import com.tarea.tarea5pmdm.UI.MainActivity;

import java.util.Random;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Podria recibir movidas en el intent? maybe
        int id = new Random().nextInt(200);
        String titulo = intent.getStringExtra("titulo");
        String texto = intent.getStringExtra("texto");
        String channelId = intent.getStringExtra("channelid");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(id, builder.build());
    }
}
