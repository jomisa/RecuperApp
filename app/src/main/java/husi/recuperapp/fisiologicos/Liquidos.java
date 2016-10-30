package husi.recuperapp.fisiologicos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Paciente;

public class Liquidos extends AppCompatActivity {

    Paciente paciente;

    private List<Fisiologico> liquidos;
    private ListView listView;
    private TextView mAlimentosRecomendadosTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getNombresApellidos());

        this.mAlimentosRecomendadosTextView = (TextView) findViewById(R.id.recomendacionesTexto);
        mAlimentosRecomendadosTextView.setVisibility(View.VISIBLE);

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaLiquidos();

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, liquidos));

    }

    private void crearListaLiquidos() {

        liquidos = new ArrayList<>();

        liquidos.add(new Fisiologico(R.drawable.ic_tasa, "Líquidos","mL"));

    }
}
