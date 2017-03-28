package husi.recuperapp.menus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.caminatas.Settings;
import husi.recuperapp.citas.CitasMedicas;
import husi.recuperapp.caminatas.Pedometer;
import husi.recuperapp.fisiologicos.Liquidos;
import husi.recuperapp.login.Login;
import husi.recuperapp.medicamentos.Medicamentos;
import husi.recuperapp.accesoDatos.Paciente;

public class MenuPrincipal extends AppCompatActivity {

    Intent iniciarActivity;

    private List<Funcionalidad> funcionalidades;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_principal);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarMenuPrincipal);
        setSupportActionBar(myToolbar);
        TextView usuario = (TextView) findViewById(R.id.toolbarUserTextView);
        usuario.setText(Paciente.getInstance().getNombresApellidos());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        findViewById(R.id.toolbarSettingsImageview).setOnClickListener(abrirAjustes());
        findViewById(R.id.toolbarSettingsTextView).setOnClickListener(abrirAjustes());
    }

    private View.OnClickListener abrirAjustes(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsActivity = new Intent(getApplicationContext(), Settings.class);
                startActivity(settingsActivity);
            }
        };
    }

    private void crearListaFunciones() {

        funcionalidades = new ArrayList<>();

        funcionalidades.add(new Funcionalidad(R.drawable.ic_man_walking_directions_button, "Realizar Caminatas",
                "Registra tus caminatas"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_restaurant_menu_black, "Alimentación",
                "Consultar comidas a evitar y regstrar líquidos"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_power_connection_indicator, "Recordar Cita Médica",
                "Recordar mis citas médicas 1 día antes de la cita"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_set_alarm, "Medicamentos",
                "Consultar medicamentos, dosis y ajustar alarmas de medicamentos"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_create_new_pencil_button, "Ingresar Datos",
                "Ingresar síntomas, mediciones, exámenes médicos y temperamento"));

        /*
        funcionalidades.add(new Funcionalidad(R.drawable.ic_content_content_paste, "Agenda de Tareas Diarias",
                "Anotar actividades y definir cuales son primordiales"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_social_poll, "Consultar Datos Ingresados",
                "Consultar Histórico de síntomas y mediciones"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_maps_map, "Mapa del Hospital San Ignacio",
                "Consultar mapa interno del hospital"));*/
    }

    private void activarActivity(int posicion){

        switch(posicion){

            case 0:
                iniciarActivity = new Intent(this, Pedometer.class);
                this.startActivity(iniciarActivity);
                break;

            case 1:
                iniciarActivity = new Intent(this, Liquidos.class);
                this.startActivity(iniciarActivity);
                break;
            case 2:
                iniciarActivity = new Intent(this, CitasMedicas.class);
                this.startActivity(iniciarActivity);
                break;

            case 3:
                iniciarActivity = new Intent(this, Medicamentos.class);
                this.startActivity(iniciarActivity);
                break;
            case 4:
                iniciarActivity = new Intent(this, MenuIngresarDatos.class);
                this.startActivity(iniciarActivity);
                break;
            case 5:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;
            case 6:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;

            case 7:
                iniciarActivity = new Intent(this, Login.class);
                this.startActivity(iniciarActivity);
                break;
        }

    }
}
