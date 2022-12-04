package pe.edu.pucp.tdm.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ClientePedidoAceptadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_pedido_aceptado);
        Intent intent = getIntent();
        PedidoDTO pedido =(PedidoDTO) intent.getSerializableExtra("pedido");


        TextView nombre = findViewById(R.id.textViewNombreDetalles2);
        nombre.setText(pedido.getNombreDispositivo());
        TextView motivo = findViewById(R.id.textView51);
        motivo.setText(pedido.getMotivo());
        TextView curso = findViewById(R.id.textViewCursoAceptado);
        curso.setText(pedido.getCurso());
        TextView tiempo = findViewById(R.id.textViewTiempoAceptado);
        tiempo.setText(pedido.getTiempo());
        TextView programas = findViewById(R.id.textViewProgramasAceptados);
        programas.setText(pedido.getProgramas());
        TextView otros = findViewById(R.id.textViewOtrosAceptados);
        otros.setText(pedido.getOtros());



    }
}