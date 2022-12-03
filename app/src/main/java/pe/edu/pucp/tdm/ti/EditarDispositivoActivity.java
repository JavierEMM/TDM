package pe.edu.pucp.tdm.ti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class EditarDispositivoActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dispositivo);

        Intent intent = getIntent();
        DispositivoDTO dispositivo = (DispositivoDTO) intent.getSerializableExtra("dispositivo");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        EditText textMarca = findViewById(R.id.textMarcaEdit);
        textMarca.setText(dispositivo.getMarca());
        EditText textCaracteristicas = findViewById(R.id.textCaracteristicasEdit);
        textCaracteristicas.setText(dispositivo.getCaracteristicas());
        EditText textIncluye = findViewById(R.id.textIncluyeEdit);
        textIncluye.setText(dispositivo.getIncluye());
        EditText textStock = findViewById(R.id.textStockEdit);
        String dispositivoStr = ""+dispositivo.getStock()+"";
        textStock.setText(dispositivoStr);
        textStock.setEnabled(false);

        Button btnMas = findViewById(R.id.btnMasEdit);
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stockStr = textStock.getText().toString();
                int stock = Integer.parseInt(stockStr);
                stockStr = ""+(stock+1)+"";
                textStock.setText(stockStr);
            }
        });

        Button btnMenos = findViewById(R.id.btnMenosEdit);
        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stockStr = textStock.getText().toString();
                int stock = Integer.parseInt(stockStr);
                if(stock != 0){
                    stockStr = ""+(stock-1)+"";
                    textStock.setText(stockStr);
                }
            }
        });

        Button btnEditar = findViewById(R.id.btnEdit);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String marca = textMarca.getText().toString();
                String caracteristicas = textCaracteristicas.getText().toString();
                String incluye = textIncluye.getText().toString();
                String stockStr = textStock.getText().toString();
                int stock = Integer.parseInt(stockStr);
                if (marca.trim().isEmpty()) {
                    Toast.makeText(EditarDispositivoActivity.this, "Marca no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (caracteristicas.trim().isEmpty()) {
                    Toast.makeText(EditarDispositivoActivity.this, "Caracteristicas no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (incluye.trim().isEmpty()) {
                    Toast.makeText(EditarDispositivoActivity.this, "Incluye no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (stock==0) {
                    Toast.makeText(EditarDispositivoActivity.this, "Stock no puede ser nulo", Toast.LENGTH_SHORT).show();
                } else {
                    dispositivo.setMarca(marca);
                    dispositivo.setCaracteristicas(caracteristicas);
                    dispositivo.setIncluye(incluye);
                    dispositivo.setStock(stock);
                    databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                    Toast.makeText(EditarDispositivoActivity.this, "Dispositivo editado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarDispositivoActivity.this,ListaDispositivosActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button btnCancelar = findViewById(R.id.btnCancelarEdit);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarDispositivoActivity.this,ListaDispositivosActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}