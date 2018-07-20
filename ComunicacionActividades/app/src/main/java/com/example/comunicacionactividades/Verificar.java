package com.example.comunicacionactividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Verificar extends Activity {
    private Button bAceptar;
    private Button bRechazar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verificar);

        Bundle extras = getIntent().getExtras();
        String nombre = extras.getString("nombre");
        TextView txtCambiado = (TextView) findViewById(R.id.textView2);
        txtCambiado.setText("Hola " + nombre + "¿Aceptas las condiciones?");

        bAceptar =(Button) findViewById(R.id.bAceptar);
        bAceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("resultado","Aceptado");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        bRechazar =(Button) findViewById(R.id.bRechazar);
        bRechazar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("resultado","Rechazado");
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}
