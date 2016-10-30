package husi.recuperapp.sintomas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Funciones;
import husi.recuperapp.accesoDatos.Paciente;

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
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_sintoma, parent, false);

            holder.sintomaTitulo = (TextView) vistaFila.findViewById(R.id.sintoma_texto);
            holder.botonIngresar = (Button) vistaFila.findViewById(R.id.boton_ingresar_sintoma);
            holder.sintoma = sintomas.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            //asocio el ViewHolder al boton (si presiona el boton puedo obtener el ViewHolder)
            holder.botonIngresar.setTag(holder);

            llenarDatosHolder(vistaFila, holder, posicion);
        } else{
            holder = (AdapatadorListViewSintomas.ViewHolder) convertView.getTag();
        }

        //Obtener boton y manejar presionar el boton
        Button botonIngresarDato = (Button)vistaFila.findViewById(R.id.boton_ingresar_sintoma);

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
            Paciente.getInstance().insertarYpostSintoma(viewHolder.sintoma.getIDSintoma(),
                        Paciente.getInstance().getCedula(), Funciones.getFechaString());

            Toast.makeText(contexto, "Se ingresó el Síntoma", Toast.LENGTH_LONG).show();
            //fisiologicos.remove(viewHolder.fisiologico);
            //notifyDataSetChanged();

        }
    }

    private void llenarDatosHolder(View vistaFila, AdapatadorListViewSintomas.ViewHolder holder, int posicion) {

        holder.sintomaTitulo.setText(holder.sintoma.getSintomaTitulo());

        //Para que empiece la lista desde 1 y poder hacer los cálculos de posicion
        posicion++;

        //Según el número de la fila se decora el contenido

        if (posicion % 3 == 0) {//detecta la tercera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#fafafa"));

            //Cambia el color del titulo de la posicion
            holder.sintomaTitulo.setTextColor(Color.parseColor("#f44336"));
        } else if (posicion % 3 == 2) {//detecta la seguna posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#9e9e9e"));

            //Cambia el color del titulo de la posicion;
            holder.sintomaTitulo.setTextColor(Color.parseColor("#424242"));
        } else {//Por descarte es la primera posicion
            //Cambia el color de toda la posicion
            vistaFila.setBackgroundColor(Color.parseColor("#424242"));

            //Cambia el color del titulo de la posicion
            holder.sintomaTitulo.setTextColor(Color.parseColor("#f44336"));
        }

        Log.i("Título sintomas: ",holder.sintomaTitulo.getText().toString());

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
        TextView sintomaTitulo;
        Button botonIngresar;
        Sintoma sintoma;
    }
}
