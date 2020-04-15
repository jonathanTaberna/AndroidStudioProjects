package kt.distribuidora.fragments_secundarios;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import kt.distribuidora.R;

public class OpcionesExportacionDialogo {

    private Context contexto;
    //private RadioButton rbExportacionDialogoBorrar;
    private RadioButton rbExportacionDialogoGuardar;
    private RadioButton rbExportacionDialogoExportar;
    Button btnDialogAceptar;
    Button btnDialogCancelar;

    public interface FinalizoOpcionesExportacionDialogo{
        void ResultadoOpcionesExportacionDialogo(int resultadoExportacion);
    }
    private FinalizoOpcionesExportacionDialogo interfaz;
    public OpcionesExportacionDialogo(Context contexto, FinalizoOpcionesExportacionDialogo actividad, int accion) {
        this.contexto = contexto;
        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.opciones_exportacion_dialogo);

        //rbExportacionDialogoBorrar = (RadioButton) dialogo.findViewById(R.id.rbExportacionDialogoBorrar);
        rbExportacionDialogoGuardar = (RadioButton) dialogo.findViewById(R.id.rbExportacionDialogoGuardar);
        rbExportacionDialogoExportar = (RadioButton) dialogo.findViewById(R.id.rbExportacionDialogoExportar);

        btnDialogAceptar = (Button) dialogo.findViewById(R.id.btnExportacionDialogoAceptar);
        btnDialogCancelar = (Button) dialogo.findViewById(R.id.btnExportacionDialogoCancelar);

        rbExportacionDialogoGuardar.setChecked(true);
        rbExportacionDialogoExportar.setChecked(false);
        /*
        switch(accion) {
            case 1:
                rbExportacionDialogoBorrar.setChecked(true);
                rbExportacionDialogoGuardar.setChecked(false);
                rbExportacionDialogoExportar.setChecked(false);
                break;
            case 2:
                rbExportacionDialogoBorrar.setChecked(false);
                rbExportacionDialogoGuardar.setChecked(true);
                rbExportacionDialogoExportar.setChecked(false);
                break;
            default:
                rbExportacionDialogoBorrar.setChecked(false);
                rbExportacionDialogoGuardar.setChecked(false);
                rbExportacionDialogoExportar.setChecked(true);
                break;
        }
        */
        btnDialogAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultado = 0;
                /*
                if (rbExportacionDialogoBorrar.isChecked()) {
                    resultado = 1;
                } else if (rbExportacionDialogoGuardar.isChecked()) {
                    resultado = 2;
                } else if (rbExportacionDialogoExportar.isChecked()) {
                    resultado = 3;
                }
                */
                if (rbExportacionDialogoGuardar.isChecked()) {
                    resultado = 1;
                } else if (rbExportacionDialogoExportar.isChecked()) {
                    resultado = 2;
                }

                interfaz.ResultadoOpcionesExportacionDialogo(resultado);
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
