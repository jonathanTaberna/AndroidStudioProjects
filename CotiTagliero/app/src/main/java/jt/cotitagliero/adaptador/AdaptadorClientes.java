package jt.cotitagliero.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jt.cotitagliero.R;
import jt.cotitagliero.provisorios.Cliente;
import jt.cotitagliero.provisorios.Clientes;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolder> {
    protected Clientes clientes;       //Turnos a mostrar
    protected LayoutInflater inflador;   //Crea Layouts a partir del XML
    protected Context contexto;          //Lo necesitamos para el inflador
    protected View.OnClickListener onClickListener; //Listener para cada elemento

    public AdaptadorClientes(Context contexto, Clientes clientes) {
        this.contexto = contexto;
        this.clientes = clientes;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView espacio, espacio2;
        public TextView fecha;
        public TextView peso;
        public TextView imc;
        public TextView gVisceral;
        public TextView mGrasa;
        public TextView mMuscular;
        public TextView edadMeta;
        public TextView tipo;

        public RelativeLayout rlElemento;

        public ViewHolder(View itemView) {
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.elemento_lista_fecha);
            peso = (TextView) itemView.findViewById(R.id.elemento_lista_peso);
            imc = (TextView) itemView.findViewById(R.id.elemento_lista_imc);
            gVisceral = (TextView) itemView.findViewById(R.id.elemento_lista_gvisceral);
            mGrasa = (TextView) itemView.findViewById(R.id.elemento_lista_mgrasa);
            mMuscular = (TextView) itemView.findViewById(R.id.elemento_lista_mmuscular);
            edadMeta = (TextView) itemView.findViewById(R.id.elemento_lista_edadmeta);
            tipo = (TextView) itemView.findViewById(R.id.elemento_lista_tipo);
            espacio = (TextView) itemView.findViewById(R.id.elemento_lista_texto_de_espacio);
            espacio2 = (TextView) itemView.findViewById(R.id.elemento_lista_texto_de_espacio2);
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
        Cliente cliente = clientes.elemento(posicion);
        personalizaVista(holder, cliente);
    }

    // Personalizamos un ViewHolder a partir de un lugar
    public void personalizaVista(ViewHolder holder, Cliente cliente) {

        String fecha = cliente.getFecha().trim();
        if (!fecha.isEmpty()) {
            String[] split = fecha.split("-");
            String anio = split[0];
            String mes = split[1];
            String dia = split[2];
            fecha = " " + dia + "/" + mes + "/" + anio;
        }

        String peso = " Peso: " + cliente.getPeso();
        String imc = " IMC: " + cliente.getImc().trim();
        String gVisceral = "|| G. Visceral: " + cliente.getgVisceral().trim();
        String mGrasa = " M. Grasa: " + cliente.getmGrasa().trim();
        String mMuscular = "|| M. Muscular: " + cliente.getmMuscular().trim();
        String edadMeta = " Edad Metabólica: " + cliente.getEdadMeta().trim();
        String tipo = "|| Tipo: " + cliente.getTipo().trim();

        holder.fecha.setText(fecha);
        holder.peso.setText(peso);
        holder.imc.setText(imc);
        holder.gVisceral.setText(gVisceral);
        holder.mGrasa.setText(mGrasa);
        holder.mMuscular.setText(mMuscular);
        holder.edadMeta.setText(edadMeta);
        holder.tipo.setText(tipo);
        holder.espacio.setText("");
        holder.espacio2.setText("");

        double pesoInicial = cliente.getPesoInicial();
        double pesoActual =  cliente.getPeso();
        int color = 0;
        int colorLetra = 0;
        if (!cliente.getTipo().trim().equals("Inicial")) {
            if (pesoActual == pesoInicial) {
                color = ContextCompat.getColor(contexto, R.color.colorAmarillo);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorPrimaryDark);
            } else if (pesoActual > pesoInicial) {
                color = ContextCompat.getColor(contexto, R.color.colorRojo);
                colorLetra = Color.BLACK;
            } else if (pesoActual < pesoInicial) {
                color = ContextCompat.getColor(contexto, R.color.colorVerde);
                colorLetra = ContextCompat.getColor(contexto, R.color.colorPrimaryDark);
            }

            ViewGroup.LayoutParams params = holder.peso.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.peso.setTextColor(colorLetra);
            holder.peso.setBackgroundColor(color);
            holder.peso.setLayoutParams(params);
        }


        /*
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
        */

    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return clientes.tamanyo();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

