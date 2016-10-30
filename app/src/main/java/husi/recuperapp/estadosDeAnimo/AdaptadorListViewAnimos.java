package husi.recuperapp.estadosDeAnimo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import husi.recuperapp.R;
import husi.recuperapp.accesoDatos.Funciones;
import husi.recuperapp.accesoDatos.Paciente;

/**
 * Created by jmss1 on 24/10/2016.
 */

public class AdaptadorListViewAnimos extends BaseAdapter {
    private Context contexto;
    private List<Animo> animos;

    public AdaptadorListViewAnimos(Context contexto, List<Animo> animos) {
        this.contexto = contexto;
        this.animos = animos;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_animo, parent, false);

            holder.animoTexto = (TextView) vistaFila.findViewById(R.id.animo_texto);
            holder.dato = (RatingBar) vistaFila.findViewById(R.id.ratingBar_animo);
            holder.botonIngresar = (Button) vistaFila.findViewById(R.id.boton_ingresar_animo);
            holder.animo = animos.get(posicion);

            holder.botonIngresar = (Button) vistaFila.findViewById(R.id.boton_ingresar_animo);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            //asocio el ViewHolder al boton (si presiona el boton puedo ontener el ViewHolder)
            holder.botonIngresar.setTag(holder);

            llenarDatosHolder(vistaFila, holder, posicion);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        //Obtener boton y manejar presionar el boton
        Button botonIngresarDato = (Button)vistaFila.findViewById(R.id.boton_ingresar_animo);

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
            String animoTexto = viewHolder.animoTexto.getText().toString();

            //Se quitan las tíldes para evitar problemas en las tablas
            if (animoTexto.equals("Líquidos")) {
                animoTexto = "Liquios";
            }

            float valor = viewHolder.dato.getRating();
            viewHolder.dato.setRating(0F);

            Log.i("Tag: ", animoTexto);//obtener el editText del ViewHolder
            Log.i("Tag: ", valor + "");//obtener el editText del ViewHolder
            Log.i("Tag: ", Funciones.getFechaString());//obtener el editText del ViewHolder

            Paciente.getInstance().insertarYpostAnimos(Funciones.getFechaString(), valor);

            Toast.makeText(contexto, "Se ingresó el Dato", Toast.LENGTH_LONG).show();

            //fisiologicos.remove(viewHolder.fisiologico);
            //notifyDataSetChanged();

        }
    }

    private void llenarDatosHolder(View vistaFila, ViewHolder holder, int posicion) {

        holder.animoTexto.setText(holder.animo.getAnimoTexto());

        //Para que empiece la lista desde 1 y poder hacer los cálculos de posicion
        posicion++;

        vistaFila.setBackgroundColor(Color.parseColor("#fafafa"));
        holder.animoTexto.setTextColor(Color.parseColor("#f44336"));

    }

    @Override
    public int getCount() {
        return this.animos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.animos.get(position);
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
        TextView animoTexto;
        RatingBar dato;
        Button botonIngresar;
        Animo animo;
    }
}
