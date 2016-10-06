package husi.recuperapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Medicamentos extends AppCompatActivity {

    Paciente paciente;

    private List<Medicamento> medicamentos;
    private ListView listViewMedicamentos;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private AdaptadorListViewMedicamentos adaptadorListViewMedicamentos;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_medicamentos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        dbHelper = new DataBaseHelper(this);

        this.listViewMedicamentos = (ListView) findViewById(R.id.listViewMedicamentos);
        crearListaMedicamentos();
        adaptadorListViewMedicamentos=new AdaptadorListViewMedicamentos(this, medicamentos);
        this.listViewMedicamentos.setAdapter(adaptadorListViewMedicamentos);

        //En caso de activar Asignar alarma a un medicamento
        this.listViewMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                cuadrarAlarmaMedicamento(posicion);
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
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

                Long t=calendar.getTimeInMillis();
                Log.i("Hora: ", ""+t);

                Toast.makeText(getApplicationContext(), "Seleccionó: " + selectedHour + ":" + selectedMinute,Toast.LENGTH_LONG).show();

                medicamentos.get(posicion).setAsignado("true");
                medicamentos.get(posicion).setHora(selectedHour + ":" + selectedMinute);

                actualizarMedicamento(posicion);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Seleccione la hora");
        mTimePicker.show();
    }

    private void crearListaMedicamentos() {
        medicamentos = new ArrayList<>();
        ArrayList<String> medicamentosBD = new ArrayList<String>();

        medicamentosBD = dbHelper.obtenerMedicamentos();

        //Obtiene los medicamentos de la BD y Llena la lista de medicamentos
        for (int i = 0; i < medicamentosBD.size(); i=i+6) {
            medicamentos.add(new Medicamento(medicamentosBD.get(i),medicamentosBD.get(i+1),medicamentosBD.get(i+2)
            ,medicamentosBD.get(i+3),medicamentosBD.get(i+4),medicamentosBD.get(i+5)));
        }
    }

    private void actualizarMedicamento(int posicion){
        dbHelper.actualizarUnMedicamento((posicion+1)+"", medicamentos.get(posicion).getNombre(),
                medicamentos.get(posicion).getDosis(),medicamentos.get(posicion).getFrecuencia(),
                medicamentos.get(posicion).getHora(),medicamentos.get(posicion).getAsignado());
        adaptadorListViewMedicamentos.notifyDataSetChanged();
    }
}