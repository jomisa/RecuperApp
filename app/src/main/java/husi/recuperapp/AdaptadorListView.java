package husi.recuperapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class AdaptadorListView extends BaseAdapter{
    private Context context;
    private List<Funcionalidad> funcionalidades;

    public AdaptadorListView(Context context, List<Funcionalidad> funcionalidades) {
        this.context = context;
        this.funcionalidades = funcionalidades;
    }

    @Override
    public int getCount() {
    return this.funcionalidades.size();
    }

    @Override
    public Object getItem(int position) {
    return this.funcionalidades.get(position);
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
            vistaFila = inflater.inflate(R.layout.item_menu_principal, parent, false);
        }

        //Para que empiece la lista desde 1
        posicion++;

        //Poner datos en la vista
        ImageView imagen = (ImageView) vistaFila.findViewById(R.id.icono_funcionalidad);
        TextView funcion = (TextView) vistaFila.findViewById(R.id.funcionalidad_texto);
        TextView descripcion = (TextView) vistaFila.findViewById(R.id.descripcion_texto);

        Funcionalidad funcionalidad = this.funcionalidades.get(posicion-1);//La lista empieza en 0
        funcion.setText(funcionalidad.getFuncionalidad());
        descripcion.setText(funcionalidad.getDescripcion());
        imagen.setImageResource(funcionalidad.getIconID());

        if (posicion % 3 == 0) {//detecta la tercera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#fafafa"));

            //Cambia el color del titulo de la posicion
            TextView funcio = (TextView) vistaFila.findViewById(R.id.funcionalidad_texto);
            funcio.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            TextView descrip = (TextView) vistaFila.findViewById(R.id.descripcion_texto);
            descrip.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFuncionalidad = (ImageView) vistaFila.findViewById(R.id.icono_funcionalidad);
            imagenFuncionalidad.setColorFilter(Color.rgb(0, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (posicion % 3 == 2) {//detecta la seguna posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#9e9e9e"));

            //Cambia el color del titulo de la posicion
            TextView funcio = (TextView) vistaFila.findViewById(R.id.funcionalidad_texto);
            funcio.setTextColor(Color.parseColor("#424242"));

            //cambia el color de la descripcion de la posicion
            TextView descrip = (TextView) vistaFila.findViewById(R.id.descripcion_texto);
            descrip.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFuncionalidad = (ImageView) vistaFila.findViewById(R.id.icono_funcionalidad);
            imagenFuncionalidad.setColorFilter(Color.rgb(160, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {//Por descarte es la primera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#424242"));

            //Cambia el color del titulo de la posicion
            TextView funcio = (TextView) vistaFila.findViewById(R.id.funcionalidad_texto);
            funcio.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            TextView descrip = (TextView) vistaFila.findViewById(R.id.descripcion_texto);
            descrip.setTextColor(Color.parseColor("#9e9e9e"));

            //Se deja la imagen blanca

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFuncionalidad = (ImageView) vistaFila.findViewById(R.id.icono_funcionalidad);
            imagenFuncionalidad.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        return vistaFila;
    }
}
