package husi.recuperapp.menuPrincipal;

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
import husi.recuperapp.caminatas.Settings;
import husi.recuperapp.fisiologicos.RegistrarFisiologicos;
import husi.recuperapp.login.Login;
import husi.recuperapp.medicamentos.Medicamentos;
import husi.recuperapp.menuPrincipal.AdaptadorListView;
import husi.recuperapp.menuPrincipal.Funcionalidad;

public class MenuPrincipal extends AppCompatActivity {

    Intent iniciarActivity;

    private List<Funcionalidad> funcionalidades;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Obtiene dato nombre usuario del intent
        String usuario;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                usuario= null;
            } else {
                usuario= extras.getString("usuario");
            }
        } else {
            usuario= (String) savedInstanceState.getSerializable("usuario");
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+usuario);
        //TODO: Toca implementar un boton en el actionbar para los ajustes y llamar al método ingresarAjustes()

        this.listView = (ListView) findViewById(R.id.listViewFunciones);
        crearListaFunciones();
        this.listView.setAdapter(new AdaptadorListView(this, funcionalidades));
        //En caso de activar un elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                activarActivity(posicion);
            }
        });
    }

    private void ingresarAjustes(){
        iniciarActivity = new Intent(this, Settings.class);
        this.startActivity(iniciarActivity);
    }

    private void crearListaFunciones() {

        funcionalidades = new ArrayList<>();

        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_directions_walk, "Realizar Actividad Física",
                "Registrar caminata u oficios varios"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_local_restaurant, "Plan de Alimentación",
                "Consultar comidas recomendadas"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_content_content_paste, "Agenda de Tareas Diarias",
                "Anotar actividades y definir cuales son primordiales"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_editor_insert_invitation, "Programar Cita Médica",
                "Programar y Recordar mis citas médicas"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_action_alarm, "Medicamentos",
                "Medicamentos, dosis y alarma de medicamentos"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_image_edit, "Ingresar Datos",
                "Ingresar síntomas, mediciones y realizar encuesta"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_social_poll, "Consultar Datos Ingresados",
                "Consultar Histórico de síntomas y mediciones"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_map, "Mapa del Hospital San Ignacio",
                "Consultar mapa interno del hospital"));
    }

    private void activarActivity(int posicion){

        switch(posicion){

            case 0:
                iniciarActivity = new Intent(this, Pedometer.class);
                this.startActivity(iniciarActivity);
                break;

            case 1:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;
            case 2:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;

            case 3:
                iniciarActivity = new Intent(this, CitasMedicas.class);
                this.startActivity(iniciarActivity);
                break;
            case 4:
                iniciarActivity = new Intent(this, Medicamentos.class);
                this.startActivity(iniciarActivity);
                break;
            case 5:
                iniciarActivity = new Intent(this, RegistrarFisiologicos.class);
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
