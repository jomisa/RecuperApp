package husi.recuperapp.menus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.estadosDeAnimo.EstadosDeAnimo;
import husi.recuperapp.fisiologicos.Examenes;
import husi.recuperapp.fisiologicos.RegistrarFisiologicos;
import husi.recuperapp.sintomas.Sintomas;
import husi.recuperapp.accesoDatos.Paciente;

public class MenuIngresarDatos extends AppCompatActivity {

    Intent iniciarActivity;

    private List<Funcionalidad> funcionalidades;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_principal);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_framed_portrait);
        getSupportActionBar().setTitle("  "+ Paciente.getInstance().getNombresApellidos());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
        }

        listView = (ListView) findViewById(R.id.listViewFunciones);
        crearListaFunciones();
        listView.setAdapter(new AdaptadorListView(this, funcionalidades));
        //En caso de activar un elemento de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicion, long arg) {
                activarActivity(posicion);
            }
        });
    }

    private void crearListaFunciones() {

        funcionalidades = new ArrayList<>();

        funcionalidades.add(new Funcionalidad(R.drawable.ic_scale_bathroom, "Datos Fisiológicos",
                "Ingresa datos como peso, frecuencia, nieveles de glucosa, entre otros"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_stethoscope, "Síntomas",
                "Ingresa algún síntoma presentado, como ahogo, nauseas, entre otros"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_emoticon_with_happy_face, "Estado de ánimo",
                "Ingresa tu estado de ánimo de hoy"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_google_drive_file, "Exámenes Médicos",
                "Ingresa los resultados de tus exámenes médicos"));
    }

    private void activarActivity(int posicion){

        switch(posicion){

            case 0:
                iniciarActivity = new Intent(this, RegistrarFisiologicos.class);
                this.startActivity(iniciarActivity);
                break;

            case 1:
                iniciarActivity = new Intent(this, Sintomas.class);
                this.startActivity(iniciarActivity);
                break;
            case 2:
                iniciarActivity = new Intent(this, EstadosDeAnimo.class);
                this.startActivity(iniciarActivity);
                break;

            case 3:
                iniciarActivity = new Intent(this, Examenes.class);
                this.startActivity(iniciarActivity);
                break;
        }

    }
}
