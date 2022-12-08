package pe.edu.pucp.tdm.cliente;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ClienteListaDispositivosAdapter extends RecyclerView.Adapter<ClienteListaDispositivosAdapter.DispositivosViewHolder> {

    private ArrayList<DispositivoDTO> listaDispositivos ;
    private Context context;

    public ArrayList<DispositivoDTO> getListaDispositivos() {
        return listaDispositivos;
    }

    public void setListaDispositivos(ArrayList<DispositivoDTO> listaDispositivos) {
        this.listaDispositivos = listaDispositivos;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public  class DispositivosViewHolder extends RecyclerView.ViewHolder{
        DispositivoDTO dispositivoDTO;
        public DispositivosViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }



    @NonNull
    @Override
    public DispositivosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.itemrvcliente,parent,false);
        return new DispositivosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DispositivosViewHolder holder, int position) {
        DispositivoDTO d= listaDispositivos.get(position);
        holder.dispositivoDTO=d;
        TextView textViewNombre= holder.itemView.findViewById(R.id.textView23);
        textViewNombre.setText(d.getNombre());
        ImageView imageView = holder.itemView.findViewById(R.id.imageDispositivo);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("dispositivos/"+d.getId()+"/photo.jpg");
        Glide.with(context).load(storageReference).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
        Button button = holder.itemView.findViewById(R.id.button9);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ClienteDetallesDispositivoActivity.class);
                intent.putExtra("dispositivo",d);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaDispositivos.size();
    }




}
