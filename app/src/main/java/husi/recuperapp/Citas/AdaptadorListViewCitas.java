package husi.recuperapp.citas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import husi.recuperapp.R;

/**
 * Created by jmss1 on 9/10/2016.
 */

public class AdaptadorListViewCitas extends BaseAdapter {
    private Context context;
    private List<Cita> citas;

    public AdaptadorListViewCitas(Context context, List<Cita> citas) {
        this.context = context;
        this.citas = citas;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;
        AdaptadorListViewCitas.ViewHolder holder;

        if (convertView == null) {
            holder = new AdaptadorListViewCitas.ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_cita, parent, false);

            holder.fecha = (TextView) vistaFila.findViewById(R.id.fecha_cita_texto);
            holder.hora = (TextView) vistaFila.findViewById(R.id.medico_hora_texto);
            holder.medico = (TextView) vistaFila.findViewById(R.id.medico_cita_texto);
            holder.cita = citas.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            llenarDatosHolder(holder);
        } else{
            holder = (AdaptadorListViewCitas.ViewHolder) convertView.getTag();
        }
        return vistaFila;
    }

    private void llenarDatosHolder(AdaptadorListViewCitas.ViewHolder holder) {
        holder.fecha.setText(holder.cita.getFecha());
        holder.hora.setText(holder.cita.getHora());
        holder.medico.setText(holder.cita.getMedico());
    }

    @Override
    public int getCount() {
        return (citas == null) ? 0 : citas.size();

        //return this.citas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.citas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
        //return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView fecha;
        TextView hora;
        TextView medico;
        Cita cita;
    }
}
