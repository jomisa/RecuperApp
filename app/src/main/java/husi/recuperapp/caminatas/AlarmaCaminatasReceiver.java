package husi.recuperapp.caminatas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.WakefulBroadcastReceiver;

import husi.recuperapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jmss1 on 24/10/2016.
 */

public class AlarmaCaminatasReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intentInfoAlarma) {

        String tituloNotifiacion = "Camina!!!";
        String mensajeNotificacion = "Recuerda caminar por lo menos 5 días a la semana";

        Intent abrirCaminatas = new Intent(context, Pedometer.class);
        abrirCaminatas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,abrirCaminatas,PendingIntent.FLAG_CANCEL_CURRENT);

        Notification n  = new Notification.Builder(context)
                .setContentTitle(tituloNotifiacion)
                .setContentText(mensajeNotificacion)
                .setSmallIcon(R.drawable.ic_man_walking_directions_button)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLights(Color.RED, 3000, 3000)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
