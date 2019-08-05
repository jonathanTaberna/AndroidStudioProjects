package hsneoclinica.neoclinica;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import hsneoclinica.neoclinica.R;
import hsneoclinica.neoclinica.constantes.constantes;

public class PopUpProductoDialogo {

    //GetImageTask getImageTask;
    final ImageView ivPopUpProductoDialogoImagen;
    final TextView tvPopUpProductoDialogoDescripcion;
    final TextView tvPopUpProductoDialogoPuntos;
    private Context contexto;

    public PopUpProductoDialogo(Context contexto, Bitmap imagen, String descripcion, int puntos) {
        contexto = contexto;
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.popup_producto_dialogo);

        ivPopUpProductoDialogoImagen = (ImageView) dialogo.findViewById(R.id.ivPopUpProductoDialogoImagen);
        tvPopUpProductoDialogoDescripcion = (TextView) dialogo.findViewById(R.id.tvPopUpProductoDialogoDescripcion);
        tvPopUpProductoDialogoPuntos = (TextView) dialogo.findViewById(R.id.tvPopUpProductoDialogoPuntos);

        ivPopUpProductoDialogoImagen.setImageBitmap(imagen);
        tvPopUpProductoDialogoDescripcion.setText(descripcion);
        tvPopUpProductoDialogoPuntos.setText("Puntos: "+puntos);
        dialogo.show();
    }

    public PopUpProductoDialogo(Context contexto, String codigoProd, String descripcion, int puntos, String comentario, String para) { //para = C:Cancje, P:Promociones
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        //dialogo.getWindow().setBackgroundDrawableResource(R.drawable.layout_bg);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.popup_producto_dialogo);

         ivPopUpProductoDialogoImagen= (ImageView) dialogo.findViewById(R.id.ivPopUpProductoDialogoImagen);
        tvPopUpProductoDialogoDescripcion = (TextView) dialogo.findViewById(R.id.tvPopUpProductoDialogoDescripcion);
        tvPopUpProductoDialogoPuntos = (TextView) dialogo.findViewById(R.id.tvPopUpProductoDialogoPuntos);

        tvPopUpProductoDialogoDescripcion.setText(descripcion);
        if (!comentario.isEmpty()) {
            tvPopUpProductoDialogoPuntos.setText(comentario);
        } else {
            tvPopUpProductoDialogoPuntos.setText("Puntos: " + puntos);
        }

        //getImageTask = new GetImageTask(codigoProd, para);
        //getImageTask.execute((Void) null);

        //para descargar imagen desde un servidor de imagenes
        //new GetImageFromURL(ivPopUpProductoDialogoImagen,"https://i.imgur.com/WS58anC.jpg").execute();

        dialogo.show();
    }

/*
    public class GetImageTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        JSONObject jsonResp = null;
        private String codigoProd;
        private Bitmap foto1 = null;
        private String para = "";

        public GetImageTask (String codigoProd, String para){
            this.codigoProd = codigoProd;
            this.para = para;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                URL url = new URL(constantes.pathConnectionProductos + "getFoto");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                obj.put("codigo", codigoProd);
                obj.put("para", para);

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
            getImageTask = null;

            int salida = 0;

            try {
                salida = jsonResp.getInt("salida");

                String foto_1 = jsonResp.getString("foto");
                if (foto_1.length() > 0) {
                    byte[] decodedString = Base64.decode(foto_1, Base64.DEFAULT);
                    foto1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }


            } catch (Exception e) {
                Log.i("catch onPost",e.getMessage());
                salida = 8;
            }

            switch (salida) {
                case 1:
                case 8:
                    if (salida == 8) {
                        if (status == 98|| status == 404  || status == 405) {
                            Toast.makeText(contexto, contexto.getResources().getString(R.string.servidor_timeout),Toast.LENGTH_SHORT).show();
                        }
                        if (status == 200) {
                            Toast.makeText(contexto, contexto.getResources().getString(R.string.servidor_error),Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (success) {
                        ivPopUpProductoDialogoImagen.setImageBitmap(foto1);
                    } else {
                        ivPopUpProductoDialogoImagen.setImageBitmap(null);
                        Log.i("onPost","no success");
                        //edtUsuario.setError(getString(R.string.error_json));
                        //edtUsuario.requestFocus();
                    }
                    break;
                case 9:
                    if (status == 99) {
                        ivPopUpProductoDialogoImagen.setImageBitmap(null);
                        Log.i("onPost","status 99");
                        //edtUsuario.setError(getString(R.string.servidor_timeout));
                        //edtUsuario.requestFocus();
                    } else {
                        ivPopUpProductoDialogoImagen.setImageBitmap(null);
                        Log.i("onPost","status 9");
                        //edtUsuario.setError(getString(R.string.usuario_existente));
                        //edtUsuario.requestFocus();
                    }
                    break;
                default:
                    ivPopUpProductoDialogoImagen.setImageBitmap(null);
                    Log.i("onPost","default");
                    //edtPassword.setError(getString(R.string.error_incorrect_password));
                    //edtPassword.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            getImageTask = null;
        }
    }
*/
    //para descargar imagen desde un servidor de imagenes
    public class GetImageFromURL extends AsyncTask<Void, Void, Bitmap>{
        ImageView imgV;
        String url;

        public GetImageFromURL(ImageView imgV, String url){
            this.imgV = imgV;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                InputStream srt = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }
    }
}