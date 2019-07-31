package hsneoclinica.neoclinica.menu_lateral;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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
import java.util.ArrayList;
import java.util.Calendar;

import hsneoclinica.neoclinica.PopUpProductoDialogo;
import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.adaptador.AdaptadorTurnos;
import hsneoclinica.neoclinica.constantes.constantes;
import hsneoclinica.neoclinica.provisorios.Check;
import hsneoclinica.neoclinica.provisorios.Turno;
import hsneoclinica.neoclinica.provisorios.Turnos;
import hsneoclinica.neoclinica.provisorios.TurnosVector;

@SuppressLint("ValidFragment")
public class TurnoFragment extends Fragment {

    //variables main activity

    private String matricula;
    private String password;
    private String profesional;
    private String nombre;
    private String fecha;
    private String cookie;
    //private ArrayList<Turno> elementos = new ArrayList<Turno>();

    private int flagPaso = 0;
    private Context contexto;
    private Turnos turnos;


    private GetTurnosTask getTurnosTask;

    //pantalla
    private RecyclerView recyclerView;
    public AdaptadorTurnos adaptador;
    private RecyclerView.LayoutManager layoutManager;
    //private TextView tvTarjeta;
    private TextView tvNroMatricula;
    private TextView tvNombre;
    private TextView tvComentario;
    private Button btnPrev;
    private Button btnNext;
    private TextView tvFecha;
    private View pbLoading;

    public TurnosVector productosVector = new TurnosVector();

    private String fragmentVisible = "";

    private Calendar c;
    private int year;
    private int month;
    private int day;

    //fin variables main activity
    /*
    @SuppressLint("ValidFragment")
    public TurnoFragment(String tarjeta, int puntos, String nombre){s
        this.tarjeta = tarjeta;
        this.puntos = puntos;
        this.nombre = nombre;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        contexto = container.getContext(); //getActivity().getApplicationContext();

        Bundle bundle = getArguments();

        this.fragmentVisible = bundle.getString("fragmentVisible");
        this.nombre = bundle.getString("nombre");
        this.matricula = bundle.getString("matricula");
        this.profesional = bundle.getString("profesional");
        this.password = bundle.getString("password");
        this.cookie = bundle.getString("cookie");
        //this.elementos = (ArrayList<Turno>) bundle.getSerializable("elementos");

        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvNroMatricula = (TextView) view.findViewById(R.id.tvNroMatricula);
        tvComentario = (TextView) view.findViewById(R.id.tvComentario);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnPrev = (Button) view.findViewById(R.id.btnPrev);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        tvFecha = (TextView) view.findViewById(R.id.tvFecha);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);

        tvNombre.setText(nombre.trim());
        //tvNombre.setGravity(Gravity.CENTER);
        tvNroMatricula.setText(matricula);
        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        showProgress(true);

        c = Calendar.getInstance();
        fecha = generarFecha(c);

        getTurnosTask = new GetTurnosTask(fecha);
        getTurnosTask.execute((Void) null);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DAY_OF_MONTH, -1);
                fecha = generarFecha(c);

                productosVector.ResetList();

                getTurnosTask = new GetTurnosTask(fecha);
                getTurnosTask.execute((Void) null);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DAY_OF_MONTH, 1);
                fecha = generarFecha(c);

                productosVector.ResetList();

                getTurnosTask = new GetTurnosTask(fecha);
                getTurnosTask.execute((Void) null);
            }
        });


        // Inflate the layout for this fragment
        return view;
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
        Turno turno = null;
        int tamanyoArray = jsonArray.length();

        if (jsonArray == null) {
            return;
        }

        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String hora = jsonObject.getString("hora");
                String paciente = jsonObject.getString("paciente");
                String mutual = jsonObject.getString("mutual");
                String obs = jsonObject.getString("obs");
                String color = jsonObject.getString("color");

                turno = new Turno(hora, paciente, mutual, obs, color);
                productosVector.anyade(turno);
            } catch (Exception e) {
                Log.e("inflarVista", e.getMessage());
            }
        }

        if (tamanyoArray == 0) { //no hay turnos para mostrar
            turno = new Turno();
            productosVector.anyade(turno);
            Toast.makeText(contexto, "No hay turnos a Mostrar", Toast.LENGTH_SHORT).show();
            TurnosVector productosVectorAux = new TurnosVector(productosVector.getArray(1, 1));
            actualizarVista(productosVectorAux);
        } else {
            actualizarVista(productosVector);
        }
    }

    public void actualizarVista(TurnosVector productosVector){
        turnos = productosVector;
        if (fragmentVisible == "agenda"){
            adaptador = new AdaptadorTurnos(contexto, turnos);
        }
        /*
        if (fragmentVisible == "promociones"){
            adaptador = new AdaptadorTurnos(contexto, turnos);
        }
        */
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(layoutManager);
        showProgress(false);
    }

    public String generarFecha (Calendar c) {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        String fecha = "";
        String monthStr;
        String dayStr;
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        fecha = dayStr + "/" + monthStr + "/" + year;
        return fecha;
    }

    private void showDatePickerDialog() {

        String [] dma = fecha.split("/");

        int anio = Integer.parseInt(dma[2]);
        int mes = Integer.parseInt(dma[1]) - 1; //los meses comienzan desde 0
        int dia = Integer.parseInt(dma[0]);

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                String dayStr = "";
                String monthStr = "";

                if (day < 10) {
                    dayStr = "0" + day;
                } else {
                    dayStr = "" + day;
                }
                month++;
                if (month < 10) {
                    monthStr = "0" + month;
                } else {
                    monthStr = "" + month;
                }

                //final String selectedDate = dayStr + "/" + monthStr + "/" + year;
                //tvFecha.setText(selectedDate);
                productosVector.ResetList();
                c.set(year, month - 1, day);
                fecha = generarFecha(c);
                getTurnosTask = new GetTurnosTask(fecha);
                getTurnosTask.execute((Void) null);
            }
        }, anio, mes, dia);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public class GetTurnosTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;
        private String fecha;

        public GetTurnosTask(String fecha) {
            this.fecha = fecha;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + constantes.metodoGetAgendaDia);// + "get_matricula="+ mMatricula+"&get_pass=" + mPassword);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Cookie",cookie);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("get_agenda_matricula", profesional);
                obj.put("get_agenda_dia", fecha);

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
            getTurnosTask = null;
            showProgress(false);

            JSONObject jsonObject = null;

            int salida = 0;
            int flag = 0;
            String comentario = "";
            JSONObject agenda = null;
            JSONArray turnos = null;

            try {
                jsonObject = jsonResp.getJSONObject("Response");

                flag = jsonObject.getInt("flag");
                comentario = jsonObject.getString("comentario");
                agenda = jsonObject.getJSONObject("agenda");
                turnos = agenda.getJSONArray("turnos");

            } catch (Exception e){
                salida = 8;
            }

            switch (salida) {
                case 0:
                case 8:
                    if (success) {
                        if (flag == 9) {
                            Toast.makeText(contexto, nombre, Toast.LENGTH_SHORT).show();
                        } else {
                            tvFecha.setText(fecha);
                            tvComentario.setText(comentario.trim());
                            //tvComentario.setGravity(Gravity.CENTER);
                            if (!comentario.trim().isEmpty()) {
                                tvComentario.setBackgroundColor(Color.BLACK);
                                tvComentario.setTextColor(Color.WHITE);
                            } else {
                                tvComentario.setBackgroundColor(0);
                            }
                            //Toast.makeText(contexto,  getString(R.string.servidor_error),Toast.LENGTH_SHORT).show();
                            inflarVista(turnos);
                        }

                    } else {
                        Toast.makeText(contexto,  getString(R.string.error_get_turnos),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(contexto,  getString(R.string.error_get_turnos),Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            getTurnosTask = null;
        }
    }

/*
    public void actualizarVistaOrdenada(String filtrarPuntos, String orden, String orderBy) {
        ultimaPaginaCargada = 0;
        productosVector.ResetList();
        this.flagPaso = 0;
        paginaActual = 1;
        fromCantidadTurnos = 1;
        toCantidadTurnos = constantes.CANTIDAD_TURNOS_LISTA;

        orderByProducto = orden + "," + orderBy;
        showProgress(true);
        getTurnosTask = new getTurnosTask(filtrarPuntos, fromCantidadTurnos, toCantidadTurnos, orderByProducto, elementos);
        getTurnosTask.execute((Void) null);
    }

    public void actualizarVistaPreferencias(ArrayList<Check> elementos){
        ultimaPaginaCargada = 0;
        productosVector.ResetList();
        this.flagPaso = 0;
        paginaActual = 1;
        fromCantidadTurnos = 1;
        toCantidadTurnos = constantes.CANTIDAD_TURNOS_LISTA;

        showProgress(true);
        orderByProducto = "nombre,asc";
        getTurnosTask = new getTurnosTask(filtrarPuntos, fromCantidadTurnos, toCantidadTurnos,orderByProducto, elementos);
        getTurnosTask.execute((Void) null);
    }
    */
}
