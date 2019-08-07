package hsneoclinica.neoclinica.menu_lateral;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import hsneoclinica.neoclinica.adaptador.AdaptadorInternados;
import hsneoclinica.neoclinica.constantes.constantes;
import hsneoclinica.neoclinica.provisorios.Internado;
import hsneoclinica.neoclinica.provisorios.Internados;
import hsneoclinica.neoclinica.provisorios.InternadosVector;

public class InternadosFragment extends Fragment {

    //variables main activity

    private String matricula;
    private String password;
    private String profesional;
    private String nombre;
    private String fecha;
    private String cookie;

    private int flagPaso = 0;
    private Context contexto;
    private Internados internados;


    private GetInternadosTask getInternadosTask;

    //pantalla
    private RecyclerView recyclerView;
    public AdaptadorInternados adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout llMatriculaMain;
    private TextView tvNroMatricula;
    private TextView tvNroMat;
    private TextView tvNombre;
    private TextView tvComentario;
    private Button btnPrev;
    private Button btnNext;
    private TextView tvFecha;
    private View pbLoading;

    public InternadosVector internadosVector = new InternadosVector();

    private String fragmentVisible = "";

    private Calendar c;
    private int year;
    private int month;
    private int day;

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

        llMatriculaMain = (LinearLayout) view.findViewById(R.id.llMatriculaMain);
        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        tvNroMatricula = (TextView) view.findViewById(R.id.tvNroMatricula);
        tvNroMat = (TextView) view.findViewById(R.id.tvNroMat);
        tvComentario = (TextView) view.findViewById(R.id.tvComentario);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnPrev = (Button) view.findViewById(R.id.btnPrev);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        tvFecha = (TextView) view.findViewById(R.id.tvFecha);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);

        tvNombre.setText(nombre.trim());
        tvComentario.setText("");
        tvComentario.setHeight(0);
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

        getInternadosTask = new GetInternadosTask(fecha);
        getInternadosTask.execute((Void) null);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnNext.setEnabled(false);
                btnPrev.setEnabled(false);

                c.add(Calendar.DAY_OF_MONTH, -1);
                fecha = generarFecha(c);

                internadosVector.ResetList();
                recyclerView.stopScroll();
                adaptador.notifyDataSetChanged();

                getInternadosTask = new GetInternadosTask(fecha);
                getInternadosTask.execute((Void) null);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnNext.setEnabled(false);
                btnPrev.setEnabled(false);

                c.add(Calendar.DAY_OF_MONTH, 1);
                fecha = generarFecha(c);

                internadosVector.ResetList();
                recyclerView.stopScroll();
                adaptador.notifyDataSetChanged();

                getInternadosTask = new GetInternadosTask(fecha);
                getInternadosTask.execute((Void) null);
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
        Internado internado = null;
        int tamanyoArray = jsonArray.length();

        if (jsonArray == null) {
            return;
        }

        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String egreso = jsonObject.getString("egreso");
                String lugar = jsonObject.getString("lugar");
                String icono = jsonObject.getString("icono");
                String paciente = jsonObject.getString("paciente");
                String mutual = jsonObject.getString("mutual");
                String profesional = jsonObject.getString("profesional");
                String motivo = jsonObject.getString("motivo");
                String fechaIng = jsonObject.getString("fec_ingerso");
                String fechaEgr = jsonObject.getString("fec_egreso");
                String edad = jsonObject.getString("edad");

                internado = new Internado(egreso, lugar, icono, paciente, mutual, profesional, motivo, fechaIng, fechaEgr, edad);
                internadosVector.anyade(internado);
            } catch (Exception e) {
                Log.e("inflarVista", e.getMessage());
            }
        }

        if (tamanyoArray == 0) { //no hay turnos para mostrar

            Toast.makeText(contexto, "No hay internados a Mostrar", Toast.LENGTH_SHORT).show();
            actualizarVista(internadosVector);
            /*
            TurnosVector productosVectorAux = new TurnosVector(productosVector.getArray(1, 1));
            actualizarVista(productosVectorAux);
            */
        } else {
            actualizarVista(internadosVector);
        }
    }

    public void actualizarVista(InternadosVector internadosVector){
        internados = internadosVector;
        if (fragmentVisible == "internados"){
            adaptador = new AdaptadorInternados(contexto, internados);
        }

        //recyclerView.swapAdapter(adaptador,true);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(layoutManager);
        showProgress(false);

        btnNext.setEnabled(true);
        btnPrev.setEnabled(true);
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

                internadosVector.ResetList();
                c.set(year, month - 1, day);
                fecha = generarFecha(c);
                getInternadosTask = new GetInternadosTask(fecha);
                getInternadosTask.execute((Void) null);
            }
        }, anio, mes, dia);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public class GetInternadosTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;
        private String fecha;

        public GetInternadosTask(String fecha) {
            this.fecha = fecha;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + constantes.metodoGetInternacionesDia);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Cookie",cookie);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("get_fecha_res", fecha);

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
            getInternadosTask = null;
            showProgress(false);

            JSONObject jsonObject = null;

            int salida = 0;
            JSONObject internaciones = null;
            JSONArray lista = null;

            try {
                jsonObject = jsonResp.getJSONObject("Response");

                internaciones = jsonObject.getJSONObject("internaciones");
                lista = internaciones.getJSONArray("lista");

            } catch (Exception e){
                salida = 8;
            }

            switch (salida) {
                case 0:
                case 8:
                    if (success) {
                        tvFecha.setText(fecha);
                        inflarVista(lista);

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
            getInternadosTask = null;
        }
    }

}
