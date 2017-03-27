package husi.recuperapp.medicamentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class AlarmaMedicamentoReceiver extends WakefulBroadcastReceiver {

    Intent activarMedicamentos;

    @Override
    public void onReceive(final Context contexto, Intent intentInfoAlarma) {

        Log.i("Receiver ","id_medicamento: "+intentInfoAlarma.getExtras().getInt("id_medicamento"));
        Log.i("Receiver ","frecuencia: "+intentInfoAlarma.getExtras().getLong("frecuencia"));
        Log.i("Receiver ","horaAlarma: "+intentInfoAlarma.getExtras().getLong("horaAlarma"));

        //Recibo la info del intent (está en el bundle) y la vuelvo a reenviar al activity

        Bundle extras = new Bundle();
        extras.putInt("id_medicamento",intentInfoAlarma.getExtras().getInt("id_medicamento"));
        extras.putLong("frecuencia",intentInfoAlarma.getExtras().getLong("frecuencia"));
        extras.putLong("horaAlarma",intentInfoAlarma.getExtras().getLong("horaAlarma"));

        activarMedicamentos = new Intent(contexto.getApplicationContext(), Medicamentos.class);
        activarMedicamentos.putExtras(extras);

        //Permite ejecutar un activity desde una clase (no tipo activity) recien activada
        activarMedicamentos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        contexto.startActivity(activarMedicamentos);
    }

}
