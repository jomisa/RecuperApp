package husi.recuperapp.citas;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import husi.recuperapp.accesoDatos.Paciente;
import husi.recuperapp.R;

public class CitasMedicas extends AppCompatActivity {

    private List<Cita> citas;
    private List<List<String>> citasBD;
    private ListView listViewCitas;
    private AdaptadorListViewCitas adaptadorListViewCitas;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Button mAgendarCitaView;

    //Variables para guardar resultado del Date Picker
    private int mAno;
    private int mMes;
    private int mDia;

    //Variables para guardar resultado del Time Picker
    private int mHora;
    private int mMinuto;

    //Variables para guardar resultado Dialog
    private String mNombreMedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_medicas);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+Paciente.getInstance().getNombresApellidos());

        citas = new ArrayList<>();
        citasBD = new ArrayList<>();

        //En caso de oprimir el boton de agendar una cita nueva
        mAgendarCitaView = (Button) findViewById(R.id.crear_cita_button);
        mAgendarCitaView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                presionoBotonAgendarCita();
            }
        });

        this.listViewCitas = (ListView) findViewById(R.id.listViewCitas);
        crearListaCitas();
        adaptadorListViewCitas=new AdaptadorListViewCitas(this, citas);
        this.listViewCitas.setAdapter(adaptadorListViewCitas);

        //Para eliminar una cita
        this.listViewCitas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, final int posicion, long arg) {
                AlertDialog.Builder eliminarDialog = new AlertDialog.Builder(view.getContext());
                eliminarDialog
                        .setTitle("Eliminar Cita")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarAlarmaCita(posicion);
                            } })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            } })
                        .create()
                        .show();

                return true;
            }
        });
    }

    private void presionoBotonAgendarCita(){

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese el nombre del medico");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNombreMedico = input.getText().toString();
                Log.i("Medico: ",mNombreMedico);

                guardarcitaBDyAgendarNotificacion();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

        Calendar calendario = Calendar.getInstance();
        TimePickerDialog mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                mHora = selectedHour;
                mMinuto = selectedMinute;

                Log.i("Hora: ","Hora "+mHora+" minuto "+mMinuto);
            }
        }, calendario.get(Calendar.HOUR), calendario.get(Calendar.MINUTE), false);//true, hora militar
        mTimePicker.setTitle("Seleccione la hora");
        mTimePicker.show();

        DatePickerDialog datePicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                mAno = selectedYear;
                mMes = selectedMonth+1;
                mDia = selectedDay;

                Log.i("Fecha: ","Año "+mAno+" Mes "+mMes+" Dia "+mDia);
            }
        },calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));
        datePicker.setTitle("Seleccione la fecha de la cita");
        datePicker.show();
    }

    private void guardarcitaBDyAgendarNotificacion(){

        String fecha=mAno+"/"+mMes+"/"+mDia+"-"+mHora+":"+mMinuto;

        //Guarda la cita en la BD
        Paciente.getInstance().insertarCitaBD(fecha,mNombreMedico);
        //Actualiza el listview
        crearListaCitas();
        adaptadorListViewCitas.notifyDataSetChanged();

        //Se agenda Notificacion
        List<Object> citaAgendada = Paciente.getInstance().buscarCitaBD(fecha,mNombreMedico);
        if(citaAgendada != null) {
            int idCita=Integer.parseInt(citaAgendada.get(0).toString());

            Log.i("nueva cita con id: ",idCita+"");
            Intent intentInfoAlarmaNotificacion = new Intent(getApplicationContext(), AlarmaCitasReceiver.class);
            intentInfoAlarmaNotificacion.putExtra("id_cita", idCita + "");

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idCita, intentInfoAlarmaNotificacion, PendingIntent.FLAG_CANCEL_CURRENT);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, mMes-1);
            cal.set(Calendar.YEAR, mAno);
            cal.set(Calendar.DAY_OF_MONTH, mDia);

            cal.set(Calendar.HOUR_OF_DAY, mHora);
            cal.set(Calendar.MINUTE, mMinuto);
            cal.set(Calendar.SECOND, 00);
            cal.setTimeInMillis(cal.getTimeInMillis()-86400000);
            Log.i("Hora notif Cita: ", cal.getTimeInMillis()+"");
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Log.i("SDK >= ","21");
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(cal.getTimeInMillis(), pendingIntent), pendingIntent);
            }else if (android.os.Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() ,pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() ,pendingIntent);
            }
        }

    }

    private void eliminarAlarmaCita(final int posicion){

        int idCita = Integer.parseInt(citas.get(posicion).getId());

        if(Paciente.getInstance().eliminarCitaBD(idCita)==true) {

            Log.i("Cancelar id cita: ", idCita+"");

            Intent intentInfoAlarmaNotificacion = new Intent(getApplicationContext(), AlarmaCitasReceiver.class);
            intentInfoAlarmaNotificacion.putExtra("id_cita", idCita + "");
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idCita, intentInfoAlarmaNotificacion, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);

            crearListaCitas();
            adaptadorListViewCitas.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Se eliminó la Cita", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Hubo un problema eliminando la cita", Toast.LENGTH_LONG).show();
        }
    }

    private void crearListaCitas() {
        citasBD = Paciente.getInstance().obtenerCitasBD();

        if(citasBD!=null){
            citas.clear();
            //Obtiene las citas de la BD y llena la lista de citas
            int i=0;
            for (List<String> citaBD: citasBD){
                citas.add(new Cita(citaBD.get(i), citaBD.get(i + 1), citaBD.get(i + 2)));
            }
        }
    }

}
