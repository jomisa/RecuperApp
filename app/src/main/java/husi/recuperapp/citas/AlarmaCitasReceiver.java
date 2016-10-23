package husi.recuperapp.citas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.medicamentos.Medicamentos;
import husi.recuperapp.utils.Paciente;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jmss1 on 23/10/2016.
 */

public class AlarmaCitasReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intentInfoAlarma) {
        int idCita =Integer.parseInt(intentInfoAlarma.getStringExtra("id_cita"));
        List<Object> cita = Paciente.getInstance().buscarCitaIdBD(idCita);

        String tituloNotifiacion = "Cita hostpital con: "+cita.get(2).toString();
        String mensajeNotificacion = "Está agendada el día: "+cita.get(1).toString();

        Intent abrirCitas = new Intent(context, CitasMedicas.class);
        abrirCitas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,idCita,abrirCitas,PendingIntent.FLAG_CANCEL_CURRENT);

        Notification n  = new Notification.Builder(context)
                .setContentTitle(tituloNotifiacion)
                .setContentText(mensajeNotificacion)
                .setSmallIcon(R.drawable.ic_action_alarm)
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
