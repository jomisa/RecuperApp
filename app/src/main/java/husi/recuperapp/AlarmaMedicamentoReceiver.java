package husi.recuperapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class AlarmaMedicamentoReceiver extends WakefulBroadcastReceiver {

    Intent activarMedicamentos;

    @Override
    public void onReceive(final Context context, Intent intent) {
        activarMedicamentos = new Intent(context.getApplicationContext(), Medicamentos.class);
        //Pongo el id del medicamento que obtube en el extra, en el extra de nuevo intent
        activarMedicamentos.putExtra("id_medicamento",intent.getStringExtra("id_medicamento"));
        //Permite ejecutar un activity desde una clase (no tipo activity) recien activada
        activarMedicamentos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activarMedicamentos);
    }

}
