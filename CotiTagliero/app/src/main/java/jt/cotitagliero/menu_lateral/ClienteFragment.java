package jt.cotitagliero.menu_lateral;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

import jt.cotitagliero.R;
import jt.cotitagliero.adaptador.AdaptadorClientes;
import jt.cotitagliero.constantes.constantes;
import jt.cotitagliero.provisorios.Cliente;
import jt.cotitagliero.provisorios.Clientes;
import jt.cotitagliero.provisorios.ClientesVector;

@SuppressLint("ValidFragment")
public class ClienteFragment extends Fragment {

    private String id;
    private String nombre;
    private String fecha_nac;
    private String peso_act;
    private String imcP;
    private String grasa;
    private String musculo;
    private String grasa_visceral;
    private String edad_metaP;
    private String fecha_ini;
    private String peso;
    private String imcS;
    private String masa_grasa;
    private String masa_muscular;
    private String masa_viseral;
    private String edad_metaS;

    private Context contexto;
    private Clientes clientes;
    private int flag_paso;

    //pantalla
    private RecyclerView recyclerView;
    public AdaptadorClientes adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvNombre;
    private TextView tvEdad;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View pbLoading;

    public ClientesVector clientesVector = new ClientesVector();

    public GetSeguimientoTask getSeguimientoTask = null;

    //fin variables main activity
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cliente, container, false);

        contexto = container.getContext();

        Bundle bundle = getArguments();

        this.id = bundle.getString("id");
        this.nombre = bundle.getString("nombre");
        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvEdad = (TextView) view.findViewById(R.id.tvEdad);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);

        getSeguimientoTask = new GetSeguimientoTask(id);
        getSeguimientoTask.execute((Void) null);

        tvNombre.setText(nombre.trim());
        tvEdad.setText(" Edad: ");

        showProgress(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                //swipeRefreshLayout.setRefreshing(t);

                clientesVector.ResetList();
                if (clientesVector.tamanyo() > 0 ) {
                    recyclerView.stopScroll();
                    adaptador.notifyDataSetChanged();
                }
                getSeguimientoTask = new GetSeguimientoTask(id);
                getSeguimientoTask.execute((Void) null);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private int calcularEdad(String fecha_nac){
        int edad = 0;

        DateFormat dateFormat = dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dob = null;
        try {
            dob = dateFormat.parse(fecha_nac);
            GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
            cal.setGregorianChange(new Date(Long.MIN_VALUE));
            cal.clear();
            cal.set(Calendar.YEAR, 0);
            cal.setTimeInMillis( cal.getTimeInMillis() + new Date().getTime() - dob.getTime());
            edad = cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return edad;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            pbLoading.setVisibility(show ? View.GONE : View.VISIBLE);
            pbLoading.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbLoading.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            pbLoading.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            pbLoading.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void inflarVista(JSONArray jsonArray) {
        Cliente cliente = null;
        int tamanyoArray = jsonArray.length();

        if (jsonArray == null) {
            return;
        }

        flag_paso = 0;
        double pesoAnterior = 0;
        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String fecha_nac = jsonObject.getString("fecha_nac");
                int edad = calcularEdad(fecha_nac);
                tvEdad.setText(" Edad: " + edad);

                double peso_act = jsonObject.optDouble("peso_act");
                String imcP = jsonObject.getString("imcP");
                String grasa = jsonObject.getString("grasa");
                String musculo = jsonObject.getString("musculo");
                String grasa_visceral = jsonObject.getString("grasa_visceral");
                String edad_metaP = jsonObject.getString("edad_metaP");
                String fecha_ini = jsonObject.getString("fecha_ini");

                double peso = jsonObject.getDouble("peso");
                String imcS = jsonObject.getString("imcS");
                String masa_grasa = jsonObject.getString("masa_grasa");
                String masa_muscular = jsonObject.getString("masa_muscular");
                String masa_viseral = jsonObject.getString("masa_viseral");
                String edad_metaS = jsonObject.getString("edad_metaS");
                String fecha = jsonObject.getString("fecha");
                int tipo = jsonObject.getInt("tipo");

                if (flag_paso == 0) {
                    flag_paso = 1;
                    cliente = new Cliente(fecha_ini, peso_act, pesoAnterior, imcP, grasa_visceral, grasa, musculo, edad_metaP, "Inicial");
                    clientesVector.anyade(cliente);
                    pesoAnterior = peso_act;
                }
                String tipoS = "";
                if (tipo == 0) {
                    tipoS = "Semanal";
                } else {
                    tipoS = "Mensual";
                }
                cliente = new Cliente(fecha, peso, pesoAnterior, imcS, masa_viseral, masa_grasa, masa_muscular, edad_metaS, tipoS);
                clientesVector.anyade(cliente);
                pesoAnterior = peso;
            } catch (Exception e) {
                Log.e("inflarVista", e.getMessage());
            }
        }

        if (tamanyoArray == 0) { //no hay turnos para mostrar
            cliente = new Cliente();
            clientesVector.anyade(cliente);
            Toast.makeText(contexto, "No hay Seguimiento a Mostrar", Toast.LENGTH_SHORT).show();
            ClientesVector clientesVectorAux = new ClientesVector(clientesVector.getArray(1, 1));
            actualizarVista(clientesVectorAux);
        } else {
            actualizarVista(clientesVector);
        }
    }

    public void actualizarVista(ClientesVector clientesVector){
        clientes = clientesVector;
        adaptador = new AdaptadorClientes(contexto, clientes);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(layoutManager);
        showProgress(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    public class GetSeguimientoTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;
        private String id;

        public GetSeguimientoTask(String id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.cotiPaciente );
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type: application/json", "charset=utf-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("metodo", "getSeguimientoById");
                obj.put("id", id);

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
                        if (inputLine.equals("null")) {
                            Toast.makeText(contexto, "JSON Null", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            jsonResp = new JSONObject(inputLine);
                        }
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
            getSeguimientoTask = null;
            showProgress(false);

            int salida = 0;
            int cantidad = 0;
            int flag = 0;
            JSONArray seguimientos = null;

            try {
                cantidad = jsonResp.getInt("cantidad");
                if (cantidad > 0) {
                    seguimientos = jsonResp.getJSONArray("items");
                    salida = 1;
                } else {
                    salida = 7;
                }
            } catch (Exception e){
                salida = 8;
            }

            switch (salida) {
                case 1:
                case 7:
                case 8:
                    if (success) {
                        if (flag == 9) {
                            Toast.makeText(contexto, nombre, Toast.LENGTH_SHORT).show();
                        } else {
                            inflarVista(seguimientos);
                        }

                    } else {
                        Toast.makeText(contexto,  getString(R.string.error_get_seguimientos),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(contexto,  getString(R.string.error_get_seguimientos),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            getSeguimientoTask = null;
        }
    }
}
