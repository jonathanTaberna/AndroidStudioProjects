package kt.distribuidoraBJ.fragments_secundarios;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import kt.distribuidoraBJ.R;

public class ConfiguracionesDialogo {

    public interface FinalizoConfiguracionesDialogo{
        void ResultadoConfiguracionesDialogo(String orden, String orderBy);
    }
    private FinalizoConfiguracionesDialogo interfaz;
    public ConfiguracionesDialogo(Context contexto, FinalizoConfiguracionesDialogo actividad, int ordenarPor, int ordenar) {
        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.configuraciones_dialogo);

        final RadioButton rbConfiguracionesDialogoCodigo = (RadioButton) dialogo.findViewById(R.id.rbConfiguracionesDialogoCodigo);
        final RadioButton rbConfiguracionesDialogoNombre = (RadioButton) dialogo.findViewById(R.id.rbConfiguracionesDialogoNombre);
        final RadioButton rbConfiguracionesDialogoFecha = (RadioButton) dialogo.findViewById(R.id.rbConfiguracionesDialogoFecha);
        final RadioButton rbConfiguracionesDialogoAsc = (RadioButton) dialogo.findViewById(R.id.rbConfiguracionesDialogoAsc);
        final RadioButton rbConfiguracionesDialogoDesc = (RadioButton) dialogo.findViewById(R.id.rbConfiguracionesDialogoDesc);

        Button btnConfiguracionesDialogoAceptar = (Button) dialogo.findViewById(R.id.btnConfiguracionesDialogoAceptar);
        Button btnConfiguracionesDialogoCancelar = (Button) dialogo.findViewById(R.id.btnConfiguracionesDialogoCancelar);

        switch (ordenarPor){
            case 1:
                rbConfiguracionesDialogoCodigo.setChecked(true);
                rbConfiguracionesDialogoNombre.setChecked(false);
                rbConfiguracionesDialogoFecha.setChecked(false);
                break;
            case 2:
                rbConfiguracionesDialogoCodigo.setChecked(false);
                rbConfiguracionesDialogoNombre.setChecked(true);
                rbConfiguracionesDialogoFecha.setChecked(false);
                break;
            case 3:
                rbConfiguracionesDialogoCodigo.setChecked(false);
                rbConfiguracionesDialogoNombre.setChecked(false);
                rbConfiguracionesDialogoFecha.setChecked(true);
                break;
        }
        switch (ordenar){
            case 1:
                rbConfiguracionesDialogoAsc.setChecked(true);
                rbConfiguracionesDialogoDesc.setChecked(false);
                break;
            case 2:
                rbConfiguracionesDialogoAsc.setChecked(false);
                rbConfiguracionesDialogoDesc.setChecked(true);
                break;
        }

        btnConfiguracionesDialogoAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orden = "";
                String orderBy = "";
                if(rbConfiguracionesDialogoCodigo.isChecked()){
                    orden = "codigo";
                }
                if(rbConfiguracionesDialogoNombre.isChecked()){
                    orden = "nombre";
                }
                if (rbConfiguracionesDialogoFecha.isChecked()){
                    orden = "fecha";
                }
                if (rbConfiguracionesDialogoAsc.isChecked()){
                    orderBy = "asc";
                }
                if (rbConfiguracionesDialogoDesc.isChecked()){
                    orderBy = "desc";
                }


                interfaz.ResultadoConfiguracionesDialogo(orden, orderBy);
                dialogo.dismiss();
            }
        });
        btnConfiguracionesDialogoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
}
