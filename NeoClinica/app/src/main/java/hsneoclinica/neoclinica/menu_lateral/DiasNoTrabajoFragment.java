package hsneoclinica.neoclinica.menu_lateral;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import hsneoclinica.neoclinica.PopUpDiasNoTrabajo;
import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.constantes.constantes;

public class DiasNoTrabajoFragment extends Fragment {

    //variables main activity

    private String matricula;
    private String password;
    private String profesional;
    private String nombre;
    private String fecha;
    private String cookie;
    private Context contexto;

    private GetDiasNoTrabajoTask getDiasNoTrabajoTask;

    //pantalla
    private TextView tvNombreDiasNoTrabajo;
    private TableLayout tlDiasNoTrabajo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = container.getContext();
        return inflater.inflate(R.layout.fragment_dias_no_trabajo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        this.nombre = bundle.getString("nombre");
        this.profesional = bundle.getString("profesional");
        this.password = bundle.getString("password");
        this.cookie = bundle.getString("cookie");

        tvNombreDiasNoTrabajo = (TextView) view.findViewById(R.id.tvNombreDiasNoTrabajo);
        tlDiasNoTrabajo = (TableLayout) view.findViewById(R.id.tlDiasNoTrabajo);
        tlDiasNoTrabajo.setStretchAllColumns(true);

        tvNombreDiasNoTrabajo.setText(nombre.trim());

        getDiasNoTrabajoTask = new GetDiasNoTrabajoTask();
        getDiasNoTrabajoTask.execute((Void) null);
    }

    private void llenarTabla(JSONArray jsonArray) {
        int tamanyoArray = jsonArray.length();

        if (jsonArray == null) {
            return;
        }
        if (tamanyoArray == 0) { //no hay turnos para mostrar
            Toast.makeText(contexto, "No hay dias No Laborables a Mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tlDiasNoTrabajo.getChildCount()>1) {
            int filas = tlDiasNoTrabajo.getChildCount();
            tlDiasNoTrabajo.removeViews(1, filas - 1);
        }

        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String desde = jsonObject.getString("desde").trim() + " | ";
                String hasta = jsonObject.getString("hasta").trim() + " | ";
                String motivo = jsonObject.getString("motivo").trim();
                final String cargo = jsonObject.getString("cargo").trim();
                final String fecha = jsonObject.getString("fecha");
                final String hora = jsonObject.getString("hora");

                //TableRow fila = new TableRow(contexto);
                TextView textView1 = new TextView(contexto);
                TextView textView2 = new TextView(contexto);
                TextView textView3 = new TextView(contexto);
                //TextView textView4 = new TextView(contexto);
                //TextView textView5 = new TextView(contexto);
                textView1.setText(desde);
                textView1.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                textView2.setText(hasta);
                textView2.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                textView3.setText(motivo);
                textView3.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                //textView4.setText(cargo);
                //textView4.setTextSize(constantes.TAMANYO_TEXT_SIZE);
                //textView5.setText(fecha);
                //textView5.setTextSize(constantes.TAMANYO_TEXT_SIZE);

                View v = new View(contexto);
                v.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.WRAP_CONTENT));
                v.setBackgroundColor(R.color.colorPrimary);

                final TableRow tr = new TableRow(contexto);
                tr.setId(i + 1);
                TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(0, 0, 0, 0);
                tr.setPadding(0,0,0,0);
                tr.setLayoutParams(trParams);
                tr.addView(textView1);
                //tr.addView(v);
                tr.addView(textView2);
                tr.addView(textView3);
                //tr.addView(textView4);
                //tr.addView(textView5);

                tr.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        tr.setBackgroundColor(R.color.colorPrimaryDark);
                        new PopUpDiasNoTrabajo(contexto, "Cargó: " + cargo,fecha + " (" + hora.trim() + ")", tr);
                        return true;
                    }
                });
                tlDiasNoTrabajo.addView(tr, trParams);

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
                tlDiasNoTrabajo.addView(trSep, trParamsSep);
            } catch (Exception e) {
                Log.e("inflarVista", e.getMessage());
            }
        }

    }

    public class GetDiasNoTrabajoTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;

        public GetDiasNoTrabajoTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + constantes.metodoGetNoJob);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type: application/json", "charset=utf-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Cookie",cookie);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("get_no_job_matricula", profesional);

                Log.i("JSON", obj.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(obj.toString());

                os.flush();
                os.close();

                status = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage().toString());

                if (status == 200){ //respuesta OK
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");
                        jsonResp = new JSONObject(inputLine);
                    }
                    JsonResponse = buffer.toString();
                    Log.i("RESPONSE",JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    status = 99;
                } else {
                    status = 98;
                }
            }catch (SocketTimeoutException e) {
                status = 99;
            } catch (Exception e){
                e.printStackTrace();
            }  finally {
                conn.disconnect();
            }

            if (status == 200) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getDiasNoTrabajoTask = null;

            JSONObject jsonObject = null;

            int salida = 0;
            int flag = 0;
            JSONObject lista = null;
            JSONArray nojob = null;

            try {
                jsonObject = jsonResp.getJSONObject("Response");
                flag = jsonObject.getInt("flag");
                lista = jsonObject.getJSONObject("lista");
                nojob = lista.getJSONArray("nojob");

            } catch (Exception e){
                salida = 8;
            }

            switch (salida) {
                case 0:
                case 8:
                    if (success) {
                        if (flag == 9) {
                            Toast.makeText(contexto,  getString(R.string.error_get_dias_no_trabajo),Toast.LENGTH_SHORT).show();
                        } else {
                            llenarTabla(nojob);
                        }
                    } else {
                        Toast.makeText(contexto,  getString(R.string.error_get_dias_no_trabajo),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(contexto,  getString(R.string.error_get_dias_no_trabajo),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            getDiasNoTrabajoTask = null;
        }
    }

}
