package husi.recuperapp.menus;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import husi.recuperapp.R;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class AdaptadorListView extends BaseAdapter{
    private Context contexto;
    private List<Funcionalidad> funcionalidades;

    public AdaptadorListView(Context contexto, List<Funcionalidad> funcionalidades) {
        this.contexto = contexto;
        this.funcionalidades = funcionalidades;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_menu, parent, false);

            holder.imagen = (ImageView) vistaFila.findViewById(R.id.icono_funcionalidad);
            holder.funcion = (TextView) vistaFila.findViewById(R.id.funcionalidad_texto);
            holder.descripcion = (TextView) vistaFila.findViewById(R.id.descripcion_texto);
            holder.funcionalidad = funcionalidades.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            llenarDatosHolder(vistaFila, holder, posicion);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        return vistaFila;
    }

    private void llenarDatosHolder(View vistaFila, ViewHolder holder, int posicion) {

        holder.funcion.setText(holder.funcionalidad.getFuncionalidad());
        holder.descripcion.setText(holder.funcionalidad.getDescripcion());
        holder.imagen.setImageResource(holder.funcionalidad.getIconID());

        //Para que empiece la lista desde 1 y poder hacer los cálculos de posicion
        posicion++;

        //Según el número de la fila se decora el contenido

        if (posicion % 3 == 0) {//detecta la tercera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#fafafa"));

            //Cambia el color del titulo de la posicion
            holder.funcion.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            holder.descripcion.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagen.setColorFilter(Color.rgb(0, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (posicion % 3 == 2) {//detecta la seguna posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#9e9e9e"));

            //Cambia el color del titulo de la posicion;
            holder.funcion.setTextColor(Color.parseColor("#424242"));

            //cambia el color de la descripcion de la posicion
            holder.descripcion.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagen.setColorFilter(Color.rgb(160, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {//Por descarte es la primera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#424242"));

            //Cambia el color del titulo de la posicion
            holder.funcion.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            holder.descripcion.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagen.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
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
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView funcion;
        TextView descripcion;
        ImageView imagen;
        Funcionalidad funcionalidad;
    }
}
