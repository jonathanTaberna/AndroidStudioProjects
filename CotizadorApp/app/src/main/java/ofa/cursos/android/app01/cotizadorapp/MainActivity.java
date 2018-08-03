package ofa.cursos.android.app01.cotizadorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText cotizacion;
    private EditText dolaresComprar;
    private EditText pesosComprar;
    private Button botonCalcular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cotizacion = (EditText) findViewById(R.id.edtCotizacion);
        this.dolaresComprar = (EditText) findViewById(R.id.edtDolaresComprar);
        this.pesosComprar = (EditText) findViewById(R.id.edtPesosComprar);
        this.botonCalcular = (Button) findViewById(R.id.btnCalcular);

        Random rand = new Random();
        final Double cotizacionDefecto = 20.0+ 6*rand.nextDouble();
        final DecimalFormat nf = new DecimalFormat("#.00");

        //this.cotizacion.setText(cotizacionDefecto.toString());
        //this.dolaresComprar.setText("100.0");
        //this.pesosComprar.setText(Double.valueOf(cotizacionDefecto*100.0).toString());
        this.cotizacion.setText("$"+nf.format(cotizacionDefecto));
        this.dolaresComprar.setText("$100.0");
        this.pesosComprar.setText("$"+nf.format(cotizacionDefecto*100.0));

        this.botonCalcular.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double cantidadAux = 0.0;
                        String dolaresComprarAux = dolaresComprar.getText().toString();
                        if(dolaresComprarAux!=null & dolaresComprarAux.length()>0){
                            cantidadAux = Double.parseDouble(dolaresComprarAux.substring(1)) * cotizacionDefecto;
                        }
                        pesosComprar.setText("$"+nf.format(cantidadAux));
                    }
                }
        );
    }
}
