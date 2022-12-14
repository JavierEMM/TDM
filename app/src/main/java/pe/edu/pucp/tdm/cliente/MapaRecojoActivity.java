package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class MapaRecojoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    PedidoDTO pedidoDTO = new PedidoDTO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_recojo);
        Intent intent = getIntent();
        PedidoDTO pedido =(PedidoDTO) intent.getSerializableExtra("pedido");
        pedidoDTO=pedido;
        SupportMapFragment mapFragment = (SupportMapFragment)  getSupportFragmentManager()
                .findFragmentById(R.id.mapRecojo);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap= googleMap;
        Log.d("mgs",pedidoDTO.getLatitud());
        Log.d("mgs",pedidoDTO.getLongitud());
        LatLng position = new LatLng(Double.parseDouble(pedidoDTO.getLatitud()),Double.parseDouble(pedidoDTO.getLongitud()));
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Lugar de recojo"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,16.0f));
    }
}