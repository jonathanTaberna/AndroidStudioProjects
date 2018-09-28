package hsfarmacia.farmaclub.menu_lateral;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import hsfarmacia.farmaclub.R;
import hsfarmacia.farmaclub.adaptador.AdaptadorNovedades;
import hsfarmacia.farmaclub.constantes.constantes;
import hsfarmacia.farmaclub.provisorios.Novedad;
import hsfarmacia.farmaclub.provisorios.Novedades;
import hsfarmacia.farmaclub.provisorios.NovedadesVector;

import static hsfarmacia.farmaclub.constantes.constantes.CANTIDAD_PRODUCTOS_LISTA;

public class NovedadesFragment extends Fragment {


    private String tarjeta;


    private Context contexto;
    private GetNovedadesTask getNovedadesTask;
    private int fromCantidadNovedades = 1;
    private int toCantidadNovedades = CANTIDAD_PRODUCTOS_LISTA;
    private int cantidadNovedades;
    private int ultimaPaginaCargada = 0;
    private Novedades novedades;
    private int flagPaso = 0;
    private int cantidadPaginas;
    private int paginaActual = 1;


    //pantalla
    private RecyclerView recyclerView;
    public AdaptadorNovedades adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvENDescripcion;
    private TextView tvENFecha;
    //private View pbLoading;

    NovedadesVector novedadesVector = new NovedadesVector();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contexto = container.getContext(); //getActivity().getApplicationContext();
        return inflater.inflate(R.layout.fragment_novedades, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        this.tarjeta = bundle.getString("tarjeta");

        tvENDescripcion = (TextView) view.findViewById(R.id.tvENDescripcion);
        tvENFecha = (TextView) view.findViewById(R.id.tvENFecha);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvNovedades);

        //showProgress(true);
        //getNovedadesTask = new GetNovedadesTask(fromCantidadNovedades,toCantidadNovedades);
        //getNovedadesTask.execute((Void) null);

        actualizarVista(novedadesVector);

    }

    /*
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
    */


    private void inflarVista(JSONArray jsonArray) {
        Novedad novedad = null;
        int tamanyoArray = jsonArray.length();

        if (jsonArray == null) {
            return;
        }

        for (int i=0; i < tamanyoArray; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                String descripcion = jsonObject.getString("descripcion");
                String detalle = jsonObject.getString("detalle");
                String fecha = jsonObject.getString("fecha");

                novedad = new Novedad(nombre, descripcion, detalle, fecha);
                novedadesVector.anyade(novedad);
            } catch (Exception e) {
                Log.e("inflarVista", e.getMessage());
            }
        }

        ultimaPaginaCargada ++;


        if (tamanyoArray == 0) { //no hay productos para mostrar
            novedad = new Novedad();
            novedadesVector.anyade(novedad);
            Toast.makeText(contexto, "No hay productos a Mostrar", Toast.LENGTH_SHORT).show();
            NovedadesVector novedadesVectorAux = new NovedadesVector(novedadesVector.getArray(1, 1));
            actualizarVista(novedadesVectorAux);
        } else {
            //if (tamanyoArray + fromCantidadProducto < toCantidadProducto) {
            //    toCantidadProducto = tamanyoArray;
            //}
            NovedadesVector novedadesVectorAux = new NovedadesVector(novedadesVector.getArray(fromCantidadNovedades, toCantidadNovedades));
            //productos = productosVector;
            actualizarVista(novedadesVectorAux);
        }
    }

    public void actualizarVista(NovedadesVector novedadesVector){
        novedades = novedadesVector;
        adaptador = new AdaptadorNovedades(contexto, novedades);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(layoutManager);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) recyclerView.getChildAdapterPosition(v);
                Novedad nov = novedades.elemento(id);


                // llamar a popUp de novedades
                //new PopUpProductoDialogo(contexto, prod.getCodigo(),prod.getNombre(),prod.getPuntos(), "");
            }
        });
        if (flagPaso == 0) {
            flagPaso = 1;
            cantidadPaginas = cantidadNovedades / CANTIDAD_PRODUCTOS_LISTA;
            if (cantidadNovedades%CANTIDAD_PRODUCTOS_LISTA > 0) { //resto de la division
                cantidadPaginas ++;
            }
            //if (cantidadPaginas > 1) {
               //btnNext.setEnabled(true);
            //} else {
                //btnNext.setEnabled(false);
            //}
        }
        //tvPagina.setText("Pagina " + paginaActual + "/" + cantidadPaginas);
        //showProgress(false);
    }


    public class GetNovedadesTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;
        private int fromCantidad;
        private int toCantidad;

        public GetNovedadesTask(int fromCantidad, int toCantidad) {
            this.fromCantidad = fromCantidad;
            this.toCantidad = toCantidad;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                //URL url = new URL(constantes.lanzoniProductos + "getCanjes");
                URL url = new URL(constantes.pathConnectionProductos + "getNovedades");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                obj.put("tarjeta", tarjeta);
                obj.put("from", fromCantidad);
                obj.put("to", toCantidad);
                //obj.put("orderBy", "nombre,asc");

                Log.i("JSON", obj.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(obj.toString());

                os.flush();
                os.close();

                status = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                if (status == 200) { //respuesta OK
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");

                    }
                    JsonResponse = buffer.toString();
                    //Log.i("RESPONSE", JsonResponse);
                    //Log.i("RESPONSE", jsonResp.toString());

                    //jsonResp = new Gson().fromJson(JsonResponse);
                    jsonResp = new JSONObject(JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    status = 99;
                } else {
                    status = 98;
                }
            } catch (SocketTimeoutException e) {
                status = 99;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
            getNovedadesTask = null;
            //showProgress(false);

            int salida = 9;
            String msj = "";
            JSONArray jsonArray = null;

            try {
                salida = jsonResp.getInt("salida");
                msj = jsonResp.getString("msj");
                cantidadNovedades = jsonResp.getInt("cantidad");
                jsonArray = jsonResp.getJSONArray("novedades");
                System.out.println(jsonArray.toString());
            } catch (Exception e) {
                Log.i("catch onPost",e.getMessage());
                salida = 8;
            }

            switch (salida) {
                case 1:
                case 8:
                    if (salida == 8) {
                        if (status == 98) {
                            Toast.makeText(contexto, getString(R.string.servidor_timeout), Toast.LENGTH_SHORT);
                            break;
                        }
                    }
                    if (success) {

                        Toast.makeText(contexto, "success",Toast.LENGTH_SHORT);
                        Log.i("onPost","success");

                        inflarVista(jsonArray);

                        //updateUserTask = new UsuarioActivity.UpdateUserTask(usuario,edtPassword.getText().toString(),tarjeta);
                        //updateUserTask.execute((Void) null);
                    } else {
                        Toast.makeText(contexto, "no success",Toast.LENGTH_SHORT);
                        Log.i("onPost","no success");
                        //edtUsuario.setError(getString(R.string.error_json));
                        //edtUsuario.requestFocus();
                    }
                    break;
                case 9:
                    if (status == 99) {
                        Toast.makeText(contexto, "status 99",Toast.LENGTH_SHORT);
                        Log.i("onPost","status 99");
                        //edtUsuario.setError(getString(R.string.servidor_timeout));
                        //edtUsuario.requestFocus();
                    } else {
                        Toast.makeText(contexto, "status 9",Toast.LENGTH_SHORT);
                        Log.i("onPost","status 9");
                        //edtUsuario.setError(getString(R.string.usuario_existente));
                        //edtUsuario.requestFocus();
                    }
                    break;
                default:
                    Toast.makeText(contexto, "default",Toast.LENGTH_SHORT);
                    Log.i("onPost","default");
                    //edtPassword.setError(getString(R.string.error_incorrect_password));
                    //edtPassword.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            //showProgress(false);
            getNovedadesTask = null;
        }
    }

    public static NovedadesFragment newInstance(String tarjeta) {
        NovedadesFragment novedadesFragment = new NovedadesFragment();
        Bundle args = new Bundle();
        args.putString("tarjeta", tarjeta);
        novedadesFragment.setArguments(args);
        return novedadesFragment;
    }
}
