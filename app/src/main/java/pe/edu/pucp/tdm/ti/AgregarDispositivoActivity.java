package pe.edu.pucp.tdm.ti;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class AgregarDispositivoActivity extends AppCompatActivity {

    String otro = "";
    FirebaseDatabase firebaseDatabase;
    Uri uri;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    uri = result.getData().getData();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dispositivo);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        EditText textNombre = findViewById(R.id.textNombreA);
        EditText textHidden = findViewById(R.id.editTextHidden);
        String[] listaTipo = {"Laptop","Tableta","Celular","Monitor","Otro"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AgregarDispositivoActivity.this, android.R.layout.simple_spinner_dropdown_item,listaTipo);
        Spinner spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerTipo.setAdapter(adapter1);
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipo = spinnerTipo.getSelectedItem().toString();
                if(tipo.equals("Otro")){
                    textHidden.setEnabled(true);
                }else{
                    textHidden.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        EditText textMarca = findViewById(R.id.textMarcaA);
        EditText textCaracteristicas = findViewById(R.id.textCaracteristicasA);
        EditText textIncluye = findViewById(R.id.textIncluyeA);
        EditText textStock = findViewById(R.id.textStockA);
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
        Button btnMenos = findViewById(R.id.btnEdit);
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
        Button btnAgregar = findViewById(R.id.btnAgregarDispositivo);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = textNombre.getText().toString();
                String tipo = spinnerTipo.getSelectedItem().toString();
                String marca = textMarca.getText().toString();
                String caracteristicas = textCaracteristicas.getText().toString();
                String incluye = textIncluye.getText().toString();
                String stockStr = textStock.getText().toString();
                int stock = Integer.parseInt(stockStr);
                if (nombre.trim().isEmpty()) {
                    Toast.makeText(AgregarDispositivoActivity.this, "Nombre no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (marca.trim().isEmpty()) {
                    Toast.makeText(AgregarDispositivoActivity.this, "Marca no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (caracteristicas.trim().isEmpty()) {
                    Toast.makeText(AgregarDispositivoActivity.this, "Caracteristicas no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (incluye.trim().isEmpty()) {
                    Toast.makeText(AgregarDispositivoActivity.this, "Incluye no puede ser vacio", Toast.LENGTH_SHORT).show();
                } else if (stock==0) {
                    Toast.makeText(AgregarDispositivoActivity.this, "Stock no puede ser nulo", Toast.LENGTH_SHORT).show();
                } else if (tipo.equals("Otro")){
                    otro = textHidden.getText().toString();
                    if(otro.trim().isEmpty()){
                        textHidden.setError("No puede ser vacío");
                        textHidden.requestFocus();
                    }else{
                        tipo = otro;
                        DispositivoDTO dispositivo = new DispositivoDTO();
                        dispositivo.setNombre(nombre);
                        dispositivo.setTipo(tipo);
                        dispositivo.setMarca(marca);
                        dispositivo.setCaracteristicas(caracteristicas);
                        dispositivo.setIncluye(incluye);
                        dispositivo.setStock(stock);
                        databaseReference.child("dispositivos").push().setValue(dispositivo);
                        Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                        startActivity(intent);
                    }
                }else{
                    DispositivoDTO dispositivo = new DispositivoDTO();
                    dispositivo.setNombre(nombre);
                    dispositivo.setTipo(tipo);
                    dispositivo.setMarca(marca);
                    dispositivo.setCaracteristicas(caracteristicas);
                    dispositivo.setIncluye(incluye);
                    dispositivo.setStock(stock);
                    databaseReference.child("dispositivos").push().setValue(dispositivo);
                    Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        Button btnCancelar = findViewById(R.id.btnCancelarA);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}