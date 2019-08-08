package hsneoclinica.neoclinica;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import hsneoclinica.neoclinica.constantes.constantes;

public class GeoLocalizacionActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    //private TextView tvLatitud, tvLongitud, tvAltura, tvPrecision;

    /*Se declara una variable de tipo LocationManager encargada de proporcionar acceso al servicio de localizaciÃ³n del sistema.*/
    private LocationManager locManager;
    /*Se declara una variable de tipo Location que accederÃ¡ a la Ãºltima posiciÃ³n conocida proporcionada por el proveedor.*/
    private Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_geo_localizacion);

        //tvLatitud = (TextView)findViewById(R.id.tvLatitud);
        //tvLongitud = (TextView)findViewById(R.id.tvLongitud);
        //tvAltura = (TextView)findViewById(R.id.tvAltura);
        //tvPrecision = (TextView)findViewById(R.id.tvPrecision);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            tvLatitud.setText("No se han definido los permisos necesarios.");
            tvLongitud.setText("");
            tvAltura.setText("");
            tvPrecision.setText("");
            Toast.makeText(this, "No se han definido los permisos necesarios. Se cancela la operacion", Toast.LENGTH_SHORT).show();
            setResult(constantes.RESULT_FAIL);
            finish();
        } else {
            //Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.
            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            tvLatitud.setText(String.valueOf(loc.getLatitude()));
            tvLongitud.setText(String.valueOf(loc.getLongitude()));
            tvAltura.setText(String.valueOf(loc.getAltitude()));
            tvPrecision.setText(String.valueOf(loc.getAccuracy()));
            Toast.makeText(this, "Geolocalizacion Correcta", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
        */
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    /*Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.*/
                    locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //tvLatitud.setText(String.valueOf(loc.getLatitude()));
                    //tvLongitud.setText(String.valueOf(loc.getLongitude()));
                    //tvAltura.setText(String.valueOf(loc.getAltitude()));
                    //tvPrecision.setText(String.valueOf(loc.getAccuracy()));
                    Toast.makeText(this, "Geolocalizacion Correcta", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    //tvLatitud.setText("No se han definido los permisos necesarios.");
                    //tvLongitud.setText("");
                    //tvAltura.setText("");
                    //tvPrecision.setText("");
                    Toast.makeText(this, "No se han definido los permisos necesarios. Se cancela la operacion", Toast.LENGTH_SHORT).show();
                    setResult(constantes.RESULT_FAIL);
                    finish();
                }
                return;
            }
            default: {
                //tvLatitud.setText("No se han definido los permisos necesarios.");
                //tvLongitud.setText("");
                //tvAltura.setText("");
                //tvPrecision.setText("");
                Toast.makeText(this, "No se han definido los permisos necesarios. Se cancela la operacion", Toast.LENGTH_SHORT).show();
                setResult(constantes.RESULT_FAIL);
                finish();
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
