package pe.edu.pucp.tdm.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.ti.ListaDispositivosActivity;
import pe.edu.pucp.tdm.ti.ListaDispositivosAdapter;

public class ClienteListaDispositivosActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    ArrayList<DispositivoDTO> listaDispositivo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista_dispositivos);


        firebaseDatabase= FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.clienteRecycleDisp);
        ClienteListaDispositivosAdapter adapter = new ClienteListaDispositivosAdapter();
        adapter.setContext(ClienteListaDispositivosActivity.this);
    }
}