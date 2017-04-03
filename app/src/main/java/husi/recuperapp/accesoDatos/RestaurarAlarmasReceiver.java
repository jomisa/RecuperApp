package husi.recuperapp.accesoDatos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import husi.recuperapp.citas.AlarmaCitasReceiver;
import husi.recuperapp.medicamentos.AlarmaMedicamentoReceiver;

import static husi.recuperapp.accesoDatos.Paciente.getInstance;

public class RestaurarAlarmasReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent intentAlarma;

    @Override
    public void onReceive(Context contexto, Intent intentInfoAlarma) {
        Log.i("RestaurarAlarmas ", "onReceive");

        if (intentInfoAlarma != null) {
            String action = intentInfoAlarma.getAction();

            if (action != null) {
                if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    Log.i("Receiver ", "Reseteando alarmas");

                    //Restaurando alarmas de medicamentos

                    int id;
                    Calendar hora = Calendar.getInstance();
                    long frecuencia;

                    List<List<String>> medicamentosBD;
                    medicamentosBD = getInstance().obtenerMedicamentosBD();
                    Log.i("Receiver medicamentosBD", "");

                    if (medicamentosBD != null) {
                        for (List<String> medicamentoBD : medicamentosBD) {
                            if (medicamentoBD.get(4) != "0") { //Verifica que la alarma esté asignada
                                id = Integer.parseInt(medicamentoBD.get(0));
                                hora.setTimeInMillis(Long.valueOf(medicamentoBD.get(4)));
                                frecuencia = Long.valueOf(medicamentoBD.get(3));
                                frecuencia = frecuencia * 60 * 60 * 1000;

                                intentAlarma = new Intent(contexto, AlarmaMedicamentoReceiver.class);

                                Bundle extras = new Bundle();
                                extras.putInt("id_medicamento", id);
                                extras.putLong("frecuencia", frecuencia);
                                extras.putLong("horaAlarma", hora.getTimeInMillis());
                                intentAlarma.putExtras(extras);

                                pendingIntent = PendingIntent.getBroadcast(contexto, id, intentAlarma,
                                        PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager = (AlarmManager) contexto.getSystemService(contexto.ALARM_SERVICE);

                                if (android.os.Build.VERSION.SDK_INT >= 21) {
                                    Log.i("SDK >= ", "21");
                                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(
                                            hora.getTimeInMillis(), pendingIntent), pendingIntent);
                                } else if (android.os.Build.VERSION.SDK_INT >= 19) {
                                    Log.i("SDK >= ", "19");
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                            hora.getTimeInMillis(), pendingIntent);
                                } else {
                                    Log.i("SDK menor a: ", "19");
                                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                                            hora.getTimeInMillis(), pendingIntent);
                                }
                            }
                        }
                    }

                    //Restaurando citas medicas

                    List<List<String>> citasBD = Paciente.getInstance().obtenerCitasBD();
                    Calendar cal = Calendar.getInstance();
                    Calendar calActual=Calendar.getInstance();

                    Log.i("RestAlar Hora actual: ", calActual.getTimeInMillis()+"");

                    if (citasBD != null) {
                        for (List<String> cita : citasBD) {
                            cal.setTimeInMillis(Long.valueOf(cita.get(1)));

                            if(cal.getTimeInMillis()>calActual.getTimeInMillis()) {
                                Log.i("Hora notif Cita: ", cal.getTimeInMillis()+"");

                                //Se agenda Notificacion
                                int idCita = Integer.parseInt(cita.get(0));

                                Log.i("nueva cita con id: ", idCita + "");
                                Intent intentInfoAlarmaNotificacion = new Intent(contexto, AlarmaCitasReceiver.class);
                                Bundle extras = new Bundle();
                                extras.putInt("id_cita", idCita);
                                intentInfoAlarmaNotificacion.putExtras(extras);

                                pendingIntent = PendingIntent.getBroadcast(contexto, idCita,
                                        intentInfoAlarmaNotificacion, PendingIntent.FLAG_UPDATE_CURRENT);

                                alarmManager = (AlarmManager) contexto.getSystemService(contexto.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
                            }
                        }
                    }

                    Log.i("Receiver ", "Se resetearon las alarmas");
                }
            }
        }
    }
}
