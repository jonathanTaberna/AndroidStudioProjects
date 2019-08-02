package hsneoclinica.neoclinica.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.StringTokenizer;

import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.provisorios.Internado;
import hsneoclinica.neoclinica.provisorios.Internados;
import hsneoclinica.neoclinica.provisorios.Turno;
import hsneoclinica.neoclinica.provisorios.Turnos;

import static hsneoclinica.neoclinica.constantes.constantes.TAMANYO_ELEMENTO_OBS;
import static hsneoclinica.neoclinica.constantes.constantes.TAMANYO_ELEMENTO_SIN_OBS;

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
        public TextView nombre, profesional, mutual, motivo;

        public RelativeLayout rlElementoInternado;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.tvNombreInternado);
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
            nombreAux = internado.getPaciente().trim() + "(" + internado.getEdad().trim() + ")";
        } else {
            nombreAux = internado.getPaciente().trim();
        }
        holder.nombre.setText(nombreAux);
        holder.profesional.setText(internado.getProfesional().trim());
        holder.mutual.setText(internado.getMutual().trim());
        holder.motivo.setText(internado.getMotivo().trim());

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
