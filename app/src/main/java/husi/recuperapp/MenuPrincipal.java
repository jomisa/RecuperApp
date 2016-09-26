package husi.recuperapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    Paciente paciente;

    Intent iniciarActivity;

    private List<Funcionalidad> funcionalidades;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        this.listView = (ListView) findViewById(R.id.listViewFunciones);

        crearListaFunciones();

        //En caso de activar un elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                activarActivity(posicion);
                //TextView texto = (TextView) view.findViewById(R.id.descripcion_texto);
            }
        });

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

        this.listView.setAdapter(new AdaptadorListView(this, funcionalidades));
    }

    private void activarActivity(int posicion){

        switch(posicion){

            case 0:
                iniciarActivity = new Intent(this, Login.class);
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
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;
            case 4:
                iniciarActivity = new Intent(this, Login.class);
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
