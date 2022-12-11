package pe.edu.pucp.tdm.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ClientePedidoAceptadoActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

        Button btn = findViewById(R.id.btnVerMapaRecojo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientePedidoAceptadoActivity.this,MapaRecojoActivity.class);
                intent.putExtra("pedido",pedido);
                startActivity(intent);
            }
        });



    }
}