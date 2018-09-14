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
import hsfarmacia.farmaclub.provisorios.Producto;
import hsfarmacia.farmaclub.provisorios.Productos;

import static hsfarmacia.farmaclub.constantes.constantes.TAMANYO_ELEMENTO_CANJE;
import static hsfarmacia.farmaclub.constantes.constantes.TAMANYO_ELEMENTO_PROMOCIONES;

public class AdaptadorProductos  extends RecyclerView.Adapter<AdaptadorProductos.ViewHolder> {
    protected Productos productos;       //Productos a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener; //Listener para cada elemento
    protected int height; //tamaño de cada vista

    public AdaptadorProductos(Context contexto, Productos productos, int height) {
        this.contexto = contexto;
        this.productos = productos;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.height = height;
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView nombre, descripcion;
        public TextView puntos, descripcion;
        public ImageView foto;

        public RelativeLayout rlElemento;

        public ViewHolder(View itemView) {
            super(itemView);
            //nombre = (TextView) itemView.findViewById(R.id.elemento_lista_nombre);
            puntos = (TextView) itemView.findViewById(R.id.elemento_lista_puntos);
            descripcion = (TextView) itemView.findViewById(R.id.elemento_lista_descripcion);
            foto = (ImageView) itemView.findViewById(R.id.elemento_lista_foto);
            rlElemento = (RelativeLayout) itemView.findViewById(R.id.rlElemento);
        }
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_lista, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Producto producto = productos.elemento(posicion);
        personalizaVista(holder, producto);
    }

    // Personalizamos un ViewHolder a partir de un lugar
    public void personalizaVista(ViewHolder holder, Producto producto) {
        //holder.nombre.setText(producto.getCodigo());
        if (height == TAMANYO_ELEMENTO_CANJE) {
            holder.puntos.setText("Puntos: " + producto.getPuntos());
        }
        if (height == TAMANYO_ELEMENTO_PROMOCIONES) {
            holder.puntos.setText(producto.getComentario());
        }
        holder.descripcion.setText(producto.getNombre());
        holder.foto.setImageBitmap(producto.getFoto1());

        ViewGroup.LayoutParams layoutParams;
        layoutParams = holder.rlElemento.getLayoutParams();
        layoutParams.height = height;
        holder.rlElemento.setLayoutParams(layoutParams);
        layoutParams = holder.foto.getLayoutParams();
        layoutParams.height = height;
        holder.foto.setLayoutParams(layoutParams);
        /*
        int id = R.drawable.otros;
        switch(lugar.getTipo()) {
            case RESTAURANTE:id = R.drawable.restaurante; break;
            case BAR:        id = R.drawable.bar;         break;
            case COPAS:      id = R.drawable.copas;       break;
            case ESPECTACULO:id = R.drawable.espectaculos; break;
            case HOTEL:      id = R.drawable.hotel;       break;
            case COMPRAS:    id = R.drawable.compras;     break;
            case EDUCACION:  id = R.drawable.educacion;   break;
            case DEPORTE:    id = R.drawable.deporte;     break;
            case NATURALEZA: id = R.drawable.naturaleza;  break;
            case GASOLINERA: id = R.drawable.gasolinera;  break;
        }
        holder.foto.setImageResource(id);
        holder.foto.setScaleType(ImageView.ScaleType.FIT_END);
        */
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return productos.tamanyo();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
