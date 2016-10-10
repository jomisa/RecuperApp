package husi.recuperapp.Citas;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import husi.recuperapp.AdaptadorListViewMedicamentos;
import husi.recuperapp.DataBaseHelper;
import husi.recuperapp.Medicamento;
import husi.recuperapp.Paciente;
import husi.recuperapp.R;

public class CitasMedicas extends AppCompatActivity {

    Paciente paciente;

    private List<Cita> citas;
    ArrayList<String> citasBD;
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
        citasBD = new ArrayList<String>();

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
        // citas = new ArrayList<>();
        citas.clear();
        citasBD.clear();

        citasBD = dbHelper.obtenerCitas();

        if(citasBD!=null) {
            //Obtiene las citas de la BD y llena la lista de citas
            for (int i = 0; i < citasBD.size(); i = i + 3) {
                citas.add(new Cita(citasBD.get(i), citasBD.get(i + 1), citasBD.get(i + 2)));
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
