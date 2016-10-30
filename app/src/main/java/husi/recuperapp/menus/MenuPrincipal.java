package husi.recuperapp.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.citas.CitasMedicas;
import husi.recuperapp.caminatas.Pedometer;
import husi.recuperapp.fisiologicos.Liquidos;
import husi.recuperapp.login.Login;
import husi.recuperapp.medicamentos.Medicamentos;
import husi.recuperapp.accesoDatos.Paciente;

public class MenuPrincipal extends AppCompatActivity {

    Intent iniciarActivity;

    private List<Funcionalidad> funcionalidades;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+ Paciente.getInstance().getNombresApellidos());

        listView = (ListView) findViewById(R.id.listViewFunciones);
        crearListaFunciones();
        listView.setAdapter(new AdaptadorListView(this, funcionalidades));
        //En caso de activar un elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                activarActivity(posicion);
            }
        });
    }

    private void crearListaFunciones() {

        funcionalidades = new ArrayList<>();

        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_directions_walk, "Realizar Caminatas",
                "Registra tus caminatas"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_local_restaurant, "Alimentación",
                "Consultar comidas a evitar y regstrar líquidos"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_editor_insert_invitation, "Recordar Cita Médica",
                "Recordar mis citas médicas 1 día antes de la cita"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_action_alarm, "Medicamentos",
                "Consultar medicamentos, dosis y ajustar alarmas de medicamentos"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_image_edit, "Ingresar Datos",
                "Ingresar síntomas, mediciones, exámenes médicos y temperamento"));

        /*
        funcionalidades.add(new Funcionalidad(R.drawable.ic_content_content_paste, "Agenda de Tareas Diarias",
                "Anotar actividades y definir cuales son primordiales"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_social_poll, "Consultar Datos Ingresados",
                "Consultar Histórico de síntomas y mediciones"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_map, "Mapa del Hospital San Ignacio",
                "Consultar mapa interno del hospital"));*/
    }

    private void activarActivity(int posicion){

        switch(posicion){

            case 0:
                iniciarActivity = new Intent(this, Pedometer.class);
                this.startActivity(iniciarActivity);
                break;

            case 1:
                iniciarActivity = new Intent(this, Liquidos.class);
                this.startActivity(iniciarActivity);
                break;
            case 2:
                iniciarActivity = new Intent(this, CitasMedicas.class);
                this.startActivity(iniciarActivity);
                break;

            case 3:
                iniciarActivity = new Intent(this, Medicamentos.class);
                this.startActivity(iniciarActivity);
                break;
            case 4:
                iniciarActivity = new Intent(this, MenuIngresarDatos.class);
                this.startActivity(iniciarActivity);
                break;
            case 5:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;
            case 6:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;

            case 7:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;
        }

    }
}
