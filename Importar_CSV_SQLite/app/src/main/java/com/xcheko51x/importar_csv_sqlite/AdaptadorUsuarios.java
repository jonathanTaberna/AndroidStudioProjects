package com.xcheko51x.importar_csv_sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.UsuariosViewHolder> {

    Context context;
    List<Usuario> listaUsuarios;

    public AdaptadorUsuarios(Context context, List<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_usuarios, null, false);
        return new AdaptadorUsuarios.UsuariosViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {

        holder.tvNombre.setText(listaUsuarios.get(position).getNombre());
        holder.tvTelefono.setText(listaUsuarios.get(position).getTelefono());

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class UsuariosViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvTelefono;

        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);

        }
    }
}
