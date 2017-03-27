package husi.recuperapp.citas;

import android.app.Activity;
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

    private Intent nuevaCitaIntent;

    static private final int REQUEST_CODE = 1;

    private List<Cita> citas;
    private List<List<String>> citasBD;
    private ListView listViewCitas;
    private AdaptadorListViewCitas adaptadorListViewCitas;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Button mAgendarCitaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_medicas);
        getSupportActionBar().hide();

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
        nuevaCitaIntent = new Intent(this, NuevaCita.class);
        startActivityForResult(nuevaCitaIntent,REQUEST_CODE);
    }

    private void eliminarAlarmaCita(final int posicion){

        int idCita = Integer.parseInt(citas.get(posicion).getId());

        if(Paciente.getInstance().eliminarCitaBD(idCita)==true) {

            Log.i("Cancelar id cita: ", idCita+"");

            Intent intentInfoAlarmaNotificacion = new Intent(getApplicationContext(), AlarmaCitasReceiver.class);
            intentInfoAlarmaNotificacion.putExtra("id_cita", idCita + "");
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idCita,
                    intentInfoAlarmaNotificacion, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);

            crearListaCitas();
            adaptadorListViewCitas.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Se eliminó la Cita", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Hubo un problema eliminando la cita",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void crearListaCitas() {
        citasBD = Paciente.getInstance().obtenerCitasBD();

        if(citasBD!=null){
            citas.clear();
            //Obtiene las citas de la BD y llena la lista de citas
            int i=0;
            for (List<String> citaBD: citasBD){
                citas.add(new Cita(citaBD.get(i), citaBD.get(i + 1), citaBD.get(i + 2), citaBD.get(i + 3)));
            }
        }
    }

}
