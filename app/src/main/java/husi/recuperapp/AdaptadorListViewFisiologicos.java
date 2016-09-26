package husi.recuperapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class AdaptadorListViewFisiologicos extends BaseAdapter {
    private Context context;
    private List<Fisiologico> fisiologicos;

    public AdaptadorListViewFisiologicos(Context context, List<Fisiologico> fisiologicos) {
        this.context = context;
        this.fisiologicos = fisiologicos;
    }

    @Override
    public int getCount() {
        return this.fisiologicos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.fisiologicos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;

        if (convertView == null) {
            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_fisiologico, parent, false);
        }

        //Para que empiece la lista desde 1
        posicion++;

        //Poner datos en la vista
        ImageView imagen = (ImageView) vistaFila.findViewById(R.id.icono_fisiologico);
        TextView medicion = (TextView) vistaFila.findViewById(R.id.medicion_texto);
        TextView unidad = (TextView) vistaFila.findViewById(R.id.unidades_texto);

        Fisiologico fisiologico = this.fisiologicos.get(posicion-1);//La lista empieza en 0
        medicion.setText(fisiologico.getMedicion());
        unidad.setText(fisiologico.getUnidades());
        imagen.setImageResource(fisiologico.getIconID());

        if (posicion % 3 == 0) {//detecta la tercera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#fafafa"));

            //Cambia el color del titulo de la posicion
            TextView medic = (TextView) vistaFila.findViewById(R.id.medicion_texto);
            medic.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            TextView unid = (TextView) vistaFila.findViewById(R.id.unidades_texto);
            unid.setTextColor(Color.parseColor("#424242"));

            //cambia el color del dato de la medida
            EditText dato = (EditText) vistaFila.findViewById(R.id.dato_medicion);
            dato.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFisiologico = (ImageView) vistaFila.findViewById(R.id.icono_fisiologico);
            imagenFisiologico.setColorFilter(Color.rgb(0, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (posicion % 3 == 2) {//detecta la seguna posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#9e9e9e"));

            //Cambia el color del titulo de la posicion
            TextView medic = (TextView) vistaFila.findViewById(R.id.medicion_texto);
            medic.setTextColor(Color.parseColor("#424242"));

            //cambia el color de la descripcion de la posicion
            TextView unid = (TextView) vistaFila.findViewById(R.id.unidades_texto);
            unid.setTextColor(Color.parseColor("#fafafa"));

            //cambia el color del dato de la medida
            EditText dato = (EditText) vistaFila.findViewById(R.id.dato_medicion);
            dato.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFisiologico = (ImageView) vistaFila.findViewById(R.id.icono_fisiologico);
            imagenFisiologico.setColorFilter(Color.rgb(160, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {//Por descarte es la primera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#424242"));

            //Cambia el color del titulo de la posicion
            TextView medic = (TextView) vistaFila.findViewById(R.id.medicion_texto);
            medic.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            TextView unid = (TextView) vistaFila.findViewById(R.id.unidades_texto);
            unid.setTextColor(Color.parseColor("#fafafa"));

            //cambia el color del dato de la medida
            EditText dato = (EditText) vistaFila.findViewById(R.id.dato_medicion);
            dato.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFisiologico = (ImageView) vistaFila.findViewById(R.id.icono_fisiologico);
            imagenFisiologico.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        //Handle buttons and add onClickListeners
        Button botonIngresarDato = (Button)vistaFila.findViewById(R.id.boton_ingresar_fisiologico);
        //EditText dato = (EditText) vistaFila.findViewById(R.id.dato_medicion);

        botonIngresarDato.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Log.i("entro", "entro");

                //fisiologicos.remove(posicion);
                //notifyDataSetChanged();
            }
        });


        return vistaFila;
    }
}
