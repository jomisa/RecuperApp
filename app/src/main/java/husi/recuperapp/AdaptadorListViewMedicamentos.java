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
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

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
            holder.botonAsignar = (Button) vistaFila.findViewById(R.id.boton_asignar_medicamento);
            holder.medicamento = medicamentos.get(posicion);

            //asocio el holder a la vista
            vistaFila.setTag(holder);

            //asocio el ViewHolder al boton (si presiona el boton puedo ontener el ViewHolder)
            holder.botonAsignar.setTag(holder);

            llenarDatosHolder(vistaFila, holder, posicion);
        } else{
            holder = (AdaptadorListViewMedicamentos.ViewHolder) convertView.getTag();
        }

        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Obtener boton y manejar presionar el boton
        Button botonAsignar = (Button)vistaFila.findViewById(R.id.boton_asignar_medicamento);

        botonAsignar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //El boton tiene guardado el viewholder en su Tag
                AdaptadorListViewMedicamentos.ViewHolder viewHolder = (AdaptadorListViewMedicamentos.ViewHolder) v.getTag();
                if(viewHolder==null)
                    Log.i("Tag: ", "Es null");
                else {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                            calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                            Intent myIntent = new Intent(context, AlarmaMedicamentoReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

                            Long t=calendar.getTimeInMillis();
                            Log.i("Hora: ", ""+t);

                            Toast.makeText(context, "Seleccionó: " + selectedHour + ":" + selectedMinute,Toast.LENGTH_LONG).show();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Seleccione la hora");
                    mTimePicker.show();

                }
            }
        });

        return vistaFila;
    }

    private void llenarDatosHolder(View vistaFila, AdaptadorListViewMedicamentos.ViewHolder holder, int posicion) {

        holder.hora.setText(holder.medicamento.getHora());
        holder.nombre.setText(holder.medicamento.getNombre());
        holder.dosis.setText(holder.medicamento.getDosis());

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
        Button botonAsignar;
        Medicamento medicamento;
    }
}
