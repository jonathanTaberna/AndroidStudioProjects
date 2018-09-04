package hsfarmacia.farmaclub;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import hsfarmacia.farmaclub.adaptador.AdaptadorProductos;
import hsfarmacia.farmaclub.constantes.constantes;
import hsfarmacia.farmaclub.provisorios.Productos;
import hsfarmacia.farmaclub.provisorios.ProductosVector;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private String tarjeta;
    private int puntos;
    private String nombre;


    private RecyclerView recyclerView;
    public AdaptadorProductos adaptador;
    private RecyclerView.LayoutManager layoutManager;

    private GetCanjesTask getCanjesTask;

    //provisorios
    public static Productos productos = new ProductosVector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        tarjeta = extras.getString("tarjeta");
        puntos = extras.getInt("puntos");
        nombre = extras.getString("nombre");

        TextView tvTarjeta = (TextView) findViewById(R.id.tvTarjeta);
        TextView tvPuntos = (TextView) findViewById(R.id.tvPuntos);
        TextView tvNombre = (TextView) findViewById(R.id.tvNombre);

        tvTarjeta.setText("El codigo de tarjeta es: " + tarjeta);
        tvPuntos.setText("Los puntos disponibles son: " + puntos);
        tvNombre.setText("Bienvenido " + nombre);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptador = new AdaptadorProductos(this, productos);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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

        getCanjesTask = new GetCanjesTask();
        getCanjesTask.execute((Void) null);

    }



    public class GetCanjesTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                URL url = new URL(constantes.lanzoniProductos + "getCanjes");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                obj.put("from", 1);
                obj.put("to", 5);
                obj.put("orderBy", "nombre,asc");

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
            int cantidad = 0;
            JSONArray jsonArray = null;

            try {
                salida = jsonResp.getInt("salida");
                msj = jsonResp.getString("msj");
                cantidad = jsonResp.getInt("cantidad");
                jsonArray = jsonResp.getJSONArray("productos");
                System.out.println(jsonArray.toString());
            } catch (Exception e) {
                Log.i("catch onPost",e.getMessage());
                salida = 9;
            }

            switch (salida) {
                case 1:
                    if (success) {

                        //quede aca, hay que decodificar la imagen e inflar la vista.

                        Toast.makeText(MainActivity.this, "success",Toast.LENGTH_SHORT);
                        Log.i("onPost","success");
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






}
