package husi.recuperapp.citas;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.rojoClaro, null));
            window.setNavigationBarColor(Color.GRAY);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(
                    getString(R.string.app_name), bm, ResourcesCompat.getColor(getResources(), R.color.rojoOscuro, null));
            setTaskDescription(taskDesc);
        }

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
            listViewCitas.setAdapter(adaptadorListViewCitas);

            Toast.makeText(getApplicationContext(), "Se eliminó la Cita", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Hubo un problema eliminando la cita",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent respuesta){
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                crearListaCitas();
                listViewCitas.setAdapter(adaptadorListViewCitas);
            }
        }
    }

    private void crearListaCitas() {
        citasBD = Paciente.getInstance().obtenerCitasBD();
        Calendar fecha = Calendar.getInstance();
        int dia;
        int mes;
        int ano;
        int hora;
        int minuto;

        if(citasBD!=null){
            citas.clear();
            //Obtiene las citas de la BD y llena la lista de citas
            for (List<String> citaBD: citasBD){
                fecha.setTimeInMillis(Long.valueOf(citaBD.get(1)));
                dia = fecha.get(Calendar.DAY_OF_MONTH);
                dia=dia+1;
                mes = fecha.get(Calendar.MONTH);
                mes=mes+1;
                ano = fecha.get(Calendar.YEAR);
                hora = fecha.get(Calendar.HOUR_OF_DAY);
                minuto = fecha.get(Calendar.MINUTE);

                citas.add(new Cita(citaBD.get(0), String.format("%02d/%02d/%04d", dia, mes, ano)
                        , String.format("%02d:%02d", hora, minuto) , citaBD.get(2)));
            }
        }
    }

}
