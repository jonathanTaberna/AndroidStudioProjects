package com.example.comunicacionactividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button bVerificar;
    private TextView tResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bVerificar =(Button) findViewById(R.id.button);
        bVerificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                lanzarVerificar(null);
            }
        });

        tResultado =(TextView) findViewById(R.id.resultado);
    }

    public void lanzarVerificar (View view) {
        EditText valor = (EditText) findViewById(R.id.nombreValue);
        Intent intent = new Intent(this, Verificar.class);
        intent.putExtra("nombre", valor.getText().toString());
        startActivityForResult(intent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            String res = data.getExtras().getString("resultado");
            tResultado.setText("Resultado:" + res);
        }
    }
}
