package pe.edu.pucp.tdm.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    }


}