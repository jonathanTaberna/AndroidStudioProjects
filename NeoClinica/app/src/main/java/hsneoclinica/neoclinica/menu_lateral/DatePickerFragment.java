package hsneoclinica.neoclinica.menu_lateral;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import hsneoclinica.neoclinica.R;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private static int anio;
    private static int mes;
    private static int dia;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener, int anioP, int mesP, int diaP) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);

        anio = anioP;
        mes = mesP;
        dia = diaP;

        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        /*
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
        */
        //return new DatePickerDialog(getActivity(), listener, anio, mes, dia);
        return new DatePickerDialog(getActivity(),R.style.TimePickerTheme, listener, anio, mes, dia);
    }

}