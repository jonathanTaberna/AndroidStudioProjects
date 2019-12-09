package hsneoclinica.neoclinica;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
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

import hsneoclinica.neoclinica.constantes.constantes;

public class HsActivity extends AppCompatActivity {


    private String empresa;
    private String nombreEmpresa;
    private String nombre;
    private String cookie;
    private JSONArray profesionales;
    private TableLayout tlProfesionalesHsActivity;
    private Context contexto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs);

        tlProfesionalesHsActivity = (TableLayout) findViewById(R.id.tlProfesionalesHsActivity);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        contexto = this;

        empresa = extras.getString("empresa");
        nombreEmpresa = extras.getString("nombreEmpresa");
        nombre = extras.getString("nombre");
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

    /*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            //Log.e("On Config Change","LANDSCAPE");
            Toast.makeText(contexto, "LANDSCAPE", Toast.LENGTH_SHORT).show();
        }
        else{
            //Log.e("On Config Change","PORTRAIT");
            Toast.makeText(contexto, "PORTRAIT", Toast.LENGTH_SHORT).show();
        }

    }
    */

    @SuppressLint("ResourceAsColor")
    private void llenarTabla(JSONArray profesionales) {
        int tamanyoArray = profesionales.length();

        if (profesionales == null) {
            return;
        }
        if (tamanyoArray == 0) { //no hay turnos para mostrar
            Toast.makeText(contexto, "No hay Profesionales", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tlProfesionalesHsActivity.getChildCount()>1) {
            int filas = tlProfesionalesHsActivity.getChildCount();
            tlProfesionalesHsActivity.removeViews(1, filas - 1);
        }

        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = profesionales.getJSONObject(i);

                String matricula = jsonObject.getString("get_matricula").trim() + " ";
                String nombre = jsonObject.getString("get_nombre").trim();
                String clave = jsonObject.getString("get_clave").trim() + " ";
                String keyEspecialidad = jsonObject.getString("get_key_especialidad").trim();
                String especialidad = jsonObject.getString("get_especialidad").trim();

                //TableRow fila = new TableRow(contexto);
                final TextView textView1 = new TextView(contexto);
                final TextView textView2 = new TextView(contexto);
                final TextView textView3 = new TextView(contexto);
                //TextView textView4 = new TextView(contexto);
                //TextView textView5 = new TextView(this);


                Typeface font = Typeface.createFromAsset(contexto.getAssets(), "fonts/HelveticaNeue Medium.ttf");
                textView1.setTypeface(font);
                textView2.setTypeface(font);
                textView3.setTypeface(font);

                textView1.setText(matricula);
                textView1.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                //textView1.setHeight(15);
                textView2.setText(clave);
                textView2.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                //textView1.setHeight(15);
                textView3.setText(nombre.trim() + " [" + especialidad.trim() + "]");
                textView3.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                //textView4.setText(especialidad);
                //textView4.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                //textView5.setText(fecha);
                //textView5.setTextSize(constantes.TAMANYO_TEXT_SIZE);

                View v = new View(contexto);
                v.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.WRAP_CONTENT));
                v.setBackgroundColor(R.color.colorPrimary);

                final TableRow tr = new TableRow(contexto);
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
                //tr.addView(textView4);
                tr.setMinimumHeight(60);
                //tr.addView(textView5);
                tr.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent();
                        intent.putExtra("RESULT_MATRICULA", textView1.getText());
                        intent.putExtra("RESULT_PROFESIONAL", textView2.getText());
                        intent.putExtra("RESULT_NOMBRE", textView3.getText());
                        intent.putExtra("RESULT_NOMBRE_EMPRESA", nombreEmpresa);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                tlProfesionalesHsActivity.addView(tr, trParams);

                // add separator row
                final TableRow trSep = new TableRow(contexto);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(0, 0, 0, 0);
                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(contexto);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams();
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(R.color.colorPrimaryDark);
                tvSep.setHeight(1);
                trSep.addView(tvSep);
                tlProfesionalesHsActivity.addView(trSep, trParamsSep);
            } catch (Exception e) {
                Log.e("hsActivity", e.getMessage());
            }
        }

    }
}
