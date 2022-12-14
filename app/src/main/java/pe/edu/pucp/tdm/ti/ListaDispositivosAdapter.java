package pe.edu.pucp.tdm.ti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaUsuariosAdapter;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ListaDispositivosAdapter extends RecyclerView.Adapter<ListaDispositivosAdapter.DispositivoViewHolder> {

    private ArrayList<DispositivoDTO> listaDispositivos;
    private ArrayList<DispositivoDTO> listaOriginal;
    private Context context;
    private OnItemClickListener editar;
    private OnItemClickListener borrar;

    public ArrayList<DispositivoDTO> getListaDispositivos() {
        return listaDispositivos;
    }

    public void setListaDispositivos(ArrayList<DispositivoDTO> listaDispositivos) {
        this.listaDispositivos = listaDispositivos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaDispositivos);
    }

    public void filtrado(String txtBuscar){
        int size = txtBuscar.length();
        if(size == 0){
            listaDispositivos.clear();
            listaDispositivos.addAll(listaOriginal);
        }else{
            listaDispositivos.clear();
            for(DispositivoDTO d : listaOriginal){
                if(d.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())){
                    listaDispositivos.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public OnItemClickListener getEditar() {
        return editar;
    }

    public void setEditar(OnItemClickListener editar) {
        this.editar = editar;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public OnItemClickListener getBorrar() {
        return borrar;
    }

    public void setBorrar(OnItemClickListener borrar) {
        this.borrar = borrar;
    }

    public class DispositivoViewHolder extends RecyclerView.ViewHolder{
        DispositivoDTO dispositivo;
        public DispositivoViewHolder(@NonNull View itemView, OnItemClickListener editar, OnItemClickListener borrar){
            super(itemView);
            Button btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editar.OnItemClick(getAdapterPosition());
                }
            });
            Button btnBorrar = itemView.findViewById(R.id.btnBorrar);
            btnBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    borrar.OnItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public DispositivoViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context).inflate(R.layout.listardispositivos_rv, parent,false);
        return new DispositivoViewHolder(itemView,editar,borrar);
    }

    @Override
    public void onBindViewHolder(DispositivoViewHolder holder, int position) {
        DispositivoDTO d = listaDispositivos.get(position);
        holder.dispositivo = d;
        TextView textNombre = holder.itemView.findViewById(R.id.textNombre);
        TextView textTipo = holder.itemView.findViewById(R.id.textTipo);
        TextView textMarca = holder.itemView.findViewById(R.id.textMarca);
        TextView textCaracteristicas = holder.itemView.findViewById(R.id.textCaracteristicas);
        TextView textIncluye = holder.itemView.findViewById(R.id.textIncluye);
        TextView textStock = holder.itemView.findViewById(R.id.textStock);
        ImageView imageView = holder.itemView.findViewById(R.id.imageDispositivo);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("dispositivos/"+d.getId()+"/photo.jpg");
        Glide.with(context).load(storageReference).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);

        textNombre.setText(d.getNombre());
        textTipo.setText(d.getTipo());
        textMarca.setText(d.getMarca());
        textCaracteristicas.setText(d.getCaracteristicas());
        textIncluye.setText(d.getIncluye());
        textStock.setText(String.valueOf(d.getStock()));
    }

    @Override
    public int getItemCount(){
        return listaDispositivos.size();
    }
}
