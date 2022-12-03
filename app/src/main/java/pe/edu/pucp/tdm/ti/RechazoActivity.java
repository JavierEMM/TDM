package pe.edu.pucp.tdm.ti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class RechazoActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechazo);

        firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("pedidos");

        Intent intent = getIntent();
        PedidoDTO pedido = (PedidoDTO) intent.getSerializableExtra("pedido");
        String nombre = pedido.getNombreDispositivo();
        TextView textNombre = findViewById(R.id.textNombreRechazo);
        textNombre.setText(nombre);
        EditText textRechazo = findViewById(R.id.editTextRechazo);
        Button btnEnviar = findViewById(R.id.btnEnviarRechazo);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {pedido.getCorreoPucp()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Rechazo de solicitud");
                intent.putExtra(Intent.EXTRA_TEXT, textRechazo.getText().toString());
                startActivity(intent);
                finish();
                pedido.setEstado("rechazado");
                databaseReference.child(pedido.getId()).setValue(pedido);
            }
        });
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