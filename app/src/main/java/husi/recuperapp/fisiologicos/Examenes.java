package husi.recuperapp.fisiologicos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.utils.Paciente;

public class Examenes extends AppCompatActivity {

    Paciente paciente;

    private List<Fisiologico> examenes;
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

        crearListaExamenes();

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, examenes));

    }

    private void crearListaExamenes() {

        examenes = new ArrayList<>();

        examenes.add(new Fisiologico(R.drawable.ic_examenes, "Número de glóbulos rojos","."));
        examenes.add(new Fisiologico(R.drawable.ic_examenes, "Número de reticulocitos","."));
        examenes.add(new Fisiologico(R.drawable.ic_examenes, "Número Plaquetas","."));
        examenes.add(new Fisiologico(R.drawable.ic_examenes, "Número Hemoglobina","."));
        examenes.add(new Fisiologico(R.drawable.ic_examenes, "Número Hematocrito","."));

    }
}
