package hsneoclinica.neoclinica.adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.provisorios.Internado;
import hsneoclinica.neoclinica.provisorios.Internados;

public class AdaptadorInternados extends RecyclerView.Adapter<AdaptadorInternados.ViewHolder> {
    protected Internados internados;       //Turnos a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener; //Listener para cada elemento

    public AdaptadorInternados(Context contexto, Internados internados) {
        this.contexto = contexto;
        this.internados = internados;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView nombre, descripcion;
        public TextView nombre, profesional, mutual, motivo, tvLugarInternado;
        public RelativeLayout rlElementoInternado;
        public ImageView ivIconoInternado;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLugarInternado = (TextView) itemView.findViewById(R.id.tvLugarInternado);
            ivIconoInternado = (ImageView) itemView.findViewById(R.id.ivIconoInternado);
            nombre = (TextView) itemView.findViewById(R.id.tvMotivoDiaNoTrabajo);
            profesional = (TextView) itemView.findViewById(R.id.tvProfesionalInternado);
            mutual = (TextView) itemView.findViewById(R.id.tvMutualInternado);
            motivo = (TextView) itemView.findViewById(R.id.tvMotivoInternado);
            rlElementoInternado = (RelativeLayout) itemView.findViewById(R.id.rlElementoInternado);
        }
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public AdaptadorInternados.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_lista_internado, parent, false);
        v.setOnClickListener(onClickListener);
        return new AdaptadorInternados.ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(AdaptadorInternados.ViewHolder holder, int posicion) {
        Internado internado = internados.elemento(posicion);
        personalizaVista(holder, internado);
    }

    // Personalizamos un ViewHolder a partir de un lugar
    public void personalizaVista(AdaptadorInternados.ViewHolder holder, Internado internado) {

        String nombreAux = "";
        if (!internado.getEdad().trim().isEmpty()) {
            nombreAux = internado.getPaciente().trim() + " (" + internado.getEdad().trim() + ")";
        } else {
            nombreAux = internado.getPaciente().trim();
        }
        holder.nombre.setText(nombreAux);
        holder.profesional.setText(internado.getProfesional().trim());
        holder.mutual.setText(internado.getMutual().trim());
        holder.motivo.setText(internado.getMotivo().trim());
        String lugarAux = internado.getLugar().trim();
        if (!lugarAux.isEmpty()) {
            if (lugarAux.length() > 20) {
                holder.tvLugarInternado.setText(lugarAux.substring(0, 20));
            } else {
                holder.tvLugarInternado.setText(lugarAux);
            }
        } else {
            holder.tvLugarInternado.setText("");
        }

        String iconoAux = internado.getIcono().trim();
        switch (iconoAux) {
            case "CAMA AZUL":
                holder.ivIconoInternado.setImageResource(R.drawable.blue_bed_icon);
                break;
            case "CAMA ROSA":
                holder.ivIconoInternado.setImageResource(R.drawable.pink_bed_icon);
                break;
            case "AMBULANCIA":
                holder.ivIconoInternado.setImageResource(R.drawable.ambulance_icon);
                break;
            case "ALTA":
                holder.ivIconoInternado.setImageResource(R.drawable.family_icon);
                break;
            case "CRUZ":
                holder.ivIconoInternado.setImageResource(R.drawable.cross_icon);
                break;
            default:
                break;
        }

        ViewGroup.LayoutParams layoutParams;
        layoutParams = holder.rlElementoInternado.getLayoutParams();
        /*
        if (internado.getLugar().trim().isEmpty()){
            layoutParams.height = TAMANYO_ELEMENTO_SIN_OBS;
        } else {
            layoutParams.height = TAMANYO_ELEMENTO_OBS;
        }
        */
        holder.rlElementoInternado.setLayoutParams(layoutParams);
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return internados.tamanyo();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}