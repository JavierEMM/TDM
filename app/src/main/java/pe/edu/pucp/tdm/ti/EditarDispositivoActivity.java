package pe.edu.pucp.tdm.ti;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class EditarDispositivoActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ClipData clipDataAbrirGaleria;
    ArrayList<Uri> clipDataTomarFoto;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    ActivityResultLauncher<Intent> openDocumentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    ImageView imageView = findViewById(R.id.imageView2);
                    Intent intent = result.getData();
                    if(clipDataAbrirGaleria != null){
                        int i=0;
                        for(int j=0;j<i;j++){
                            clipDataAbrirGaleria.addItem(intent.getClipData().getItemAt(j));
                        }
                    }else{
                        clipDataAbrirGaleria = intent.getClipData();
                    }
                    Glide.with(EditarDispositivoActivity.this).load(intent.getClipData().getItemAt(0).getUri()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
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
                                Toast.makeText(EditarDispositivoActivity.this, "Dispositivo editado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditarDispositivoActivity.this,ListaDispositivosActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(EditarDispositivoActivity.this, "Deben ser más de 3 imagenes", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(EditarDispositivoActivity.this, "Dispositivo editado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditarDispositivoActivity.this,ListaDispositivosActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(EditarDispositivoActivity.this, "Deben ser más de 3 imagenes", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(EditarDispositivoActivity.this, "Dispositivo editado correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditarDispositivoActivity.this,ListaDispositivosActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(EditarDispositivoActivity.this, "No hay imagen adjunta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        Button btnFoto = findViewById(R.id.button2);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent1.setType("image/jpeg");
                openDocumentLauncher.launch(intent1);
            }
        });
        Button btnTomarFoto = findViewById(R.id.button3);
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tomarFoto.launch(intent1);
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