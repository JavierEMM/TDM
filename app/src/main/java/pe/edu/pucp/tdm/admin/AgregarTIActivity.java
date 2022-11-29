package pe.edu.pucp.tdm.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class AgregarTIActivity extends AppCompatActivity {

    ValueEventListener valueEventListener;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tiactivity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference  = firebaseDatabase.getReference();
        //NAVIGATION
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(AgregarTIActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("mensaje","ENTRA AQUí");
                switch (item.getItemId()){
                    case R.id.btnListarUsuariosTI:
                        Toast.makeText(AgregarTIActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(AgregarTIActivity.this, ListaUsuariosActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnReportes:
                        Toast.makeText(AgregarTIActivity.this, "Reportes", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnVerPerfil:
                        Toast.makeText(AgregarTIActivity.this, "Ver Perfil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnLogOut:
                        Toast.makeText(AgregarTIActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(AgregarTIActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
        ((Button) findViewById(R.id.btnAgregarTI)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nombres = findViewById(R.id.editTextNombre);
                EditText apellidos = findViewById(R.id.editTextApellido);
                EditText correo = findViewById(R.id.editTextCorreo);
                EditText dni = findViewById(R.id.editTextDNI);
                boolean bool = true;
                if(nombres.getText().toString().trim().isEmpty()){
                    nombres.setError("Ingrese un nombre");
                    bool = false;
                }
                if(correo.getText().toString().trim().isEmpty()){
                    correo.setError("Ingrese un correo");
                    bool = false;
                }
                if(apellidos.getText().toString().trim().isEmpty()){
                    apellidos.setError("Ingrese un apellido");
                    bool = false;
                }
                if(dni.getText().toString().trim().isEmpty()){
                    dni.setError("Ingrese un dni");
                    bool=false;
                }
                if(bool){
                    TIUserDTO tiUserDTO =  new TIUserDTO();
                    tiUserDTO.setNombres(nombres.getText().toString());
                    tiUserDTO.setApellidos(apellidos.getText().toString());
                    tiUserDTO.setCorreo(correo.getText().toString());
                    tiUserDTO.setDni(dni.getText().toString());
                    tiUserDTO.setRol("ROL_TI");
                    firebaseAuth.createUserWithEmailAndPassword(correo.getText().toString(),dni.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task1) {
                            if(task1.isSuccessful()){
                                firebaseAuth.sendPasswordResetEmail(task1.getResult().getUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            databaseReference.child("users").child(task1.getResult().getUser().getUid()).setValue(tiUserDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Intent intent =  new Intent(AgregarTIActivity.this,AdminMainActivity.class);
                                                        startActivity(intent);
                                                    }else{
                                                        Toast.makeText(AgregarTIActivity.this,"Error al crear TI",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            Toast.makeText(AgregarTIActivity.this,"Se le ha enviado un correo",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(AgregarTIActivity.this,"Error al enviar correo de confirmacion",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }else{
                                Toast.makeText(AgregarTIActivity.this,"Error al guardar credenciales",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}