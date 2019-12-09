package jt.cotitagliero.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.StringTokenizer;

import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.provisorios.Turno;
import hsneoclinica.neoclinica.provisorios.Turnos;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolder> {
    protected Turnos turnos;       //Turnos a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener; //Listener para cada elemento

    public AdaptadorClientes(Context contexto, Turnos turnos) {
        this.contexto = contexto;
        this.turnos = turnos;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView nombre, descripcion;
        public TextView nombre, mutual, obs, espacio, espacio2;

        public RelativeLayout rlElemento;

        public ViewHolder(View itemView) {
            super(itemView);
            //nombre = (TextView) itemView.findViewById(R.id.elemento_lista_nombre);
            nombre = (TextView) itemView.findViewById(R.id.elemento_lista_nombre_paciente);
            mutual = (TextView) itemView.findViewById(R.id.elemento_lista_mutual);
            obs = (TextView) itemView.findViewById(R.id.elemento_lista_obs);
            espacio = (TextView) itemView.findViewById(R.id.elemento_lista_texto_de_espacio);
            espacio2 = (TextView) itemView.findViewById(R.id.elemento_lista_texto_de_espacio2);
            rlElemento = (RelativeLayout) itemView.findViewById(R.id.rlElemento);


            Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/HelveticaNeueBd.ttf");
            nombre.setTypeface(font);
            font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/HelveticaNeueMed.ttf");
            espacio.setTypeface(font);
            espacio2.setTypeface(font);
            mutual.setTypeface(font);
            obs.setTypeface(font);

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
        Turno turno = turnos.elemento(posicion);
        personalizaVista(holder, turno);
    }

    // Personalizamos un ViewHolder a partir de un lugar
    public void personalizaVista(ViewHolder holder, Turno turno) {

        String nombreAux = "";
        if (turno.getPaciente().trim().isEmpty()) {
            nombreAux = turno.getHora().trim();
        } else {
            nombreAux = turno.getHora() + " | " + turno.getPaciente().trim();
        }
        holder.nombre.setText(nombreAux);
        holder.mutual.setText(turno.getMutual().trim());

        holder.espacio.setText("");
        holder.espacio2.setText("");

        String obsAux = turno.getObs();
        if (obsAux.contains("|")){
            StringTokenizer st = new StringTokenizer(obsAux, "|");
            String obs1 = st.nextToken();
            String obs2 = st.nextToken();
            String obs3 = st.nextToken();
            String obs4 = st.nextToken();
            obsAux = obs1.trim();
            if (!obs2.trim().isEmpty()) {
                obsAux += " , " + obs2.trim();
            }
            if (!obs3.trim().isEmpty()) {
                obsAux += " , " + obs3.trim();
            }
            if (!obs4.trim().isEmpty()) {
                obsAux += " , " + obs4.trim();
            }
        }
        holder.obs.setText(obsAux);
        int color = 0;
        int colorLetra = 0;
        switch (turno.getColor()){
            case "VERDE":
                //color = Color.GREEN;
                color = ContextCompat.getColor(contexto, R.color.colorVerde);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorLetraVerde);
                break;
            case "ROJO":
                //color = Color.RED;
                color = ContextCompat.getColor(contexto, R.color.colorRojo);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorLetraRojo);
                break;
            case "AMARILLO":
                //color = Color.YELLOW;
                color = ContextCompat.getColor(contexto, R.color.colorAmarillo);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorLetraAmarillo);
                break;
            case "GRIS":
                //color = Color.LTGRAY;
                color = ContextCompat.getColor(contexto, R.color.colorGris);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorLetraGris);
                break;
            case "AZUL":
                //color = Color.BLUE;
                color = ContextCompat.getColor(contexto, R.color.colorAzul);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorLetraAzul);
                break;
            case "CELESTE":
                //color = Color.CYAN;
                color = ContextCompat.getColor(contexto, R.color.colorCeleste);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorLetraCeleste);
                break;
            case "":
                color = 0;
                colorLetra = Color.BLACK;
                break;

        }

        //holder.rlElemento.setBackgroundColor(color);
        holder.nombre.setTextColor(colorLetra);
        holder.nombre.setBackgroundColor(color);
        holder.mutual.setTextColor(colorLetra);
        holder.mutual.setBackgroundColor(color);
        holder.espacio.setTextColor(colorLetra);
        holder.espacio.setBackgroundColor(color);
        holder.espacio2.setTextColor(colorLetra);
        holder.espacio2.setBackgroundColor(color);
        //holder.obs.setTextColor(colorLetra);
        ViewGroup.LayoutParams params = holder.obs.getLayoutParams();
        if (obsAux.trim().isEmpty()){
            params.height = 0;
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.obs.setTextColor(Color.BLACK);
            holder.obs.setBackgroundColor(Color.YELLOW);
        }
        holder.obs.setLayoutParams(params);

        /*
        ViewGroup.LayoutParams layoutParams;
        layoutParams = holder.rlElemento.getLayoutParams();
        if (obsAux.trim().isEmpty()){
            layoutParams.height = TAMANYO_ELEMENTO_SIN_OBS;
        } else {
            layoutParams.height = TAMANYO_ELEMENTO_OBS;
            holder.obs.setTextColor(R.color.colorLetraAmarillo);
            holder.obs.setBackgroundColor(R.color.colorAmarillo);
        }
        holder.rlElemento.setLayoutParams(layoutParams);
        */
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return turnos.tamanyo();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
