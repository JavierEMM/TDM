package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.xml.sax.DTDHandler;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ClienteDetallesDispositivoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalles_dispositivo);
        Intent intent = getIntent();
        DispositivoDTO dispositivo =(DispositivoDTO) intent.getSerializableExtra("dispositivo");
        Log.d("msg","Se recepcionó"+dispositivo.getNombre());

        TextView nombre = findViewById(R.id.textViewNombreDetalles);
        TextView caracteristicas = findViewById(R.id.textViewAtributo1);

        nombre.setText(dispositivo.getNombre());
        caracteristicas.setText(dispositivo.getCaracteristicas());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cliente_detalle,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case  R.id.cancel_detail_cliente:
                Intent intent = new Intent(ClienteDetallesDispositivoActivity.this,ClienteListaDispositivosActivity.class);
                startActivity(intent);
                return  true;
            case R.id.submit_solicitud:
                Log.d("msg","Se envio el form");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}