package pe.edu.pucp.tdm.ti;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class AgregarDispositivoActivity extends AppCompatActivity {

    String otro = "";
    FirebaseDatabase firebaseDatabase;
    ClipData clipDataAbrirGaleria;
    ArrayList<Uri> clipDataTomarFoto;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    ActivityResultLauncher<Intent> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    ImageView imageView = findViewById(R.id.imageDispositivoA);
                    Intent intent = result.getData();
                    if(intent.getClipData() != null){
                        if(clipDataAbrirGaleria != null){
                            int i=intent.getClipData().getItemCount();
                            for(int j=0;j<i;j++){
                                clipDataAbrirGaleria.addItem(intent.getClipData().getItemAt(j));
                            }
                        }else{
                            clipDataAbrirGaleria = intent.getClipData();
                        }
                    }else{
                        ClipData.Item item =  new ClipData.Item(intent.getData());
                        if(clipDataAbrirGaleria != null){
                            clipDataAbrirGaleria.addItem(item);
                        }else{
                            ClipDescription clipDescription = new ClipDescription(null, new String[]{"image/jpeg"});
                            clipDataAbrirGaleria =  new ClipData(clipDescription,item);
                        }
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> tomarFoto = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    ImageView imageView = findViewById(R.id.imageDispositivoA);
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                    String path =  MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);
                    Uri uri = Uri.parse(path);
                    if(clipDataTomarFoto != null){
                        clipDataTomarFoto.add(uri);
                    }else{
                        clipDataTomarFoto =  new ArrayList<>();
                        clipDataTomarFoto.add(uri);
                    }
                }
            }
    );

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
                        dispositivo.setId(databaseReference.child("dispositivos").push().getKey());
                        if(clipDataAbrirGaleria != null){
                            if(clipDataTomarFoto!=null){
                                int k = clipDataAbrirGaleria.getItemCount();
                                int x = clipDataTomarFoto.size();
                                if(k + x>=3){
                                    for(int j = 0; j<k;j++){
                                        Uri uri = clipDataAbrirGaleria.getItemAt(j).getUri();
                                        if(j==0){
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo.jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }else{
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+j+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }
                                    }
                                    for(int j = 0; j<x;j++){
                                        Uri uri = clipDataTomarFoto.get(j);
                                        if(j==0){
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+k+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }else{
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+(k+j)+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }
                                    }
                                    databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                                    Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(AgregarDispositivoActivity.this, "Deben ser más de 3 imagenes", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                int i = clipDataAbrirGaleria.getItemCount();
                                Log.d("NUMERO",String.valueOf(i));
                                if(i>=3){
                                    for(int j = 0; j<i;j++){
                                        Uri uri = clipDataAbrirGaleria.getItemAt(j).getUri();
                                        if(j==0){
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo.jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }else{
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+j+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }
                                    }
                                    databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                                    Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(AgregarDispositivoActivity.this, "Deben ser más de 3 imagenes", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            if(clipDataTomarFoto != null){
                                int x = clipDataTomarFoto.size();
                                if(x>=3){
                                    for(int j = 0; j<x;j++){
                                        Uri uri = clipDataTomarFoto.get(j);
                                        if(j==0){
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }else{
                                            storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+j+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                }
                                            });
                                        }
                                    }
                                    databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                                    Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                Toast.makeText(AgregarDispositivoActivity.this, "No hay imagen adjunta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                    DispositivoDTO dispositivo = new DispositivoDTO();
                    dispositivo.setNombre(nombre);
                    dispositivo.setTipo(tipo);
                    dispositivo.setMarca(marca);
                    dispositivo.setCaracteristicas(caracteristicas);
                    dispositivo.setIncluye(incluye);
                    dispositivo.setStock(stock);
                    dispositivo.setId(databaseReference.child("dispositivos").push().getKey());
                    if(clipDataAbrirGaleria != null){
                        if(clipDataTomarFoto!=null){
                            int k = clipDataAbrirGaleria.getItemCount();
                            int x = clipDataTomarFoto.size();
                            if(k + x>=3){
                                for(int j = 0; j<k;j++){
                                    Uri uri = clipDataAbrirGaleria.getItemAt(j).getUri();
                                    if(j==0){
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo.jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }else{
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+j+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }
                                }
                                for(int j = 0; j<x;j++){
                                    Uri uri = clipDataTomarFoto.get(j);
                                    if(j==0){
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+k+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }else{
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+(k+j)+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }
                                }
                                databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                                Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(AgregarDispositivoActivity.this, "Deben ser más de 3 imagenes", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            int i = clipDataAbrirGaleria.getItemCount();
                            Log.d("NUMERO",String.valueOf(i));
                            if(i>=3){
                                for(int j = 0; j<i;j++){
                                    Uri uri = clipDataAbrirGaleria.getItemAt(j).getUri();
                                    if(j==0){
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo.jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }else{
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+j+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }
                                }
                                databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                                Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(AgregarDispositivoActivity.this, "Deben ser más de 3 imagenes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        if(clipDataTomarFoto != null){
                            int x = clipDataTomarFoto.size();
                            if(x>=3){
                                for(int j = 0; j<x;j++){
                                    Uri uri = clipDataTomarFoto.get(j);
                                    if(j==0){
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }else{
                                        storageReference.child("dispositivos").child(dispositivo.getId() + "/photo"+j+".jpg").putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            }
                                        });
                                    }
                                }
                                databaseReference.child("dispositivos").child(dispositivo.getId()).setValue(dispositivo);
                                Toast.makeText(AgregarDispositivoActivity.this, "Dispositivo agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarDispositivoActivity.this,ListaDispositivosActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(AgregarDispositivoActivity.this, "No hay imagen adjunta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        Button btnImportar = findViewById(R.id.btnImportarDispositivo);
        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/jpeg");
                openDocumentLauncher.launch(intent);
            }
        });
        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tomarFoto.launch(intent);
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