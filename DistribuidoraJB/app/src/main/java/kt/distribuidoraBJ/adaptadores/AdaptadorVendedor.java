package kt.distribuidoraBJ.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kt.distribuidoraBJ.R;
import kt.distribuidoraBJ.elementos.Vendedor;

//import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorVendedor extends RecyclerView.Adapter<AdaptadorVendedor.VendedorViewHolder> {

    Context context;
    ArrayList<Vendedor> listaVendedores;

    public AdaptadorVendedor(ArrayList<Vendedor> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    @Override
    public VendedorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_vendedores, null, false);
        return new VendedorViewHolder(vista);
    }

    @Override
    public void onBindViewHolder( VendedorViewHolder holder, int position) {

        try {
            int codigo = listaVendedores.get(position).getCodigo();
            String nombre = listaVendedores.get(position).getNombre();

            if (codigo == 0) {
                holder.tvCodigo.setText("No hay Vendedores cargados");
                holder.tvNombre.setText("");
            } else {

                holder.tvCodigo.setText("Código: " + codigo);
                holder.tvNombre.setText(nombre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaVendedores.size();
    }

    public class VendedorViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCodigo, tvNombre;

        public VendedorViewHolder( View itemView) {
            super(itemView);

            tvCodigo = (TextView) itemView.findViewById(R.id.tvCodigo);
            tvNombre = (TextView)itemView.findViewById(R.id.tvNombre);

        }
    }
}
