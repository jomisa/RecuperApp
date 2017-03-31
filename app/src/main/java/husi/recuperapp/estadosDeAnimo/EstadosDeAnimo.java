package husi.recuperapp.estadosDeAnimo;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Paciente;

public class EstadosDeAnimo extends AppCompatActivity {

    Paciente paciente;

    private List<Animo> animos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
        }

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaAnimos();

        this.listView.setAdapter(new AdaptadorListViewAnimos(this, animos));

    }

    private void crearListaAnimos() {
        animos = new ArrayList<>();
        animos.add(new Animo("Ingresa tu estado de ánimo"));
    }
}
