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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class AgregarDispositivoActivity extends AppCompatActivity {

    ArrayList<String> listaTipos = new ArrayList<>();
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
        ArrayList<String> tipos = listaTipos;
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AgregarDispositivoActivity.this, android.R.layout.simple_spinner_dropdown_item,tipos);
        Spinner spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerTipo.setAdapter(adapter1);
        EditText textMarca = findViewById(R.id.textMarcaA);
        EditText textCaracteristicas = findViewById(R.id.textCaracteristicasA);
        EditText textIncluye = findViewById(R.id.textIncluyeA);
        EditText textStock = findViewById(R.id.textStockA);

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
                    textNombre.setError("No puede ser vacio");
                    textNombre.requestFocus();
                } else if (marca.trim().isEmpty()) {
                    textMarca.setError("No puede ser vacío");
                    textMarca.requestFocus();
                } else if (caracteristicas.trim().isEmpty()) {
                    textCaracteristicas.setError("No puede ser vacío");
                    textCaracteristicas.requestFocus();
                } else if (incluye.trim().isEmpty()) {
                    textIncluye.setError("No puede ser vacío");
                    textIncluye.requestFocus();
                } else if (stockStr.trim().isEmpty()) {
                    textStock.setError("No puede ser vacío");
                    textStock.requestFocus();
                } else{
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
            }
        });
        Button btnCancelar = findViewById(R.id.btnCancelarA);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                startActivity(intent);
            }
        });
    }
}