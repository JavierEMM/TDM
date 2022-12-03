package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xml.sax.DTDHandler;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.admin.AdminMainActivity;
import pe.edu.pucp.tdm.admin.ListaUsuariosActivity;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;
import pe.edu.pucp.tdm.login.LoginActivity;
import pe.edu.pucp.tdm.ti.ListaDispositivosActivity;

public class ClienteDetallesDispositivoActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cliente_detalle,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        switch (item.getItemId()){
            case  R.id.cancel_detail_cliente:
                Intent intent = new Intent(ClienteDetallesDispositivoActivity.this,ClienteListaDispositivosActivity.class);
                startActivity(intent);
                return  true;
            case R.id.submit_solicitud:
                PedidoDTO pedidoDTO = new PedidoDTO();
                TextView textViewNombre= findViewById(R.id.textViewNombreDetalles);
                pedidoDTO.setNombreDispositivo(textViewNombre.toString());
                TextView textViewCurso = findViewById(R.id.editTextClaveCurso);
                pedidoDTO.setCurso(textViewCurso.toString());
                TextView textVieMotivo = findViewById(R.id.editTextTextMotivoSolicitud);
                pedidoDTO.setMotivo(textVieMotivo.toString());
                TextView textViewOtros = findViewById(R.id.editTextOtros);
                pedidoDTO.setOtros(textViewOtros.toString());
                TextView textViewProgramas = findViewById(R.id.editTextProgramas);
                pedidoDTO.setProgramas(textViewProgramas.toString());
                Spinner spinner = findViewById(R.id.spinner);
                pedidoDTO.setProgramas(spinner.toString());
                pedidoDTO.setEstado("pendiente");
                databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                            Toast.makeText(ClienteDetallesDispositivoActivity.this,dataSnapshot.child("dni").getValue().toString(),Toast.LENGTH_SHORT).show();
                            pedidoDTO.setCorreoPucp(dataSnapshot.child("dni").getValue().toString());
                    }
                });



                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}