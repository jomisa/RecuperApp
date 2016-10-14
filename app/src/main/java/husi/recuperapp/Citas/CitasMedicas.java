package husi.recuperapp.citas;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import husi.recuperapp.DataBaseHelper;
import husi.recuperapp.Paciente;
import husi.recuperapp.R;

public class CitasMedicas extends AppCompatActivity {

    Paciente paciente;

    private List<Cita> citas;
    private List<List<String>> citasBD;
    private ListView listViewCitas;
    private AdaptadorListViewCitas adaptadorListViewCitas;
    DataBaseHelper dbHelper;

    private Button mAgendarCitaView;

    //Variables para guardar resultado del Date Picker
    private int mAno;
    private int mMes;
    private int mDia;

    //Variables para guardar resultado Dialog
    private String nombreMedico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_medicas);

        paciente=(Paciente)getApplicationContext();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        dbHelper = new DataBaseHelper(this);

        citas = new ArrayList<>();
        citasBD = new ArrayList<List<String>>();

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

        //Para modificar una cita agendada
        this.listViewCitas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                //TODO implementar modificar una cita agendada
                //modificarCita(posicion);
                return true;
            }

        });

        //TODO este método se debe ejecutar en el Receiver de Citas Medicas
        notificar();
    }

    private void presionoBotonAgendarCita(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    private void ingresarNombreMedico() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese el nombre del medico");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombreMedico = input.getText().toString();
                Log.i("Medico: ",nombreMedico);
                dbHelper.insertarUnaCita(mAno+""+mMes+""+mDia ,nombreMedico+"");
                crearListaCitas();
                adaptadorListViewCitas.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void crearListaCitas() {
        citasBD = dbHelper.obtenerCitas();

        if(citasBD!=null){
            citas.clear();
            //Obtiene las citas de la BD y llena la lista de citas
            int i=0;
            for (List<String> citaBD: citasBD){
                citas.add(new Cita(citaBD.get(i), citaBD.get(i + 1), citaBD.get(i + 2)));
            }
        }
    }

    private void modificarCita(int posicion){
        //Modifica la tabla de citas en la BD
        actualizarCitaBD(posicion);
    }

    private void actualizarCitaBD(int posicion){
        dbHelper.actualizarUnaCita((posicion+1)+"", citas.get(posicion).getFecha(),
                citas.get(posicion).getMedico());
        adaptadorListViewCitas.notifyDataSetChanged();
    }

    //TODO este método debe ir en Receiber de la alarma, debe contener la hora de la cita y el nombre del médico
    //TODO tambien debe ejecutarse 1 día antes de la cita y debe cancelar la alarma al ejecutar la notificación
    //Notifiacion
    private void notificar() {
        String tituloNotifiacion = "Recordatorio cita";
        String mensajeNotificacion = "Recuerde que tiene una cita el día de mañana";

        Intent notificationIntent = new Intent(this, CitasMedicas.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),//tiempo actual como id
                notificationIntent, 0);

        Notification n  = new Notification.Builder(this)
            .setContentTitle(tituloNotifiacion)
            .setContentText(mensajeNotificacion)
            .setSmallIcon(R.drawable.ic_action_alarm)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }


    //Date Picker
    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mAno = year;
            mMes = monthOfYear+1;
            mDia = dayOfMonth;

            Log.i("Fecha: ","Año "+mAno+" Mes "+mMes+" Dia "+mDia);

            ingresarNombreMedico();
            nombreMedico="Medico ";
            Log.i("Medico: ",nombreMedico);
        }
    }

}
