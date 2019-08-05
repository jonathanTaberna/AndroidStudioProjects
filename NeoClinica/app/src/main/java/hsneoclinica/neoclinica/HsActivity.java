package hsneoclinica.neoclinica;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class HsActivity extends AppCompatActivity {


    private String empresa;
    private String nombreEmpresa;
    private String nombre;
    private String cookie;
    private JSONArray profesionales;
    private TableLayout tlTituloHsActivity;
    private TableLayout tlProfesionalesHsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);

        tlTituloHsActivity = (TableLayout) findViewById(R.id.tlTituloHsActivity);
        tlProfesionalesHsActivity = (TableLayout) findViewById(R.id.tlProfesionalesHsActivity);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        empresa = extras.getString("empresa");
        nombreEmpresa = extras.getString("nombreEmpresa");
        nombre = extras.getString("nombre");
        //profesionales = extras.getString("profesionales");
        cookie = extras.getString("cookie");
        String jsonArray = extras.getString("profesionales");

        try {
            profesionales = new JSONArray(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (nombreEmpresa.trim().isEmpty()) {
            this.setTitle("High Soft Login");
        } else {
            this.setTitle(nombreEmpresa);
        }
        
        llenarTabla(profesionales);
    }
    
    @SuppressLint("ResourceAsColor")
    private void llenarTabla(JSONArray profesionales) {
        int tamanyoArray = profesionales.length();

        if (profesionales == null) {
            return;
        }
        if (tamanyoArray == 0) { //no hay turnos para mostrar
            Toast.makeText(this, "No hay Profesionales", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tlProfesionalesHsActivity.getChildCount()>1) {
            int filas = tlProfesionalesHsActivity.getChildCount();
            tlProfesionalesHsActivity.removeViews(1, filas - 1);
        }

        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = profesionales.getJSONObject(i);

                String matricula = jsonObject.getString("get_matricula").trim();
                String nombre = jsonObject.getString("get_nombre").trim();
                String clave = jsonObject.getString("get_clave").trim();
                String keyEspecialidad = jsonObject.getString("get_key_especialidad").trim();
                String especialidad = jsonObject.getString("get_especialidad").trim();

                //TableRow fila = new TableRow(contexto);
                TextView textView1 = new TextView(this);
                TextView textView2 = new TextView(this);
                TextView textView3 = new TextView(this);
                TextView textView4 = new TextView(this);
                //TextView textView5 = new TextView(this);
                textView1.setText(matricula);
                //textView1.setTextSize(20);
                textView2.setText(clave);
                //textView2.setTextSize(20);
                textView3.setText(nombre);
                //textView3.setTextSize(20);
                textView4.setText(especialidad);
                //textView4.setTextSize(20);
                //textView5.setText(fecha);
                //textView5.setTextSize(14);

                View v = new View(this);
                //v.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT));
                v.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.WRAP_CONTENT));
                v.setBackgroundColor(R.color.colorPrimary);

                final TableRow tr = new TableRow(this);
                tr.setId(i + 1);
                final TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(0, 0, 0, 0);
                tr.setPadding(0,0,0,0);
                tr.setLayoutParams(trParams);
                tr.addView(textView1);
                //tr.addView(v);
                tr.addView(textView2);
                tr.addView(textView3);
                tr.addView(textView4);
                //tr.addView(textView5);
                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //tr.setBackgroundColor(android.R.color.holo_blue_dark);
                        tr.setBackgroundColor(R.color.colorPrimaryDark);
                    }
                });
                tlProfesionalesHsActivity.addView(tr, trParams);

                // add separator row
                final TableRow trSep = new TableRow(this);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(0, 0, 0, 0);
                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams();
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(R.color.colorPrimaryDark);
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                tlProfesionalesHsActivity.addView(trSep, trParamsSep);
                //tlTituloHsActivity.addView(trSep);


                //tlTituloHsActivity.addView(fila);
            } catch (Exception e) {
                Log.e("hsActivity", e.getMessage());
            }
        }

    }
}
