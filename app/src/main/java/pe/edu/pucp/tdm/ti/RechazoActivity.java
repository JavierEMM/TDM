package pe.edu.pucp.tdm.ti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class RechazoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechazo);

        Intent intent = getIntent();
        PedidoDTO pedido = (PedidoDTO) intent.getSerializableExtra("pedido");
        String nombre = pedido.getNombreDispositivo();
        TextView textNombre = findViewById(R.id.textNombreRechazo);
        textNombre.setText(nombre);
        Button btnCancelar = findViewById(R.id.btnCancelarRechazo);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(RechazoActivity.this, PedidosActivity.class);
                startActivity(intent);
            }
        });
    }
}