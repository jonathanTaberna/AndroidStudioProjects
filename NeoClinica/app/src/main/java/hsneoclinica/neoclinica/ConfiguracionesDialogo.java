package hsneoclinica.neoclinica;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import hsneoclinica.neoclinica.R;

public class ConfiguracionesDialogo {

    public interface FinalizoConfiguracionesDialogo{
        void ResultadoConfiguracionesDialogo(String filtrarPuntos, String orden, String orderBy);
    }
    private FinalizoConfiguracionesDialogo interfaz;
    public ConfiguracionesDialogo(Context contexto, FinalizoConfiguracionesDialogo actividad, int filtrarPor, int ordenarPor, int ordenar) {
        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        //dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.configuraciones_dialogo);

        final RadioButton rbNombre = (RadioButton) dialogo.findViewById(R.id.rbNombre);
        final RadioButton rbPuntos = (RadioButton) dialogo.findViewById(R.id.rbPuntos);
        final RadioButton rbAsc = (RadioButton) dialogo.findViewById(R.id.rbAsc);
        final RadioButton rbDesc = (RadioButton) dialogo.findViewById(R.id.rbDesc);
        final RadioButton rbTodosPuntos = (RadioButton) dialogo.findViewById(R.id.rbTodosPuntos);
        final RadioButton rbPuntosDisponibles = (RadioButton) dialogo.findViewById(R.id.rbPuntosDisponibles);

        Button btnDialogAceptar = (Button) dialogo.findViewById(R.id.btnDialogAceptar);
        Button btnDialogCancelar = (Button) dialogo.findViewById(R.id.btnDialogCancelar);

        if (filtrarPor == 1) {
            rbTodosPuntos.setChecked(true);
            rbPuntosDisponibles.setChecked(false);
        } else {
            rbTodosPuntos.setChecked(false);
            rbPuntosDisponibles.setChecked(true);
        }

        if (ordenarPor == 1) {
            rbNombre.setChecked(true);
            rbPuntos.setChecked(false);
        } else {
            rbNombre.setChecked(false);
            rbPuntos.setChecked(true);
        }

        if (ordenar == 1) {
            rbAsc.setChecked(true);
            rbDesc.setChecked(false);
        } else {
            rbAsc.setChecked(false);
            rbDesc.setChecked(true);
        }

        btnDialogAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filtrarPuntos = "";
                String orden = "";
                String orderBy = "";
                if(rbNombre.isChecked()){
                   orden = "nombre";
                }
                if (rbPuntos.isChecked()){
                    orden = "puntos";
                }
                if (rbAsc.isChecked()){
                    orderBy = "asc";
                }
                if (rbDesc.isChecked()){
                    orderBy = "desc";
                }
                if (rbTodosPuntos.isChecked()){
                    filtrarPuntos = "todos";
                }
                if (rbPuntosDisponibles.isChecked()){
                    filtrarPuntos = "disponibles";
                }


                interfaz.ResultadoConfiguracionesDialogo(filtrarPuntos, orden, orderBy);
                dialogo.dismiss();
            }
        });
        btnDialogCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
}
