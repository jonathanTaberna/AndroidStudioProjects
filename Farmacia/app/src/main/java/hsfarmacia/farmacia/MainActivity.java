package hsfarmacia.farmacia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String tarjeta;
    private int puntos;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        tarjeta = extras.getString("tarjeta");
        puntos = extras.getInt("puntos");
        nombre = extras.getString("nombre");

        TextView tvTarjeta = (TextView) findViewById(R.id.tvTarjeta);
        TextView tvPuntos = (TextView) findViewById(R.id.tvPuntos);
        TextView tvNombre = (TextView) findViewById(R.id.tvNombre);

        tvTarjeta.setText("El codigo de tarjeta es: " + tarjeta);
        tvPuntos.setText("Los puntos disponibles son: " + puntos);
        tvNombre.setText("Bienvenido " + nombre);
    }

}
