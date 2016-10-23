package husi.recuperapp.medicamentos;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class AlarmaMedicamentoReceiver extends WakefulBroadcastReceiver {

    Intent activarMedicamentos;

    @Override
    public void onReceive(final Context contexto, Intent intentInfoAlarma) {
        activarMedicamentos = new Intent(contexto.getApplicationContext(), Medicamentos.class);
        //Pongo el id del medicamento que obtube en el extra, en el extra de nuevo intent
        activarMedicamentos.putExtra("id_medicamento",intentInfoAlarma.getStringExtra("id_medicamento"));
        //Permite ejecutar un activity desde una clase (no tipo activity) recien activada
        activarMedicamentos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        contexto.startActivity(activarMedicamentos);
    }

}
