package pe.edu.pucp.tdm.ti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pe.edu.pucp.tdm.R;

public class UbicacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        Button btnCancelar = findViewById(R.id.btnCancelarPedido);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(UbicacionActivity.this, PedidosActivity.class);
                startActivity(intent);
            }
        });
    }
}