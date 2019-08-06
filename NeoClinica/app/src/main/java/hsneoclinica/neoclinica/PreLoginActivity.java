package hsneoclinica.neoclinica;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hsneoclinica.neoclinica.constantes.constantes;

import static hsneoclinica.neoclinica.R.drawable.clinica_gral_deheza_logo;


public class PreLoginActivity extends AppCompatActivity {
    private Button btnSanLucas;
    private Button btnSanatorioPrivado;
    private Button btnNeoclinica;
    private Button btnOdontograssi;
    private Button btnResonanciaR4;
    private Button btnUrologico;
    private Button btnClinicaPrivadaGralDeheza;
    private Button btnHospitalComGralDeheza;
    private Button btnHS;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        //toolbar = (Toolbar) findViewById(R.id.toolbarPreLogin);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setIcon(R.drawable.salud_icon);

        btnSanLucas = (Button) findViewById(R.id.btnSanLucas);
        btnSanLucas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "SanLucas");
                startActivity(i);
            }
        });
        btnSanatorioPrivado = (Button) findViewById(R.id.btnSanatorioPrivado);
        btnSanatorioPrivado.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "SanatorioPrivado");
                startActivity(i);
            }
        });
        btnNeoclinica = (Button) findViewById(R.id.btnNeoclinica);
        btnNeoclinica.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "Neoclinica");
                startActivity(i);
            }
        });
        btnOdontograssi = (Button) findViewById(R.id.btnOdontograssi);
        btnOdontograssi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "Odontograssi");
                startActivity(i);
            }
        });
        btnResonanciaR4 = (Button) findViewById(R.id.btnResonanciaR4);
        btnResonanciaR4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "ResonanciaR4");
                startActivity(i);
            }
        });
        btnUrologico = (Button) findViewById(R.id.btnUrologico);
        btnUrologico.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "Urologico");
                startActivity(i);
            }
        });
        btnClinicaPrivadaGralDeheza = (Button) findViewById(R.id.btnClinicaPrivadaGralDeheza);
        btnClinicaPrivadaGralDeheza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "ClinicaPrivGralDeheza");
                startActivity(i);
            }
        });
        btnHospitalComGralDeheza = (Button) findViewById(R.id.btnHospitalComGralDeheza);
        btnHospitalComGralDeheza.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "HospitalComGralDeheza");
                startActivity(i);
            }
        });
        btnHS = (Button) findViewById(R.id.btnHS);
        btnHS.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("empresa", "HS");
                startActivity(i);
            }
        });
    }
}

