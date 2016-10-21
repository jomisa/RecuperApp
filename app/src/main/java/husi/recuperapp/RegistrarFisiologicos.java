package husi.recuperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RegistrarFisiologicos extends AppCompatActivity {

    Paciente paciente;

    private List<Fisiologico> fisiologicos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getNombresApellidos());

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaFisiologicos();

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, fisiologicos));

    }

    private void crearListaFisiologicos() {

        fisiologicos = new ArrayList<>();

        fisiologicos.add(new Fisiologico(R.drawable.ic_balanza, "Peso",
                "Kg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_corazon, "Frecuencia Cardíaca",
                "bpm"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_presion, "Presión Sanguínea SYS",
                "mmHg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_presion, "Presión Sanguínea DIA",
                "mmHg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_glucosa, "Niveles de Glucosa",
                "mg"));

    }

}
