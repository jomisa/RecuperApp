package husi.recuperapp.citas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Paciente;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by jmss1 on 23/10/2016.
 */

public class AlarmaCitasReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intentInfoAlarma) {
        int idCita =intentInfoAlarma.getExtras().getInt("id_cita");
        List<String> cita = Paciente.getInstance().buscarCitaIdBD(idCita);

        Calendar fecha = Calendar.getInstance();
        int dia;
        int mes;
        int ano;
        int hora;
        int minuto;

        fecha.setTimeInMillis(Long.valueOf(cita.get(1)));
        dia = fecha.get(Calendar.DAY_OF_MONTH);
        dia=dia+1;
        mes = fecha.get(Calendar.MONTH);
        mes=mes+1;
        ano = fecha.get(Calendar.YEAR);
        hora = fecha.get(Calendar.HOUR_OF_DAY);
        minuto = fecha.get(Calendar.MINUTE);

        String tituloNotifiacion = "Cita hostpital con: "+cita.get(2);
        String mensajeNotificacion = "Está agendada el día: "+dia+"/"+mes+"/"+ano +" a las "+
                hora+":"+minuto;

        Intent abrirCitas = new Intent(context, CitasMedicas.class);
        abrirCitas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,idCita,abrirCitas,
                PendingIntent.FLAG_CANCEL_CURRENT);

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

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
