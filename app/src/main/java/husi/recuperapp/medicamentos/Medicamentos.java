package husi.recuperapp.medicamentos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private Intent intentAlarma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medicamentos);

        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
        }

        medicamentos = new ArrayList<>();
        medicamentosBD = new ArrayList<>();

        listViewMedicamentos = (ListView) findViewById(R.id.listViewMedicamentos);
        crearListaMedicamentos();
        adaptadorListViewMedicamentos = new AdaptadorListViewMedicamentos(this, medicamentos);
        listViewMedicamentos.setAdapter(adaptadorListViewMedicamentos);

        //Si el activity se activó por una alarma, se extrae el id de la alarma
        intentAlarma = getIntent();
        Bundle extras = intentAlarma.getExtras();
        if (extras != null)
            manejarAlarma(extras.getInt("id_medicamento"), extras.getLong("frecuencia")
                    , extras.getLong("horaAlarma"));

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
                        else
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
    private void manejarAlarma(int idMedicamento, long frecuencia, long horaAlarma){

        Log.i("idMedicamento: ",idMedicamento+"");
        Log.i("frecuencia: ",frecuencia+"");
        horaAlarma=horaAlarma+frecuencia;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(horaAlarma);
        Log.i("horaAlarma: ",calendar.getTimeInMillis()+"");

        crearAlarma(idMedicamento, frecuencia, calendar);

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

        String mensajeDialog = Paciente.getInstance().buscarMedicamento(idMedicamento).get(1).toString() + //1 Es la col Medicamento
                " "+ Paciente.getInstance().buscarMedicamento(idMedicamento).get(2).toString(); //2 es la col dosis

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

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                int idMedicamento = Integer.parseInt(medicamentos.get(posicion).getId());
                Log.i("posicion lista: ", posicion + "");
                Log.i("idMedicamento de pos: ", idMedicamento + "");

                long frecuencia = Long.parseLong(medicamentos.get(posicion).getFrecuencia());
                frecuencia = frecuencia * 60 * 60 * 1000;
                Log.i("Frecuenca Inicial: ", frecuencia + "");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 00);

                Log.i("horaAlarma: ", calendar.getTimeInMillis() + "");

                crearAlarma(idMedicamento, frecuencia, calendar);


                Toast.makeText(getApplicationContext(), "Seleccionó: " + selectedHour + ":" +
                        selectedMinute,Toast.LENGTH_LONG).show();
            }
        }, hour, minute, false);//true hora militar
        mTimePicker.setTitle("Seleccione la hora");
        mTimePicker.show();
    }

    private void crearAlarma(int idMedicamento, long frecuencia, Calendar calendar){

        Intent intentInfoAlarma = new Intent(getApplicationContext(), AlarmaMedicamentoReceiver.class);

        Bundle extras = new Bundle();
        extras.putInt("id_medicamento", idMedicamento);
        extras.putLong("frecuencia", frecuencia);
        extras.putLong("horaAlarma", calendar.getTimeInMillis());
        intentInfoAlarma.putExtras(extras);

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idMedicamento,
                intentInfoAlarma, PendingIntent.FLAG_UPDATE_CURRENT );

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Log.i("SDK >= ","21");
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent),
                    pendingIntent);
        }
        else if (android.os.Build.VERSION.SDK_INT >= 19) {
            Log.i("SDK >= ","19");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            Log.i("SDK menor a: ","19");
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Paciente.getInstance().actualizarMedicamentoBD(idMedicamento+"", calendar.getTimeInMillis(),"true");

        crearListaMedicamentos();
        listViewMedicamentos.setAdapter(adaptadorListViewMedicamentos);

    }

    private void eliminarAlarmaMedicamento(final int posicion){

        int idMedicamento = Integer.parseInt(medicamentos.get(posicion).getId());
        Intent intentInfoAlarma = new Intent(getApplicationContext(), AlarmaMedicamentoReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idMedicamento, intentInfoAlarma, PendingIntent.FLAG_UPDATE_CURRENT );
        pendingIntent.cancel();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getApplicationContext(), "Se eliminó la Alarma",Toast.LENGTH_LONG).show();

        Paciente.getInstance().actualizarMedicamentoBD(idMedicamento+"", 0 ,"false");

        crearListaMedicamentos();
        listViewMedicamentos.setAdapter(adaptadorListViewMedicamentos);

    }

    private void crearListaMedicamentos() {

        Calendar hora = Calendar.getInstance();

        medicamentosBD = Paciente.getInstance().obtenerMedicamentosBD();
        Log.i("obtener medicamentosBD ","");

        if(medicamentosBD!=null) {
            Log.i("medicamentosBD ","");
            medicamentos.clear();
            //Obtiene los medicamentos de la BD y Llena la lista de medicamentos
            String horaS;
            int hour;
            int minute;
            for (List<String> medicamentoBD: medicamentosBD) {
                horaS="Sin Asignar";
                if(!medicamentoBD.get(4).equals("0")){
                    Log.i("Medicamentos alarma BD ",Long.valueOf(medicamentoBD.get(4))+"");
                    hora.setTimeInMillis(Long.valueOf(medicamentoBD.get(4)));
                    Log.i("Medicamentos alarma ",hora.getTimeInMillis()+"");
                    hour = hora.get(Calendar.HOUR_OF_DAY);
                    minute = hora.get(Calendar.MINUTE);
                    horaS=String.format("%02d:%02d", hour, minute);
                }
                medicamentos.add(new Medicamento(medicamentoBD.get(0), medicamentoBD.get(1),
                        medicamentoBD.get(2), medicamentoBD.get(3), horaS, medicamentoBD.get(6)));
            }
        }
    }
}