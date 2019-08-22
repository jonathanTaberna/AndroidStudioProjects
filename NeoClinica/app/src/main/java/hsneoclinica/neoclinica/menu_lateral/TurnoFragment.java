package hsneoclinica.neoclinica.menu_lateral;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Calendar;

import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.adaptador.AdaptadorTurnos;
import hsneoclinica.neoclinica.constantes.constantes;
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

    private int flagPaso = 0;
    private Context contexto;
    private Turnos turnos;


    private GetTurnosTask getTurnosTask;

    //pantalla
    private RecyclerView recyclerView;
    public AdaptadorTurnos adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvNroMatricula;
    private TextView tvNroMat;
    private TextView tvNombre;
    private TextView tvComentario;
    private Button btnPrev;
    private Button btnNext;
    private TextView tvFecha;
    private View pbLoading;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TurnosVector productosVector = new TurnosVector();

    private String fragmentVisible = "";

    private Calendar c;
    private int year;
    private int month;
    private int day;

    //fin variables main activity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        contexto = container.getContext();

        Bundle bundle = getArguments();

        this.fragmentVisible = bundle.getString("fragmentVisible");
        this.nombre = bundle.getString("nombre");
        this.matricula = bundle.getString("matricula");
        this.profesional = bundle.getString("profesional");
        this.password = bundle.getString("password");
        this.cookie = bundle.getString("cookie");

        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvNroMatricula = (TextView) view.findViewById(R.id.tvNroMatricula);
        tvNroMat = (TextView) view.findViewById(R.id.tvNroMat);
        tvComentario = (TextView) view.findViewById(R.id.tvComentario);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnPrev = (Button) view.findViewById(R.id.btnPrev);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        tvFecha = (TextView) view.findViewById(R.id.tvFecha);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        tvNombre.setText(nombre.trim());
        if (matricula.trim().equals("0")) {
            tvNroMat.setHeight(0);
            tvNroMatricula.setHeight(0);
        } else {
            tvNroMatricula.setText(matricula);
        }
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
                setearEnable(false);

                c.add(Calendar.DAY_OF_MONTH, -1);
                fecha = generarFecha(c);

                productosVector.ResetList();
                if (productosVector.tamanyo() > 0 ) {
                    recyclerView.stopScroll();
                    adaptador.notifyDataSetChanged();
                }

                getTurnosTask = new GetTurnosTask(fecha);
                getTurnosTask.execute((Void) null);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setearEnable(false);

                c.add(Calendar.DAY_OF_MONTH, 1);
                fecha = generarFecha(c);

                productosVector.ResetList();
                if (productosVector.tamanyo() > 0 ) {
                    recyclerView.stopScroll();
                    adaptador.notifyDataSetChanged();
                }
                getTurnosTask = new GetTurnosTask(fecha);
                getTurnosTask.execute((Void) null);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                //swipeRefreshLayout.setRefreshing(t);

                productosVector.ResetList();
                if (productosVector.tamanyo() > 0 ) {
                    recyclerView.stopScroll();
                    adaptador.notifyDataSetChanged();
                }
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
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(layoutManager);
        showProgress(false);
        swipeRefreshLayout.setRefreshing(false);
        setearEnable(true);
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

                productosVector.ResetList();
                c.set(year, month - 1, day);
                fecha = generarFecha(c);
                getTurnosTask = new GetTurnosTask(fecha);
                getTurnosTask.execute((Void) null);
            }
        }, anio, mes, dia);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void setearEnable(boolean enable){
        btnNext.setEnabled(enable);
        btnPrev.setEnabled(enable);
        tvFecha.setEnabled(enable);
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
                conn.setRequestProperty("Content-Type: application/json", "charset=utf-8");
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

            tvFecha.setText(fecha);

            switch (salida) {
                case 0:
                case 8:
                    if (success) {
                        if (flag == 9) {
                            Toast.makeText(contexto, nombre, Toast.LENGTH_SHORT).show();
                            setearEnable(false);
                        } else {
                            tvComentario.setText(comentario.trim());
                            ViewGroup.LayoutParams params = tvComentario.getLayoutParams();
                            if (!comentario.trim().isEmpty()) {
                                tvComentario.setBackgroundColor(Color.BLACK);
                                tvComentario.setTextColor(Color.WHITE);
                                tvComentario.setTextSize(constantes.TAMANYO_TEXT_SIZE);

                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                            } else {
                                tvComentario.setBackgroundColor(0);

                                params.height = 0;
                            }
                            tvComentario.setLayoutParams(params);
                            setearEnable(true);
                            inflarVista(turnos);
                        }

                    } else {
                        setearEnable(false);
                        Toast.makeText(contexto,  getString(R.string.error_get_turnos),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    setearEnable(false);
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
}
