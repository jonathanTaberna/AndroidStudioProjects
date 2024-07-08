package kt.distribuidoraBJ.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kt.distribuidoraBJ.Interfaces.Clientes;
import kt.distribuidoraBJ.R;
import kt.distribuidoraBJ.elementos.Cliente;


public class AdaptadorListaClientes extends RecyclerView.Adapter<AdaptadorListaClientes.ViewHolder> {
    protected Clientes clientes;       //Turnos a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener; //Listener para cada elemento

    public AdaptadorListaClientes(Context contexto, Clientes clientes) {
        this.contexto = contexto;
        this.clientes = clientes;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView nombre, descripcion;
        public TextView nombre;

        public RelativeLayout rlElemento;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.elemento_lista_cliente_codigo_nombre);
            rlElemento = (RelativeLayout) itemView.findViewById(R.id.rlElemento);


            /*
            Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/HelveticaNeueBd.ttf");
            nombre.setTypeface(font);
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/HelveticaNeueMed.ttf");

            */
        }
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_lista_cliente, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Cliente cliente = clientes.elemento(posicion);
        personalizaVista(holder, cliente);
    }

    // Personalizamos un ViewHolder a partir de un lugar
    public void personalizaVista(ViewHolder holder, Cliente cliente) {

        String nombreAux = cliente.getNombre();
        long codigoAux = cliente.getCodigo();
        String elemento = codigoAux + " - " + nombreAux;
        holder.nombre.setText(elemento);
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return clientes.tamanyo();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
