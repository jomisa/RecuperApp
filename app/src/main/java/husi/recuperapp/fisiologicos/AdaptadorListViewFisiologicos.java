package husi.recuperapp.fisiologicos;

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

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Funciones;
import husi.recuperapp.accesoDatos.Paciente;

/**
 * Created by jmss1 on 25/09/2016.
 */

public class AdaptadorListViewFisiologicos extends BaseAdapter{
    private Context contexto;
    private List<Fisiologico> fisiologicos;

    public AdaptadorListViewFisiologicos(Context contexto, List<Fisiologico> fisiologicos) {
        this.contexto = contexto;
        this.fisiologicos = fisiologicos;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                presionoBotonIngresarDato(v);
            }
        });

        return vistaFila;
    }

    private void presionoBotonIngresarDato(View v){
        //El boton tiene guardado el viewholder en su Tag
        ViewHolder viewHolder = (ViewHolder) v.getTag();
        if(viewHolder==null)
            Log.i("Tag: ", "Es null");
        else {
            if("".equals(viewHolder.dato.getText().toString())) {
                Log.i("Tag: ","Está vacío");
                Toast.makeText(contexto, "Ingrese un dato", Toast.LENGTH_LONG).show();
            }
            else{
                String medicion = viewHolder.medicion.getText().toString();

                //Se quitan las tíldes para evitar problemas en las tablas
                if (medicion.equals("Líquidos")) {
                    medicion = "Liquidos";
                }
                if (medicion.equals("Número de glóbulos rojos")) {
                    medicion = "Numero globulos rojos";
                }
                if (medicion.equals("Número de reticulocitos")) {
                    medicion = "Numero de reticulocitos";
                }
                if (medicion.equals("Número Plaquetas")) {
                    medicion = "Numero Plaquetas";
                }
                if (medicion.equals("Número Hemoglobina")) {
                    medicion = "Numero Hemoglobina";
                }
                if (medicion.equals("Número Hematocrito")) {
                    medicion = "Numero Hematocrito";
                }

                double valor = Double.parseDouble(viewHolder.dato.getText().toString());

                Log.i("Tag: ", medicion);//obtener el editText del ViewHolder
                Log.i("Tag: ", valor + "");//obtener el editText del ViewHolder
                Log.i("Tag: ", Funciones.getFechaString());//obtener el editText del ViewHolder

                Paciente.getInstance().insertarYpostFisiologicos(Funciones.getFechaString(), medicion, valor);

                Toast.makeText(contexto, "Se ingresó el Dato", Toast.LENGTH_LONG).show();
                viewHolder.dato.setText("");
                viewHolder.dato.clearFocus();

                //fisiologicos.remove(viewHolder.fisiologico);
                //notifyDataSetChanged();
            }

        }
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
            holder.dato.setHintTextColor(Color.parseColor("#9e9e9e"));

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
            holder.dato.setHintTextColor(Color.parseColor("#f44336"));

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
            holder.dato.setHintTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para tener transparencia de fondo y de color blanco)
            holder.imagenFisiologico.setColorFilter(Color.rgb(255, 255, 255), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

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

