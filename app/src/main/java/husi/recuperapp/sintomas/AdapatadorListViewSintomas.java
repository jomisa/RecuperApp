package husi.recuperapp.sintomas;

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
import husi.recuperapp.utils.Funciones;
import husi.recuperapp.utils.Paciente;

/**
 * Created by jmss1 on 23/10/2016.
 */

public class AdapatadorListViewSintomas extends BaseAdapter {
    private Context contexto;
    private List<Sintoma> sintomas;

    public AdapatadorListViewSintomas(Context contexto, List<Sintoma> sintomas) {
        this.contexto = contexto;
        this.sintomas = sintomas;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;
        AdapatadorListViewSintomas.ViewHolder holder;

        if (convertView == null) {
            holder = new AdapatadorListViewSintomas.ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_fisiologico, parent, false);

            holder.imagenFisiologico = (ImageView) vistaFila.findViewById(R.id.icono_fisiologico);
            holder.medicion = (TextView) vistaFila.findViewById(R.id.medicion_texto);
            holder.unidades = (TextView) vistaFila.findViewById(R.id.unidades_texto);
            holder.dato = (EditText) vistaFila.findViewById(R.id.dato_medicion);
            holder.botonIngresar = (Button) vistaFila.findViewById(R.id.boton_ingresar_fisiologico);
            holder.fisiologico = sintomas.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            //asocio el ViewHolder al boton (si presiona el boton puedo ontener el ViewHolder)
            holder.botonIngresar.setTag(holder);

            llenarDatosHolder(vistaFila, holder, posicion);
        } else{
            holder = (AdapatadorListViewSintomas.ViewHolder) convertView.getTag();
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
        AdapatadorListViewSintomas.ViewHolder viewHolder = (AdapatadorListViewSintomas.ViewHolder) v.getTag();
        if(viewHolder==null)
            Log.i("Tag: ", "Es null");
        else {
            String medicion = viewHolder.medicion.getText().toString();
            String unidades = viewHolder.unidades.getText().toString();

            if(unidades.equals("")){
                Log.i("Tag: ", medicion);//obtener el editText del ViewHolder
                Log.i("Tag: ", Funciones.getFechaString());//obtener el editText del ViewHolder

                Paciente.getInstance().insertarYpostSintoma(Funciones.getNumerosString(medicion),
                        Paciente.getInstance().getCedula(), Funciones.getFechaString());

                Toast.makeText(contexto, "Se ingresó el Dato", Toast.LENGTH_LONG).show();
            }else {

                //Se quitan las tíldes para evitar problemas en las tablas
                if (medicion.equals("Líquidos")) {
                    medicion = "Liquios";
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

    private void llenarDatosHolder(View vistaFila, AdapatadorListViewSintomas.ViewHolder holder, int posicion) {

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

        Log.i("Unidades sintomas: ",holder.unidades.getText().toString());

        //Quiere decir que solo se debe indicar si presenta el síntoma
        if(holder.unidades.equals("")) {
            Log.i("Sintomas Holder ","Unidades vacias");
            holder.dato.setVisibility(View.GONE);
        }

    }

    @Override
    public int getCount() {
        return this.sintomas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.sintomas.get(position);
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
        Sintoma fisiologico;
    }
}
