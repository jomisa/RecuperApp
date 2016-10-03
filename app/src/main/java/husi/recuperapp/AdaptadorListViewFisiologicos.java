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

import java.util.List;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class AdaptadorListViewFisiologicos extends BaseAdapter {
    private Context context;
    private List<Fisiologico> fisiologicos;
    private View.OnClickListener clickListener;

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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_fisiologico, parent, false);

            holder.imagenFisiologico = (ImageView) vistaFila.findViewById(R.id.icono_fisiologico);
            holder.medicion = (TextView) vistaFila.findViewById(R.id.medicion_texto);
            holder.unidades = (TextView) vistaFila.findViewById(R.id.unidades_texto);
            holder.dato = (EditText) vistaFila.findViewById(R.id.dato_medicion);
            holder.botonIngresar = (Button) vistaFila.findViewById(R.id.boton_ingresar_fisiologico);
            holder.fisiologico = fisiologicos.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            //asocio el ViewHolder al boton (si presiona el boton puedo ontener el ViewHolder)
            holder.botonIngresar.setTag(holder);

            llenarDatosHolder(vistaFila, holder, posicion);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        //Obtener boton y manejar presionar el boton
        Button botonIngresarDato = (Button)vistaFila.findViewById(R.id.boton_ingresar_fisiologico);

        botonIngresarDato.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //El boton tiene guardado el viewholder en su Tag
                ViewHolder viewHolder = (ViewHolder) v.getTag();
                if(viewHolder==null)
                    Log.i("Tag: ", "Es null");
                else
                    Log.i("Tag: ", viewHolder.dato.getText().toString());//obtener el editText del ViewHolder

                //fisiologicos.remove(posicion);
                //notifyDataSetChanged();
            }
        });

        return vistaFila;
    }

    private void llenarDatosHolder(View vistaFila, ViewHolder holder, int posicion) {

        holder.medicion.setText(holder.fisiologico.getMedicion());
        holder.unidades.setText(holder.fisiologico.getUnidades());
        holder.imagenFisiologico.setImageResource(holder.fisiologico.getIconID());

        //Para que empiece la lista desde 1 y poder hacer los cálculos de posicion
        posicion++;

        //Según el número de la fila se decora el contenido

        if (posicion % 3 == 0) {//detecta la tercera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#fafafa"));

            //Cambia el color del titulo de la posicion
            holder.medicion.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de las unidades de la posicion
            holder.unidades.setTextColor(Color.parseColor("#000000"));

            //cambia el color del dato de la medida
            holder.dato.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagenFisiologico.setColorFilter(Color.rgb(0, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (posicion % 3 == 2) {//detecta la seguna posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#9e9e9e"));

            //Cambia el color del titulo de la posicion;
            holder.medicion.setTextColor(Color.parseColor("#424242"));

            //cambia el color de las unidades de la posicion
            holder.unidades.setTextColor(Color.parseColor("#fafafa"));

            //cambia el color del dato de la medida
            holder.dato.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagenFisiologico.setColorFilter(Color.rgb(160, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {//Por descarte es la primera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#424242"));

            //Cambia el color del titulo de la posicion
            holder.medicion.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la s unidades de la posicion
            holder.unidades.setTextColor(Color.parseColor("#fafafa"));

            //cambia el color del dato de la medida
            holder.dato.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagenFisiologico.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

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
        TextView medicion;
        TextView unidades;
        EditText dato;
        ImageView imagenFisiologico;
        Button botonIngresar;
        Fisiologico fisiologico;
    }
}

