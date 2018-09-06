package hsfarmacia.farmaclub;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class PopUpProductoDialogo {

    public PopUpProductoDialogo(Context contexto, Bitmap imagen, String descripcion, int puntos) {
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.popup_producto_dialogo);

        final ImageView ivPopUpProductoDialogoImagen= (ImageView) dialogo.findViewById(R.id.ivPopUpProductoDialogoImagen);
        final TextView tvPopUpProductoDialogoDescripcion = (TextView) dialogo.findViewById(R.id.tvPopUpProductoDialogoDescripcion);
        final TextView tvPopUpProductoDialogoPuntos = (TextView) dialogo.findViewById(R.id.tvPopUpProductoDialogoPuntos);

        ivPopUpProductoDialogoImagen.setImageBitmap(imagen);
        tvPopUpProductoDialogoDescripcion.setText(descripcion);
        tvPopUpProductoDialogoPuntos.setText("Puntos: "+puntos);
        dialogo.show();
    }
}
