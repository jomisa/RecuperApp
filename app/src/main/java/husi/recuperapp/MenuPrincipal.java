package husi.recuperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    private List<Funcionalidad> funcionalidades = new ArrayList<>();
    private RecyclerView recicledView;
    private ControladorRecicledView controladorRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_principal);

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

        funcionalidades.add(new Funcionalidad(R.drawable.ic_image_portrait, "Realizar Actividad Física",
                "Registrar caminata u oficios varios"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_action_lock_outline, "Plan de Alimentación",
                "Consultar comidas recomendadas"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_communication_call, "Agenda de Tareas Diarias",
                "Anotar actividades y definir cuales son primordiales"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_communication_email, "Programar Cita Médica",
                "Programar y Recordar mis citas médicas"));

        controladorRV.notifyDataSetChanged();
    }
}
