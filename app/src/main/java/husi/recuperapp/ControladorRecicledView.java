package husi.recuperapp;

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

        public ItemMenuPrincipal(View itemView) {
            super(itemView);
            funcionalidad = (TextView) itemView.findViewById(R.id.funcionalidad_texto);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion_texto);
            iconoFuncionalidad = (ImageView) itemView.findViewById(R.id.icono_funcionalidad);
        }
    }

    ControladorRecicledView(List<Funcionalidad> funcionalidades){
        this.funcionalidades = funcionalidades;
    }

    @Override
    public ItemMenuPrincipal onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_principal, viewGroup, false);

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
