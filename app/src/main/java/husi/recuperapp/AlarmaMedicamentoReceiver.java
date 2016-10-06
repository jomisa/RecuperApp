package husi.recuperapp;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class AlarmaMedicamentoReceiver extends WakefulBroadcastReceiver {

    Intent activarMedicamentos;

    @Override
    public void onReceive(final Context context, Intent intent) {

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        Log.i("Alarma: ","ring");
        activarMedicamentos = new Intent(context.getApplicationContext(), Medicamentos.class);
        activarMedicamentos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activarMedicamentos);
    }

}
