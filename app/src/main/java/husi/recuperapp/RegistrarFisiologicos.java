package husi.recuperapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegistrarFisiologicos extends AppCompatActivity {

    Paciente paciente;

    Intent iniciarActivity;

    private List<Fisiologico> fisiologicos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paciente=(Paciente)getApplicationContext();

        setContentView(R.layout.activity_registrar_fisiologicos);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_image_portrait);
        getSupportActionBar().setTitle("  "+paciente.getUsuario());

        this.listView = (ListView) findViewById(R.id.listViewFisiologicos);

        crearListaFisiologicos();
/*
        //En caso de activar un elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                //activarActivity(posicion);

            }
        });
*/

    }
    private void crearListaFisiologicos() {

        fisiologicos = new ArrayList<>();

        fisiologicos.add(new Fisiologico(R.drawable.ic_maps_directions_walk, "Peso",
                "Kg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_maps_local_restaurant, "Frecuencia Cardíaca",
                "bpm"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_content_content_paste, "Presión Sanguínea SYS",
                "mmHg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_editor_insert_invitation, "Presión Sanguínea DIA",
                "mmHg"));
        fisiologicos.add(new Fisiologico(R.drawable.ic_action_alarm, "Niveles de Glucosa",
                "mg"));

        this.listView.setAdapter(new AdaptadorListViewFisiologicos(this, fisiologicos));
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
        }

    }
}
