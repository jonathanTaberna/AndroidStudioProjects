package hsneoclinica.neoclinica.geolocalizacion;





//AplicaciÃ³n Android que permite visualizar la posiciÃ³n global del dispositivo mÃ³vil,
//mostrando los datos de latitud, longitud, altura y precisiÃ³n de la seÃ±al,
//y posteriormente trasladar dichos datos a Google Maps.
//
//academiaandroid.com
//
//by JosÃ© Antonio GÃ¡zquez RodrÃ­guez

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import hsneoclinica.neoclinica.R;

/*Clase GeoLocalizacionActivity, que hereda de la clase base Activity, y que permite mostrar
los datos asociados a la localizaciÃ³n del dispositivo GPS.*/
public class GeoLocalizacionActivity extends Activity{

    //private TextView tvLatitud, tvLongitud,tvPrecision,tvAltura,tvPorDefecto;
    /*Se declara la clase encargada de proporcionar acceso al servicio
    de localizaciÃ³n del sistema.*/
    private LocationManager locManager;
    /*Interfaz encargada de recibir las notificaciones del LocationManager
    cuando se cambia la localizaciÃ³n.*/
    private LocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //setContentView(R.layout.activity_geo_localizacion);

        /*Se asignan los controles definidos en el layout a cada variable definida.*/
        /*
        tvLatitud = (TextView)findViewById(R.id.tvLatitud);
        tvLongitud = (TextView)findViewById(R.id.tvLongitud);
        tvPrecision = (TextView)findViewById(R.id.tvPrecision);
        tvAltura = (TextView)findViewById(R.id.tvAltura);
        tvPorDefecto = (TextView)findViewById(R.id.tvPorDefecto);
        */

        rastreoGPS();

    }

    /*MÃ©todo encargado de actualizar la posiciÃ³n del dispositivo
    GPS cuando este cambie de localizaciÃ³n.*/
    private void rastreoGPS()
    {
        /*Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.*/
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        /*Se declara y asigna a la clase Location la Ãºltima posiciÃ³n conocida proporcionada por el proveedor.*/
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        mostrarPosicion(loc);

        //Se define la interfaz LocationListener, que deberÃ¡ implementarse con los siguientes mÃ©todos.
        locListener = new LocationListener()
        {
            //MÃ©todo que serÃ¡ llamado cuando cambie la localizaciÃ³n.
            @Override
            public void onLocationChanged(Location location) {
                mostrarPosicion(location);
            }

            //MÃ©todo que serÃ¡ llamado cuando se produzcan cambios en el estado del proveedor.
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            //MÃ©todo que serÃ¡ llamado cuando el proveedor estÃ© habilitado para el usuario.
            @Override
            public void onProviderEnabled(String provider)
            {
            }

            //MÃ©todo que serÃ¡ llamado cuando el proveedor estÃ© deshabilitado para el usuario.
            @Override
            public void onProviderDisabled(String provider)
            {
            }
        };

        /*Por Ãºltimo se llama al mÃ©todo encargado establecer la localizaciÃ³n actualizada,
        recibiendo como parÃ¡metros de entrada el nombre del proveedor, el intervalo de tiempo entre cada
        actualizaciÃ³n, distancia en metros entre localizaciones actualizadas, y la variable de tipo LocationListener
        que actualizarÃ¡ la localizaciÃ³n en caso de producirse nuevos cambios.*/
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locListener);

    }

    /*MÃ©todo que recibe como parÃ¡metro de entrada una variable de tipo Location, y que permitirÃ¡
    mostrar los diferentes datos de la ubicaciÃ³n geogrÃ¡fica del dispositivo. En el supuesto de no
    tener habilitada la opciÃ³n de ubicaciÃ³n, se establecerÃ¡n valores por defecto (dichos valores se almacenarÃ¡n en un
    array de datos de tipo String).*/
    private String[] mostrarPosicion(Location loc)
    {
        String[] datos = new String[0];
        if(loc != null)
        {
            float prec = loc.getAccuracy();
            if (prec < 25) {
                //tvPorDefecto.setText("Precision de +-25 mts ");
                datos = new String[]{String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()),String.valueOf(loc.getAccuracy())};

                Intent intent=new Intent();
                intent.putExtra("RESULT_DATOS", datos);
                setResult(RESULT_OK, intent);
                finish();

                return datos;
            }
            /*
            else {
                tvPorDefecto.setText("(valores GPS)");
            }
            tvLatitud.setText(String.valueOf(loc.getLatitude()));
            tvLongitud.setText(String.valueOf(loc.getLongitude()));
            tvAltura.setText(String.valueOf(loc.getAltitude()));
            tvPrecision.setText(String.valueOf(loc.getAccuracy()));

            datos = new String[]{String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude())};
            */
        }
        /*
        else
        {
            tvPorDefecto.setText("(valores por defecto)");
            datos = new String[]{String.valueOf(40.4167754), String.valueOf(-3.7037901999999576),"PosiciÃ³n por defecto"};
            tvLatitud.setText(String.valueOf(40.4167754));
            tvLongitud.setText(String.valueOf(-3.7037901999999576));
            tvAltura.setText(String.valueOf(15.00));
            tvPrecision.setText(String.valueOf(1.0));
        }
        */
        return datos;
    }
}