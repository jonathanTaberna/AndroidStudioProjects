package hsneoclinica.neoclinica;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TableRow;
import android.widget.TextView;

public class PopUpDiasNoTrabajo {

    final TextView tvQuienPopUpDiasNoTrabajo;
    final TextView tvCuandoPopUpDiasNoTrabajo;
    private Context contexto;

    public PopUpDiasNoTrabajo(Context contexto, String quien, String cuando, final TableRow tr) {
        contexto = contexto;
        final Dialog dialogo = new Dialog(contexto){
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                // Tap anywhere to close dialog.
                tr.setBackgroundColor(0);
                this.dismiss();
                return true;
            }
        };
        dialogo.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    tr.setBackgroundColor(0);
                    dialogo.dismiss();
                }
                return true;
            }
        });
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.setCanceledOnTouchOutside(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.popup_dias_no_trabajo);

        tvQuienPopUpDiasNoTrabajo = (TextView) dialogo.findViewById(R.id.tvQuienPopUpDiasNoTrabajo);
        tvCuandoPopUpDiasNoTrabajo = (TextView) dialogo.findViewById(R.id.tvCuandoPopUpDiasNoTrabajo);

        tvQuienPopUpDiasNoTrabajo.setText(quien);
        tvCuandoPopUpDiasNoTrabajo.setText(cuando);
        dialogo.show();
    }

}
