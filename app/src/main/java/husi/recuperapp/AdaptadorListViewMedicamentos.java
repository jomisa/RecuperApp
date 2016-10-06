package husi.recuperapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by jmss1 on 4/10/2016.
 */

public class AdaptadorListViewMedicamentos extends BaseAdapter {
    private Context context;
    private List<Medicamento> medicamentos;

    public AdaptadorListViewMedicamentos(Context context, List<Medicamento> medicamentos) {
        this.context = context;
        this.medicamentos = medicamentos;
    }

    @Override
    public int getCount() {
        return this.medicamentos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.medicamentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        View vistaFila = convertView;
        AdaptadorListViewMedicamentos.ViewHolder holder;

        if (convertView == null) {
            holder = new AdaptadorListViewMedicamentos.ViewHolder();

            //Crear una nueva vista en la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vistaFila = inflater.inflate(R.layout.item_medicamento, parent, false);

            holder.hora = (TextView) vistaFila.findViewById(R.id.hora_medicamento_texto);
            holder.nombre = (TextView) vistaFila.findViewById(R.id.medicamento_medicamento_texto);
            holder.dosis = (TextView) vistaFila.findViewById(R.id.dosis_medicamento_texto);
            holder.frecuencia = (TextView) vistaFila.findViewById(R.id.frecuencia_medicamento_texto);
            holder.medicamento = medicamentos.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            llenarDatosHolder(holder);
        } else{
            holder = (AdaptadorListViewMedicamentos.ViewHolder) convertView.getTag();
        }
        return vistaFila;
    }

    private void llenarDatosHolder(AdaptadorListViewMedicamentos.ViewHolder holder) {
        holder.hora.setText(holder.medicamento.getHora());
        holder.nombre.setText(holder.medicamento.getNombre());
        holder.dosis.setText(holder.medicamento.getDosis());
        holder.frecuencia.setText(holder.medicamento.getFrecuencia());
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
        TextView hora;
        TextView nombre;
        TextView dosis;
        TextView frecuencia;
        Medicamento medicamento;
    }
}
