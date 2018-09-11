package hsfarmacia.farmaclub.menu_lateral;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import hsfarmacia.farmaclub.ConfiguracionesDialogo;
import hsfarmacia.farmaclub.PopUpProductoDialogo;
import hsfarmacia.farmaclub.R;
import hsfarmacia.farmaclub.adaptador.AdaptadorProductos;
import hsfarmacia.farmaclub.constantes.constantes;
import hsfarmacia.farmaclub.provisorios.Producto;
import hsfarmacia.farmaclub.provisorios.Productos;
import hsfarmacia.farmaclub.provisorios.ProductosVector;

import static hsfarmacia.farmaclub.constantes.constantes.CANTIDAD_PRODUCTOS_LISTA;

@SuppressLint("ValidFragment")
public class CanjeFragment extends Fragment {

    //variables main activity


    private String tarjeta;
    private int puntos;
    private String nombre;
    private int cantidadProductos;
    private int cantidadPaginas;
    private int paginaActual = 1;
    private int flagPaso = 0;
    private Context contexto;
    private Productos productos;
    private String filtrarPuntos;
    private int filtrarPor;
    private int ordenarPor;
    private int ordenar;
    private int ultimaPaginaCargada = 0;



    private GetCanjesTask getCanjesTask;
    private int fromCantidadProducto = 1;
    private int toCantidadProducto = CANTIDAD_PRODUCTOS_LISTA;
    private String orderByProducto = "nombre,asc";

    //pantalla
    private RecyclerView recyclerView;
    public AdaptadorProductos adaptador;
    private RecyclerView.LayoutManager layoutManager;
    //private TextView tvTarjeta;
    private TextView tvCantidadPuntos;
    private TextView tvNombre;
    private Button btnPrev;
    private Button btnNext;
    private TextView tvPagina;
    private View pbLoading;


    ProductosVector productosVector = new ProductosVector();


    //fin variables main activity

    @SuppressLint("ValidFragment")
    public CanjeFragment(String tarjeta, int puntos, String nombre){
        this.tarjeta = tarjeta;
        this.puntos = puntos;
        this.nombre = nombre;
    }

    public void setOrden(String filtrarPuntos, int filtrarPor, int ordenarPor, int ordenar) {
        this.filtrarPuntos = filtrarPuntos;
        this.filtrarPor = filtrarPor;
        this.ordenarPor = ordenarPor;
        this.ordenar = ordenar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        contexto = container.getContext(); //getActivity().getApplicationContext();
        filtrarPuntos = "todos";
        filtrarPor = 1;
        ordenarPor = 1;
        ordenar = 1;


        //tarjeta = getActivity().getIntent().getStringExtra("tarjeta");
        //puntos = getActivity().getIntent().getIntExtra("puntos", 0);
        //nombre = getActivity().getIntent().getStringExtra("nombre");

        tvCantidadPuntos = (TextView) view.findViewById(R.id.tvCantidadPuntos);
        tvNombre = (TextView) view.findViewById(R.id.tvNombre);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        btnPrev = (Button) view.findViewById(R.id.btnPrev);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        tvPagina = (TextView) view.findViewById(R.id.tvPagina);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);

        tvNombre.setText("Bienvenido " + nombre);
        tvCantidadPuntos.setText("" + puntos);

        showProgress(true);
        getCanjesTask = new GetCanjesTask(filtrarPuntos,fromCantidadProducto,toCantidadProducto,orderByProducto);
        getCanjesTask.execute((Void) null);

        btnPrev.setEnabled(false); //estado inicial
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromCantidadProducto -= CANTIDAD_PRODUCTOS_LISTA;
                toCantidadProducto -= CANTIDAD_PRODUCTOS_LISTA;
                paginaActual --;
                showProgress(true);

                ProductosVector productosVectorAux = new ProductosVector(productosVector.getArray(fromCantidadProducto, toCantidadProducto));
                actualizarVista(productosVectorAux);

                if (paginaActual == 1) {
                    btnPrev.setEnabled(false); //estado inicial
                }
                if (cantidadPaginas > 1) {
                    btnNext.setEnabled(true);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromCantidadProducto += CANTIDAD_PRODUCTOS_LISTA;
                toCantidadProducto += CANTIDAD_PRODUCTOS_LISTA;
                paginaActual ++;
                showProgress(true);

                if (paginaActual > ultimaPaginaCargada) {
                    getCanjesTask = new GetCanjesTask(filtrarPuntos, fromCantidadProducto, toCantidadProducto, orderByProducto);
                    getCanjesTask.execute((Void) null);
                } else {
                    ProductosVector productosVectorAux = new ProductosVector(productosVector.getArray(fromCantidadProducto, toCantidadProducto));
                    actualizarVista(productosVectorAux);
                }

                btnPrev.setEnabled(true);
                if (paginaActual == cantidadPaginas) {
                    btnNext.setEnabled(false);
                }
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
        Producto producto = null;
        // hasta aca el ctrl   z

        if (jsonArray == null) {
            return;
        }

        for (int i=0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String codigo = jsonObject.getString("codigo");
                String nombre = jsonObject.getString("nombre");
                int puntos = jsonObject.getInt("puntos");

                //byte[] decodedString = Base64.decode(jsonObject.getString("foto_2").getBytes(), Base64.DEFAULT);
                //Bitmap foto1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                Bitmap foto1 = null;

                String foto_1 = jsonObject.getString("foto");
                if (foto_1.length() > 0) {
                    byte[] decodedString = Base64.decode(foto_1, Base64.DEFAULT);
                    foto1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }


                producto = new Producto(codigo, nombre, puntos, foto1);
                productosVector.anyade(producto);
            } catch (Exception e) {
                Log.e("inflarVista", e.getMessage());
            }
        }

        ultimaPaginaCargada ++;

        ProductosVector productosVectorAux = new ProductosVector(productosVector.getArray(fromCantidadProducto, toCantidadProducto));
        //productos = productosVector;
        actualizarVista(productosVectorAux);
    }

    public void actualizarVista(ProductosVector productosVector){
        productos = productosVector;
        adaptador = new AdaptadorProductos(contexto, productos);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(layoutManager);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) recyclerView.getChildAdapterPosition(v);
                Producto prod = productos.elemento(id);
                //cambiar esta llamada, por:
                  new PopUpProductoDialogo(contexto, prod.getCodigo(),prod.getNombre(),prod.getPuntos());
                //new PopUpProductoDialogo(contexto, prod.getFoto1(),prod.getNombre(),prod.getPuntos());

            }
        });
        if (flagPaso == 0) {
            flagPaso = 1;
            cantidadPaginas = cantidadProductos / CANTIDAD_PRODUCTOS_LISTA;
            if (cantidadProductos%CANTIDAD_PRODUCTOS_LISTA > 0) {
                cantidadPaginas ++;
            }
            if (cantidadPaginas > 1) {
                btnNext.setEnabled(true);
            } else {
                btnNext.setEnabled(false);
            }
        }
        tvPagina.setText("Pagina " + paginaActual + "/" + cantidadPaginas);
        showProgress(false);
    }

    public class GetCanjesTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;
        private int fromCantidad;
        private int toCantidad;
        private String orderBy;
        private String filtrarPuntos;

        public GetCanjesTask(String filtrarPuntos, int fromCantidad, int toCantidad, String orderBy) {
            this.filtrarPuntos = filtrarPuntos;
            this.fromCantidad = fromCantidad;
            this.toCantidad = toCantidad;
            this.orderBy = orderBy;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                //URL url = new URL(constantes.lanzoniProductos + "getCanjes");
                URL url;
                if (filtrarPuntos == "todos") {
                    url = new URL(constantes.pathConnectionProductos + "getCanjes");
                } else {
                    url = new URL(constantes.pathConnectionProductos + "getCanjesXPuntos");
                }
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                if (filtrarPuntos == "disponibles"){
                    obj.put("tarjeta", tarjeta);
                }
                obj.put("from", fromCantidad);
                obj.put("to", toCantidad);
                //obj.put("orderBy", "nombre,asc");
                obj.put("orderBy", orderBy);

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
            getCanjesTask = null;
            showProgress(false);

            int salida = 9;
            String msj = "";
            JSONArray jsonArray = null;

            try {
                salida = jsonResp.getInt("salida");
                msj = jsonResp.getString("msj");
                cantidadProductos = jsonResp.getInt("cantidad");
                jsonArray = jsonResp.getJSONArray("productos");
                System.out.println(jsonArray.toString());
            } catch (Exception e) {
                Log.i("catch onPost",e.getMessage());
                salida = 9;
            }

            switch (salida) {
                case 1:
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
            showProgress(false);
            getCanjesTask = null;
        }
    }

    public void actualizarVistaOrdenada(String filtrarPuntos, String orden, String orderBy) {
        ultimaPaginaCargada = 0;
        productosVector.ResetList();
        this.flagPaso = 0;
        paginaActual = 1;
        fromCantidadProducto = 1;
        toCantidadProducto = CANTIDAD_PRODUCTOS_LISTA;

        orderByProducto = orden + "," + orderBy;
        showProgress(true);
        getCanjesTask = new GetCanjesTask(filtrarPuntos, fromCantidadProducto, toCantidadProducto, orderByProducto);
        getCanjesTask.execute((Void) null);
        btnPrev.setEnabled(false); //estado inicial
    }
}
