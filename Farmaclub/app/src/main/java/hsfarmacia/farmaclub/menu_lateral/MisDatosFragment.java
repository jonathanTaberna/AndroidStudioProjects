package hsfarmacia.farmaclub.menu_lateral;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hsfarmacia.farmaclub.MainActivity2;
import hsfarmacia.farmaclub.R;

public class MisDatosFragment extends Fragment {

    private EditText edtMisDatosNombre;
    private EditText edtMisDatosCorreo;
    private EditText edtMisDatosTelefono;
    private LinearLayout llNombre;
    private LinearLayout llCorreo;
    private LinearLayout llBotones;
    private Button btnAceptar;
    private Button btnCancelar;

    private Boolean enableView;
    private String nombre;
    private String correo;
    private String telefono;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_datos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableView = getArguments().getBoolean("vistaActiva");
        nombre = getArguments().getString("nombre");
        correo = getArguments().getString("correo");
        telefono = getArguments().getString("telefono");

        edtMisDatosNombre = (EditText) view.findViewById(R.id.edtMisDatosNombre);
        edtMisDatosCorreo = (EditText) view.findViewById(R.id.edtMisDatosCorreo);
        edtMisDatosTelefono = (EditText) view.findViewById(R.id.edtMisDatosTelefono);
        llNombre = (LinearLayout) view.findViewById(R.id.llNombre);
        llCorreo = (LinearLayout) view.findViewById(R.id.llCorreo);
        llBotones = (LinearLayout) view.findViewById(R.id.llBotones);
        btnAceptar = (Button) view.findViewById(R.id.btnMisDatosAceptar);
        btnCancelar = (Button) view.findViewById(R.id.btnMisDatosCancelar);

        edtMisDatosNombre.setText(nombre);
        edtMisDatosCorreo.setText(correo);
        edtMisDatosTelefono.setText(telefono);

        edtMisDatosNombre.setEnabled(enableView);
        edtMisDatosNombre.setFocusable(enableView);
        edtMisDatosCorreo.setEnabled(enableView);
        edtMisDatosCorreo.setFocusable(enableView);
        edtMisDatosTelefono.setEnabled(enableView);
        edtMisDatosTelefono.setFocusable(enableView);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = edtMisDatosNombre.getText().toString();
                correo = edtMisDatosCorreo.getText().toString();
                telefono = edtMisDatosTelefono.getText().toString();
                ((MainActivity2)getActivity()).ResultadoMisDatos("ACEPTAR", nombre, correo, telefono);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity2)getActivity()).ResultadoMisDatos("CANCELAR", nombre, correo, telefono);
            }
        });
        if (enableView) {
            edtMisDatosNombre.requestFocus();
        }

        if (!enableView) {
            llBotones.setVisibility(View.INVISIBLE);
        }

    }


    public static MisDatosFragment newInstance(boolean enableView, String nombre, String correo, String telefono) {
        MisDatosFragment misDatosFragment = new MisDatosFragment();
        Bundle args = new Bundle();
        args.putBoolean("vistaActiva", enableView);
        args.putString("nombre", nombre);
        args.putString("correo", correo);
        args.putString("telefono", telefono);
        misDatosFragment.setArguments(args);
        return misDatosFragment;
    }


}
