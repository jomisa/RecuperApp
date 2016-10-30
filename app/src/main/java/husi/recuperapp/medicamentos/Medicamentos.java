package husi.recuperapp.medicamentos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import husi.recuperapp.accesoDatos.Paciente;
import husi.recuperapp.R;

public class Medicamentos extends AppCompatActivity {

    private List<Medicamento> medicamentos;
    private List<List<String>> medicamentosBD;
    private AdaptadorListViewMedicamentos adaptadorListViewMedicamentos;
    private ListView listViewMedicamentos;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    Intent intentAlarma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicamentos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+Paciente.getInstance().getNombresApellidos());

        medicamentos = new ArrayList<>();
        medicamentosBD = new ArrayList<>();

        listViewMedicamentos = (ListView) findViewById(R.id.listViewMedicamentos);
        crearListaMedicamentos();
        adaptadorListViewMedicamentos = new AdaptadorListViewMedicamentos(this, medicamentos);
        listViewMedicamentos.setAdapter(adaptadorListViewMedicamentos);

        //Si el activity se activo por una alarma, se extrae el id de la alarma
        intentAlarma = getIntent();
        Bundle idAlarma = intentAlarma.getExtras();
        if (idAlarma != null) {
            manejarAlarma(idAlarma.getString("id_medicamento"));
        }

        //Para cuadrar la alarma de un medicamento
        this.listViewMedicamentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, final int posicion, long arg) {

                CharSequence opciones[] = new CharSequence[] {"Cuadrar/Modificar", "Eliminar"};

                AlertDialog.Builder dialogOpcionAlarma = new AlertDialog.Builder(view.getContext());
                dialogOpcionAlarma.setTitle("Que desea hacer con la hora");
                dialogOpcionAlarma.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int opcion) {
                        if(opcion==0)
                            cuadrarAlarmaMedicamento(posicion);
                        eliminarAlarmaMedicamento(posicion);
                    }
                });
                dialogOpcionAlarma.create();
                dialogOpcionAlarma.show();

                return true;
            }

        });
    }

    //Este metodo se ejecta al recibir un intent de una alarma agendada
    private void manejarAlarma(String idAlarma){
        Log.i("id: ",idAlarma);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
        Log.i("Alarma: ","ring");

        final Vibrator v = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);


        AlertDialog.Builder dialogAlarma = new AlertDialog.Builder(this);
        dialogAlarma.setTitle("Hora De Tomar El Medicamento");

        String mensajeDialog = Paciente.getInstance().buscarMedicamento(Integer.parseInt(idAlarma)).get(1).toString() + //1 Es la col Medicamento
                " "+ Paciente.getInstance().buscarMedicamento(Integer.parseInt(idAlarma)).get(2).toString(); //2 es la col dosis

        dialogAlarma.setMessage(mensajeDialog);
        dialogAlarma.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(ringtone!=null) {
                    ringtone.stop();
                    v.cancel();
                }
            }
        });
        dialogAlarma.create();
        dialogAlarma.show();
    }

    private void cuadrarAlarmaMedicamento(final int posicion){

        //adaptadorListViewMedicamentos.notifyDataSetChanged();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                int idMedicamento = Integer.parseInt(medicamentos.get(posicion).getId());

                Log.i("posicion lista: ",posicion+"");
                Log.i("idMedicamento de pos: ",idMedicamento+"");

                Intent intentInfoAlarma = new Intent(getApplicationContext(), AlarmaMedicamentoReceiver.class);
                intentInfoAlarma.putExtra("id_medicamento",idMedicamento+"");

                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idMedicamento, intentInfoAlarma, PendingIntent.FLAG_CANCEL_CURRENT );

                int frecuencia= Integer.parseInt(medicamentos.get(posicion).getFrecuencia());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frecuencia*60*60*1000, pendingIntent);

                Long t=calendar.getTimeInMillis();
                Log.i("Hora: ", ""+t);

                Toast.makeText(getApplicationContext(), "Seleccionó: " + selectedHour + ":" + selectedMinute,Toast.LENGTH_LONG).show();

                Paciente.getInstance().actualizarMedicamentoBD(idMedicamento+"", selectedHour + ":" + selectedMinute,"true");
                crearListaMedicamentos();

                adaptadorListViewMedicamentos.notifyDataSetChanged();

            }
        }, hour, minute, false);//true hora militar
        mTimePicker.setTitle("Seleccione la hora");
        mTimePicker.show();
    }

    private void eliminarAlarmaMedicamento(final int posicion){

        int idMedicamento = Integer.parseInt(medicamentos.get(posicion).getId());
        Intent intentInfoAlarma = new Intent(getApplicationContext(), AlarmaMedicamentoReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idMedicamento, intentInfoAlarma, PendingIntent.FLAG_CANCEL_CURRENT );
        pendingIntent.cancel();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getApplicationContext(), "Se eliminó la Alarma",Toast.LENGTH_LONG).show();

        Paciente.getInstance().actualizarMedicamentoBD(idMedicamento+"", "Sin Asignar","false");
        crearListaMedicamentos();

        adaptadorListViewMedicamentos.notifyDataSetChanged();
    }

    private void crearListaMedicamentos() {

        medicamentosBD = Paciente.getInstance().obtenerMedicamentosBD();
        Log.i("obtener medicamentosBD ","");

        if(medicamentosBD!=null) {
            Log.i("medicamentosBD ","");
            medicamentos.clear();
            //Obtiene los medicamentos de la BD y Llena la lista de medicamentos
            int i = 0;
            for (List<String> medicamentoBD: medicamentosBD) {
                medicamentos.add(new Medicamento(medicamentoBD.get(i), medicamentoBD.get(i + 1), medicamentoBD.get(i + 2)
                        , medicamentoBD.get(i + 3), medicamentoBD.get(i + 4), medicamentoBD.get(i + 6)));
            }
        }
    }
}