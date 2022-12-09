package pe.edu.pucp.tdm.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class AdminDispositivoMasPrestadoActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    private int prestamoTotal;
    private DispositivoDTO dispositivoDTO2;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("dispositivos").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                int prestamo = 0;
                prestamoTotal = 0;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DispositivoDTO dispositivoDTO =  snapshot.getValue(DispositivoDTO.class);
                    if(dispositivoDTO!=null){
                        prestamoTotal += dispositivoDTO.getCantPedidos();
                        if(dispositivoDTO.getCantPedidos() > prestamo){
                            prestamo = dispositivoDTO.getCantPedidos();
                            dispositivoDTO2 = dispositivoDTO;
                        }
                    }
                }
                llenarReporte(dispositivoDTO2,prestamoTotal);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dispositivo_mas_prestado);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View view =navigationView.getHeaderView(0);
        TextView nombre =  view.findViewById(R.id.nombreNav);
        TextView correo = view.findViewById(R.id.correoNav);

        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                TIUserDTO tiUserDTO =  dataSnapshot.getValue(TIUserDTO.class);
                nombre.setText(tiUserDTO.getNombres());
                correo.setText(tiUserDTO.getCorreo());
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(AdminDispositivoMasPrestadoActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btnListarUsuariosTI:
                        Toast.makeText(AdminDispositivoMasPrestadoActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(AdminDispositivoMasPrestadoActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnReportes:
                        break;
                    case R.id.btnLogOut:
                        Toast.makeText(AdminDispositivoMasPrestadoActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(AdminDispositivoMasPrestadoActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    public void llenarReporte(DispositivoDTO dispositivoDTO, int prestamoTotal){
        TextView textReportesPrestamos = findViewById(R.id.textReportesPrestamos);
        TextView textTituloPrestamo = findViewById(R.id.textTituloPrestamo);
        TextView textTipo = findViewById(R.id.textTipo);
        TextView textMarca = findViewById(R.id.textMarca);
        TextView textCaracteristicas = findViewById(R.id.textCaracteristicas);
        TextView textTotalPrestamos  = findViewById(R.id.textTotalPrestamos);
        ImageView imageView = findViewById(R.id.imageReportes);
        textReportesPrestamos.setText("Cantidad de Prestamos Totales: "+prestamoTotal);
        textTituloPrestamo.setText("Nombre: "+ dispositivoDTO.getNombre());
        textTipo.setText("Tipo: "+dispositivoDTO.getTipo());
        textMarca.setText("Marca: "+dispositivoDTO.getMarca());
        textCaracteristicas.setText("Caracteristicas: "+dispositivoDTO.getCaracteristicas());
        textTotalPrestamos.setText("Prestamos: "+dispositivoDTO.getCantPedidos());
        Glide.with(AdminDispositivoMasPrestadoActivity.this).load(firebaseStorage.getReference().child("dispositivos").child(dispositivoDTO.getId()+"/photo.jpg")).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
    }
}