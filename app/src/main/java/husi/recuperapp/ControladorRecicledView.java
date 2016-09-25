package husi.recuperapp;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jmss1 on 24/09/2016.
 */

public class ControladorRecicledView extends RecyclerView.Adapter<ControladorRecicledView.ItemMenuPrincipal>{

    List<Funcionalidad> funcionalidades;

    public static class ItemMenuPrincipal extends RecyclerView.ViewHolder {

        public TextView funcionalidad;
        public TextView descripcion;
        public ImageView iconoFuncionalidad;
        public ConstraintLayout itemConstraint;

        public ItemMenuPrincipal(View itemView) {
            super(itemView);
            funcionalidad = (TextView) itemView.findViewById(R.id.funcionalidad_texto);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion_texto);
            iconoFuncionalidad = (ImageView) itemView.findViewById(R.id.icono_funcionalidad);
            itemConstraint = (ConstraintLayout) itemView.findViewById(R.id.item_constraintLayout);
        }
    }

    ControladorRecicledView(List<Funcionalidad> funcionalidades){
        this.funcionalidades = funcionalidades;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ItemMenuPrincipal onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_principal, viewGroup, false);

        //Este código infla (instancia la vista) cada elemento de la lista
        //Se hicieron algunos alculos para detectar el numero de posicion
        //por motivos de diseno los colores cambian cada 3 posiciones

        //Para que empiece la lista desde 1
        viewType++;

        if (viewType%3==0) {//detecta la tercera posicion
            //Cambia el color de toda la posicion
            itemView.setBackgroundColor(Color.parseColor("#fafafa"));

            //Cambia el color del titulo de la posicion
            TextView funcio = (TextView) itemView.findViewById(R.id.funcionalidad_texto);
            funcio.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            TextView descrip = (TextView) itemView.findViewById(R.id.descripcion_texto);
            descrip.setTextColor(Color.parseColor("#9e9e9e"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFuncionalidad = (ImageView) itemView.findViewById(R.id.icono_funcionalidad);
            imagenFuncionalidad.setColorFilter(Color.rgb(0,0,0),android.graphics.PorterDuff.Mode.MULTIPLY );
        }
        else if (viewType%3==2) {//detecta la seguna posicion
            //Cambia el color de toda la posicion
            itemView.setBackgroundColor(Color.parseColor("#9e9e9e"));

            //Cambia el color del titulo de la posicion
            TextView funcio = (TextView) itemView.findViewById(R.id.funcionalidad_texto);
            funcio.setTextColor(Color.parseColor("#424242"));

            //cambia el color de la descripcion de la posicion
            TextView descrip = (TextView) itemView.findViewById(R.id.descripcion_texto);
            descrip.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la imagen (todas las imagenes deben ser PNG para ctenr transparencia de fondo y de color blanco)
            ImageView imagenFuncionalidad = (ImageView) itemView.findViewById(R.id.icono_funcionalidad);
            imagenFuncionalidad.setColorFilter(Color.rgb(160,0,0),android.graphics.PorterDuff.Mode.MULTIPLY );
        }
        else{//Por descarte es la primera posicion
            //Cambia el color de toda la posicion
            itemView.setBackgroundColor(Color.parseColor("#424242"));

            //Cambia el color del titulo de la posicion
            TextView funcio = (TextView) itemView.findViewById(R.id.funcionalidad_texto);
            funcio.setTextColor(Color.parseColor("#f44336"));

            //cambia el color de la descripcion de la posicion
            TextView descrip = (TextView) itemView.findViewById(R.id.descripcion_texto);
            descrip.setTextColor(Color.parseColor("#9e9e9e"));

            //Se deja la imagen blanca
        }

        return new ItemMenuPrincipal(itemView);
    }

    @Override
    public void onBindViewHolder(ItemMenuPrincipal itemMenuPrincipal, int posicion) {
        Funcionalidad funcion = funcionalidades.get(posicion);

        itemMenuPrincipal.funcionalidad.setText(funcion.getFuncionalidad());
        itemMenuPrincipal.descripcion.setText(funcion.getDescripcion());
        itemMenuPrincipal.iconoFuncionalidad.setImageResource(funcion.getIconID());
    }

    @Override
    public int getItemCount() {
        return funcionalidades.size();
    }
}
