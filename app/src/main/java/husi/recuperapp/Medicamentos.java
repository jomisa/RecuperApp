package husi.recuperapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

public class Medicamentos extends AppCompatActivity {

    Paciente paciente;

    private List<Medicamento> medicamentos;
    private List<List<String>> medicamentosBD;

    private ListView listViewMedicamentos;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private AdaptadorListViewMedicamentos adaptadorListViewMedicamentos;
    DataBaseHelper dbHelper;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_medicamentos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        dbHelper = new DataBaseHelper(this);

        medicamentos = new ArrayList<>();
        medicamentosBD = new ArrayList<List<String>>();

        this.listViewMedicamentos = (ListView) findViewById(R.id.listViewMedicamentos);
        crearListaMedicamentos();
        adaptadorListViewMedicamentos=new AdaptadorListViewMedicamentos(this, medicamentos);
        this.listViewMedicamentos.setAdapter(adaptadorListViewMedicamentos);

        //Obtiene el id del medicamento que ejecuto la alarma
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String id = extras.getString("id_medicamento");
            Log.i("id: ",id);
            //TODO: Colorear fila según id medicamento

            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.play();
            Log.i("Alarma: ","ring");
        }

        //Para desactivar la alarma de un medicamento
        this.listViewMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                //TODO: Desactivar alarma al hacer click en el medicamento
                if(ringtone!=null)
                    ringtone.stop();
            }

        });
        //Para cuadrar la alarma de un medicamento
        this.listViewMedicamentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                cuadrarAlarmaMedicamento(posicion);
                return true;
            }

        });
    }

    private void cuadrarAlarmaMedicamento(final int posicion){
        adaptadorListViewMedicamentos.notifyDataSetChanged();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                Intent myIntent = new Intent(getApplicationContext(), AlarmaMedicamentoReceiver.class);

                String idMedicamento=medicamentos.get(posicion).getId();
                myIntent.putExtra("id_medicamento",idMedicamento);

                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        Integer.parseInt(idMedicamento), myIntent, posicion);

                int frecuencia= Integer.parseInt(medicamentos.get(posicion).getFrecuencia());

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frecuencia*60*60*1000, pendingIntent);

                Long t=calendar.getTimeInMillis();
                Log.i("Hora: ", ""+t);

                Toast.makeText(getApplicationContext(), "Seleccionó: " + selectedHour + ":" + selectedMinute,Toast.LENGTH_LONG).show();

                medicamentos.get(posicion).setAsignado("true");
                medicamentos.get(posicion).setHora(selectedHour + ":" + selectedMinute);

                actualizarMedicamentoBD(posicion);
                crearListaMedicamentos();

                adaptadorListViewMedicamentos.notifyDataSetChanged();

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Seleccione la hora");
        mTimePicker.show();
    }

    private void crearListaMedicamentos() {

        medicamentosBD = dbHelper.obtenerMedicamentos();
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

    private void actualizarMedicamentoBD(int posicion){
        dbHelper.actualizarHoraConsumoMedicamento((posicion+1)+"", medicamentos.get(posicion).getHora(),medicamentos.get(posicion).getAsignado());
    }
}