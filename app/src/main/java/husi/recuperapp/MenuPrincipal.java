package husi.recuperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private List<Funcionalidad> funcionalidades = new ArrayList<Funcionalidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        populateCarList();
        populateListView();
        registerClickCallback();
    }
    private void populateCarList() {
        funcionalidades.add(new Funcionalidad(R.drawable.ic_image_portrait, "Realizar Actividad Física",
                "Registrar caminata u oficios varios"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_action_lock_outline, "Plan de Alimentación",
                "Consultar comidas recomendadas"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_communication_call, "Agenda de Tareas Diarias",
                "Anotar actividades y definir cuales son primordiales"));
        funcionalidades.add(new Funcionalidad(R.drawable.ic_communication_email, "Programar Cita Médica",
                "Programar y Recordar mis citas médicas"));
    }

    private void populateListView() {
        ArrayAdapter<Funcionalidad> adapter = new AdaptadorListView();
        ListView list = (ListView) findViewById(R.id.menuPrincipal_listView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.menuPrincipal_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Funcionalidad clickedCar = funcionalidades.get(position);
                String message = "You clicked position " + position
                        + " Descripción: " + clickedCar.getDescripcion();
                Toast.makeText(MenuPrincipal.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class AdaptadorListView extends ArrayAdapter<Funcionalidad> {
        public AdaptadorListView() {
            super(MenuPrincipal.this, R.layout.item_menu_principal, funcionalidades);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_menu_principal, parent, false);
            }

            Funcionalidad funcion = funcionalidades.get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.icono_funcionalidad);
            imageView.setImageResource(funcion.getIconID());

            TextView makeText = (TextView) itemView.findViewById(R.id.funcionalidad_texto);
            makeText.setText(funcion.getFuncionalidad());

            TextView condionText = (TextView) itemView.findViewById(R.id.descripcion_texto);
            condionText.setText(funcion.getDescripcion());

            return itemView;
        }

    }
    private class Funcionalidad {
        private int iconID;
        private String funcionalidad;
        private String descripcion;

        public Funcionalidad(int iconID, String funcionalidad,  String descripcion) {
            super();
            this.iconID = iconID;
            this.funcionalidad = funcionalidad;
            this.descripcion = descripcion;
        }

        public String getFuncionalidad() {
            return this.funcionalidad;
        }
        public int getIconID() {
            return this.iconID;
        }
        public String getDescripcion() {
            return this.descripcion;
        }
    }
}
