package husi.recuperapp;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    Paciente paciente;

    private List<Funcionalidad> funcionalidades = new ArrayList<>();
    private RecyclerView recicledView;
    private ControladorRecicledView controladorRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        recicledView=(RecyclerView)findViewById(R.id.recycler_view);

        controladorRV = new ControladorRecicledView(funcionalidades);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recicledView.setLayoutManager(llm);
        recicledView.setHasFixedSize(true);
        recicledView.setItemAnimator(new DefaultItemAnimator());
        recicledView.setAdapter(controladorRV);

        crearListaFunciones();

    }
    private void crearListaFunciones() {

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

        controladorRV.notifyDataSetChanged();
    }
}
