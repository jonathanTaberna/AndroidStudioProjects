package hsfarmacia.farmaclub.adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hsfarmacia.farmaclub.R;
import hsfarmacia.farmaclub.provisorios.Novedad;
import hsfarmacia.farmaclub.provisorios.Novedades;

import static hsfarmacia.farmaclub.constantes.constantes.TAMANYO_ELEMENTO_CANJE;
import static hsfarmacia.farmaclub.constantes.constantes.TAMANYO_ELEMENTO_PROMOCIONES;

public class AdaptadorNovedades extends RecyclerView.Adapter<AdaptadorNovedades.ViewHolder> {
    protected Novedades novedades;       //novedades a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener; //Listener para cada elemento

    public AdaptadorNovedades(Context contexto, Novedades novedades) {
        this.contexto = contexto;
        this.novedades = novedades;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView nombre, descripcion;
        public TextView descripcion, fecha;

        public RelativeLayout rlElementoNovedades;

        public ViewHolder(View itemView) {
            super(itemView);
            descripcion = (TextView) itemView.findViewById(R.id.tvENDescripcion);
            fecha = (TextView) itemView.findViewById(R.id.tvENFecha);
        }
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_lista_novedades, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Novedad novedad = novedades.elemento(posicion);
        personalizaVista(holder, novedad);
    }

    // Personalizamos un ViewHolder a partir de un lugar
    public void personalizaVista(ViewHolder holder, Novedad novedad) {
        //holder.nombre.setText(producto.getCodigo());
        String desc = novedad.getNombre() + ": " + novedad.getDescripcion();
        //holder.descripcion.setText(novedad.getNombre() + ": " + novedad.getDescripcion());
        holder.descripcion.setText(desc);
        holder.fecha.setText(novedad.getFecha());
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return novedades.tamanyo();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
