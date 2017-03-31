package husi.recuperapp.sintomas;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Paciente;

public class Sintomas extends AppCompatActivity {

    Paciente paciente;

    private List<Sintoma> sintomas;
    private List<List<Object>> listaSintomasBD;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente = (Paciente)getApplicationContext();

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
        }

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaSintomas();

        this.listView.setAdapter(new AdapatadorListViewSintomas(this, sintomas));

    }

    private void crearListaSintomas() {

        sintomas = new ArrayList<>();

        listaSintomasBD = Paciente.getInstance().obtenerListaSintomasBD();

        if(listaSintomasBD!=null) {
            for (List<Object> sinBd : listaSintomasBD) {
                sintomas.add(new Sintoma(Integer.parseInt(sinBd.get(0).toString()),sinBd.get(1).toString()));
            }
        }

    }
}
