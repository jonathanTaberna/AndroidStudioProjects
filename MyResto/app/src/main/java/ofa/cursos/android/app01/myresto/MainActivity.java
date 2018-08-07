package ofa.cursos.android.app01.myresto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ofa.cursos.android.app01.myresto.modelo.DetallePedido;
import ofa.cursos.android.app01.myresto.modelo.Estado;
import ofa.cursos.android.app01.myresto.modelo.Pedido;
import ofa.cursos.android.app01.myresto.modelo.PedidoDAOMemory;
import ofa.cursos.android.app01.myresto.modelo.PedidoDao;
import ofa.cursos.android.app01.myresto.modelo.ProductoMenu;

public class MainActivity extends AppCompatActivity {

    private Button btnConfirmar;
    private Button btnAddProducto;
    private EditText txtNombre;
    private Pedido pedidoActual;
    private PedidoDao pedidoDao;
    private EditText txtDetallePedido;
    private TextView tvTotalPedido;
    private Double totalPedido;
    private CheckBox cbBebidaXL;
    private CheckBox cbIncluirPropina;
    private RadioGroup rgTipoPedido;
    private Switch swtNotificaciones;
    private ToggleButton toggleButton;
    private EditText edtDetallePedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedidoActual = new Pedido();
        pedidoDao = new PedidoDAOMemory();
        txtNombre = (EditText) findViewById(R.id.txtNombreCliente);
        txtDetallePedido = (EditText) findViewById(R.id.edtDetallePedido);
        tvTotalPedido = (TextView) findViewById(R.id.tvTotalPedido);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        totalPedido = 0.00;
        cbBebidaXL = (CheckBox) findViewById(R.id.cbBebidaXL);
        cbIncluirPropina = (CheckBox) findViewById(R.id.cbIncluirPropina);
        rgTipoPedido = (RadioGroup) findViewById(R.id.rgTipoPedido);
        swtNotificaciones = (Switch) findViewById(R.id.swtNotificaciones);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        edtDetallePedido = (EditText) findViewById(R.id.edtDetallePedido);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedidoActual.setEstado(Estado.CONFIRMADO);
                pedidoActual.setNombre(txtNombre.getText().toString());
                pedidoActual.setBebidaXL(cbBebidaXL.isChecked());
                pedidoActual.setIncluyePropina(cbIncluirPropina.isChecked());
                pedidoActual.setEnvioDomicilio(
                        rgTipoPedido.getCheckedRadioButtonId()==R.id.radBtnDelivery
                );
                pedidoActual.setEnviarNotificaciones(swtNotificaciones.isChecked());
                pedidoActual.setPagoAuotomatico(!toggleButton.isChecked());
                Toast.makeText(MainActivity.this,"Pedido creado",Toast.LENGTH_LONG).show();
                Log.d("APP_MY_RESTO","Pedido confirmado!!!! ");
                Log.d("APP_MY_RESTO",pedidoActual.toString());
                // se agrega el pedido a la lista de pedidos
                pedidoDao.agregar(pedidoActual);
                // limpiar la variable para poder cargar un nuevo pedido
                pedidoActual = new Pedido();
                // limpiar el edit text en la pantalla
                txtNombre.setText("");
                edtDetallePedido.setText("");
                Intent i = new Intent(MainActivity.this,ListaPedidosActivity.class);
                startActivity(i);
            }
        });

        btnAddProducto = (Button) findViewById(R.id.btnAddProducto);
        btnAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listaMenu= new
                        Intent(MainActivity.this,DetallePedidoActivity.class);
                startActivityForResult(listaMenu, 999);
            }
        });

        if(getIntent().getExtras()!=null){
            int idPedidoSeleccionado = getIntent().getExtras().getInt("idPedido",-1);
            if(idPedidoSeleccionado>0){
                this.loadPedido(idPedidoSeleccionado);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==999){
                int cantidad = data.getIntExtra("cantidad",0);
                ProductoMenu prod= (ProductoMenu)
                        data.getParcelableExtra("producto");
                DetallePedido detalle = new DetallePedido();
                detalle.setCantidad(cantidad);
                detalle.setProductoPedido(prod);
                pedidoActual.addItemDetalle(detalle);
                txtDetallePedido.getText()
                        .append(
                                prod.getNombre()+ "$"+
                                        (prod.getPrecio()*cantidad)+"\r\n"
                        );

                totalPedido += (prod.getPrecio()*cantidad);
                tvTotalPedido.setText("Total: " + totalPedido);
            }
        }
    }

    private void loadPedido(int id){
        pedidoActual= this.pedidoDao.buscarPorId(id);
        txtNombre.setText(pedidoActual.getNombre());
        cbBebidaXL.setChecked(pedidoActual.isBebidaXL());
        cbIncluirPropina.setChecked(pedidoActual.isIncluyePropina());
        swtNotificaciones.setChecked(pedidoActual.isEnviarNotificaciones());
        RadioButton rbDelivery = (RadioButton) findViewById(R.id.radBtnDelivery);
        RadioButton rbMesa = (RadioButton) findViewById(R.id.radBtnMesa);
        if(pedidoActual.isEnvioDomicilio()){
            rbDelivery.setChecked(true);
            rbMesa.setChecked(false);
        }else{
            rbDelivery.setChecked(false);
            rbMesa.setChecked(true);
        }
        toggleButton.setChecked(!pedidoActual.isPagoAuotomatico());
        double totalOrden = 0.0;
        for(DetallePedido det : pedidoActual.getItemsPedidos()){
            edtDetallePedido.getText().append(det.getProductoPedido().getNombre()+
                    " $"+(det.getProductoPedido().getPrecio()*det.getCantidad())+"\r\n");
            totalOrden += det.getCantidad()*det.getProductoPedido().getPrecio();
        }
        tvTotalPedido.setText("$"+totalOrden);
    }
}
