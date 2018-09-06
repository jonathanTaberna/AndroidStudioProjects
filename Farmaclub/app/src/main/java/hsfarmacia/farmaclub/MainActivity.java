package hsfarmacia.farmaclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import hsfarmacia.farmaclub.adaptador.AdaptadorProductos;
import hsfarmacia.farmaclub.constantes.constantes;
import hsfarmacia.farmaclub.provisorios.Producto;
import hsfarmacia.farmaclub.provisorios.Productos;
import hsfarmacia.farmaclub.provisorios.ProductosVector;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements ConfiguracionesDialogo.FinalizoConfiguracionesDialogo{

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


    private RecyclerView recyclerView;
    public AdaptadorProductos adaptador;
    private RecyclerView.LayoutManager layoutManager;

    private GetCanjesTask getCanjesTask;
    private int fromCantidadProducto = 1;
    private int toCantidadProducto = 20;
    private String orderByProducto = "nombre,asc";

    //pantalla
    //private TextView tvTarjeta;
    private TextView tvCantidadPuntos;
    private TextView tvNombre;
    private Button btnPrev;
    private Button btnNext;
    private TextView tvPagina;


    //provisorios
    //public static Productos productos = new ProductosVector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        flagPaso = 0;
        contexto = this;
        filtrarPuntos = "todos";
        filtrarPor = 1;
        ordenarPor = 1;
        ordenar = 1;



        tarjeta = extras.getString("tarjeta");
        puntos = extras.getInt("puntos");
        nombre = extras.getString("nombre");

        //tvTarjeta = (TextView) findViewById(R.id.tvTarjeta);
        tvCantidadPuntos = (TextView) findViewById(R.id.tvCantidadPuntos);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvPagina = (TextView) findViewById(R.id.tvPagina);

        //tvTarjeta.setText("El codigo de tarjeta es: " + tarjeta);
        tvNombre.setText("Bienvenido " + nombre);
        tvCantidadPuntos.setText("" + puntos);

        getCanjesTask = new GetCanjesTask(filtrarPuntos,fromCantidadProducto,toCantidadProducto,orderByProducto);
        getCanjesTask.execute((Void) null);

        btnPrev.setEnabled(false); //estado inicial
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromCantidadProducto -= 20;
                toCantidadProducto -= 20;
                getCanjesTask = new GetCanjesTask(filtrarPuntos,fromCantidadProducto,toCantidadProducto,orderByProducto);
                getCanjesTask.execute((Void) null);
                paginaActual --;
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
                fromCantidadProducto += 20;
                toCantidadProducto += 20;
                getCanjesTask = new GetCanjesTask(filtrarPuntos,fromCantidadProducto,toCantidadProducto,orderByProducto);
                getCanjesTask.execute((Void) null);
                btnPrev.setEnabled(true);
                paginaActual ++;
                if (paginaActual == cantidadPaginas) {
                    btnNext.setEnabled(false);
                }
            }
        });


        /*
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptador = new AdaptadorProductos(this, productos);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        */




        //DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        //recyclerView.addItemDecoration(decoration);
        // iniciar Actividad para detalle del producto
        //adaptador.setOnItemClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent i = new Intent(MainActivity.this, VistaLugarActivity.class);
        //        i.putExtra("id", (long) recyclerView.getChildAdapterPosition(v));
        //        startActivity(i);
        //    }
        //});


    }

    private void inflarVista(JSONArray jsonArray) {
        Producto producto = null;
        ProductosVector productosVector = new ProductosVector();

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

        productos = productosVector;
        adaptador = new AdaptadorProductos(this, productos);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) recyclerView.getChildAdapterPosition(v);
                Producto prod = productos.elemento(id);
                new PopUpProductoDialogo(contexto, prod.getFoto1(),prod.getNombre(),prod.getPuntos());

            }
        });
        if (flagPaso == 0) {
            flagPaso = 1;
            cantidadPaginas = cantidadProductos / 20;
            if (cantidadProductos%20 > 0) {
                cantidadPaginas ++;
            }
            if (cantidadPaginas > 1) {
                btnNext.setEnabled(true);
            } else {
                btnNext.setEnabled(false);
            }
        }
        tvPagina.setText("Pagina " + paginaActual + "/" + cantidadPaginas);
    }

    @Override
    public void ResultadoConfiguracionesDialogo(String filtrarPuntos, String orden, String orderBy) {
        fromCantidadProducto = 1;
        toCantidadProducto = 20;
        orderByProducto = orden + "," + orderBy;
        if (this.filtrarPuntos != filtrarPuntos) {
            this.flagPaso = 0;
        }
        this.filtrarPuntos = filtrarPuntos;

        if (filtrarPuntos == "todos") {
            this.filtrarPor = 1;
        } else {
            this.filtrarPor = 2;
        }
        if (orden == "nombre") {
            this.ordenarPor = 1;
        } else {
            this.ordenarPor = 2;
        }
        if (orderBy == "asc") {
            this.ordenar = 1;
        } else {
            this.ordenar = 2;
        }

        getCanjesTask = new GetCanjesTask(filtrarPuntos, fromCantidadProducto, toCantidadProducto, orderByProducto);
        getCanjesTask.execute((Void) null);
        btnPrev.setEnabled(false); //estado inicial
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
                    url = new URL(constantes.hsServerNombreProductos + "getCanjes");
                } else {
                    url = new URL(constantes.hsServerNombreProductos + "getCanjesXPuntos");
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

                        Toast.makeText(MainActivity.this, "success",Toast.LENGTH_SHORT);
                        Log.i("onPost","success");

                        inflarVista(jsonArray);

                        //updateUserTask = new UsuarioActivity.UpdateUserTask(usuario,edtPassword.getText().toString(),tarjeta);
                        //updateUserTask.execute((Void) null);
                    } else {
                        Toast.makeText(MainActivity.this, "no success",Toast.LENGTH_SHORT);
                        Log.i("onPost","no success");
                        //edtUsuario.setError(getString(R.string.error_json));
                        //edtUsuario.requestFocus();
                    }
                    break;
                case 9:
                    if (status == 99) {
                        Toast.makeText(MainActivity.this, "status 99",Toast.LENGTH_SHORT);
                        Log.i("onPost","status 99");
                        //edtUsuario.setError(getString(R.string.servidor_timeout));
                        //edtUsuario.requestFocus();
                    } else {
                        Toast.makeText(MainActivity.this, "status 9",Toast.LENGTH_SHORT);
                        Log.i("onPost","status 9");
                        //edtUsuario.setError(getString(R.string.usuario_existente));
                        //edtUsuario.requestFocus();
                    }
                    break;
                default:
                    Toast.makeText(MainActivity.this, "default",Toast.LENGTH_SHORT);
                    Log.i("onPost","default");
                    //edtPassword.setError(getString(R.string.error_incorrect_password));
                    //edtPassword.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            getCanjesTask = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...

                //quede aca, falta crear dialogggggggggg

                //Toast.makeText(this, "menusssss", Toast.LENGTH_SHORT).show();
                new ConfiguracionesDialogo(contexto, MainActivity.this,filtrarPor, ordenarPor,ordenar);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



}
