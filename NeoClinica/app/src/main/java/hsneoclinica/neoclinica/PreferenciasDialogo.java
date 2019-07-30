package hsneoclinica.neoclinica;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.provisorios.Check;

public class PreferenciasDialogo {

    public interface FinalizoPreferenciasDialogo{
        void ResultadoPreferenciasDialogo(ArrayList<Check> elementos);
    }
    private FinalizoPreferenciasDialogo interfaz;
    public PreferenciasDialogo(final Context contexto, FinalizoPreferenciasDialogo actividad, final ArrayList<Check> elementos) {
        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        //dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.preferencias_dialogo);

        LinearLayout llvPreferencias = (LinearLayout) dialogo.findViewById(R.id.llvPreferencias);

        for (final Check c:elementos){
            final CheckBox cb = new CheckBox(contexto);
            cb.setText(c.nombre);
            cb.setId(c.id);
            cb.setTextColor(Color.BLACK);
            cb.setChecked(c.marcado);
            //cb.setHeight(40);
            llvPreferencias.addView(cb);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //elementosAux.get(elementosAux.indexOf(c)).marcado = isChecked;
                    //Toast.makeText(contexto, "check" + buttonView.getId(), Toast.LENGTH_SHORT).show();

                    cb.setChecked(isChecked);
                }
            });

        };


        Button btnDialogAceptar = (Button) dialogo.findViewById(R.id.btnDialogAceptar);
        Button btnDialogCancelar = (Button) dialogo.findViewById(R.id.btnDialogCancelar);

        btnDialogAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Check c:elementos){
                    CheckBox cb = (CheckBox) dialogo.findViewById(c.id);
                    elementos.get(elementos.indexOf(c)).marcado = cb.isChecked();

                };
                interfaz.ResultadoPreferenciasDialogo(elementos);
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
