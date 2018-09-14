package hsfarmacia.farmaclub.menu_lateral;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import hsfarmacia.farmaclub.MainActivity2;
import hsfarmacia.farmaclub.R;

public class MisDatosFragment extends Fragment {

    private EditText edtMisDatosNombre;
    private EditText edtMisDatosCorreo;
    private EditText edtMisDatosTelefono;
    private EditText edtMisDatosDireccion;
    private EditText edtMisDatosLocalidad;
    private EditText edtMisDatosCodpos;
    private EditText edtMisDatosFechaNacimiento;
    private LinearLayout llNombre;
    private LinearLayout llCorreo;
    private LinearLayout llBotones;
    private Button btnAceptar;
    private Button btnCancelar;

    private Boolean enableView;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private String localidad;
    private int codpos;
    private String fecnac;


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
        direccion = getArguments().getString("direccion");
        localidad = getArguments().getString("localidad");
        codpos = getArguments().getInt("codpos");
        fecnac = getArguments().getString("fecnac");

        edtMisDatosNombre = (EditText) view.findViewById(R.id.edtMisDatosNombre);
        edtMisDatosCorreo = (EditText) view.findViewById(R.id.edtMisDatosCorreo);
        edtMisDatosTelefono = (EditText) view.findViewById(R.id.edtMisDatosTelefono);
        edtMisDatosDireccion = (EditText) view.findViewById(R.id.edtMisDatosDireccion);
        edtMisDatosLocalidad = (EditText) view.findViewById(R.id.edtMisDatosLocalidad);
        edtMisDatosCodpos = (EditText) view.findViewById(R.id.edtMisDatosCodpos);
        edtMisDatosFechaNacimiento = (EditText) view.findViewById(R.id.edtMisDatosFechaNacimiento);
        llNombre = (LinearLayout) view.findViewById(R.id.llNombre);
        llCorreo = (LinearLayout) view.findViewById(R.id.llCorreo);
        llBotones = (LinearLayout) view.findViewById(R.id.llBotones);
        btnAceptar = (Button) view.findViewById(R.id.btnMisDatosAceptar);
        btnCancelar = (Button) view.findViewById(R.id.btnMisDatosCancelar);

        edtMisDatosNombre.setText(nombre);
        edtMisDatosCorreo.setText(correo);
        edtMisDatosTelefono.setText(telefono);
        edtMisDatosDireccion.setText(direccion);
        edtMisDatosLocalidad.setText(localidad);
        if (codpos != 0) {
            edtMisDatosCodpos.setText(String.valueOf(codpos));
        }
        edtMisDatosFechaNacimiento.setText(fecnac);

        edtMisDatosNombre.setEnabled(enableView);
        edtMisDatosNombre.setFocusable(enableView);
        edtMisDatosCorreo.setEnabled(enableView);
        edtMisDatosCorreo.setFocusable(enableView);
        edtMisDatosTelefono.setEnabled(enableView);
        edtMisDatosTelefono.setFocusable(enableView);
        edtMisDatosDireccion.setEnabled(enableView);
        edtMisDatosDireccion.setFocusable(enableView);
        edtMisDatosLocalidad.setEnabled(enableView);
        edtMisDatosLocalidad.setFocusable(enableView);
        edtMisDatosCodpos.setEnabled(enableView);
        edtMisDatosCodpos.setFocusable(enableView);
        edtMisDatosFechaNacimiento.setEnabled(enableView);
        edtMisDatosFechaNacimiento.setFocusable(enableView);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreEdt = edtMisDatosNombre.getText().toString();
                String correoEdt = edtMisDatosCorreo.getText().toString();
                String telefonoEdt = edtMisDatosTelefono.getText().toString();
                String direccionEdt = edtMisDatosDireccion.getText().toString();
                String localidadEdt = edtMisDatosLocalidad.getText().toString();
                int codposEdt = 0;
                String fecnacEdt = edtMisDatosFechaNacimiento.getText().toString();

                if (!edtMisDatosCodpos.getText().toString().isEmpty()){
                    codposEdt = Integer.parseInt(edtMisDatosCodpos.getText().toString());
                }

                if (nombre != nombreEdt ||
                        correo != correoEdt ||
                        telefono != telefonoEdt ||
                        direccion != direccionEdt ||
                        localidad != localidadEdt ||
                        codpos != codposEdt ||
                        fecnac != fecnacEdt) {

                    nombre = nombreEdt;
                    correo = correoEdt;
                    telefono = telefonoEdt;
                    direccion = direccionEdt;
                    localidad = localidadEdt;
                    codpos = codposEdt;
                    fecnac = fecnacEdt;
                    ((MainActivity2) getActivity()).ResultadoMisDatos("ACEPTAR", nombre, correo, telefono, direccion, localidad, codpos, fecnac);
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity2)getActivity()).ResultadoMisDatos("CANCELAR", nombre, correo, telefono, direccion, localidad, codpos, fecnac);
            }
        });

        edtMisDatosFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        if (enableView) {
            edtMisDatosNombre.requestFocus();
        }

        if (!enableView) {
            llBotones.setVisibility(View.INVISIBLE);
        }

    }


    public static MisDatosFragment newInstance(boolean enableView, String nombre, String correo, String telefono, String direccion, String localidad, int codpos, String fecnac) {
        MisDatosFragment misDatosFragment = new MisDatosFragment();
        Bundle args = new Bundle();
        args.putBoolean("vistaActiva", enableView);
        args.putString("nombre", nombre);
        args.putString("correo", correo);
        args.putString("telefono", telefono);
        args.putString("direccion", direccion);
        args.putString("localidad", localidad);
        args.putInt("codpos", codpos);
        args.putString("fecnac", fecnac);
        misDatosFragment.setArguments(args);
        return misDatosFragment;
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = String.valueOf(day) + "/" + String.valueOf((month+1)) + "/" + String.valueOf(year);
                edtMisDatosFechaNacimiento.setText(selectedDate);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


}
