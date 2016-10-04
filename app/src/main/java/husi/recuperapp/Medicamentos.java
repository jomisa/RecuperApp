package husi.recuperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Medicamentos extends AppCompatActivity {

    Paciente paciente;

    private List<Medicamento> medicamentos;
    private ListView listViewMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_medicamentos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        this.listViewMedicamentos = (ListView) findViewById(R.id.listViewMedicamentos);

        crearListaMedicamentos();
    }

    private void crearListaMedicamentos() {

        medicamentos = new ArrayList<>();

        medicamentos.add(new Medicamento("10 am", "dolex", "10g"));
        medicamentos.add(new Medicamento("11 am", "dolex forte", "20g"));
        medicamentos.add(new Medicamento("14 pm", "buscapina", "30g"));
        medicamentos.add(new Medicamento("13 am", "mareol", "5g"));
        medicamentos.add(new Medicamento("20 pm", "advil", "30g"));

        this.listViewMedicamentos.setAdapter(new AdaptadorListViewMedicamentos(this, medicamentos));
    }
}
