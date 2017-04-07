package husi.recuperapp.citas;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Paciente;

public class NuevaCita extends AppCompatActivity {

    private Intent activarCitasMedicas;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    //Variables para guardar resultado del Date Picker
    private int mAno;
    private int mMes;
    private int mDia;

    //Variables para guardar resultado del Time Picker
    private int mHora;
    private int mMinuto;

    Calendar calendario;

    //Variables para guardar resultado Dialog
    private String mNombreMedico;

    private Button mAgendarCitaView;

    private EditText mNombreMedicoView;
    private TextView mHoraView;
    private TextView mFechaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);
        getSupportActionBar().hide();

        calendario = Calendar.getInstance();
        mNombreMedicoView = (EditText) findViewById(R.id.medico_cita_editText);

        mHoraView = (TextView) findViewById(R.id.hora_cita_texto);
        mHoraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionoTextoHora();
            }
        });

        mFechaView = (TextView) findViewById(R.id.fecha_cita_texto);
        mFechaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presionoTextoFecha();
            }
        });

        //En caso de oprimir el boton de agendar una cita nueva
        mAgendarCitaView = (Button) findViewById(R.id.nueva_cita_button);
        mAgendarCitaView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                presionoBotonAgendarCita();
            }
        });
    }

    private void presionoTextoHora(){
        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mHora = selectedHour;
                mMinuto = selectedMinute;

                Log.i("Hora: ",String.format("%02d:%02d", selectedHour, selectedMinute));
                mHoraView.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        }, calendario.get(Calendar.HOUR), calendario.get(Calendar.MINUTE), false);//true, hora militar
        mTimePicker.setTitle("Seleccione la hora");
        mTimePicker.show();
    }

    private void presionoTextoFecha(){
        DatePickerDialog datePicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                mAno = selectedYear;
                mMes = selectedMonth+1;
                mDia = selectedDay;

                Log.i("Fecha: ",String.format("%02d/%02d/%04d", mDia, mMes, mAno));
                mFechaView.setText(String.format("%02d/%02d/%04d", mDia, mMes, mAno));
            }
        },calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));
        datePicker.setTitle("Seleccione la fecha de la cita");
        datePicker.show();
    }

    private void presionoBotonAgendarCita(){

        mNombreMedico = mNombreMedicoView.getText().toString();

        if (mNombreMedico.matches("") || mHoraView.getText().toString().matches("Ingrese la Hora")
                || mFechaView.getText().toString().matches("Ingrese la Fecha")) {
            Toast.makeText(this, "Diligencie todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, mMes-1);
        cal.set(Calendar.YEAR, mAno);
        cal.set(Calendar.DAY_OF_MONTH, mDia);

        cal.set(Calendar.HOUR_OF_DAY, mHora);
        cal.set(Calendar.MINUTE, mMinuto);
        cal.set(Calendar.SECOND, 00);
        cal.setTimeInMillis(cal.getTimeInMillis()-86400000);
        Log.i("Hora notif Cita: ", cal.getTimeInMillis()+"");

        //Guarda la cita en la BD
        Paciente.getInstance().insertarCitaBD(cal.getTimeInMillis(), mNombreMedico);

        //Se agenda Notificacion
        int idCita= Integer.parseInt( Paciente.getInstance().buscarCitaBD(cal.getTimeInMillis(),
                mNombreMedico).get(0));

        Log.i("nueva cita con id: ",idCita+"");
        Intent intentInfoAlarmaNotificacion = new Intent(getApplicationContext(), AlarmaCitasReceiver.class);
        Bundle extras = new Bundle();
        extras.putInt("id_cita", idCita);
        intentInfoAlarmaNotificacion.putExtras(extras);

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idCita,
                intentInfoAlarmaNotificacion, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis() ,pendingIntent);

        //Crea un intent
        activarCitasMedicas = new Intent(this, CitasMedicas.class);
        setResult(Activity.RESULT_OK, activarCitasMedicas);
        //ejecuta el metodo onDestry() de esta actividad
        this.finish();

    }
}
